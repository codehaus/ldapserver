package org.codehaus.plexusl.ldapserver;

import javax.naming.directory.DirContext;

import org.codehaus.plexus.ldapserver.server.LDAPServer;
import org.codehaus.plexus.ldapserver.server.util.ServerConfig;

import junit.framework.TestCase;

/**
 * @author  Ben Walding
 * @version $Id: ConnectTest.java,v 1.1 2003-11-19 03:40:19 bwalding Exp $
 */
public class ConnectTest extends TestCase
{
    /** log4j logger */
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ConnectTest.class);
    public void testSimple() throws Exception
    { 
        ServerConfig.getInstance().setProperty(ServerConfig.JAVALDAP_SERVER_PORT, "389");
        Runnable r = new Runnable()
        {
            public void run()
            {
                try
                {
                    LDAPServer server = new LDAPServer();
                    server.doMain(new String[0]);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };

        Thread t = new Thread(r);
        t.start();

        Ldap ldap = new Ldap();
        ldap.setBaseDN("");
        ldap.setPort("389");
        ldap.setUser("");
        ldap.setServer("127.0.0.1");
        ldap.setPassword("");

        LDAPConnection conn = new LDAPConnection();

        conn.loadConfiguration(ldap);
        conn.connect();

        DirContext dc = conn.getDirContext();
        LOGGER.info(dc);
    }
}
