package org.codehaus.plexus.ldapserver.btree;

/*
  File: Comparator.java

  Biel School of Engineering, Computer Science Division.
  Course "Algorithms with Objects in Java"
  Originally written by Roger Cattin (c) 1997/98

  History:
  Date     Who                 What
  Nov97    ctr@info.isbiel.ch  Created.
*/

/**
 * Comparator is an interface for any class possessing an ordering
 * comparison method.
 **/

public interface Comparator
{
    /**
     * Compare two Objects with respect to ordering. Typical
     * implementations first cast their arguments to particular
     * types in order to perform comparison.
     * @param obj object to be compared with this
     * @return a negative number if this is less than obj; a
     * positive number if this is greater than obj; else 0
     **/
    public int compareTo( Object obj );
}
