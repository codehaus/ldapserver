package org.codehaus.plexus.ldapserver.server.backend;



/**
 * Backend for Queries to a subschema entry
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.ldapv3.Filter;
import org.codehaus.plexus.ldapserver.ldapv3.SearchRequestEnum;
import org.codehaus.plexus.ldapserver.server.Entry;
import org.codehaus.plexus.ldapserver.server.EntrySet;
import org.codehaus.plexus.ldapserver.server.schema.ObjectClass;
import org.codehaus.plexus.ldapserver.server.schema.SchemaChecker;
import org.codehaus.plexus.ldapserver.server.syntax.DirectoryString;
import org.codehaus.plexus.ldapserver.server.util.DirectoryException;
import org.codehaus.plexus.ldapserver.server.util.InvalidDNException;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class BackendSchema extends BaseBackend
{

    public EntrySet get( DirectoryString base, int scope, Filter filter, boolean attrsOnly, Vector attrs ) throws DirectoryException
    {
        if ( scope == SearchRequestEnum.BASEOBJECT && base.equals( new DirectoryString( "cn=schema" ) ) )
        {
            Vector entries = new Vector();
            entries.addElement( new Long( 1 ) );
            return (EntrySet) new GenericEntrySet( this, entries );
        }
        return (EntrySet) new GenericEntrySet( this, new Vector() );
    }

    public Entry getByID( Long id )
    {

        Entry schemaEntry = null;
        try
        {
            schemaEntry = new Entry( new DirectoryString( "cn=schema" ) );
        }
        catch ( InvalidDNException ide )
        {
        }


        Vector objClasses = new Vector();
        Hashtable classes = SchemaChecker.getInstance().getObjectClasses();
        Enumeration classEnum = classes.keys();
        while ( classEnum.hasMoreElements() )
        {
            ObjectClass aClass = (ObjectClass) classes.get( (DirectoryString) classEnum.nextElement() );
            objClasses.addElement( new DirectoryString( aClass.toString() ) );
        }
        schemaEntry.put( new DirectoryString( "objectclasses" ), objClasses );
        return schemaEntry;
    }
}
