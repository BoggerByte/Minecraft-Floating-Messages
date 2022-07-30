package me.boggerbyte.floatingmessages.managers;

import me.boggerbyte.floatingmessages.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ConfigManager {
    private final Plugin plugin = Main.getInstance();
    private final String filename;
    private final List<String> requiredFields;

    public ConfigManager(String filename, List<String> requiredFields) {
        this.filename = filename;
        this.requiredFields = requiredFields;
    }

    private FileConfiguration config;

    public FileConfiguration getConfig() {
        if (config == null) loadConfig();
        return this.config;
    }

    private void loadConfig() {
        File configFile = new File(plugin.getDataFolder(), filename);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(filename, false);
        }

        try {
            config = new YamlConfiguration();
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public List<String> getMissingRequiredFields() {
        if (config == null) return Collections.emptyList();
        return requiredFields.stream()
                .filter(field -> config.get(field) == null)
                .toList();
    }
}
