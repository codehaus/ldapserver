package org.codehaus.plexus.ldapserver.server;

/**
 * LDAPServer initializes configuration, schema, and the BackendHandler.
 * It then creates a ServerSocket and spawns ConnectionHandlers on incoming
 * connections.
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.server.schema.InitSchema;
import org.codehaus.plexus.ldapserver.server.util.ServerConfig;
import org.codehaus.plexus.ldapserver.server.acl.ACLChecker;
import org.codehaus.plexus.ldapserver.server.backend.BackendHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class LDAPServer
{
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(LDAPServer.class);
    
    private int port = -1;
    private boolean done;
    private ServerSocket serverSock;
    
    public static void main( String[] args ) throws Exception
    {
        LDAPServer server = new LDAPServer();
        server.doMain(args);
    }
    
    public void setDone() {
        LOGGER.info("Shutdown signalled.");

        done = true;
        try {
            serverSock.close();
        }
        catch(IOException ex) {
            // ignore
        }

        serverSock = null;
    }
    
    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        try {
            if(port == -1)
                return Integer.parseInt((String)ServerConfig.getInstance().get(ServerConfig.JAVALDAP_SERVER_PORT));
            else
                return port;
        }
        catch(NumberFormatException ex) {
            return -1;
        }
    }
    
    public void doMain(String args[]) throws IOException, Exception {
        if(!initialize())
            return;
        run();
    }
    
    public boolean initialize() throws IOException {
        int serverPort;
        LOGGER.info("Initializing");
        
        // Initialize Server Configuration
        if(!ServerConfig.getInstance().init())
            return false;

        // Initialize the Server Schema
        if(!new InitSchema().init())
            return false;

        if(!ACLChecker.getInstance().initialize())
            return false;

        // Read LDAP Server Port from Configuration
        serverPort = getPort();
        if(serverPort == -1)
            return false;

        // Initialize Backend Handler
        if(!BackendHandler.Handler().init())
            return false;

        // Start Listening on the LDAP Port
        LOGGER.info("Listening on " + serverPort );
        try {
            serverSock = new ServerSocket();
            serverSock.setReuseAddress(true);
            serverSock.bind(new InetSocketAddress(serverPort));
            LOGGER.debug("Bound to: " + ((InetSocketAddress)serverSock.getLocalSocketAddress()).getAddress());
        }
        catch(IOException ex) {
            LOGGER.info("Could not open socket.", ex);
            return false;
        }
        
        LOGGER.debug("Initialized");
        done = false;
        
        return true;
    }
    
    public void run() throws Exception {
        LOGGER.info("Server running");
        
        // Loop infinitely and create new ConnectionHandler threads
        // when new connections are received
        while ( !done ) {
            try {
                //ConnectionHandler cHandle = (ConnectionHandler)ConnectionHandlerPool.getInstance().checkOut();
                //cHandle.prepare(serverSock.accept());
                //cHandle.start();
                Socket socket = serverSock.accept();
                LOGGER.debug("Connection Initiated.");
                new ConnectionHandler( socket ).start();
            }
            catch(SocketTimeoutException ex) {
                // ignore
            }
            catch(IOException ex) {
                if(!done)
                    LOGGER.info("Exception while accepting connection", ex);
                else
                    LOGGER.info("Exception while accepting connection, connection closed");
            }
        }

        LOGGER.debug("Cleaning up.");
        
        LOGGER.debug("Exiting.");
    }
}
