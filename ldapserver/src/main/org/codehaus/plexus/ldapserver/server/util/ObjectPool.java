package org.codehaus.plexus.ldapserver.server.util;


/**
 * ObjectPool cleanup thread based on code from Javaworld
 * Issue: August, 1998
 * @author: Thomas E. Davis
 */

import java.util.Enumeration;
import java.util.Hashtable;

public abstract class ObjectPool
{

    private long expirationTime;
    private long lastCheckOut;

    private Hashtable locked;
    private Hashtable unlocked;

    private CleanUpThread cleaner;


    /**
     * ObjectPool constructor comment.
     */
    public ObjectPool()
    {

        expirationTime = ( 1000 * 30 ); // 30 seconds

        locked = new Hashtable();
        unlocked = new Hashtable();

        lastCheckOut = System.currentTimeMillis();

        cleaner = new CleanUpThread( this, expirationTime );
        cleaner.start();
    }

    public synchronized void checkIn( Object o )
    {
        if ( o != null )
        {
            locked.remove( o );
            unlocked.put( o, new Long( System.currentTimeMillis() ) );
        }
    }

    public synchronized Object checkOut() throws Exception
    {
        long now = System.currentTimeMillis();
        lastCheckOut = now;
        Object o;

        if ( unlocked.size() > 0 )
        {
            Enumeration e = unlocked.keys();

            while ( e.hasMoreElements() )
            {
                o = e.nextElement();

                if ( validate( o ) )
                {
                    unlocked.remove( o );
                    locked.put( o, new Long( now ) );
                    return ( o );
                }
                else
                {
                    unlocked.remove( o );
                    expire( o );
                    o = null;
                }
            }
        }

        o = create();

        locked.put( o, new Long( now ) );
        return ( o );
    }

    public synchronized void cleanUp()
    {
        Object o;

        long now = System.currentTimeMillis();

        Enumeration e = unlocked.keys();

        while ( e.hasMoreElements() )
        {
            o = e.nextElement();

            if ( ( now - ( (Long) unlocked.get( o ) ).longValue() ) > expirationTime )
            {
                unlocked.remove( o );
                expire( o );
                o = null;
            }
        }

        //System.gc();
    }

    public abstract Object create() throws Exception;

    public abstract void expire( Object o );

    public abstract boolean validate( Object o );
}
