// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ByteArrayComparator.java

package com.ibm.util;


// Referenced classes of package com.ibm.util:
//            Comparator

public class ByteArrayComparator extends Comparator
{

    public ByteArrayComparator(boolean flag)
    {
        mask = flag ? 255 : -1;
    }

    public int compare(Object obj, Object obj1)
    {
        byte abyte0[] = (byte[])obj;
        byte abyte1[] = (byte[])obj1;
        for(int j = 0; j < abyte0.length && j < abyte1.length; j++)
        {
            int i;
            if((i = (abyte0[j] & mask) - abyte1[j] & mask) != 0)
                return i;
        }

        return abyte0.length - abyte1.length;
    }

    private int mask;
}
