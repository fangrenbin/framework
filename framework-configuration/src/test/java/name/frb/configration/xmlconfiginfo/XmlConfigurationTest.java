package name.frb.configration.xmlconfiginfo;

import name.frb.configuration.resourceloader.RescourceLoader;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlConfigurationTest {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private Map<String, Object> valueMap = new HashMap<String, Object>();

    private String xmlConfigFilePath;
    private String[] xmlConfigFilePathArray;

    private RescourceLoader rescourceLoader;
    private XMLConfiguration xmlConfig = new XMLConfiguration();

    public void loadXml() {
        if (this.xmlConfigFilePathArray.length <= 0) {
            return;
        }

        for (String xmlconfigPath : this.xmlConfigFilePathArray) {
            if (StringUtils.isEmpty(xmlconfigPath)) {
                LOGGER.error("Failed to load xml config files up. Cause: xml config FilePath is empty.");
                continue;
            }

            File xmlConfigFile = new File(xmlconfigPath);
            if (!xmlConfigFile.exists()) {
                LOGGER.error("Failed to load xml config files up. Cause: xml config file doesn't exist");
                continue;
            }

            SAXReader reader = new SAXReader();
            Document doc = null;
            try {
                doc = reader.read(new File(xmlconfigPath));
            } catch (DocumentException e) {
                LOGGER.error("Failed to load xml config files up. Cause: An error occur to SAXReader. {}", e);

                continue;
            }

            Element root = doc.getRootElement();
            List childrenList = root.elements();

            for (int i = 0; i < childrenList.size(); i++) {
                Element element = (Element) childrenList.get(i);

                String key = element.attribute("key").getStringValue();

                String value = null;
                if (element.nodeCount() > 0) {
                    value = element.node(0).getStringValue();
                }

                valueMap.put(key, value);
            }
        }
    }
}
