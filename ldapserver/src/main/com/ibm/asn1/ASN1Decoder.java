// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ASN1Decoder.java

package com.ibm.asn1;

import com.ibm.util.BitString;
import java.math.BigInteger;
import java.util.Calendar;

// Referenced classes of package com.ibm.asn1:
//            ASN1Tag, ASN1Exception, ASN1Any, ASN1OID

public abstract class ASN1Decoder extends ASN1Tag
{

    public ASN1Decoder()
    {
    }

    public abstract ASN1Any decodeAny()
        throws ASN1Exception;

    public abstract byte[] decodeAnyAsByteArray()
        throws ASN1Exception;

    public abstract String decodeBMPString()
        throws ASN1Exception;

    public abstract BitString decodeBitString()
        throws ASN1Exception;

    public abstract boolean decodeBoolean()
        throws ASN1Exception;

    public int decodeChoice(int ai[])
        throws ASN1Exception
    {
        return peekNextTag();
    }

    public abstract int decodeEnumeration()
        throws ASN1Exception;

    public abstract int decodeExplicit(int i)
        throws ASN1Exception;

    public abstract String decodeGeneralString()
        throws ASN1Exception;

    public abstract Calendar decodeGeneralizedTime()
        throws ASN1Exception;

    public abstract String decodeGraphicString()
        throws ASN1Exception;

    public abstract String decodeIA5String()
        throws ASN1Exception;

    public String decodeISO646String()
        throws ASN1Exception
    {
        return decodeVisibleString();
    }

    public abstract BigInteger decodeInteger()
        throws ASN1Exception;

    public abstract int decodeIntegerAsInt()
        throws ASN1Exception;

    public abstract long decodeIntegerAsLong()
        throws ASN1Exception;

    public abstract void decodeNull()
        throws ASN1Exception;

    public abstract String decodeNumericString()
        throws ASN1Exception;

    public abstract ASN1OID decodeObjectIdentifier()
        throws ASN1Exception;

    public abstract byte[] decodeOctetString()
        throws ASN1Exception;

    public abstract int decodeOctetString(byte abyte0[], int i)
        throws ASN1Exception;

    public abstract ASN1Any decodeOctetStringAsAny()
        throws ASN1Exception;

    public abstract String decodePrintableString()
        throws ASN1Exception;

    public abstract double decodeReal()
        throws ASN1Exception;

    public abstract int decodeSequence()
        throws ASN1Exception;

    public abstract int decodeSequenceOf()
        throws ASN1Exception;

    public abstract int decodeSet()
        throws ASN1Exception;

    public abstract int decodeSetOf()
        throws ASN1Exception;

    public abstract String decodeT61String()
        throws ASN1Exception;

    public String decodeTeletexString()
        throws ASN1Exception
    {
        return decodeT61String();
    }

    public abstract Calendar decodeUTCTime()
        throws ASN1Exception;

    public abstract String decodeVideotexString()
        throws ASN1Exception;

    public abstract String decodeVisibleString()
        throws ASN1Exception;

    public abstract boolean endOf(int i)
        throws ASN1Exception;

    public abstract void nextHasAlphabetConstraints(String s)
        throws ASN1Exception;

    public abstract void nextHasSizeConstraints(long l, long l1)
        throws ASN1Exception;

    public abstract boolean nextIsDefault(int i)
        throws ASN1Exception;

    public abstract void nextIsImplicit(int i)
        throws ASN1Exception;

    public abstract boolean nextIsOptional(int i)
        throws ASN1Exception;

    public abstract int peekNextTag()
        throws ASN1Exception;

    public abstract int peekNextValueLength()
        throws ASN1Exception;

    public abstract void skipNext()
        throws ASN1Exception;

    public abstract String toString();
}
