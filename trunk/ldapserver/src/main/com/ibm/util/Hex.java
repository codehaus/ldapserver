// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Hex.java

package com.ibm.util;


public class Hex
{

    public Hex()
    {
    }

    public static byte[] toByteArray(String s)
    {
        return toByteArray(s, 0, s.length());
    }

    public static byte[] toByteArray(String s, int i, int j)
    {
        if(j % 2 != 0)
            throw new IllegalArgumentException("Illegal length of Hex encoding: " + j + " (not n*2)");
        if(j == 0)
            return new byte[0];
        byte abyte0[] = new byte[j / 2];
        int k = 0;
        int l = 0;
        for(int i1 = i + j; i < i1;)
        {
            int j1 = Character.digit(s.charAt(i), 16);
            if(j1 < 0)
                throw new IllegalArgumentException("Illegal characters in Hex encoding: " + s.charAt(i));
            k = (k << 4) + j1;
            if(l % 2 == 1)
                abyte0[l / 2] = (byte)k;
            i++;
            l++;
        }

        return abyte0;
    }

    public static String toString(byte abyte0[])
    {
        return toString(abyte0, 0, abyte0.length, true);
    }

    public static String toString(byte abyte0[], int i, int j)
    {
        return toString(abyte0, i, j, true);
    }

    public static String toString(byte abyte0[], int i, int j, boolean flag)
    {
        String s = flag ? "0123456789ABCDEF" : "0123456789abcdef";
        StringBuffer stringbuffer = new StringBuffer(j * 2);
        for(int k = i + j; i < k; i++)
        {
            stringbuffer.append(s.charAt((abyte0[i] & 0xf0) >>> 4));
            stringbuffer.append(s.charAt(abyte0[i] & 0xf));
        }

        return stringbuffer.toString();
    }

    public static String toString(byte abyte0[], boolean flag)
    {
        return toString(abyte0, 0, abyte0.length, flag);
    }

    private static final String upperCaseAlphabet = "0123456789ABCDEF";
    private static final String lowerCaseAlphabet = "0123456789abcdef";
}
