package com.swaglabs.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);

    private static final Properties properties = new Properties();

    private ConfigReader() {}

    static {
        logger.debug("DEBUG: ConfigReader static block triggered — loading config.properties");

        try (InputStream inputStream =
                     ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {

            if (inputStream == null) {
            
                logger.fatal("FATAL: config.properties NOT FOUND on classpath! " +
                        "Check src/test/resources/config.properties exists.");
                throw new RuntimeException("config.properties not found in classpath.");
            }

            properties.load(inputStream);
            logger.info("config.properties loaded successfully. " +
                    "Keys found: " + properties.stringPropertyNames());

        } catch (IOException e) {
            logger.fatal("FATAL: IOException while loading config.properties: " + e.getMessage());
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage());
        }
    }

    public static String getProperty(String key) {
        logger.trace("TRACE: getProperty() called with key = '" + key + "'");

        String value = properties.getProperty(key);

        if (value == null || value.trim().isEmpty()) {
            logger.warn("WARN: Property key '" + key + "' not found or empty in config.properties");
        } else {
            if (key.toLowerCase().contains("password")) {
                logger.debug("DEBUG: Property retrieved: " + key + " = [HIDDEN]");
            } else {
                logger.debug("DEBUG: Property retrieved: " + key + " = " + value);
            }
        }

        return value;
    }
}
