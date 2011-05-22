package ru.fors.diagnostics;

import junit.framework.TestCase;
import ru.fors.diagnostics.config.Config;

/**
 * User: sahmed
 * Date: 05.05.11 14:55
 *
 * @Copyright Fors Developer center
 */
public class ReadConfigTest extends TestCase{

    public void testRead() throws Exception {
        ReadXMLConfig conf = new ReadXMLConfig();
        Config config = conf.getConfig();
        assertEquals(4, config.getServers().getServer().size());
    }
}
