// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ASN1Encoder.java

package com.ibm.asn1;

import com.ibm.util.BitString;
import java.math.BigInteger;
import java.util.Calendar;

// Referenced classes of package com.ibm.asn1:
//            ASN1Tag, ASN1Any, ASN1Exception, ASN1OID

public abstract class ASN1Encoder extends ASN1Tag
{

    public ASN1Encoder()
    {
    }

    public void encodeAny(ASN1Any asn1any)
        throws ASN1Exception
    {
        encodeAny(asn1any.data, asn1any.begin, asn1any.length);
    }

    public void encodeAny(byte abyte0[])
        throws ASN1Exception
    {
        encodeAny(abyte0, 0, abyte0.length);
    }

    public abstract void encodeAny(byte abyte0[], int i, int j)
        throws ASN1Exception;

    public abstract void encodeBMPString(String s)
        throws ASN1Exception;

    public abstract void encodeBitString(BitString bitstring)
        throws ASN1Exception;

    public abstract void encodeBoolean(boolean flag)
        throws ASN1Exception;

    public int encodeChoice(int i, int ai[])
        throws ASN1Exception
    {
        return -1;
    }

    public abstract boolean encodeDefault()
        throws ASN1Exception;

    public abstract void encodeEnumeration(int i)
        throws ASN1Exception;

    public abstract int encodeExplicit(int i)
        throws ASN1Exception;

    public abstract void encodeGeneralString(String s)
        throws ASN1Exception;

    public abstract void encodeGeneralizedTime(String s)
        throws ASN1Exception;

    public abstract void encodeGeneralizedTime(Calendar calendar)
        throws ASN1Exception;

    public abstract void encodeGraphicString(String s)
        throws ASN1Exception;

    public abstract void encodeIA5String(String s)
        throws ASN1Exception;

    public void encodeISO646String(String s)
        throws ASN1Exception
    {
        encodeVisibleString(s);
    }

    public abstract void encodeInteger(int i)
        throws ASN1Exception;

    public abstract void encodeInteger(long l)
        throws ASN1Exception;

    public abstract void encodeInteger(BigInteger biginteger)
        throws ASN1Exception;

    public abstract void encodeNull()
        throws ASN1Exception;

    public abstract void encodeNumericString(String s)
        throws ASN1Exception;

    public abstract void encodeObjectIdentifier(ASN1OID asn1oid)
        throws ASN1Exception;

    public void encodeOctetString(byte abyte0[])
        throws ASN1Exception
    {
        encodeOctetString(abyte0, 0, abyte0.length);
    }

    public abstract void encodeOctetString(byte abyte0[], int i, int j)
        throws ASN1Exception;

    public abstract void encodePrintableString(String s)
        throws ASN1Exception;

    public abstract void encodeReal(double d)
        throws ASN1Exception;

    public abstract int encodeSequence()
        throws ASN1Exception;

    public abstract int encodeSequenceOf()
        throws ASN1Exception;

    public abstract int encodeSet()
        throws ASN1Exception;

    public abstract int encodeSetOf()
        throws ASN1Exception;

    public int encodeSimple()
    {
        return -1;
    }

    public abstract void encodeT61String(String s)
        throws ASN1Exception;

    public void encodeTeletexString(String s)
        throws ASN1Exception
    {
        encodeT61String(s);
    }

    public abstract void encodeUTCTime(String s)
        throws ASN1Exception;

    public abstract void encodeUTCTime(Calendar calendar)
        throws ASN1Exception;

    public abstract void encodeVideotexString(String s)
        throws ASN1Exception;

    public abstract void encodeVisibleString(String s)
        throws ASN1Exception;

    public abstract void endOf(int i)
        throws ASN1Exception;

    public abstract void finish()
        throws ASN1Exception;

    public abstract void nextHasAlphabetConstraint(String s)
        throws ASN1Exception;

    public abstract void nextHasSizeConstraints(long l, long l1)
        throws ASN1Exception;

    public abstract void nextIsImplicit(int i)
        throws ASN1Exception;

    public void setDefinedName(String s)
    {
    }

    public abstract String toString();
}
