package org.codehaus.plexus.ldapserver.server.backend;



/**
 * A Backend that handles requests for the root naming context.
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.ldapv3.Filter;
import org.codehaus.plexus.ldapserver.ldapv3.SearchRequestEnum;
import org.codehaus.plexus.ldapserver.server.Entry;
import org.codehaus.plexus.ldapserver.server.EntrySet;
import org.codehaus.plexus.ldapserver.server.syntax.DirectoryString;
import org.codehaus.plexus.ldapserver.server.util.DirectoryException;
import org.codehaus.plexus.ldapserver.server.util.InvalidDNException;

import java.util.Enumeration;
import java.util.Vector;

public class BackendRoot extends BaseBackend
{

    public EntrySet get( DirectoryString base, int scope, Filter filter, boolean attrsOnly, Vector attrs ) throws DirectoryException
    {
        if ( scope == SearchRequestEnum.BASEOBJECT && base.equals( new DirectoryString( "" ) ) )
        {
            Vector entries = new Vector();
            entries.addElement( new Long( 1 ) );
            return (EntrySet) new GenericEntrySet( this, entries );
        }
        return (EntrySet) new GenericEntrySet( this, new Vector() );
    }

    public Entry getByID( Long id )
    {

        Entry rootEntry = null;
        try
        {
            rootEntry = new Entry( new DirectoryString( "" ) );
        }
        catch ( InvalidDNException ide )
        {
        }


        Vector namingContexts = new Vector();
        Vector objectClass = new Vector();
        Vector subschemaEntry = new Vector();
        Vector supportedLDAPVersion = new Vector();
        //Vector supportedSASLMechanisms = new Vector();
        //Vector supportedControl = new Vector();

        Enumeration ncEnum = BackendHandler.Handler().getHandlerTable().keys();
        while ( ncEnum.hasMoreElements() )
        {
            DirectoryString nc = (DirectoryString) ncEnum.nextElement();
            if ( !nc.equals( new DirectoryString( "" ) ) && !nc.equals( new DirectoryString( "cn=schema" ) ) )
                namingContexts.addElement( nc );
        }

        objectClass.addElement( new DirectoryString( "top" ) );

        subschemaEntry.addElement( new DirectoryString( "cn=schema" ) );

        supportedLDAPVersion.addElement( new DirectoryString( "3" ) );

        rootEntry.put( new DirectoryString( "namingContexts" ), namingContexts );
        rootEntry.put( new DirectoryString( "objectClass" ), objectClass );
        rootEntry.put( new DirectoryString( "subschemaEntry" ), subschemaEntry );
        rootEntry.put( new DirectoryString( "supportedLDAPVersion" ), supportedLDAPVersion );


        return rootEntry;
    }
}
