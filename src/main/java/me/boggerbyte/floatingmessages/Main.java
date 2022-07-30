package me.boggerbyte.floatingmessages;

import me.boggerbyte.floatingmessages.floating_message.FloatingMessageFormatter;
import me.boggerbyte.floatingmessages.floating_message.FloatingMessageSpawner;
import me.boggerbyte.floatingmessages.listeners.ChatListener;
import me.boggerbyte.floatingmessages.managers.ConfigManager;
import me.boggerbyte.floatingmessages.utils.Logger;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class Main extends JavaPlugin {
    private ConfigManager configManager;
    private FloatingMessageSpawner floatingMessageSpawner;

    @Override
    public void onLoad() {
        configManager = new ConfigManager(
                "config.yml",
                Arrays.asList(
                        "max-lines-width",
                        "max-lines-amount",
                        "min-duration",
                        "max-duration",
                        "read-speed"));
        floatingMessageSpawner = new FloatingMessageSpawner(
                new FloatingMessageFormatter(
                        configManager.getConfig().getInt("max-lines-width"),
                        configManager.getConfig().getInt("max-lines-amount")),
                configManager.getConfig().getInt("min-duration"),
                configManager.getConfig().getInt("max-duration"),
                configManager.getConfig().getInt("read-speed"));
    }

    @Override
    public void onEnable() {
        var missingConfigFields = configManager.getMissingRequiredFields();
        if (!missingConfigFields.isEmpty()) {
            missingConfigFields.forEach(field -> Logger.log("Missing required config field <" + field + ">"));
            return;
        }

        getServer().getPluginManager().registerEvents(new ChatListener(floatingMessageSpawner), this);
    }

    @Override
    public void onDisable() {
        floatingMessageSpawner.despawnAll();
    }

    public static Plugin getInstance() {
        return Main.getPlugin(Main.class);
    }
}
