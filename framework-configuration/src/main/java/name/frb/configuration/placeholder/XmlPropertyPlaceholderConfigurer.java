package name.frb.configuration.placeholder;

import name.frb.configuration.xmlconfiguration.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

public class XmlPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements InitializingBean {
    /**
     * LOGGER
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlPropertyPlaceholderConfigurer.class);

    /**
     * configuration instance of the PlaceHolder Configurer
     */
    private XmlConfiguration config;

    /**
     * Override the PropertyPlaceholderConfigurer.resolvePlaceholder(...) method
     * to first look for a placeholder with the platform prefix.
     */
    protected String resolvePlaceholder(String placeholder, Properties props, int systemPropertiesMode) {
        String propVal = null;
        if (systemPropertiesMode == SYSTEM_PROPERTIES_MODE_OVERRIDE) {
            propVal = resolveSystemProperty(placeholder);
        }
        if (propVal == null) {
            propVal = config.getString(placeholder);
        }
        if (propVal == null) {
            propVal = resolvePlaceholder(placeholder, props);
        }

        if (propVal == null && systemPropertiesMode == SYSTEM_PROPERTIES_MODE_FALLBACK) {
            if (propVal == null) {
                propVal = resolveSystemProperty(placeholder);
            }
        }
        return propVal;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (config != null) {
            LOGGER.info("Sysconfig has been initializated and its name is {}", config.getXmlConfigFilePath());
        } else {
            LOGGER.error("Sysconfig has not been initializated and it maybe not exist.");
        }
    }

    /**
     * setter method
     *
     * @param config the config to set
     * @see XmlPropertyPlaceholderConfigurer#config
     */
    public void setConfig(XmlConfiguration config) {
        this.config = config;
    }
}
