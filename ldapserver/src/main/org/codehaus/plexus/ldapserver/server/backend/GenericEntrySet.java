package org.codehaus.plexus.ldapserver.server.backend;


/**
 * A generic entry set contains a Vector of Entry objects with the entire
 * result set.
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.server.Entry;
import org.codehaus.plexus.ldapserver.server.EntrySet;

import java.util.Vector;

public class GenericEntrySet implements EntrySet
{
    private Vector entries = null;
    private Backend myBackend = null;
    private int entryCount = 0;
    private boolean hasMore = false;

    public GenericEntrySet()
    {
        super();
    }

    /**
     * GenericEntrySet constructor comment.
     */
    public GenericEntrySet( Backend myBackend, Vector entries )
    {
        super();
        this.myBackend = myBackend;
        this.entries = entries;
        if ( !entries.isEmpty() )
        {
            this.hasMore = true;
            this.entryCount = 0;
        }
    }

    public Entry getNext()
    {
        if ( hasMore == false )
        {
            return null;
        }

        Entry current = (Entry) myBackend.getByID( (Long) entries.elementAt( entryCount ) );
        if ( entryCount < entries.size() - 1 )
        {
            entryCount++;
        }
        else
        {
            hasMore = false;
        }

        return current;
    }

    public boolean hasMore()
    {
        return hasMore;
    }
}
