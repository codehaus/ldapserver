package org.codehaus.plexus.ldapserver.server;

import com.ibm.asn1.*;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPMessage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * A MessageHandler uses a Connection object to communicate with the client.
 * The Connection class not only manages the communications socket, it also
 * manages BER encoding and decoding.
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */
public class Connection
{
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(Connection.class);
    
    private Socket client = null;
    private boolean debug;

    private Credentials authCred = null;

    private int lastOp = 0;

    private BufferedInputStream bufIn = null;
    private BufferedOutputStream bufOut = null;

    private BEREncoder berEncoder = null;
    BERDecoder berDecoder = null;

    public Connection()
    {
    }

    public void close()
    {
        try
        {
            getClient().close();
        }
        catch ( IOException ex )
        {
            LOGGER.debug( "Exception while closing, ", ex );
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/18/2000 9:05:35 AM)
     * @return org.codehaus.server.Credentials
     */
    public Credentials getAuthCred()
    {
        return authCred;
    }

    public BERDecoder getBERDecoder()
    {
        return this.berDecoder;
    }

    public BEREncoder getBEREncoder()
    {
        return this.berEncoder;
    }

    public Socket getClient()
    {
        return this.client;
    }

    public boolean getDebug()
    {
        return this.debug;
    }

    public int getLastOp()
    {
        return this.lastOp;
    }

    public LDAPMessage getNextRequest() throws ASN1Exception
    {
        LDAPMessage request = new LDAPMessage();
        request.decode( getBERDecoder() );
        return request;
    }

    public void sendResponse( LDAPMessage response ) throws Exception
    {
        getBEREncoder().init();
        response.encode( getBEREncoder() );
        getBEREncoder().finish();
        getBEREncoder().writeTo( this.bufOut );
        bufOut.flush();
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/18/2000 9:05:35 AM)
     * @param newAuthCred org.codehaus.server.Credentials
     */
    public void setAuthCred( Credentials newAuthCred )
    {
        authCred = newAuthCred;
    }

    private void setBERDecoder( BERDecoder berDecoder )
    {
        this.berDecoder = berDecoder;
    }

    private void setBEREncoder( BEREncoder berEncoder )
    {
        this.berEncoder = berEncoder;
    }

    public void setClient( Socket client ) throws IOException
    {
        this.client = client;

        if ( getBEREncoder() == null )
        {
            setBEREncoder( new BEREncoder() );
        }
        this.bufOut = new BufferedOutputStream( client.getOutputStream() );
        this.bufIn = new BufferedInputStream( client.getInputStream() );

        if ( getBERDecoder() == null )
        {
            setBERDecoder( new BERDecoder( this.bufIn ) );
        }
        else
        {
            getBERDecoder().setInputStream( this.bufIn );
        }

    }

    public void setDebug( boolean debug )
    {
        this.debug = debug;
    }

    public void setLastOp( int lastOp )
    {
        this.lastOp = lastOp;
    }
}
