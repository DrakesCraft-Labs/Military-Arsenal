<p align="center">
    <img src="https://raw.githubusercontent.com/DrakesCraft-Labs/Military-Arsenal-addon-for-Slimefun4/main/banner.svg" alt="Military Arsenal" width="800">
</p>

<p align="center">
    <a href="https://github.com/DrakesCraft-Labs/Military-Arsenal/releases">
        <img src="https://img.shields.io/github/v/release/DrakesCraft-Labs/Military-Arsenal?label=Release&color=8A2BE2" alt="Release">
    </a>
    <a href="https://github.com/DrakesCraft-Labs/Military-Arsenal/actions">
        <img src="https://img.shields.io/github/actions/workflow/status/DrakesCraft-Labs/Military-Arsenal/build.yml?label=Build&color=00BFFF" alt="Build">
    </a>
    <a href="https://modrinth.com/plugin/weaponsaddon">
        <img src="https://img.shields.io/badge/Modrinth-MilitaryArsenal-1BD96A" alt="Modrinth">
    </a>
    <a href="https://www.curseforge.com/minecraft/bukkit-plugins/militaryarsenal">
        <img src="https://img.shields.io/badge/CurseForge-MilitaryArsenal-orange" alt="CurseForge">
    </a>
    <a href="LICENSE">
        <img src="https://img.shields.io/github/license/DrakesCraft-Labs/Military-Arsenal?label=License&color=228B22" alt="License">
    </a>
    <img src="https://img.shields.io/badge/Java-25-orange" alt="Java 25">
    <img src="https://img.shields.io/badge/Paper%20%2F%20Purpur-26.2%2B-blue" alt="Paper/Purpur 26.2+">
    <img src="https://img.shields.io/badge/MC%20Compat-1.20.4%20%E2%86%92%201.21.11-green" alt="MC 1.20.4 to 1.21.11">
    <img src="https://img.shields.io/badge/Slimefun4-Drake%20v11-brightgreen" alt="Slimefun4-Drake v11">
</p>

# ⚔️ Military Arsenal — Advanced Warfare for Slimefun

> ### 🏰 ¡Únete a la Comunidad Oficial de DrakesCraft!
> 
> * 🎮 **IP del Servidor**: `play.drakescraft.net` *(Java 1.21.11 & Bedrock)*
> * 💬 **Discord Oficial**: [discord.gg/drakescraft](https://discord.gg/rR7FbfCt9Y)
> * 🌐 **Web & Guía**: [drakescraft.net](https://drakescraft.net) — 🛒 **Tienda**: [tienda.drakescraft.net](https://tienda.drakescraft.net)
> 
> *¡Juega con este addon y más de 80 expansiones optimizadas en vivo en nuestra network de supervivencia técnica!*

---

> Turn your Slimefun server into a tactical battlefield with cutting-edge military technology.

**Military Arsenal** is a combat addon for **Slimefun4-Drake** featuring firearms, dedicated ammunition, defensive turrets, war machines, Void/Antimatter progression, and GPS coordinate-based bombardment.

⚠️ **Disclaimer**: unofficial community addon; not affiliated with or endorsed by the official Slimefun4 team.

---

## 🚀 Features

### 🔫 Weapons & Ammunition
- **Machine Gun** — burst-fire combat: 5 rapid shots, 5 HP per bullet (25 HP per burst), unbreakable, epic particle effects and realistic sounds
- **Antimatter Rifle** — endgame weapon with Void/Antimatter progression
- **Ammunition system** — craftable bullets with automatic consumption when firing
- **Weapon Upgrade Table** — damage and speed upgrade modules

### 🛡️ Defensive Systems
- **Turrets** Attack, Sniper, Melee and Machine Gun with 4-stage progression
- **Wraith-class mountable turret** — pilotable war machine
- Multi-level NBT structures with protected upgrade/dismantle handling

### 💣 Machines & War
- **Bombardment Terminal** — GPS coordinate-based airstrikes (X Y Z), interactive GUI with real-time energy display, dual fuel (TNT + Nether Stars), 2 waves of 4 TNT bombs
- **Military Crafting Table** and **Military Machine Fabricator** — military crafting workbenches
- **Ammunition Workshop** — ammunition production
- **Antimatter Pedestal & Ritual** — antimatter progression
- **Military Vouchers**, **Void armor** and tiered components (Military Circuit → Targeting System / Guidance Chip / Reinforced Frame → Quantum Processor / Explosive Core)

---

## 🧰 Compatibility

| Component | Range |
|---|---|
| **Server** | Paper / Purpur **26.2+** (build target) |
| **Version detection (VersionSafe)** | Minecraft Java **1.20.4 → 1.21.11** |
| **Java runtime** | 25 (build) / 21-compatible bytecode |
| **Slimefun** | Slimefun4-Drake v11 (required) |
| **Networks** | Optional (loads before to register recipes) |

`VersionSafe` resolves attributes, materials, enchantments, sounds, particles and potions through reflection using both modern names (no `GENERIC_` prefix, `IRON_CHAIN`) and the 1.20.4–1.21.1 ones (`GENERIC_ATTACK_DAMAGE`, `CHAIN`), with no static references to symbols missing on older servers.

---

## 📥 Installation

1. Install **Slimefun4-Drake v11** on a Paper/Purpur **26.2+** server running **Java 25**.
2. Download `MilitaryArsenal-v1.1.3.jar` from [GitHub Releases](https://github.com/DrakesCraft-Labs/Military-Arsenal/releases), [Modrinth](https://modrinth.com/plugin/weaponsaddon) or [CurseForge](https://www.curseforge.com/minecraft/bukkit-plugins/militaryarsenal).
3. Place the JAR in the server's `plugins/` directory.
4. Restart the server completely.
5. Open the Slimefun guide (`/sf guide`) and navigate to the **MILITARY ARSENAL** category.

**Networks** is optional: Military Arsenal loads before Networks so its items and recipes are registered before Networks builds its indexes.

---

## 🛠️ Build

Build with JDK 25 (the generated bytecode is compatible with Java 21+):

```bash
mvn -B -Dmaven.test.skip=true clean package
```

The final JAR is written to `target/MilitaryArsenal-v1.1.3.jar`. GitHub Actions publishes the raw JAR and tags attach the same JAR to the GitHub Release.

---

## 📖 Documentation

- [Como_Funciona.md](Como_Funciona.md) — full addon guide (items, recipes, machines and mechanics)
- [GitHub Issues](https://github.com/DrakesCraft-Labs/Military-Arsenal/issues) — report bugs and suggest improvements

---

## 📜 License

Licensed under the **GNU General Public License v3.0**.
