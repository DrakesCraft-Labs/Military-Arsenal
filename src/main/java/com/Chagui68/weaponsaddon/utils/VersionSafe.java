package com.Chagui68.weaponsaddon.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;

import java.util.Locale;

public class VersionSafe {

    /**
     * Resolves an attribute by its registry key ("attack_damage", "generic_attack_damage",
     * "max_health", "horse_jump_strength", ...).
     *
     * <p>Compatible from Minecraft 1.20.4 to 1.21.11+:
     * <ul>
     *   <li>1.20.4–1.21.1: the constants are {@code Attribute.GENERIC_ATTACK_DAMAGE} and there
     *       is no {@code Registry.ATTRIBUTE}.</li>
     *   <li>1.21.2–1.21.11: {@code Registry.ATTRIBUTE} exists and the constants dropped the
     *       {@code GENERIC_} prefix.</li>
     * </ul>
     * Everything is resolved through reflection so the class never references a field that does
     * not exist on older servers (no {@code NoSuchFieldError} at class load time).
     */
    public static Attribute getAttribute(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }

        String key = name.toLowerCase(Locale.ROOT);
        if (key.startsWith("generic_")) {
            key = key.substring("generic_".length());
        }
        if (key.equals("horse_jump_strength")) {
            key = "jump_strength";
        }

        // Modern path: Paper's attribute registry (1.21.2+).
        Attribute fromRegistry = getAttributeFromRegistry(key);
        if (fromRegistry != null) {
            return fromRegistry;
        }

        // Legacy path: enum constants with GENERIC_ prefix (1.20.4–1.21.1).
        Attribute fromConstant = getAttributeConstant(key, true);
        if (fromConstant != null) {
            return fromConstant;
        }

