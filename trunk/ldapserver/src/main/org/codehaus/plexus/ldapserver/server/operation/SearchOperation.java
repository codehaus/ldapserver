package org.codehaus.plexus.ldapserver.server.operation;



import org.codehaus.plexus.ldapserver.ldapv3.Filter;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPMessage;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPMessageChoice;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPResultEnum;
import org.codehaus.plexus.ldapserver.ldapv3.PartialAttributeList;
import org.codehaus.plexus.ldapserver.ldapv3.PartialAttributeListSeq;
import org.codehaus.plexus.ldapserver.ldapv3.PartialAttributeListSeqSetOf;
import org.codehaus.plexus.ldapserver.ldapv3.Referral;
import org.codehaus.plexus.ldapserver.ldapv3.SearchResultDone;
import org.codehaus.plexus.ldapserver.ldapv3.SearchResultEntry;
import org.codehaus.plexus.ldapserver.server.Credentials;
import org.codehaus.plexus.ldapserver.server.Entry;
import org.codehaus.plexus.ldapserver.server.EntrySet;
import org.codehaus.plexus.ldapserver.server.acl.ACLChecker;
import org.codehaus.plexus.ldapserver.server.backend.BackendHandler;
import org.codehaus.plexus.ldapserver.server.syntax.DirectoryString;
import org.codehaus.plexus.ldapserver.server.util.DirectoryException;
import org.codehaus.plexus.ldapserver.server.util.DNUtility;
import org.codehaus.plexus.ldapserver.server.util.InvalidDNException;

import java.util.Enumeration;
import java.util.Vector;

public class SearchOperation implements Operation
{

    LDAPMessage request = null;
    LDAPMessage response = null;
    Credentials creds = null;

    boolean more = true;
    Vector entries = null;
    int nextEntry = 0;
    Enumeration backEnum;
    Vector attributes;
    boolean typesOnly;
    EntrySet currentEntrySet = null;

    public SearchOperation( Credentials creds, LDAPMessage request )
    {
        this.request = request;
        this.creds = creds;
    }

    private PartialAttributeList attributeListFromEntry( Entry entry, boolean returnValues, Vector attrs )
    {
        PartialAttributeList pal = new PartialAttributeList();
        for ( Enumeration enumTypes = entry.keys(); enumTypes.hasMoreElements(); )
        {
            DirectoryString curType = (DirectoryString) enumTypes.nextElement();
            if ( ( attrs.size() == 0 || attrs.contains( curType ) ) &&
                ( ACLChecker.getInstance().isAllowed( creds, new Character( 'r' ), entry.getName(), curType ) ||
                ACLChecker.getInstance().isAllowed( creds, new Character( 's' ), entry.getName(), curType ) ) )
            {
                PartialAttributeListSeq pals = new PartialAttributeListSeq();
                pals.type = curType.getBytes();
                pals.vals = new PartialAttributeListSeqSetOf();
                if ( !returnValues )
                {
                    Vector values = (Vector) entry.get( curType );
                    for ( Enumeration enumVals = values.elements(); enumVals.hasMoreElements(); )
                    {
                        DirectoryString curValue = (DirectoryString) enumVals.nextElement();
                        pals.vals.put( curValue.getBytes(), curValue.getBytes() );
                    }
                }
                pal.addElement( pals );
            }
        }
        return pal;
    }

    public LDAPMessage getResponse()
    {
        return this.response;
    }

    public boolean isMore()
    {
        return this.more;
    }

