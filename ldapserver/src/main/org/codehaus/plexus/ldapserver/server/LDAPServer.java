package org.codehaus.plexus.ldapserver.server;



/**
 * LDAPServer initializes configuration, schema, and the BackendHandler.
 * It then creates a ServerSocket and spawns ConnectionHandlers on incoming
 * connections.
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.server.schema.InitSchema;
import org.codehaus.plexus.ldapserver.server.util.Logger;
import org.codehaus.plexus.ldapserver.server.util.ServerConfig;
import org.codehaus.plexus.ldapserver.server.acl.ACLChecker;
import org.codehaus.plexus.ldapserver.server.backend.BackendHandler;

import java.io.IOException;
import java.net.ServerSocket;

public class LDAPServer
{


    public static void main( String[] args ) throws Exception
    {
        LDAPServer server = new LDAPServer();
        server.doMain(args);
    }
    
    public void doMain(String args[]) throws IOException, Exception {

        // Initialize Server Configuration
        ServerConfig.getInstance().init();

        // Initialize the Server Schema
        new InitSchema().init();

        ACLChecker.getInstance().initialize();

        // Read LDAP Server Port from Configuration
        String configPort = (String) ServerConfig.getInstance().get( ServerConfig.JAVALDAP_SERVER_PORT );
        int serverPort = new Integer( configPort ).intValue();

        // Initialize Backend Handler
        BackendHandler.Handler();

        // Start Listening on the LDAP Port
        Logger.getInstance().log( Logger.LOG_NORMAL,
                                  "Server Starting on port " + serverPort );
        ServerSocket serverSock = new ServerSocket( serverPort );

        // Loop infinitely and create new ConnectionHandler threads
        // when new connections are received
        while ( true )
        {
            Logger.getInstance().log( Logger.LOG_DEBUG, "Connection Initiated." );
            //ConnectionHandler cHandle = (ConnectionHandler)ConnectionHandlerPool.getInstance().checkOut();
            //cHandle.prepare(serverSock.accept());
            //cHandle.start();
            new ConnectionHandler( serverSock.accept() ).start();
        }


    }
}
