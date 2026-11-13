package dev.redener_ben.benoptimizer.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import net.fabricmc.loader.api.FabricLoader;

public class BenOptimizerConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "benoptimizer.json");

    public int crystalHitDelayMs = 50;
    public boolean fastPlace = true;
    /** Entfernt explodierte Respawn Anchors sofort auf dem Client. Glowstone bleibt normal. */
    public boolean anchorOptimizer = true;

    private static BenOptimizerConfig instance;

    public static BenOptimizerConfig getInstance() {
        if (instance == null) {
            instance = new BenOptimizerConfig();
            instance.load();
        }
        return instance;
    }

    public void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                BenOptimizerConfig loaded = GSON.fromJson(reader, BenOptimizerConfig.class);
                if (loaded != null) {
                    this.crystalHitDelayMs = loaded.crystalHitDelayMs;
                    this.fastPlace = loaded.fastPlace;
                    this.anchorOptimizer = loaded.anchorOptimizer;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Schreibt neue Optionen (z. B. anchorOptimizer) auch in alte Config-Dateien
        this.save();
    }

    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
