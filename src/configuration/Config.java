package configuration;

import logger.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private static Config cfg;
    private static final Logger LOG = Logger.getLog();
    private static final String PATH = "./settings/settings.txt";
    private String host;
    private int port;

    private Config() {
        try (FileReader fileReader = new FileReader(PATH)){
            Properties property = new Properties();
            property.load(fileReader);
            host = property.getProperty("host");
            port = Integer.parseInt(property.getProperty("port"));
        } catch (IOException e){
            LOG.log("Ошибка в конструкторе класса" + Config.class.getName());
            e.printStackTrace();
        }
    }

    public static Config getInstance() {
        if (cfg == null) {
            cfg = new Config();
        }
        return cfg;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
