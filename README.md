# Ben Optimizer

A small **client-side** Fabric mod for Minecraft **1.21.11** that removes the wait for the server in crystal and anchor PvP.

## Features

### 💎 Crystal Optimizer
- End crystals are removed **instantly on your client** when you hit them, instead of waiting for the server.
- **Fast Place Crystals:** removes the 4-tick right-click delay while holding end crystals.
- **Crystal Hit Delay:** configurable minimum delay between crystal hits (default: 50 ms).

### ⚓ Anchor Optimizer
- Charged respawn anchors are removed **instantly on your client** when you blow them up (outside the Nether).
- **Glowstone is not touched:** charging anchors works exactly like vanilla.

## Config
In-game via **Mod Menu**, or in `config/crystaloptimizer.json`:

| Option | Default |
|---|---|
| `crystalHitDelayMs` | `50` |
| `fastPlace` | `true` |
| `anchorOptimizer` | `true` |

## Requirements
- Fabric Loader ≥ 0.19.2
- Fabric API
- Cloth Config – bundled in the JAR
- Mod Menu – optional

## Building
Requires Java 21.

```bash
./gradlew build
```

The mod JAR is written to `build/libs/`.

## License
[MIT](LICENSE) © redener_ben. Bundles [Cloth Config](https://github.com/shedaniel/ClothConfig) (LGPL-3.0) by shedaniel.
