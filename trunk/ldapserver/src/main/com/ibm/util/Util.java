// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Util.java

package com.ibm.util;


public class Util
{

    public Util()
    {
    }

    public static int arraycmp(byte abyte0[], int i, int j, byte abyte1[], int k, int l)
    {
        int i1;
        for(int j1 = Math.min(j, l); j1-- > 0;)
            if((i1 = abyte0[i++] - abyte1[k++]) != 0)
                return i1;

        return j - l;
    }

    public static int arraycmp(byte abyte0[], byte abyte1[])
    {
        return arraycmp(abyte0, 0, abyte0.length, abyte1, 0, abyte1.length);
    }

    public static void arrayset(int i, byte abyte0[], int j, int k)
    {
        while(k-- > 0) 
            abyte0[j++] = (byte)i;

    }

    public static void intLSBF(int i, byte abyte0[], int j)
    {
        abyte0[j] = (byte)i;
        abyte0[j + 1] = (byte)(i >> 8);
        abyte0[j + 2] = (byte)(i >> 16);
        abyte0[j + 3] = (byte)(i >> 24);
    }

    public static int intLSBF(byte abyte0[], int i)
    {
        return (abyte0[i] & 0xff) + ((abyte0[i + 1] & 0xff) << 8) + ((abyte0[i + 2] & 0xff) << 16) + (abyte0[i + 3] << 24);
    }

    public static void intMSBF(int i, byte abyte0[], int j)
    {
        abyte0[j] = (byte)(i >> 24);
        abyte0[j + 1] = (byte)(i >> 16);
        abyte0[j + 2] = (byte)(i >> 8);
        abyte0[j + 3] = (byte)i;
    }

    public static int intMSBF(byte abyte0[], int i)
    {
        return (abyte0[i] << 24) + ((abyte0[i + 1] & 0xff) << 16) + ((abyte0[i + 2] & 0xff) << 8) + (abyte0[i + 3] & 0xff);
    }

    public static void longLSBF(long l, byte abyte0[], int i)
    {
        abyte0[i] = (byte)(int)l;
        abyte0[i + 1] = (byte)(int)(l >> 8);
        abyte0[i + 2] = (byte)(int)(l >> 16);
        abyte0[i + 3] = (byte)(int)(l >> 24);
        abyte0[i + 4] = (byte)(int)(l >> 32);
        abyte0[i + 5] = (byte)(int)(l >> 40);
        abyte0[i + 6] = (byte)(int)(l >> 48);
        abyte0[i + 7] = (byte)(int)(l >> 56);
    }

    public static long longLSBF(byte abyte0[], int i)
    {
        return ((long)abyte0[i] & 255L) + (((long)abyte0[i + 1] & 255L) << 8) + (((long)abyte0[i + 2] & 255L) << 16) + (((long)abyte0[i + 3] & 255L) << 24) + (((long)abyte0[i + 4] & 255L) << 32) + (((long)abyte0[i + 5] & 255L) << 40) + (((long)abyte0[i + 6] & 255L) << 48) + ((long)abyte0[i + 7] << 56);
    }

    public static void longMSBF(long l, byte abyte0[], int i)
    {
        abyte0[i] = (byte)(int)(l >> 56);
        abyte0[i + 1] = (byte)(int)(l >> 48);
        abyte0[i + 2] = (byte)(int)(l >> 40);
        abyte0[i + 3] = (byte)(int)(l >> 32);
        abyte0[i + 4] = (byte)(int)(l >> 24);
        abyte0[i + 5] = (byte)(int)(l >> 16);
        abyte0[i + 6] = (byte)(int)(l >> 8);
        abyte0[i + 7] = (byte)(int)l;
    }

    public static long longMSBF(byte abyte0[], int i)
    {
        return ((long)abyte0[i] << 56) + (((long)abyte0[i + 1] & 255L) << 48) + (((long)abyte0[i + 2] & 255L) << 40) + (((long)abyte0[i + 3] & 255L) << 32) + (((long)abyte0[i + 4] & 255L) << 24) + (((long)abyte0[i + 5] & 255L) << 16) + (((long)abyte0[i + 6] & 255L) << 8) + ((long)abyte0[i + 7] & 255L);
    }

    public static void shortLSBF(int i, byte abyte0[], int j)
    {
        abyte0[j] = (byte)i;
        abyte0[j + 1] = (byte)(i >> 8);
    }

    public static int shortLSBF(byte abyte0[], int i)
    {
        return (abyte0[i] & 0xff) + ((abyte0[i + 1] & 0xff) << 8);
    }

    public static void shortMSBF(int i, byte abyte0[], int j)
    {
        abyte0[j] = (byte)(i >> 8);
        abyte0[j + 1] = (byte)i;
    }

    public static int shortMSBF(byte abyte0[], int i)
    {
        return ((abyte0[i] & 0xff) << 8) + (abyte0[i + 1] & 0xff);
    }
}
