package org.codehaus.plexus.ldapserver.server.operation;



import org.codehaus.plexus.ldapserver.ldapv3.AttributeTypeAndValues;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPMessage;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPMessageChoice;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPResultEnum;
import org.codehaus.plexus.ldapserver.ldapv3.ModifyRequestSeqOfSeq;
import org.codehaus.plexus.ldapserver.ldapv3.ModifyResponse;
import org.codehaus.plexus.ldapserver.ldapv3.Referral;
import org.codehaus.plexus.ldapserver.server.Credentials;
import org.codehaus.plexus.ldapserver.server.EntryChange;
import org.codehaus.plexus.ldapserver.server.backend.BackendHandler;
import org.codehaus.plexus.ldapserver.server.syntax.DirectoryString;
import org.codehaus.plexus.ldapserver.server.util.DirectoryException;
import org.codehaus.plexus.ldapserver.server.util.DNUtility;

import java.util.Enumeration;
import java.util.Vector;


public class ModifyOperation implements Operation
{

    LDAPMessage request = null;
    LDAPMessage response = null;
    Credentials creds = null;

    public ModifyOperation( Credentials creds, LDAPMessage request )
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

        DirectoryString name = new DirectoryString( this.request.protocolOp.modifyRequest.object );

        Vector changeVector = new Vector();
        for ( Enumeration enumMods = this.request.protocolOp.modifyRequest.modification.elements();
              enumMods.hasMoreElements(); )
        {
            ModifyRequestSeqOfSeq oneMod = (ModifyRequestSeqOfSeq) enumMods.nextElement();
            int modType = oneMod.operation.value;
            AttributeTypeAndValues modification = oneMod.modification;
            DirectoryString modAttr = new DirectoryString( modification.type );
            Vector modValues = new Vector();
            for ( Enumeration enumVals = modification.vals.elements(); enumVals.hasMoreElements(); )
            {
                DirectoryString modValue = new DirectoryString( (byte[]) enumVals.nextElement() );
                modValues.addElement( modValue );
            }
            //BackendHandler.Handler().oneChange(name,modType,modAttr,modValues);
            EntryChange oneChange = new EntryChange( modType, modAttr, modValues );
            changeVector.addElement( oneChange );
        }

        this.response = new LDAPMessage();
        ModifyResponse modifyResponse = new ModifyResponse();
        modifyResponse.resultCode = new LDAPResultEnum( 0 );
        modifyResponse.matchedDN = new byte[0];
        modifyResponse.errorMessage = new byte[0];
        modifyResponse.referral = new Referral();

        try
        {
            BackendHandler.Handler().modify( creds, DNUtility.getInstance().normalize( name ), changeVector );
        }
        catch ( DirectoryException de )
        {
            modifyResponse.resultCode = new LDAPResultEnum( de.getLDAPErrorCode() );
            if ( de.getMessage() != null )
            {
                modifyResponse.errorMessage = de.getMessage().getBytes();
            }
        }

        LDAPMessageChoice op = new LDAPMessageChoice();
        op.choiceId = LDAPMessageChoice.MODIFYRESPONSE_CID;
        op.modifyResponse = modifyResponse;

        this.response.messageID = this.request.messageID;
        this.response.protocolOp = op;

    }
}
