package name.frb.configuration.resourceloader;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

public class RescourceLoader extends PathMatchingResourcePatternResolver {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Override
    public Resource[] getResources(String locationPattern) throws IOException {
        Assert.notNull(locationPattern, "Location pattern must not be null");
        if (locationPattern.startsWith(CLASSPATH_ALL_URL_PREFIX)) {
            // a class path resource (multiple resources for same name possible)
            if (getPathMatcher().isPattern(locationPattern.substring(CLASSPATH_ALL_URL_PREFIX.length()))) {
                // a class path resource pattern
                return findPathMatchingResources(locationPattern);
            } else {
                // all class path resources with the given name
                return findAllClassPathResources(locationPattern.substring(CLASSPATH_ALL_URL_PREFIX.length()));
            }
        } else if (locationPattern.startsWith(CLASSPATH_URL_PREFIX)) {
            // Only look for a pattern after a prefix here
            // (to not get fooled by a pattern symbol in a strange prefix).
            int prefixEnd = locationPattern.indexOf(":") + 1;
            if (getPathMatcher().isPattern(locationPattern.substring(prefixEnd))) {
                // a file pattern
                return findPathMatchingResources(locationPattern);
            } else {
                // a single resource with the given name
                return new Resource[]{getResourceLoader().getResource(locationPattern)};
            }
        } else {
            // a single resource with the given name
            return new Resource[]{getResourceLoader().getResource(ResourceUtils.FILE_URL_PREFIX + locationPattern)};
        }
    }

    @Override
    public Resource getResource(String location) {
        Resource[] resources = null;
        try {
            resources = getResources(location);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        if (ArrayUtils.isNotEmpty(resources)) {
            return resources[0];
        } else {
            return null;
        }
    }
}
