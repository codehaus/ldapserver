// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ComparableComparator.java

package com.ibm.util;


// Referenced classes of package com.ibm.util:
//            Comparator, Comparable

public class ComparableComparator extends Comparator
{

    public ComparableComparator()
    {
    }

    public int compare(Object obj, Object obj1)
    {
        return ((Comparable)obj).compareTo((Comparable)obj1);
    }
}
