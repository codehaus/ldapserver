// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ReverseComparator.java

package com.ibm.util;


// Referenced classes of package com.ibm.util:
//            Comparator

public class ReverseComparator extends Comparator
{

    public ReverseComparator(Comparator comparator)
    {
        orig = comparator;
    }

    public int compare(Object obj, Object obj1)
    {
        return orig.compare(obj1, obj);
    }

    public Comparator getOrigComparator()
    {
        return orig;
    }

    private Comparator orig;
}
