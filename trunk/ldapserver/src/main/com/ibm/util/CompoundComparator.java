// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CompoundComparator.java

package com.ibm.util;


// Referenced classes of package com.ibm.util:
//            Comparator

public class CompoundComparator extends Comparator
{

    public CompoundComparator(Comparator comparator, Comparator comparator1)
    {
        major = comparator;
        minor = comparator1;
    }

    public int compare(Object obj, Object obj1)
    {
        int i = major.compare(obj, obj1);
        if(i != 0)
            return i;
        else
            return minor.compare(obj, obj1);
    }

    private Comparator major;
    private Comparator minor;
}
