package org.codehaus.plexus.ldapserver.server.backend;



/**
 * A JDBCEntrySet contains a reference to a ResultSet and can be used to iterate
 * through the entries returned by BackendJDBC.
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.server.Entry;
import org.codehaus.plexus.ldapserver.server.EntrySet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCEntrySet implements EntrySet
{
    private ResultSet rs = null;
    private Connection dbcon = null;
    private Backend myBackend = null;
    private boolean hasMore = false;

    /**
     * GenericEntrySet constructor comment.
     */
    public JDBCEntrySet()
    {
        super();
    }

    /**
     * GenericEntrySet constructor comment.
     */
    public JDBCEntrySet( Backend myBackend, ResultSet rs, Connection dbcon )
    {
        super();
        this.myBackend = myBackend;
        this.rs = rs;
        this.dbcon = dbcon;
        try
        {
            if ( rs.next() )
            {
                this.hasMore = true;
            }
            else
            {
                this.rs = null;
            }
        }
        catch ( SQLException se )
        {
            se.printStackTrace();
        }
    }

    protected void finalize() throws Throwable
    {
        super.finalize();
        if ( dbcon != null )
        {
            BackendJDBCConnPool.getInstance().checkIn( dbcon );
        }
        rs = null;
    }

    public Entry getNext()
    {
        if ( hasMore == false )
        {
            return null;
        }

        Entry entry = null;

        try
        {
            byte[] entryBytes = rs.getBytes( 1 );
            entry = new Entry( entryBytes );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        try
        {
            if ( !rs.next() )
            {
                hasMore = false;
                this.rs = null;
                BackendJDBCConnPool.getInstance().checkIn( dbcon );
            }
        }
        catch ( SQLException se )
        {
            se.printStackTrace();
            hasMore = false;
            this.rs = null;
            BackendJDBCConnPool.getInstance().checkIn( dbcon );
        }
        return entry;

    }

    public boolean hasMore()
    {
        return hasMore;
    }
}