    public void perform()
    {
        // Create a new response message and operation choice
        this.response = new LDAPMessage();
        LDAPMessageChoice op = new LDAPMessageChoice();

        // Set the messageID in the response to be the same as the one in the request to which we are responding
        this.response.messageID = this.request.messageID;

        // If no results are present, initialize the search
        if ( this.entries == null )
        {
            // Set the search base, scope, filter, returned attribute types, etc... from the incoming request
            DirectoryString base = null;
            try
            {
                base = DNUtility.getInstance().normalize( new DirectoryString( request.protocolOp.searchRequest.baseObject ) );
            }
            catch ( InvalidDNException ide )
            {
                ide.printStackTrace();
            }
            int scope = request.protocolOp.searchRequest.scope.value;
            Filter filter = request.protocolOp.searchRequest.filter;
            typesOnly = request.protocolOp.searchRequest.typesOnly;
            attributes = new Vector();
            for ( Enumeration enumAttrs = request.protocolOp.searchRequest.attributes.elements(); enumAttrs.hasMoreElements(); )
            {
                DirectoryString oneAttr = new DirectoryString( (byte[]) enumAttrs.nextElement() );
                attributes.addElement( oneAttr );
            }

            // Call the backend handler to actually perform the search on all applicable backends
            try
            {
                this.entries = BackendHandler.Handler().get( base, scope, filter, typesOnly, attributes );
            }
            catch ( DirectoryException de )
            {
                // If we get a directory exception, return it and end the search
                SearchResultDone srd = new SearchResultDone();
                srd.resultCode = new LDAPResultEnum( de.getLDAPErrorCode() );
                srd.matchedDN = new byte[0];
                if ( de.getMessage() != null )
                {
                    srd.errorMessage = de.getMessage().getBytes();
                }
                else
                {
                    srd.errorMessage = new byte[0];
                }
                srd.referral = new Referral();
                op.choiceId = LDAPMessageChoice.SEARCHRESDONE_CID;
                op.searchResDone = srd;
                this.response.protocolOp = op;
                this.more = false;
                return;
            }
        }

        // If no backend is active, advance to the first backend with matching results
        if ( backEnum == null )
        {
            backEnum = entries.elements();
            if ( backEnum.hasMoreElements() )
            {
                currentEntrySet = (EntrySet) backEnum.nextElement();
            }
            this.nextEntry = 0;
        }

        boolean canProvideResult = false;
        while ( !canProvideResult )
        {
            // If we're out of entries in all backends, return the SearchResDone message type
            if ( currentEntrySet == null || !currentEntrySet.hasMore() && !backEnum.hasMoreElements() )
            {
                SearchResultDone srd = new SearchResultDone();
                srd.resultCode = new LDAPResultEnum( 0 );
                srd.matchedDN = new byte[0];
                srd.errorMessage = new byte[0];
                srd.referral = new Referral();
                op.choiceId = LDAPMessageChoice.SEARCHRESDONE_CID;
                op.searchResDone = srd;
                this.response.protocolOp = op;
                this.more = false;
                return;
            }

            // If this backend has no more entries, advance to the next backend and reset the entry counter
            if ( !currentEntrySet.hasMore() )
            {
                currentEntrySet = (EntrySet) backEnum.nextElement();
                this.nextEntry = 0;
            }


            // Get the next entryID from the current backend
            Entry currentEntry = currentEntrySet.getNext();

            if ( ACLChecker.getInstance().isAllowed( creds, ACLChecker.PERM_BROWSEDN, currentEntry.getName() ) ||
                ACLChecker.getInstance().isAllowed( creds, ACLChecker.PERM_RETURNDN, currentEntry.getName() ) )
            {
                // Create a new SearchResultEntry object
                SearchResultEntry sre = new SearchResultEntry();

                // Set the Name of this result and convert the Entry object into attributes
                sre.objectName = currentEntry.getName().getBytes();
                sre.attributes = attributeListFromEntry( currentEntry, typesOnly, attributes );

                // We set the current message type to be an LDAP search result entry
                op.choiceId = LDAPMessageChoice.SEARCHRESENTRY_CID;

                // Place the entire result into the proper ASN.1 object
                op.searchResEntry = sre;

                // Place the current operation into the response message
                this.response.protocolOp = op;

                // Advance the entry counter to the next entry
                this.nextEntry++;

                return;
            }
            this.nextEntry++;
        }
    }
}
