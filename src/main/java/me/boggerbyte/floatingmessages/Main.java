package me.boggerbyte.floatingmessages;

import me.boggerbyte.floatingmessages.floating_message.FloatingMessageFormatter;
import me.boggerbyte.floatingmessages.floating_message.FloatingMessageFactory;
import me.boggerbyte.floatingmessages.listeners.ChatListener;
import me.boggerbyte.floatingmessages.utils.Logger;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.logging.Level;

public final class Main extends JavaPlugin {
    private FloatingMessageFactory floatingMessageFactory;

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
        Arrays.stream(configRequiredFields)
                .filter(field -> !config.contains(field, true))
                .forEach(field -> Logger.log(Level.WARNING, "Missing required config field <" + field + ">. Using default value"));
    }

    @Override
    public void onEnable() {
        var config = getConfig();

        var messageFormatter = new FloatingMessageFormatter(
                config.getInt("max-lines-width"),
                config.getInt("max-lines-amount"));
        floatingMessageFactory = new FloatingMessageFactory(
                messageFormatter,
                config.getInt("min-duration"),
                config.getInt("max-duration"),
                config.getInt("read-speed"));

        getServer().getPluginManager().registerEvents(new ChatListener(floatingMessageFactory), this);
    }

    @Override
    public void onDisable() {
        floatingMessageFactory.despawnAll();
    }

    public static Plugin getInstance() {
        return Main.getPlugin(Main.class);
    }
}
