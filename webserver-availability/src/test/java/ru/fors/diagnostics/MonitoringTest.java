package ru.fors.diagnostics;

import junit.framework.TestCase;
import ru.fors.diagnostics.config.Server;

import java.net.URL;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * User: sahmed
 * Date: 05.05.11 13:30
 *
 * @Copyright Fors Developer center
 */
public class MonitoringTest extends TestCase {
    private Monitoring monitor;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Server server = new Server("http","sunny.fors.ru",80,"archiva");
        List<Server> servers = Collections.singletonList(server);
        monitor = new Monitoring(servers);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        monitor = null;
    }

    public void testGetUrl() throws Exception {
        URL url =  monitor.getUrl(monitor.getServers().get(0));
        assertEquals(url.toString(), "http://sunny.fors.ru:80/archiva");

    }
    public void testIsTimeToResend() throws Exception{
        Runner runner = new Runner();
        //boolean bool =  runner.isTimeToResend(new Date(System.currentTimeMillis()));

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, 2011);
        cal.set(Calendar.MONTH, 04);
        cal.set(Calendar.DATE, 9);

        cal.set(Calendar.HOUR, 13);
        cal.set(Calendar.MINUTE, 10);
        cal.set(Calendar.SECOND, 10);
        //boolean bool =  runner.isTimeToResend(new Date(System.currentTimeMillis()));
        boolean bool =  runner.isTimeToResend(cal.getTime());
        assertEquals(false, bool);
    }
}
