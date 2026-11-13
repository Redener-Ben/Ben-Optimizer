package dev.redener_ben.crystaloptimizer.client;

import dev.redener_ben.crystaloptimizer.client.config.CrystalOptimizerConfig;
import net.fabricmc.api.ClientModInitializer;

public class CrystaloptmizerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CrystalOptimizerConfig.getInstance();
    }
}
