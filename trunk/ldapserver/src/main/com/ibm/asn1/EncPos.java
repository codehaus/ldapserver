// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EncPos.java

package com.ibm.asn1;


class EncPos
{

    EncPos()
    {
        info = 0;
        length = 0;
    }

    final int getEncodedTag()
    {
        return implicitTag != -1 ? implicitTag : tag;
    }

    final int getTLLength()
    {
        return info & 0xf;
    }

    final int getUnderLyingTag()
    {
        return tag;
    }

    final int howManyChildren()
    {
        return info != -1 ? (info << 1) >>> 5 : 0;
    }

    final void init()
    {
        info = tag = implicitTag = length = offset = 0;
        parent = null;
    }

    final boolean isPrimitive()
    {
        return info == -1;
    }

    final boolean isSetOf()
    {
        return (info & 0x80000000) != 0;
    }

    final int oneMoreChild()
    {
        return info += 16;
    }

    final void setPrimitive()
    {
        info = -1;
    }

    final void setSetOfFlag()
    {
        info |= 0x80000000;
    }

    final void setTLLength(int i)
    {
        info += i;
    }

    final void setTag(int i, int j)
    {
        tag = i;
        implicitTag = j;
    }

    int info;
    int tag;
    int implicitTag;
    int length;
    int offset;
    EncPos parent;
}
