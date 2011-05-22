package ru.fors.diagnostics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * User: sahmed
 * Date: 05.05.11 13:44
 *
 * @Copyright sahmed
 */
public abstract class ReadConfig {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private InputStream is;

    protected ReadConfig() {
        try {
            String curDir = System.getProperty("user.dir");

            //is = this.getClass().getClassLoader().getResourceAsStream("config.xml");
            is = new FileInputStream(curDir+"/../conf/config.xml");
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error("Configuration file not found.. system will be shutdown");
            System.exit(-1);
        }
    }

    abstract void read();

    public InputStream getIs() {
        return is;
    }
}