        // Modern constant path without prefix (1.21.2+).
        return getAttributeConstant(key, false);
    }

    private static Attribute getAttributeFromRegistry(String key) {
        try {
            Class<?> registryClass = Class.forName("org.bukkit.Registry");
            Object attributeRegistry = registryClass.getField("ATTRIBUTE").get(null);
            if (attributeRegistry == null) {
                return null;
            }
            Object keyObject = NamespacedKey.minecraft(key);
            Object value = attributeRegistry.getClass().getMethod("get", Object.class).invoke(attributeRegistry, keyObject);
            return value instanceof Attribute attribute ? attribute : null;
        } catch (ReflectiveOperationException | LinkageError ignored) {
            // Registry.ATTRIBUTE is absent before 1.21.2.
            return null;
        }
    }

    private static Attribute getAttributeConstant(String key, boolean genericPrefix) {
        try {
            String constantName = genericPrefix ? "GENERIC_" + key.toUpperCase(Locale.ROOT)
                    : key.toUpperCase(Locale.ROOT);
            Object value = Attribute.class.getField(constantName).get(null);
            return value instanceof Attribute attribute ? attribute : null;
        } catch (ReflectiveOperationException | LinkageError ignored) {
            return null;
        }
    }

    /**
     * Resolves a Material by its modern registry-style name, falling back to the
     * legacy enum constant that existed before 1.21.11 renamed it.
     *
     * <p>Examples:
     * <ul>
     *   <li>{"CHAIN" resolves to {@code Material.CHAIN} on 1.20.4 and to
     *       {@code Material.IRON_CHAIN} on 1.21.11+.</li>
     * </ul>
     */
    public static Material getMaterial(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }

        String upper = name.toUpperCase(Locale.ROOT);
        Material direct = getMaterialConstant(upper);
        if (direct != null) {
            return direct;
        }

        // 1.21.11 renamed CHAIN to IRON_CHAIN (and COPPER_CHAIN variants appeared).
        if (upper.equals("CHAIN")) {
            return getMaterialConstant("IRON_CHAIN");
        }
        if (upper.equals("IRON_CHAIN")) {
            return getMaterialConstant("CHAIN");
        }

        return null;
    }

    private static Material getMaterialConstant(String name) {
        try {
            Object value = Material.class.getField(name).get(null);
            return value instanceof Material material ? material : null;
        } catch (ReflectiveOperationException | LinkageError ignored) {
            return null;
        }
    }

    /**
     * Safely sets the base value of an attribute on an entity.
     * Does nothing if the attribute does not exist or the entity doesn't have it.
     */
    public static void setAttributeBaseValue(LivingEntity entity, String attributeName, double value) {
        Attribute attr = getAttribute(attributeName);
        if (attr != null && entity.getAttribute(attr) != null) {
            entity.getAttribute(attr).setBaseValue(value);
        }
    }

    /**
     * Safely gets an Enchantment by a key that is valid across versions.
     */
    public static Enchantment getEnchantment(String key) {
        try {
            Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(key.toLowerCase(Locale.ROOT)));
            if (ench != null) {
                return ench;
            }
        } catch (NoClassDefFoundError | NoSuchMethodError ignored) {
            // Retain the original fallback behavior for older Slimefun-compatible environments.
        }

        String name = null;
        switch (key.toLowerCase(Locale.ROOT)) {
            case "sharpness":
                name = "DAMAGE_ALL";
                break;
            case "power":
                name = "ARROW_DAMAGE";
                break;
            case "punch":
                name = "ARROW_KNOCKBACK";
                break;
            case "protection":
                name = "PROTECTION_ENVIRONMENTAL";
                break;
            case "projectile_protection":
                name = "PROTECTION_PROJECTILE";
                break;
            case "blast_protection":
                name = "PROTECTION_EXPLOSIONS";
                break;
            case "fire_protection":
                name = "PROTECTION_FIRE";
                break;
            case "respiration":
                name = "OXYGEN";
                break;
            case "looting":
                name = "LOOT_BONUS_MOBS";
                break;
            case "unbreaking":
                name = "DURABILITY";
                break;
            case "efficiency":
                name = "DIG_SPEED";
                break;
            case "smite":
                name = "DAMAGE_UNDEAD";
                break;
            case "bane_of_arthropods":
                name = "DAMAGE_ARTHROPODS";
                break;
            default:
                break;
        }

        if (name != null) {
            try {
                return Enchantment.getByName(name);
            } catch (Exception ignored) {
                return null;
            }
        }

        return null;
    }

    /**
     * Safely gets a Particle by name and retains the upstream legacy aliases.
     */
    public static Particle getParticle(String name) {
        try {
            return Particle.valueOf(name);
        } catch (IllegalArgumentException e1) {
            try {
                if (name.equals("DUST"))
                    return Particle.valueOf("REDSTONE");
                if (name.equals("HAPPY_VILLAGER"))
                    return Particle.valueOf("VILLAGER_HAPPY");
                if (name.equals("ANGRY_VILLAGER"))
                    return Particle.valueOf("VILLAGER_ANGRY");
                if (name.equals("EXPLOSION"))
                    return Particle.valueOf("EXPLOSION_NORMAL");
                if (name.equals("LARGE_SMOKE"))
                    return Particle.valueOf("SMOKE_LARGE");
                if (name.equals("EXPLOSION_EMITTER"))
                    return Particle.valueOf("HUGE_EXPLOSION");
                if (name.equals("WITCH"))
                    return Particle.valueOf("SPELL_WITCH");
            } catch (IllegalArgumentException ignored) {
                // Ignore unavailable aliases.
            }
            return null;
        }
    }

    /**
     * Resolves Bukkit Sound constants without using the OldEnum valueOf compatibility method,
     * which Paper 26.2 schedules for removal.
     */
    public static Sound getSound(String name) {
        Sound sound = getSoundConstant(name);
        if (sound != null) {
            return sound;
        }

        if (name.contains("FIREWORK_ROCKET")) {
            sound = getSoundConstant(name.replace("FIREWORK_ROCKET", "FIREWORK"));
            if (sound != null) {
                return sound;
            }
        }

        if (name.equals("BLOCK_NOTE_BLOCK_HAT")) {
            return getSoundConstant("BLOCK_NOTE_HAT");
        }

        return null;
    }

    private static Sound getSoundConstant(String name) {
        try {
            Object value = Sound.class.getField(name).get(null);
            return value instanceof Sound sound ? sound : null;
        } catch (ReflectiveOperationException | SecurityException ignored) {
            return null;
        }
    }

    /**
     * Safely gets a PotionEffectType by name.
     */
    @SuppressWarnings("deprecation")
    public static PotionEffectType getPotionEffectType(String name) {
        try {
            PotionEffectType type = PotionEffectType.getByName(name);
            if (type != null)
                return type;

            if (name.equalsIgnoreCase("SLOWNESS"))
                return PotionEffectType.getByName("SLOW");
            if (name.equalsIgnoreCase("MINING_FATIGUE"))
                return PotionEffectType.getByName("SLOW_DIGGING");
            if (name.equalsIgnoreCase("HASTE"))
                return PotionEffectType.getByName("FAST_DIGGING");
            if (name.equalsIgnoreCase("STRENGTH"))
                return PotionEffectType.getByName("INCREASE_DAMAGE");
            if (name.equalsIgnoreCase("INSTANT_HEALTH"))
                return PotionEffectType.getByName("HEAL");
            if (name.equalsIgnoreCase("INSTANT_DAMAGE"))
                return PotionEffectType.getByName("HARM");
            if (name.equalsIgnoreCase("NAUSEA"))
                return PotionEffectType.getByName("CONFUSION");
            if (name.equalsIgnoreCase("RESISTANCE"))
                return PotionEffectType.getByName("DAMAGE_RESISTANCE");
            if (name.equalsIgnoreCase("SPEED"))
                return PotionEffectType.getByName("SPEED");
            if (name.equalsIgnoreCase("FIRE_RESISTANCE"))
                return PotionEffectType.getByName("FIRE_RESISTANCE");
            if (name.equalsIgnoreCase("JUMP_BOOST"))
                return PotionEffectType.getByName("JUMP");
            if (name.equalsIgnoreCase("NIGHT_VISION"))
                return PotionEffectType.getByName("NIGHT_VISION");
            if (name.equalsIgnoreCase("ABSORPTION"))
                return PotionEffectType.getByName("ABSORPTION");
            if (name.equalsIgnoreCase("SATURATION"))
                return PotionEffectType.getByName("SATURATION");
            if (name.equalsIgnoreCase("LEVITATION"))
                return PotionEffectType.getByName("LEVITATION");
            if (name.equalsIgnoreCase("GLOWING"))
                return PotionEffectType.getByName("GLOWING");
            if (name.equalsIgnoreCase("WITHER"))
                return PotionEffectType.getByName("WITHER");
            if (name.equalsIgnoreCase("HUNGER"))
                return PotionEffectType.getByName("HUNGER");
            if (name.equalsIgnoreCase("WEAKNESS"))
                return PotionEffectType.getByName("WEAKNESS");
            if (name.equalsIgnoreCase("DARKNESS"))
                return PotionEffectType.getByName("DARKNESS");

            return null;
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Detects the Minecraft Java version currently running on the server.
     * Returns a plain version string such as "1.21.11" or "unknown" when it cannot
     * be determined.
     */
    public static String getMinecraftVersion() {
        try {
            String version = Bukkit.getMinecraftVersion();
            if (version != null && !version.isBlank()) {
                return version;
            }
        } catch (Throwable ignored) {
            // Older Bukkit/Paper builds without Bukkit#getMinecraftVersion().
        }

        String bukkit = Bukkit.getBukkitVersion();
        if (bukkit == null || bukkit.isBlank()) {
            return "unknown";
        }
        int dash = bukkit.indexOf('-');
        String version = dash > 0 ? bukkit.substring(0, dash) : bukkit;
        return version.isBlank() ? "unknown" : version;
    }

    /**
     * Checks whether the server is running the exact given Minecraft Java version,
     * e.g. isMinecraft("1.21.11").
     */
    public static boolean isMinecraft(String version) {
        return getMinecraftVersion().equalsIgnoreCase(version);
    }

    /**
     * Checks whether the server is running the given Minecraft Java version or a
     * newer one, e.g. isMinecraftAtLeast("1.21.11").
     */
    public static boolean isMinecraftAtLeast(String version) {
        int[] current = parseVersion(getMinecraftVersion());
        int[] required = parseVersion(version);
        int max = Math.max(current.length, required.length);
        for (int i = 0; i < max; i++) {
            int a = i < current.length ? current[i] : 0;
            int b = i < required.length ? required[i] : 0;
            if (a != b) {
                return a > b;
            }
        }
        return true;
    }

    private static int[] parseVersion(String version) {
        if (version == null) {
            return new int[0];
        }
        String[] parts = version.split("\\.");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            StringBuilder digits = new StringBuilder();
            for (char c : parts[i].toCharArray()) {
                if (Character.isDigit(c)) {
                    digits.append(c);
                } else {
                    break;
                }
            }
            result[i] = digits.length() == 0 ? 0 : Integer.parseInt(digits.toString());
        }
        return result;
    }
}
