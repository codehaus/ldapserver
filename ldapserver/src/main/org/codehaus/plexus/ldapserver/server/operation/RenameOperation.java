package org.codehaus.plexus.ldapserver.server.operation;


import org.codehaus.plexus.ldapserver.ldapv3.LDAPMessage;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPMessageChoice;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPResultEnum;
import org.codehaus.plexus.ldapserver.ldapv3.ModifyDNResponse;
import org.codehaus.plexus.ldapserver.ldapv3.Referral;
import org.codehaus.plexus.ldapserver.server.Credentials;
import org.codehaus.plexus.ldapserver.server.backend.BackendHandler;
import org.codehaus.plexus.ldapserver.server.syntax.DirectoryString;


public class RenameOperation implements Operation
{

    LDAPMessage request = null;
    LDAPMessage response = null;
    Credentials creds = null;

    public RenameOperation( Credentials creds, LDAPMessage request )
    {
        this.request = request;
        this.creds = creds;
    }

    public LDAPMessage getResponse()
    {
        return this.response;
    }

    public void perform()
    {

        DirectoryString name =
            new DirectoryString( this.request.protocolOp.modDNRequest.entry );
        DirectoryString newname =
            new DirectoryString( this.request.protocolOp.modDNRequest.newrdn );
        boolean deleterdn =
            this.request.protocolOp.modDNRequest.deleteoldrdn;
        DirectoryString newSuperior =
            new DirectoryString( this.request.protocolOp.modDNRequest.newSuperior );

        this.response = new LDAPMessage();
        ModifyDNResponse modDNResponse = new ModifyDNResponse();
        modDNResponse.resultCode = new LDAPResultEnum( 0 );
        modDNResponse.matchedDN = new byte[0];
        modDNResponse.errorMessage = new byte[0];
        modDNResponse.referral = new Referral();

        LDAPMessageChoice op = new LDAPMessageChoice();
        op.choiceId = LDAPMessageChoice.MODDNRESPONSE_CID;
        op.modDNResponse = modDNResponse;

        try
        {
            BackendHandler.Handler().rename( creds, name, newname );
        }
        catch ( org.codehaus.plexus.ldapserver.server.util.DirectoryException de )
        {
            modDNResponse.resultCode = new LDAPResultEnum( de.getLDAPErrorCode() );
            if ( de.getMessage() != null )
            {
                modDNResponse.errorMessage = de.getMessage().getBytes();
            }
        }

        this.response.messageID = this.request.messageID;
        this.response.protocolOp = op;

    }
}
