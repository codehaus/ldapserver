package org.codehaus.plexus.ldapserver.btree;

/**
 * Insert the type's description here.
 * Creation date: (6/17/2000 9:04:15 PM)
 * @author: Administrator
 */

import java.util.Enumeration;

public class BTreeNewTest
{

    /**
     * BTreeNewTest constructor comment.
     */
    public BTreeNewTest()
    {
        super();
    }

    /**
     * Starts the application.
     * @param args an array of command-line arguments
     */
    public static void main( java.lang.String[] args )
    {
        // Insert code to start the application here.
        BTree btree = new BTree( 10 );
        btree.put( "one", "no1" );
        btree.put( "two", "no2" );
        btree.put( "three", "no3" );
        btree.put( "four", "no4" );
        btree.put( "five", "no5" );
        for ( int i = 1; i < 100000; i++ )
        {
            btree.put( "key" + i, "value" + i );
        }

        Enumeration btenum = btree.keys();
        while ( btenum.hasMoreElements() )
        {
            System.out.println( "Key: " + (String) ( (StringComparator) btenum.nextElement() ).getKey() );
        }
    }
}
