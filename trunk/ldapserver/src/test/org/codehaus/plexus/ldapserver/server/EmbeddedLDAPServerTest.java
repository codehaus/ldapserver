package org.codehaus.plexus.ldapserver.server;

/*
 * LICENSE
 */

import junit.framework.TestCase;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author <a href="mailto:trygvis@inamo.no">Trygve Laugst&oslash;l</a>
 * @version $Id: EmbeddedLDAPServerTest.java,v 1.1 2003-11-27 12:09:41 trygvis Exp $
 */
public class EmbeddedLDAPServerTest extends TestCase {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(EmbeddedLDAPServerTest.class);

    public void setUp() throws Exception {
    }

    public void tearDown() throws Exception {
    }
    
    public void testStartupAndShutdown() throws Exception {
        int port = 6666;
        EmbeddedLDAPServer server;
        
        server = new EmbeddedLDAPServer(port);
        
        server.start();
        server.sendStopSignal();
        server.waitForShutdown(1000);
        assertFalse("The server didn't stop in time.", server.isRunning());
       
        // done twice to check that the server socket is released
        server.start();
        server.sendStopSignal();
        server.waitForShutdown(1000);
        assertFalse("The server didn't stop in time.", server.isRunning());
    }

    private void checkSocket(int port) {
        ServerSocket socket;

        // check that the socket is available
        try {
            socket = new ServerSocket(port);
            socket.close();
        }
        catch(IOException ex) {
            fail("Unable to create server socket on ldap port.");
        }
    }
}