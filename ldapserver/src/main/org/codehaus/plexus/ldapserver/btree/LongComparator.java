package org.codehaus.plexus.ldapserver.btree;

/***********************************************************************
 * B-Tree Test programm
 *
 *	by L.Horisberger & G.Schweizer
 * last change: 26.02.1998 by horil
 **/


class LongComparator implements Comparator
{
    protected Long number;

    public LongComparator( Long val )
    {
        number = val;
    }

    public int compareTo( Object obj )
    {
        long result = number.longValue() - ( (LongComparator) obj ).number.longValue();
        if ( result < 0 ) return -1;
        if ( result == 0 ) return 0;
        return 1;
    }
}
