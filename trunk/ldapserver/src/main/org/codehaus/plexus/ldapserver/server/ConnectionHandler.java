package org.codehaus.plexus.ldapserver.server;

/**
 * The ConnectionHandler is spawned when a new connection arrives. It retrieves a
 * suitable MessageHandler from the pool and uses it to parse incoming messages.
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.ldapv3.LDAPMessage;

import java.net.Socket;

public class ConnectionHandler extends Thread
{
    private MessageHandler msgHandler = null;

    public ConnectionHandler( Socket client ) throws Exception
    {

        client.setTcpNoDelay( true );
        msgHandler = (MessageHandler) MessageHandlerPool.getInstance().checkOut();
        msgHandler.reset();
        msgHandler.getConnection().setClient( client );
        msgHandler.getConnection().setDebug( false );

        setPriority( NORM_PRIORITY - 1 );
    }

    public void run()
    {

        boolean continueSession = true;

        while ( continueSession == true )
        {
            LDAPMessage request = msgHandler.getNextRequest();
            System.out.println("Request: " + request);
            if ( request != null )
            {
                continueSession = msgHandler.answerRequest( request );
                request = null;
            }
            else
            {
                continueSession = false;
            }
        }

        MessageHandlerPool.getInstance().checkIn( msgHandler );
    }
}
