// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BaseBEREncoder.java

package com.ibm.asn1;

import com.ibm.util.BitString;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Calendar;

// Referenced classes of package com.ibm.asn1:
//            ASN1Encoder, ASN1Exception, ASN1OID, ASN1Tag, 
//            BERConstants

public abstract class BaseBEREncoder extends ASN1Encoder
    implements BERConstants
{

    public BaseBEREncoder()
    {
    }

    private final void encode2DecimalDigits(int i)
        throws ASN1Exception
    {
        putOctet((i / 10) % 10 + 48);
        putOctet(i % 10 + 48);
    }

    public void encodeBMPString(String s)
        throws ASN1Exception
    {
        int i = s.length();
        encodeTagAndLength(false, 30, 2 * i);
        encodeStringValue(s, "UnicodeBigUnmarked");
    }

    public void encodeBitString(BitString bitstring)
        throws ASN1Exception
    {
        int i = bitstring.length();
        int j = 8 - (i & 0x7) & 0x7;
        encodeTagAndLength(false, 3, i + 15 >> 3);
        putOctet(j);
        for(i -= 8; i >= 0; i -= 8)
            putOctet(bitstring.toInt(i, 8));

        if(i != -8)
            putOctet((byte)(bitstring.toInt(0, 8 - j) << j));
    }

    public void encodeBoolean(boolean flag)
        throws ASN1Exception
    {
        encodeTagAndLength(false, 1, 1);
        putOctet(flag ? 255 : 0);
    }

    public boolean encodeDefault()
    {
        return true;
    }

    public void encodeEnumeration(int i)
        throws ASN1Exception
    {
        nextIsImplicit(10);
        encodeInteger(i);
    }

    public void encodeGeneralString(String s)
        throws ASN1Exception
    {
        int i = s.length();
        encodeTagAndLength(false, 27, i);
        encodeStringValue(s, "ISO8859_1");
    }

    public void encodeGeneralizedTime(String s)
        throws ASN1Exception
    {
        encodeTag(false, 24);
        encodeLength(s.length());
        encodeStringValue(s, "ISO8859_1");
    }

    public void encodeGeneralizedTime(Calendar calendar)
        throws ASN1Exception
    {
        int i = calendar.get(14);
        int j = calendar.get(15) / 60000;
        int k = 14 + (i != 0 ? i % 100 != 0 ? i % 10 != 0 ? ((byte) (4)) : 3 : 2 : 0);
        encodeTag(false, 24);
        encodeLength(k + (j == 0 ? 1 : 5));
        int l = calendar.get(1);
        encode2DecimalDigits(l / 100);
        encode2DecimalDigits(l % 100);
        encode2DecimalDigits(calendar.get(2) + 1);
        encode2DecimalDigits(calendar.get(5));
        encode2DecimalDigits(calendar.get(11));
        encode2DecimalDigits(calendar.get(12));
        encode2DecimalDigits(calendar.get(13));
        if(i != 0)
        {
            putOctet(46);
            putOctet((i / 100) % 10 + 48);
            if(k > 16)
                putOctet((i / 10) % 10 + 48);
            if(k > 17)
                putOctet(i % 10 + 48);
        }
        if(j == 0)
            putOctet(90);
        else
        if(j > 0)
        {
            putOctet(43);
            encode2DecimalDigits(j / 60);
            encode2DecimalDigits(j % 60);
        }
        else
        {
            putOctet(45);
            encode2DecimalDigits(-j / 60);
            encode2DecimalDigits(-j % 60);
        }
    }

    public void encodeGraphicString(String s)
        throws ASN1Exception
    {
        int i = s.length();
        encodeTagAndLength(false, 25, i);
        encodeStringValue(s, "ISO8859_1");
    }

    public void encodeIA5String(String s)
        throws ASN1Exception
    {
        int i = s.length();
        encodeTagAndLength(false, 22, i);
        encodeStringValue(s, "ISO8859_1");
    }

    public void encodeInteger(int i)
        throws ASN1Exception
    {
        byte byte0;
        if(i >= 0)
        {
            if(i >= 0x800000)
                byte0 = 4;
            else
            if(i >= 32768)
                byte0 = 3;
            else
            if(i >= 128)
                byte0 = 2;
            else
                byte0 = 1;
        }
        else
        if(i < 0xff800000)
            byte0 = 4;
        else
        if(i < -32768)
            byte0 = 3;
        else
        if(i < -128)
            byte0 = 2;
        else
            byte0 = 1;
        encodeTag(false, 2);
        encodeLength(byte0);
        for(int j = (byte0 - 1) * 8; j >= 0; j -= 8)
            putOctet((byte)(i >> j));

    }

    public void encodeInteger(long l)
        throws ASN1Exception
    {
        byte byte0;
        if(l >= 0L)
        {
            if(l >= 0x80000000000000L)
                byte0 = 8;
            else
            if(l >= 0x800000000000L)
                byte0 = 7;
            else
            if(l >= 0x8000000000L)
                byte0 = 6;
            else
            if(l >= 0x80000000L)
                byte0 = 5;
            else
            if(l >= 0x800000L)
                byte0 = 4;
            else
            if(l >= 32768L)
                byte0 = 3;
            else
            if(l >= 128L)
                byte0 = 2;
            else
                byte0 = 1;
        }
        else
        if(l < 0xff80000000000000L)
            byte0 = 8;
        else
        if(l < 0xffff800000000000L)
            byte0 = 7;
        else
        if(l < 0xffffff8000000000L)
            byte0 = 6;
        else
        if(l < 0xffffffff80000000L)
            byte0 = 5;
        else
        if(l < 0xffffffffff800000L)
            byte0 = 4;
        else
        if(l < -32768L)
            byte0 = 3;
        else
        if(l < -128L)
            byte0 = 2;
        else
            byte0 = 1;
        encodeTag(false, 2);
        encodeLength(byte0);
        for(int i = (byte0 - 1) * 8; i >= 0; i -= 8)
            putOctet((byte)(int)(l >> i));

    }

    public void encodeInteger(BigInteger biginteger)
        throws ASN1Exception
    {
        byte abyte0[] = biginteger.toByteArray();
        encodeTag(false, 2);
        encodeLength(abyte0.length);
        putOctets(abyte0, 0, abyte0.length);
    }

    protected void encodeLength(int i)
        throws ASN1Exception
    {
        if(i >= 128)
        {
            byte byte0;
            if(i >= 0x1000000)
                byte0 = 4;
            else
            if(i >= 0x10000)
                byte0 = 3;
            else
            if(i >= 256)
                byte0 = 2;
            else
                byte0 = 1;
            putOctet(0x80 | byte0);
            for(int j = (byte0 - 1) * 8; j >= 0; j -= 8)
                putOctet(i >> j);

        }
        else
        {
            putOctet(i);
        }
    }

    public void encodeNull()
        throws ASN1Exception
    {
        encodeTag(false, 5);
        encodeLength(0);
    }

    public void encodeNumericString(String s)
        throws ASN1Exception
    {
        int i = s.length();
        encodeTagAndLength(false, 18, i);
        encodeStringValue(s, "ISO8859_1");
    }

    public void encodeObjectIdentifier(ASN1OID asn1oid)
        throws ASN1Exception
    {
        int j1 = 1;
        int k = 2;
        for(int l = asn1oid.level(); k < l; k++)
        {
            int i = asn1oid.nthComponent(k);
            if(i >= 0x10000000)
                j1 += 5;
            else
            if(i >= 0x200000)
                j1 += 4;
            else
            if(i >= 16384)
                j1 += 3;
            else
            if(i >= 128)
                j1 += 2;
            else
                j1++;
        }

        encodeTagAndLength(false, 6, j1);
        putOctet(asn1oid.nthComponent(0) * 40 + asn1oid.nthComponent(1));
        k = 2;
        for(int i1 = asn1oid.level(); k < i1; k++)
        {
            int j = asn1oid.nthComponent(k);
            if(j >= 0x10000000)
                putOctet(j >>> 28 | 0x80);
            if(j >= 0x200000)
                putOctet(j >>> 21 | 0x80);
            if(j >= 16384)
                putOctet(j >>> 14 | 0x80);
            if(j >= 128)
                putOctet(j >>> 7 | 0x80);
            putOctet(j & 0x7f);
        }

    }

    public void encodeOctetString(byte abyte0[], int i, int j)
        throws ASN1Exception
    {
        encodeTagAndLength(false, 4, j);
        for(int k = 0; k < j; k++)
            putOctet(abyte0[k]);

    }

    public void encodePrintableString(String s)
        throws ASN1Exception
    {
        int i = s.length();
        encodeTagAndLength(false, 19, i);
        encodeStringValue(s, "ISO8859_1");
    }

    public void encodeReal(double d)
        throws ASN1Exception
    {
        throw new Error("Real encoding not yet implemented");
    }

    public int encodeSequenceOf()
        throws ASN1Exception
    {
        return encodeSequence();
    }

    public int encodeSetOf()
        throws ASN1Exception
    {
        return encodeSet();
    }

    protected void encodeStringValue(String s, String s1)
        throws ASN1Exception
    {
        try
        {
            byte abyte0[] = s.getBytes(s1);
            for(int i = 0; i < abyte0.length; i++)
                putOctet(abyte0[i]);

        }
        catch(UnsupportedEncodingException _ex)
        {
            throw new ASN1Exception("Character encoding " + s1 + " not supported");
        }
    }

    public void encodeT61String(String s)
        throws ASN1Exception
    {
        int i = s.length();
        encodeTagAndLength(false, 20, i);
        encodeStringValue(s, "ISO8859_1");
    }

    protected int encodeTag(boolean flag, int i)
        throws ASN1Exception
    {
        if(specialTag)
        {
            i = implicitTag;
            specialTag = false;
        }
        int j = ASN1Tag.getTagNumber(i);
        int k = flag ? 32 : 0;
        switch(ASN1Tag.getTagClass(i))
        {
        case 0: // '\0'
            k |= 0;
            break;

        case 1: // '\001'
            k |= 0x40;
            break;

        case 2: // '\002'
            k |= 0x80;
            break;

        case 3: // '\003'
            k |= 0xc0;
            break;

        }
        if(j > 30)
        {
            putOctet(k | 0x1f);
            if(j >= 0x10000000)
                putOctet(j >>> 28 | 0x80);
            if(j >= 0x200000)
                putOctet(j >>> 21 | 0x80);
            if(j >= 16384)
                putOctet(j >>> 14 | 0x80);
            if(j >= 128)
                putOctet(j >>> 7 | 0x80);
            putOctet(j & 0x7f);
        }
        else
        {
            putOctet(k | j);
        }
        return 0;
    }

    protected void encodeTagAndLength(boolean flag, int i, int j)
        throws ASN1Exception
    {
        encodeTag(flag, i);
        encodeLength(j);
    }

    public void encodeUTCTime(String s)
        throws ASN1Exception
    {
        encodeTag(false, 23);
        encodeLength(s.length());
        encodeStringValue(s, "ISO8859_1");
    }

    public void encodeUTCTime(Calendar calendar)
        throws ASN1Exception
    {
        encodeTag(false, 23);
        int i = calendar.get(15) / 60000;
        if(i != 0)
            encodeLength(17);
        else
            encodeLength(13);
        int j = calendar.get(1);
        encode2DecimalDigits(j < 2000 ? j - 1900 : j - 2000);
        encode2DecimalDigits(calendar.get(2) + 1);
        encode2DecimalDigits(calendar.get(5));
        encode2DecimalDigits(calendar.get(11));
        encode2DecimalDigits(calendar.get(12));
        encode2DecimalDigits(calendar.get(13));
        if(i == 0)
            putOctet(90);
        else
        if(i < 0)
        {
            putOctet(45);
            encode2DecimalDigits(-i / 60);
            encode2DecimalDigits(-i % 60);
        }
        else
        {
            putOctet(43);
            encode2DecimalDigits(i / 60);
            encode2DecimalDigits(i % 60);
        }
    }

    public void encodeVideotexString(String s)
        throws ASN1Exception
    {
        int i = s.length();
        encodeTagAndLength(false, 21, i);
        encodeStringValue(s, "ISO8859_1");
    }

    public void encodeVisibleString(String s)
        throws ASN1Exception
    {
        int i = s.length();
        encodeTagAndLength(false, 26, i);
        encodeStringValue(s, "ISO8859_1");
    }

    public void nextHasAlphabetConstraint(String s)
    {
    }

    public void nextHasSizeConstraints(long l, long l1)
    {
    }

    public void nextIsImplicit(int i)
    {
        if(!specialTag)
        {
            specialTag = true;
            implicitTag = i;
        }
    }

    protected abstract void putOctet(int i)
        throws ASN1Exception;

    protected abstract void putOctets(byte abyte0[], int i, int j)
        throws ASN1Exception;

    protected boolean specialTag;
    protected int implicitTag;
}
