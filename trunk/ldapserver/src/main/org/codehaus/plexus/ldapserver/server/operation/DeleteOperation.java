package org.codehaus.plexus.ldapserver.server.operation;



/**
 * DeleteOperation attempts to delete an entry
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.ldapv3.DelResponse;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPMessage;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPMessageChoice;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPResultEnum;
import org.codehaus.plexus.ldapserver.ldapv3.Referral;
import org.codehaus.plexus.ldapserver.server.Credentials;
import org.codehaus.plexus.ldapserver.server.util.InvalidDNException;
import org.codehaus.plexus.ldapserver.server.util.DNUtility;
import org.codehaus.plexus.ldapserver.server.backend.BackendHandler;
import org.codehaus.plexus.ldapserver.server.syntax.DirectoryString;

public class DeleteOperation implements Operation
{

    LDAPMessage request = null;
    LDAPMessage response = null;
    Credentials creds = null;

    public DeleteOperation( Credentials creds, LDAPMessage request )
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

        LDAPResultEnum resultCode = null;
        try
        {
            resultCode =
                BackendHandler.Handler().delete( creds, DNUtility.getInstance().normalize( new DirectoryString( this.request.protocolOp.delRequest.value ) ) );
        }
        catch ( InvalidDNException ide )
        {
            resultCode = new LDAPResultEnum( 34 );
        }
        this.response = new LDAPMessage();
        LDAPMessageChoice op = new LDAPMessageChoice();
        DelResponse delResponse = new DelResponse();

        delResponse.resultCode = resultCode;
        delResponse.matchedDN = new byte[0];
        delResponse.errorMessage = new byte[0];
        delResponse.referral = new Referral();

        op.choiceId = LDAPMessageChoice.DELRESPONSE_CID;
        op.delResponse = delResponse;

        this.response.messageID = this.request.messageID;
        this.response.protocolOp = op;

    }
}
