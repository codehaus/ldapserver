package org.codehaus.plexus.ldapserver.btree;

/***********************************************************************
 * B-Tree Test programm
 *
 *	by L.Horisberger & G.Schweizer
 * last change: 26.02.1998 by horil
 **/


class IntegerComparator implements Comparator
{
    protected int number;

    public IntegerComparator( int i )
    {
        number = i;
    }

    public int compareTo( Object obj )
    {
        return number - ( (IntegerComparator) obj ).number;
    }
}
