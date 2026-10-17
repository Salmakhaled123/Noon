package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.FileInputStream;
import java.util.Properties;

public class ConfigHandler {
    private Logger log = LogManager.getLogger(ConfigHandler.class);

    Properties properties;

    public ConfigHandler(String filePath) {
        properties = new Properties();
        try {
            FileInputStream fileInputStream = new
                    FileInputStream(filePath);
            properties.load(fileInputStream);
            log.info("file is loaded in properties");

        } catch (Exception e) {
            log.error("catch error during loading file in properties file:{}",e);
        }

    }

    public String getValue(String key) {
        log.info(" the key is from config file: {}",key);
        return properties.getProperty(key);
    }
}
