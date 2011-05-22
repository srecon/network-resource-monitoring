package ru.fors.diagnostics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.fors.diagnostics.config.Config;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * User: sahmed
 * Date: 05.05.11 13:47
 *
 * @Copyright sahmed
 */
public class ReadXMLConfig extends ReadConfig{
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private Unmarshaller unmarshaller;

    @Override
    void read() {
        try {
            JAXBContext jaxbContext =JAXBContext.newInstance("ru.fors.diagnostics.config");
            unmarshaller =jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            logger.error(e.getMessage());
        }
    }
    public synchronized Config getConfig(){
        read();
        Config configEl = null;
        try {
           configEl = (Config) unmarshaller.unmarshal(getIs());
        } catch (JAXBException e) {
           logger.error(e.getMessage());
        }
        return configEl;
    }

}
