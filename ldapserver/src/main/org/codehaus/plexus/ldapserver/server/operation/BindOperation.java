package org.codehaus.plexus.ldapserver.server.operation;



/**
 * BindOperation performs an LDAP Bind operation. It currently ignores credentials
 * and returns SUCCESS.
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.ldapv3.AuthenticationChoice;
import org.codehaus.plexus.ldapserver.ldapv3.BindResponse;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPMessage;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPMessageChoice;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPResultEnum;
import org.codehaus.plexus.ldapserver.ldapv3.Referral;
import org.codehaus.plexus.ldapserver.server.Credentials;
import org.codehaus.plexus.ldapserver.server.Entry;
import org.codehaus.plexus.ldapserver.server.backend.BackendHandler;
import org.codehaus.plexus.ldapserver.server.syntax.DirectoryString;
import org.codehaus.plexus.ldapserver.server.util.DirectoryBindException;
import org.codehaus.plexus.ldapserver.server.util.DirectoryException;
import org.codehaus.plexus.ldapserver.server.util.ServerConfig;

import java.util.Vector;

public class BindOperation implements Operation
{

    LDAPMessage request = null;
    LDAPMessage response = null;
    boolean success = false;
    Credentials creds = null;

    private static final DirectoryString USERPASSWORD = new DirectoryString( "userpassword" );

    public BindOperation( LDAPMessage request )
    {
        this.request = request;
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/18/2000 10:38:16 AM)
     * @return org.codehaus.server.Credentials
     */
    public org.codehaus.plexus.ldapserver.server.Credentials getCreds()
    {
        return creds;
    }

    public LDAPMessage getResponse()
    {
        return this.response;
    }

    public void perform() throws DirectoryBindException
    {

        this.response = new LDAPMessage();

        LDAPMessageChoice op = new LDAPMessageChoice();
        BindResponse bindResponse = new BindResponse();

        bindResponse.resultCode = new LDAPResultEnum( 0 );
        bindResponse.matchedDN = new byte[0];
        bindResponse.errorMessage = new byte[0];
        bindResponse.referral = new Referral();
        bindResponse.serverSaslCreds = new byte[0];

        op.choiceId = LDAPMessageChoice.BINDRESPONSE_CID;
        op.bindResponse = bindResponse;

        this.response.messageID = this.request.messageID;
        this.response.protocolOp = op;


        if ( this.request.protocolOp.bindRequest != null )
        {
            AuthenticationChoice ac = this.request.protocolOp.bindRequest.authentication;
            DirectoryString subject = new DirectoryString( this.request.protocolOp.bindRequest.name );
            // Currently only checking Simple Auth, need to add SASL support and support for encrypted passwords
            if ( ac.choiceId == ac.SIMPLE_CID )
            {
                // First check for the Root User
                String pw = new String( ac.simple );
                if ( new DirectoryString( (String) ServerConfig.getInstance().get( ServerConfig.JAVALDAP_ROOTUSER ) ).equals( subject ) )
                {
                    if ( ( (String) ServerConfig.getInstance().get( ServerConfig.JAVALDAP_ROOTPW ) ).equals( pw ) )
                    {
                        creds = new Credentials();
                        creds.setUser( subject );
                        return;
                    }
                    bindResponse.resultCode = new LDAPResultEnum( 49 );
                    return;
                }
                Entry bindEnt = null;
                try
                {
                    bindEnt = BackendHandler.Handler().getByDN( subject );
                }
                catch ( DirectoryException de )
                {
                }
                if ( bindEnt == null || !bindEnt.containsKey( USERPASSWORD ) )
                {
                    bindResponse.resultCode = new LDAPResultEnum( 49 );
                }
                else
                {
                    if ( pw.equals( ( (DirectoryString) ( (Vector) bindEnt.get( USERPASSWORD ) ).elementAt( 0 ) ).toString() ) )
                    {
                        creds = new Credentials();
                        creds.setUser( subject );
                    }
                    else
                    {
                        bindResponse.resultCode = new LDAPResultEnum( 49 );
                    }
                }
            }
        }
        return;
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/18/2000 10:38:16 AM)
     * @param newCreds org.codehaus.server.Credentials
     */
    public void setCreds( org.codehaus.plexus.ldapserver.server.Credentials newCreds )
    {
        creds = newCreds;
    }
}
