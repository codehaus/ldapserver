package org.codehaus.plexus.ldapserver.btree;

/***********************************************************************
 * B-Tree
 *
 *	by L.Horisberger & G.Schweizer
 * last change: 26.02.1998 by horil
 **/


/**
 * Class KeyNode
 * An Container were an object with a given key is stored.
 **/
class KeyNode
{
    private Comparator key;
    private Object data;

    KeyNode( Comparator key, Object data )
    {
        this.key = key;
        this.data = data;
    }

    Comparator getKey()
    {
        return key;
    }

    Object getObj()
    {
        return data;
    }
}
