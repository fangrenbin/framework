package name.frb.configuration.xmlconfiguration;

import name.frb.configuration.resourceloader.RescourceLoader;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class XmlConfiguration {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private Map<String, Object> valueMap = new HashMap<String, Object>();

    private String xmlConfigFilePath;

    private String[] xmlConfigFilePathArray;

    private RescourceLoader rescourceLoader;

    private XMLConfiguration xmlConfig = new XMLConfiguration();


    /**
     * Constructor
     *
     * @param xmlConfigFilePath
     */
    public XmlConfiguration(String xmlConfigFilePath) {
        this.xmlConfigFilePath = xmlConfigFilePath;
        this.xmlConfigFilePathArray = StringUtils.split(xmlConfigFilePath, ";");
    }

    /**
     * Loading xml file
     */
    public void init() {
        if (ArrayUtils.isEmpty(xmlConfigFilePathArray)) {
            LOGGER.error("arg-constructor must be filled in.");
            return;
        }

        for (String xmlConfigFilePath : xmlConfigFilePathArray) {
            if (StringUtils.isEmpty(xmlConfigFilePath)) {
                continue;
            }
            try {
                Resource[] resources = rescourceLoader.getResources(xmlConfigFilePath);

                if (ArrayUtils.isEmpty(resources)) {
                    continue;
                }

                for (Resource resource : resources) {
                    if (resource == null || !resource.exists() && !resource.isReadable()) {
                        continue;
                    }

                    String filePath = resource.getURL().getPath();

                    XMLConfiguration xmlConfiguration = new XMLConfiguration();
                    try {
                        xmlConfiguration.load(resource.getInputStream());
                    } catch (ConfigurationException e) {
                        LOGGER.error("Failed to load XML configuration. {}", e);
                        e.printStackTrace();
                    }

                    LOGGER.info("Success to load XML configuration {}", filePath);
                    if (this.xmlConfig.isEmpty()) {
                        this.xmlConfig = xmlConfiguration;
                    } else {
                        this.xmlConfig.append(xmlConfiguration);
                    }
                }

            } catch (IOException e) {
                LOGGER.error("Failed to load xml configuration. {}", e);
            }
        }
    }


    /**
     * Get instance of <code>XMLConfiguration</code>.
     *
     * @return instance of <code>XMLConfiguration</code>.
     */
    public XMLConfiguration getXmlConfig() {
        if (this.xmlConfig == null) {
            LOGGER.error("Failed to get xmlConfig, plz make sure config xml {} file exist and it has been invoked init() method.", xmlConfigFilePathArray);
        }
        return this.xmlConfig;
    }

    /**
     * Get double value by key
     *
     * @param key key
     * @return double
     */
    public double getDouble(String key) {
        if (valueMap.containsKey(key)) {
            String value = String.valueOf(valueMap.get(key));
            if (StringUtils.isNotEmpty(value)) {
                return Double.valueOf(value);
            } else {
                LOGGER.error("Can not parse value={} to double data type, key={}", key);
            }
        }

        double value;
        if (getXmlConfig().containsKey(key + "[@value]")) {
            value = getXmlConfig().getDouble(key + "[@value]");
        } else {
            value = getXmlConfig().getDouble(key);
        }

        return value;
    }

    /**
     * Get long value
     *
     * @param key
     * @return long value
     */
    public long getLong(String key) {
        if (valueMap.containsKey(key)) {
            String value = String.valueOf(valueMap.get(key));
            if (StringUtils.isNotEmpty(value)) {
                return Long.valueOf(value);
            } else {
                LOGGER.error("Can not parse string to long type, key={}", key);
            }
        }

        long value;
        if (getXmlConfig().containsKey(key + "[@value]")) {
            value = getXmlConfig().getInt(key + "[@value]");
        } else {
            value = getXmlConfig().getInt(key);
        }

        putKeyValueIntoMap(key, value);

        return value;
    }


    /**
     * Get Intger
     *
     * @param key
     * @return int value
     */
    public int getIntger(String key) {
        return getInt(key);
    }

    /**
     * Get int value
     *
     * @param key
     * @return int value
     */
    private int getInt(String key) {
        if (valueMap.containsKey(key)) {
            String value = String.valueOf(valueMap.get(key));
            if (StringUtils.isNotEmpty(value)) {
                return Integer.valueOf(value);
            } else {
                LOGGER.error("Can not parse string to int type, key={}", key);
            }
        }

        int value;
        if (getXmlConfig().containsKey(key + "[@value]")) {
            value = getXmlConfig().getInt(key + "[@value]");
        } else {
            value = getXmlConfig().getInt(key);
        }

        putKeyValueIntoMap(key, value);

        return value;
    }

    /**
     * Get String
     *
     * @param key
     * @return String value
     */
    public String getString(String key) {
        if (valueMap.containsKey(key)) {
            return String.valueOf(valueMap.get(key));
        }

        if (valueMap.containsValue(key)) {
            return String.valueOf(valueMap.get(key + "[@value]"));
        }

        String value = getXmlConfig().getString(key);

        putKeyValueIntoMap(key, value);

        return value;
    }

    /**
     * Get boolean value
     *
     * @param key key
     * @return boolean
     */
    public boolean getBoolean(String key) {
        if (valueMap.containsKey(key)) {
            return Boolean.valueOf(String.valueOf(valueMap.get(key)));
        }

        boolean value;
        if (getXmlConfig().containsKey(key + "[@value]")) {
            value = getXmlConfig().getBoolean(key + "[@value]");
        } else {
            value = getXmlConfig().getBoolean(key);
        }

        return value;
    }

    /**
     * Put this key and value into the map
     *
     * @param key   key
     * @param value value
     */
    private void putKeyValueIntoMap(String key, Object value) {
        valueMap.put(key, value);
    }

    /**
     * Set RescourceLoader
     *
     * @param rescourceLoader
     */
    public void setRescourceLoader(RescourceLoader rescourceLoader) {
        this.rescourceLoader = rescourceLoader;
    }
}