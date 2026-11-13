package dev.redener_ben.crystaloptimizer.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            CrystalOptimizerConfig config = CrystalOptimizerConfig.getInstance();
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Component.literal("Crystal & Anchor Optimizer"));
            builder.setSavingRunnable(config::save);

            ConfigCategory general = builder.getOrCreateCategory(Component.literal("General"));
            ConfigEntryBuilder entry = builder.entryBuilder();

            general.addEntry(entry.startIntField(Component.literal("Crystal Hit Delay (ms)"), config.crystalHitDelayMs)
                    .setDefaultValue(50)
                    .setMin(0)
                    .setTooltip(Component.literal("Minimum delay between hitting crystals. Lower = faster, but higher ban risk. Default is 50ms."))
                    .setSaveConsumer(v -> config.crystalHitDelayMs = v)
                    .build());

            general.addEntry(entry.startBooleanToggle(Component.literal("Fast Place Crystals"), config.fastPlace)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("Removes the 4-tick delay when placing end crystals."))
                    .setSaveConsumer(v -> config.fastPlace = v)
                    .build());

            general.addEntry(entry.startBooleanToggle(Component.literal("Anchor Optimizer"), config.anchorOptimizer)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("Removes a charged respawn anchor instantly on your client when you blow it up, so you do not wait for the server. Does NOT speed up glowstone placing."))
                    .setSaveConsumer(v -> config.anchorOptimizer = v)
                    .build());

            return builder.build();
        };
    }
}
