package org.codehaus.plexus.ldapserver.server.backend;



/**
 * Backends must implement this interface or subclass another class that implements
 * this interface.
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.ldapv3.Filter;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPResultEnum;
import org.codehaus.plexus.ldapserver.server.Entry;
import org.codehaus.plexus.ldapserver.server.EntrySet;
import org.codehaus.plexus.ldapserver.server.syntax.DirectoryString;
import org.codehaus.plexus.ldapserver.server.util.DirectoryException;

import java.util.Vector;

public interface Backend
{

    public abstract LDAPResultEnum add( Entry entry );

    public abstract LDAPResultEnum delete( DirectoryString name );

    public abstract EntrySet get( DirectoryString base, int scope, Filter filter,
                                  boolean typesOnly, Vector attributes ) throws DirectoryException;

    public abstract Entry getByDN( DirectoryString dn ) throws DirectoryException;

    public abstract Entry getByID( Long id );

    public abstract void modify( DirectoryString name, Vector changeEntries ) throws DirectoryException;

    public abstract LDAPResultEnum rename( DirectoryString oldname, DirectoryString newname );
}
