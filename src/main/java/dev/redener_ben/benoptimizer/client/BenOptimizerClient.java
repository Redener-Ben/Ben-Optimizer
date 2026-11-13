package dev.redener_ben.benoptimizer.client;

import dev.redener_ben.benoptimizer.client.config.BenOptimizerConfig;
import net.fabricmc.api.ClientModInitializer;

public class BenOptimizerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BenOptimizerConfig.getInstance();
    }
}
