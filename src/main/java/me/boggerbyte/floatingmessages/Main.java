package me.boggerbyte.floatingmessages;

import me.boggerbyte.floatingmessages.floating_message.FloatingMessageFormatter;
import me.boggerbyte.floatingmessages.floating_message.FloatingMessageSpawner;
import me.boggerbyte.floatingmessages.listeners.ChatListener;
import me.boggerbyte.floatingmessages.utils.Logger;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.logging.Level;

public final class Main extends JavaPlugin {
    private FloatingMessageSpawner floatingMessageSpawner;

    @Override
    public void onLoad() {
        saveDefaultConfig();

        var config = getConfig();
        var configRequiredFields = new String[]{
                "max-lines-width",
                "max-lines-amount",
                "min-duration",
                "max-duration",
                "read-speed"
        };
        Arrays.stream(configRequiredFields).filter(field -> !config.contains(field, true))
                .forEach(field -> Logger.log(Level.WARNING, "Missing required config field <" + field + ">. Using default value"));
    }

    @Override
    public void onEnable() {
        var messageFormatter = new FloatingMessageFormatter(
                getConfig().getInt("max-lines-width"),
                getConfig().getInt("max-lines-amount"));
        floatingMessageSpawner = new FloatingMessageSpawner(
                messageFormatter,
                getConfig().getInt("min-duration"),
                getConfig().getInt("max-duration"),
                getConfig().getInt("read-speed"));

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
