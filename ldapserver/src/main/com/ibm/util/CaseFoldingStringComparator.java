// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CaseFoldingStringComparator.java

package com.ibm.util;


// Referenced classes of package com.ibm.util:
//            Comparator

public class CaseFoldingStringComparator extends Comparator
{

    public CaseFoldingStringComparator()
    {
    }

    public int compare(Object obj, Object obj1)
    {
        return obj.toString().toLowerCase().compareTo(obj1.toString().toLowerCase());
    }
}
