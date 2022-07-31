package me.boggerbyte.floatingmessages.utils;

import me.boggerbyte.floatingmessages.Main;

import java.util.logging.Level;

public class Logger {
    private static final java.util.logging.Logger logger = Main.getInstance().getLogger();
    public static void log(Level level, String message) {
        logger.log(level, message);
    }
}
