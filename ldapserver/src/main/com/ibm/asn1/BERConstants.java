// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BERConstants.java

package com.ibm.asn1;


interface BERConstants
{

    public static final int UNIVERSAL_TAG_CODE = 0;
    public static final int APPLICATION_TAG_CODE = 64;
    public static final int CONTEXT_TAG_CODE = 128;
    public static final int PRIVATE_TAG_CODE = 192;
    public static final int BER_TAG_CLASS_MASK = 192;
    public static final boolean PRIMITIVE = false;
    public static final boolean CONSTRUCTED = true;
    public static final int CONSTRUCTED_BIT = 32;
    public static final int BIGTAGCODE = 31;
    public static final int TAGMASK = 31;
    public static final int MAXSMALLTAG = 30;
    public static final int INDEFINITE_LEN = 128;
}
