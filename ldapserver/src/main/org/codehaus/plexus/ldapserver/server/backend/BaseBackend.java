package org.codehaus.plexus.ldapserver.server.backend;


/**
 * A simple backend that rejects all attempted operations. A useful
 * base for developing backends that only allow particular operations.
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.ldapv3.LDAPResultEnum;
import org.codehaus.plexus.ldapserver.server.Entry;
import org.codehaus.plexus.ldapserver.server.EntrySet;
import org.codehaus.plexus.ldapserver.server.syntax.DirectoryString;
import org.codehaus.plexus.ldapserver.server.util.DirectoryException;

import java.util.Vector;

public class BaseBackend implements Backend
{


    /**
     * add method comment.
     */
    public org.codehaus.plexus.ldapserver.ldapv3.LDAPResultEnum add( org.codehaus.plexus.ldapserver.server.Entry entry )
    {
        return new LDAPResultEnum( 53 );
    }

    /**
     * delete method comment.
     */
    public org.codehaus.plexus.ldapserver.ldapv3.LDAPResultEnum delete( DirectoryString name )
    {
        return new LDAPResultEnum( 53 );
    }

    /**
     * get method comment.
     */
    public EntrySet get( DirectoryString base, int scope, org.codehaus.plexus.ldapserver.ldapv3.Filter filter,
                         boolean typesOnly, java.util.Vector attributes ) throws DirectoryException
    {
        return (EntrySet) new GenericEntrySet( this, new Vector() );
    }

    public Entry getByDN( DirectoryString dn ) throws DirectoryException
    {
        throw new DirectoryException( 32 );
    }

    public Entry getByID( Long id )
    {
        return new Entry();
    }

    /**
     * modify method comment.
     */
    public void modify( DirectoryString name, java.util.Vector changeEntries ) throws org.codehaus.plexus.ldapserver.server.util.DirectoryException
    {
        throw new DirectoryException( 53 );
    }

    /**
     * rename method comment.
     */
    public org.codehaus.plexus.ldapserver.ldapv3.LDAPResultEnum rename( DirectoryString oldname, DirectoryString newname )
    {
        return new LDAPResultEnum( 53 );
    }
}
