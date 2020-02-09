package Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class IOUtils {

    private static final Logger logger = LoggerFactory.getLogger(IOUtils.class);

    public static String getProperty(String name) {
        Properties prop = new Properties();
        try {
            prop.load(Objects.requireNonNull(IOUtils.class.getClassLoader().getResourceAsStream("config.properties")));
            return prop.getProperty(name);

        } catch (IOException ex) {
            logger.warn("Could not find properties file");
            ex.printStackTrace();
        }catch (NullPointerException ex) {
            logger.warn("Could not find property: {}", name);
            ex.printStackTrace();
        }
        return null;
    }
}
