// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ASN1Any.java

package com.ibm.asn1;

import com.ibm.util.Util;
import java.io.Serializable;

// Referenced classes of package com.ibm.asn1:
//            ASN1Decoder, ASN1Encoder, ASN1Exception, ASN1Type

public abstract class ASN1Any
    implements ASN1Type, Serializable
{

    public ASN1Any()
    {
    }

    public ASN1Any(ASN1Any asn1any)
    {
        tag = asn1any.tag;
        begin = asn1any.begin;
        valueBegin = asn1any.valueBegin;
        length = asn1any.length;
        data = (byte[])asn1any.data.clone();
    }

    public Object clone()
    {
        throw new RuntimeException("Subclass of ASN1Any *must* implement clone()");
    }

    public void decode(ASN1Decoder asn1decoder)
        throws ASN1Exception
    {
        ASN1Any asn1any = asn1decoder.decodeAny();
        tag = asn1any.tag;
        begin = asn1any.begin;
        valueBegin = asn1any.valueBegin;
        length = asn1any.length;
        data = asn1any.data;
    }

    public void encode(ASN1Encoder asn1encoder)
        throws ASN1Exception
    {
        asn1encoder.encodeAny(this);
    }

    public boolean equals(Object obj)
    {
        if(obj == null || !getClass().equals(obj.getClass()))
        {
            return false;
        }
        else
        {
            ASN1Any asn1any = (ASN1Any)obj;
            return length == asn1any.length && Util.arraycmp(data, begin, length, asn1any.data, asn1any.begin, asn1any.length) == 0;
        }
    }

    public abstract ASN1Decoder getDecoder();

    public abstract String getEncoding();

    static final long serialVersionUID = 0x3a1b24f1e0c749d8L;
    public int tag;
    public int begin;
    public int valueBegin;
    public int length;
    public byte data[];
}
