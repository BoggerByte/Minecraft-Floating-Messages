package me.boggerbyte.floatingmessages.utils;

import me.boggerbyte.floatingmessages.Main;

import java.util.logging.Level;

public class Logger {
    public static void log(Level level, String message) {
        var logger = Main.getInstance().getLogger();
        logger.log(level, message);
    }
}
