package org.codehaus.plexus.ldapserver.server.operation;

/**
 * AddOperation calls the BackendHandler to add a new entry and builds an
 * appropriate LDAP Result.
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.ldapv3.AddResponse;
import org.codehaus.plexus.ldapserver.ldapv3.AttributeList;
import org.codehaus.plexus.ldapserver.ldapv3.AttributeListSeq;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPMessage;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPMessageChoice;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPResultEnum;
import org.codehaus.plexus.ldapserver.ldapv3.Referral;
import org.codehaus.plexus.ldapserver.server.Credentials;
import org.codehaus.plexus.ldapserver.server.Entry;
import org.codehaus.plexus.ldapserver.server.backend.BackendHandler;
import org.codehaus.plexus.ldapserver.server.syntax.DirectoryString;
import org.codehaus.plexus.ldapserver.server.util.DirectorySchemaViolation;
import org.codehaus.plexus.ldapserver.server.util.InvalidDNException;

import java.math.BigInteger;
import java.util.Enumeration;
import java.util.Vector;

public class AddOperation implements Operation
{

    LDAPMessage request = null;
    LDAPMessage response = null;
    Credentials creds = null;

    public AddOperation( Credentials creds, LDAPMessage request )
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
        this.response = new LDAPMessage();
        AddResponse addResponse = new AddResponse();
        addResponse.matchedDN = new byte[0];
        addResponse.errorMessage = new byte[0];
        addResponse.referral = new Referral();

        try
        {
            addResponse.resultCode = BackendHandler.Handler().add( creds, requestToEntry() );
        }
        catch ( DirectorySchemaViolation dsv )
        {
            addResponse.resultCode = new LDAPResultEnum( 65 );
            addResponse.errorMessage = dsv.getMessage().getBytes();
        }
        catch ( InvalidDNException ide )
        {
            addResponse.resultCode = new LDAPResultEnum( 34 );
            if ( ide.getMessage() != null )
            {
                addResponse.errorMessage = ide.getMessage().getBytes();
            }
        }

        LDAPMessageChoice op = new LDAPMessageChoice();
        op.choiceId = LDAPMessageChoice.ADDRESPONSE_CID;
        op.addResponse = addResponse;

        this.response.messageID = new BigInteger( this.request.messageID.toString() );
        this.response.protocolOp = op;

    }

    public Entry requestToEntry() throws InvalidDNException
    {

        Entry entry = new Entry( new DirectoryString( this.request.protocolOp.addRequest.entry ) );
        AttributeList attrList = this.request.protocolOp.addRequest.attributes;

        for ( Enumeration enumAttr = attrList.elements(); enumAttr.hasMoreElements(); )
        {
            AttributeListSeq als = (AttributeListSeq) enumAttr.nextElement();
            DirectoryString type = new DirectoryString( als.type );
            als.type = null;
            Vector values = new Vector();
            for ( Enumeration enumVal = als.vals.elements(); enumVal.hasMoreElements(); )
            {
                byte[] thisVal = (byte[]) enumVal.nextElement();
                if ( thisVal.length > 0 )
                    values.addElement( new DirectoryString( thisVal ) );
            }
            als.vals = null;
            if ( values.size() > 0 )
            {
                entry.put( type, values );
            }
        }
        return entry;
    }
}
