// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EncBuf.java

package com.ibm.asn1;


final class EncBuf
{

    EncBuf(int i)
    {
        next = null;
        data = new byte[i];
    }

    EncBuf(EncBuf encbuf)
    {
        next = null;
        data = new byte[encbuf.data.length];
    }

    byte data[];
    EncBuf next;
}
