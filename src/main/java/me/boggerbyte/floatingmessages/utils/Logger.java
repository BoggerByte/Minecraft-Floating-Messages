package me.boggerbyte.floatingmessages.utils;

import me.boggerbyte.floatingmessages.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

public class Logger {
    public static void log(String message) {
        var console = Bukkit.getConsoleSender();
        var pluginName = Main.getInstance().getName();
        console.sendMessage(Component.text( "[" + pluginName + "] " + message));
    }
}
