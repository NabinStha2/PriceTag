package com.example.pricetag.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColorLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(ColorLogger.class);

    public static void logDebug(String logging) {
        LOGGER.debug("\u001B[34m" + logging + "\u001B[0m");
    }

    public static void logInfo(String logging) {
        LOGGER.info("\u001B[32m" + logging + "\u001B[0m");
    }

    public static void logError(String logging) {
        LOGGER.error("\u001B[31m" + logging + "\u001B[0m");
    }

    public static void logTrace(String logging) {
        LOGGER.trace("\u001B[33m" + logging + "\u001B[0m");
    }
}
