package org.codehaus.plexus.ldapserver.server;


/**
 * The MessageHandler is responsible for looking at BER-decoded objects to
 * understand the LDAP operation requests and call appropriate operations to
 * return results.
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import com.ibm.asn1.*;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPMessage;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPMessageChoice;
import org.codehaus.plexus.ldapserver.server.operation.AddOperation;
import org.codehaus.plexus.ldapserver.server.operation.BindOperation;
import org.codehaus.plexus.ldapserver.server.operation.CompareOperation;
import org.codehaus.plexus.ldapserver.server.operation.DeleteOperation;
import org.codehaus.plexus.ldapserver.server.operation.ModifyOperation;
import org.codehaus.plexus.ldapserver.server.operation.RenameOperation;
import org.codehaus.plexus.ldapserver.server.operation.SearchOperation;
import org.codehaus.plexus.ldapserver.server.util.DirectoryBindException;
import org.codehaus.plexus.ldapserver.server.util.DirectoryException;

import java.io.IOException;

public class MessageHandler
{
    private Connection connection = null;

    //private boolean debug = false;

    public MessageHandler()
    {
        setConnection( new Connection() );
    }

    public MessageHandler( Connection connection ) throws IOException
    {
        setConnection( connection );
    }

    public boolean answerRequest( LDAPMessage request )
    {

        switch ( request.protocolOp.choiceId )
        {
            case LDAPMessageChoice.BINDREQUEST_CID:
                return doBind( request );
            case LDAPMessageChoice.SEARCHREQUEST_CID:
                return doSearch( request );
            case LDAPMessageChoice.MODIFYREQUEST_CID:
                return doModify( request );
            case LDAPMessageChoice.ADDREQUEST_CID:
                return doAdd( request );
            case LDAPMessageChoice.DELREQUEST_CID:
                return doDelete( request );
            case LDAPMessageChoice.MODDNREQUEST_CID:
                return doRename( request );
            case LDAPMessageChoice.COMPAREREQUEST_CID:
                return doCompare( request );
            case LDAPMessageChoice.ABANDONREQUEST_CID:
                return true;
            case LDAPMessageChoice.UNBINDREQUEST_CID:
                return false;
        }
        // Unrecognized request
        return false;
    }

    public boolean doAdd( LDAPMessage request )
    {
        AddOperation addop = new AddOperation( connection.getAuthCred(), request );
        addop.perform();
        try
        {
            sendResponse( addop.getResponse() );
        }
        catch ( DirectoryException de )
        {
            return false;
        }
        return true;
    }

    public boolean doBind( LDAPMessage request )
    {
        BindOperation bindop = new BindOperation( request );
        try
        {
            bindop.perform();
        }
        catch ( DirectoryBindException dbe )
        {
            try
            {
                sendResponse( bindop.getResponse() );
            }
            catch ( DirectoryException de )
            {
                return false;
            }
            return false;
        }
        try
        {
            sendResponse( bindop.getResponse() );
        }
        catch ( DirectoryException de )
        {
            return false;
        }
        getConnection().setAuthCred( bindop.getCreds() );
        return true;
    }

    public boolean doCompare( LDAPMessage request )
    {
        CompareOperation compop = new CompareOperation( request );
        compop.perform();
        try
        {
            sendResponse( compop.getResponse() );
        }
        catch ( DirectoryException de )
        {
            return false;
        }
        return true;
    }

    public boolean doDelete( LDAPMessage request )
    {
        DeleteOperation delop = new DeleteOperation( connection.getAuthCred(), request );
        delop.perform();
        try
        {
            sendResponse( delop.getResponse() );
        }
        catch ( DirectoryException de )
        {
            return false;
        }
        return true;
    }

    public boolean doModify( LDAPMessage request )
    {
        ModifyOperation modop = new ModifyOperation( connection.getAuthCred(), request );
        modop.perform();
        try
        {
            sendResponse( modop.getResponse() );
        }
        catch ( DirectoryException de )
        {
            return false;
        }
        return true;
    }

    public boolean doRename( LDAPMessage request )
    {
        RenameOperation renop = new RenameOperation( connection.getAuthCred(), request );
        renop.perform();

        try
        {
            sendResponse( renop.getResponse() );
        }
        catch ( DirectoryException de )
        {
            return false;
        }
        return true;
    }

    public boolean doSearch( LDAPMessage request )
    {
        SearchOperation searchop = new SearchOperation( connection.getAuthCred(), request );
        while ( searchop.isMore() == true )
        {
            searchop.perform();
            try
            {
                sendResponse( searchop.getResponse() );
            }
            catch ( DirectoryException de )
            {
                return false;
            }
        }
        return true;
    }

    public Connection getConnection()
    {
        return this.connection;
    }

    public LDAPMessage getNextRequest()
    {
        LDAPMessage request = null;

        try
        {
            request = getConnection().getNextRequest();
        }
        catch ( ASN1Exception e )
        {
            System.out.println( "Error: " + e );
            return null;
        }

        if ( getConnection().getDebug() == true && request.messageID != null )
        {
            request.print( System.out );
        }

        return request;
    }

    public void reset()
    {
        getConnection().setAuthCred( new org.codehaus.plexus.ldapserver.server.Credentials() );
    }

    public void sendResponse( LDAPMessage response ) throws DirectoryException
    {
        try
        {
            getConnection().sendResponse( response );
        }
        catch ( Exception e )
        {
            // IO or ASN1
            throw new DirectoryException( "Error Communicating with Client: " + e );
        }

        if ( getConnection().getDebug() == true && response.messageID != null )
        {
            response.print( System.out );
        }
    }
    //private BEREncoder berEncoder = null;
    //private BERDecoder berDecoder = null;

    public void setConnection( Connection connection )
    {
        this.connection = connection;
    }
}
