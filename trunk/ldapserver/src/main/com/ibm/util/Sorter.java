// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Sorter.java

package com.ibm.util;

import java.util.Enumeration;
import java.util.Vector;

// Referenced classes of package com.ibm.util:
//            ByteArrayComparator, CaseFoldingStringComparator, ComparableComparator, Comparator, 
//            ReverseComparator, StringComparator

public class Sorter
{

    public Sorter()
    {
        comparator = stringComparator;
    }

    public Sorter(Comparator comparator1)
    {
        comparator = stringComparator;
        comparator = comparator1;
    }

    public Sorter(Comparator comparator1, boolean flag)
    {
        comparator = stringComparator;
        if(flag)
        {
            if(comparator1 instanceof ReverseComparator)
                comparator = ((ReverseComparator)comparator1).getOrigComparator();
            else
                comparator = new ReverseComparator(comparator1);
        }
        else
        {
            comparator = comparator1;
        }
    }

    public Comparator getComparator()
    {
        return comparator;
    }

    private final void quickSort(Object aobj[], int ai[], int i, int j)
    {
        int k = i;
        int l = j;
        Object obj = aobj[k];
        int i1 = 0;
        if(ai != null)
            i1 = ai[k];
        while(k < l) 
        {
            while(comparator.compare(obj, aobj[l]) <= 0 && k < l) 
                l--;

            aobj[k] = aobj[l];
            if(ai != null)
                ai[k] = ai[l];
            for(; comparator.compare(aobj[k], obj) <= 0 && k < l; k++);
            aobj[l] = aobj[k];
            if(ai != null)
                ai[l] = ai[k];
        }

        aobj[k] = obj;
        if(ai != null)
            ai[k] = i1;
        if(i < k - 1)
            quickSort(aobj, ai, i, k - 1);
        if(k + 1 < j)
            quickSort(aobj, ai, k + 1, j);
    }

    public Sorter reverseSorter()
    {
        if(comparator instanceof ReverseComparator)
            return new Sorter(((ReverseComparator)comparator).getOrigComparator());
        else
            return new Sorter(new ReverseComparator(comparator));
    }

    public Enumeration sort(Enumeration enumeration)
    {
        Vector vector = new Vector();
        for(; enumeration.hasMoreElements(); vector.addElement(enumeration.nextElement()));
        vector = sort(vector);
        return vector.elements();
    }

    public Vector sort(Vector vector)
    {
        Object aobj[] = new Object[vector.size()];
        vector.copyInto(aobj);
        aobj = sort(aobj);
        Vector vector1 = new Vector(aobj.length);
        for(int i = 0; i < aobj.length; i++)
            vector1.addElement(aobj[i]);

        return vector1;
    }

    public Object[] sort(Object aobj[])
    {
        return sort(aobj, 0, aobj.length);
    }

    public Object[] sort(Object aobj[], int i, int j)
    {
        Object aobj1[] = new Object[j];
        System.arraycopy(((Object) (aobj)), i, ((Object) (aobj1)), 0, j);
        sortIt(aobj1, null, 0, aobj1.length - 1);
        return aobj1;
    }

    public void sortDestructive(Object aobj[])
    {
        sortIt(aobj, null, 0, aobj.length - 1);
    }

    public void sortDestructive(Object aobj[], int i, int j)
    {
        sortIt(aobj, null, i, (i + j) - 1);
    }

    protected void sortIt(Object aobj[], int ai[], int i, int j)
    {
        if(i >= j)
        {
            return;
        }
        else
        {
            quickSort(aobj, ai, i, j);
            return;
        }
    }

    public int[] sortOrder(Object aobj[], boolean flag)
    {
        if(!flag)
        {
            Object aobj1[] = aobj;
            aobj = new Object[aobj1.length];
            System.arraycopy(((Object) (aobj1)), 0, ((Object) (aobj)), 0, aobj1.length);
        }
        int ai[] = new int[aobj.length];
        for(int i = 0; i < ai.length; i++)
            ai[i] = i;

        sortIt(aobj, ai, 0, aobj.length - 1);
        return ai;
    }

    public static final ComparableComparator comparableComparator;
    public static final StringComparator stringComparator = new StringComparator();
    public static final CaseFoldingStringComparator caseFoldingStringComparator;
    public static final ByteArrayComparator byteArrayComparator;
    public static final ByteArrayComparator unsignedByteArrayComparator;
    public static final Sorter comparableSorter;
    public static final Sorter stringSorter = new Sorter();
    public static final Sorter caseFoldingStringSorter;
    public static final Sorter byteArraySorter;
    public static final Sorter unsignedByteArraySorter;
    protected Comparator comparator;

    static 
    {
        comparableComparator = new ComparableComparator();
        caseFoldingStringComparator = new CaseFoldingStringComparator();
        byteArrayComparator = new ByteArrayComparator(false);
        unsignedByteArrayComparator = new ByteArrayComparator(true);
        comparableSorter = new Sorter(comparableComparator, false);
        caseFoldingStringSorter = new Sorter(caseFoldingStringComparator, false);
        byteArraySorter = new Sorter(byteArrayComparator, false);
        unsignedByteArraySorter = new Sorter(unsignedByteArrayComparator, false);
    }
}
