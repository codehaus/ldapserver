package org.codehaus.plexus.ldapserver.btree;

/***********************************************************************
 * B-Tree
 *
 *	by L.Horisberger & G.Schweizer
 * last change: 26.02.1998 by horil
 **/


/**
 * Class SearchResult
 * A dummy class for saving the search result
 **/
class SearchResult
{
    private BTNode btnode;
    private int keyIndex;

    SearchResult( BTNode btnode, int keyIndex )
    {
        this.btnode = btnode;
        this.keyIndex = keyIndex;
    }

    BTNode getBTNode()
    {
        return btnode;
    }

    int getKeyIndex()
    {
        return keyIndex;
    }
}
