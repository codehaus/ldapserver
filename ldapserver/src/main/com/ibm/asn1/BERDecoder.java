// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BERDecoder.java

package com.ibm.asn1;

import com.ibm.util.BitString;
import com.ibm.util.Constants;
import java.io.*;
import java.math.BigInteger;
import java.util.*;

// Referenced classes of package com.ibm.asn1:
//            ASN1Decoder, ASN1Any, ASN1Exception, ASN1OID, 
//            ASN1Tag, BERAny, BERConstants, BEREncoder, 
//            DecStack

public class BERDecoder extends ASN1Decoder
    implements BERConstants
{

    public BERDecoder(InputStream inputstream)
    {
        this(inputstream, 32);
    }

    public BERDecoder(InputStream inputstream, int i)
    {
        idlist = new int[64];
        currStack = new DecStack();
        currStack.prev = currStack.next = null;
        currStack.stack = new int[2 * i];
        tagLenBuf = new byte[32];
        setInputStream(inputstream);
    }

    public BERDecoder(byte abyte0[])
    {
        this(new ByteArrayInputStream(abyte0));
    }

    public BERDecoder(byte abyte0[], int i, int j)
    {
        this(new ByteArrayInputStream(abyte0, i, j));
    }

    protected void checkNextTag(int i)
        throws ASN1Exception
    {
        if(!decodeTagAndLength())
            throwASN1Exception("Expected one more tag in constructed encoding");
        if(implicitTag != 0)
        {
            if(implicitTag != tag)
                throwASN1Exception("Read tag " + ASN1Tag.tagToString(tag) + " but expected (implicit) " + ASN1Tag.tagToString(implicitTag) + " / original data tag " + ASN1Tag.tagToString(i));
        }
        else
        if(tag != i)
            throwASN1Exception("Read tag " + ASN1Tag.tagToString(tag) + " but expected " + ASN1Tag.tagToString(i));
        if(!constructed || ASN1Tag.getTagClass(i) == 0 && !ASN1Tag.isUniversalStringTag(i))
            implicitTag = 0;
        if(constructed)
            push(i);
    }

    private void copyInto(ASN1Any asn1any, byte abyte0[], int i, int j)
    {
        if(asn1any.length + j >= asn1any.data.length)
        {
            byte abyte1[] = new byte[Math.max(asn1any.data.length + 512, asn1any.length + j)];
            if(asn1any.data != null && asn1any.length > 0)
                System.arraycopy(asn1any.data, 0, abyte1, 0, asn1any.length);
            asn1any.data = abyte1;
        }
        if(abyte0 != null)
        {
            System.arraycopy(abyte0, i, asn1any.data, asn1any.length, j);
            asn1any.length += j;
        }
    }

    public void debug()
    {
        System.out.println("readOctets:  0x" + Integer.toString(readOctets, 16) + "/" + readOctets);
        System.out.println("implicitTag: " + ASN1Tag.tagToString(implicitTag));
        System.out.println("tag:         " + ASN1Tag.tagToString(tag));
        System.out.println("constructed: " + constructed);
        System.out.println("valueLength: " + valueLength);
        System.out.println("stackPos:    " + stackPos);
        System.out.println("pushedBack:  " + pushedBack);
        System.out.println("stack:");
        System.out.println("  rest:" + currStack.stack[stackIndex]);
        System.out.println("  tag: " + currStack.stack[stackIndex + 1]);
    }

    public ASN1Any decodeAny()
        throws ASN1Exception
    {
        if(!decodeTagAndLength())
            throwASN1Exception("End of (nested) encoding - can't read one more element");
        BERAny berany = new BERAny();
        berany.tag = tag;
        berany.data = new byte[16];
        berany.begin = berany.length = 0;
        berany.valueBegin = tlbIndex;
        decodeAny(berany);
        return berany;
    }

    private boolean decodeAny(ASN1Any asn1any)
        throws ASN1Exception
    {
        copyInto(asn1any, tagLenBuf, 0, tlbIndex);
        if(valueLength >= 0)
        {
            copyInto(asn1any, null, 0, valueLength);
            int i = asn1any.length;
            for(int j = asn1any.length + valueLength; i < j; i++)
                asn1any.data[i] = (byte)getOctet();

            asn1any.length = i;
        }
        else
        {
            push(tag);
            for(; decodeTagAndLength(); decodeAny(asn1any));
            pop();
            copyInto(asn1any, null, 0, 2);
            asn1any.data[asn1any.length++] = 0;
            asn1any.data[asn1any.length++] = 0;
        }
        return true;
    }

    public byte[] decodeAnyAsByteArray()
        throws ASN1Exception
    {
        ASN1Any asn1any = decodeAny();
        if(asn1any.begin == 0 && asn1any.length == asn1any.data.length)
        {
            return asn1any.data;
        }
        else
        {
            byte abyte0[] = new byte[asn1any.length];
            System.arraycopy(asn1any.data, asn1any.begin, abyte0, 0, asn1any.length);
            return abyte0;
        }
    }

    public String decodeBMPString()
        throws ASN1Exception
    {
        checkNextTag(30);
        return decodeString("UnicodeBig");
    }

    public BitString decodeBitString()
        throws ASN1Exception
    {
        checkNextTag(3);
        byte abyte0[] = new byte[Math.max(0, valueLength * 8)];
        int i = decodeBitStringContents(abyte0, 0);
        return new BitString(abyte0, 0, i, true);
    }

    protected int decodeBitStringContents(byte abyte0[], int i)
        throws ASN1Exception
    {
        if(constructed)
        {
            while(decodeTagAndLength()) 
            {
                if(constructed)
                    push(tag);
                i = decodeBitStringContents(abyte0, i);
            }

            pop();
        }
        else
        {
            int j = valueLength * 8 - 8 - getOctet();
            int k = i / 8;
            int l = abyte0[k];
            int i1 = i % 8;
            while(j > 0) 
            {
                l = l << 8 | getOctet();
                i1 += j <= 8 ? j : 8;
                j -= 8;
                for(; i1 >= 8; i1 -= 8)
                    abyte0[k++] = (byte)(l >> i1 - 8);

            }

            if(i1 > 0)
                abyte0[k] = (byte)l;
            i = k * 8 + i1;
        }
        return i;
    }

    public boolean decodeBoolean()
        throws ASN1Exception
    {
        checkNextTag(1);
        return getOctet() != 0;
    }

    public int decodeChoice(int ai[])
        throws ASN1Exception
    {
        return peekNextTag();
    }

    public int decodeEnumeration()
        throws ASN1Exception
    {
        return decodeInt(10);
    }

    public int decodeExplicit(int i)
        throws ASN1Exception
    {
        checkNextTag(i);
        return stackPos;
    }

    public String decodeGeneralString()
        throws ASN1Exception
    {
        checkNextTag(27);
        return decodeString("ISO8859_1");
    }

    public Calendar decodeGeneralizedTime()
        throws ASN1Exception
    {
        int k1 = 0;
        int l1 = 0;
        checkNextTag(24);
        String s = decodeString("ISO8859_1");
        int i = get2AsciiDigits(s, 0) * 100 + get2AsciiDigits(s, 2);
        int j = get2AsciiDigits(s, 4);
        int k = get2AsciiDigits(s, 6);
        int l = get2AsciiDigits(s, 8);
        int i1 = get2AsciiDigits(s, 10);
        int j1 = get2AsciiDigits(s, 12);
        int i2 = 14;
        if(s.charAt(14) == '.' || s.charAt(14) == ',')
        {
            int j2 = 100;
            i2 = 15;
            char c;
            while((c = s.charAt(i2)) >= '0' && c <= '9') 
            {
                l1 += j2 * (c - 48);
                i2++;
                j2 /= 10;
            }

        }
        if(s.length() > i2)
        {
            char c1 = s.charAt(i2);
            if(s.length() == i2 + 1 && c1 == 'Z')
                k1 = 0;
            else
            if(c1 == '-' || c1 == '+')
            {
                byte byte0 = c1 != '-' ? ((byte) (1)) : -1;
                k1 = (get2AsciiDigits(s, i2 + 1) * byte0 * 60 + get2AsciiDigits(s, i2 + 3) * byte0) * 60000;
            }
            else
            {
                throw new ASN1Exception("Garbled GeneralizedTime - no trailing Z or time zone offset ");
            }
        }
        GregorianCalendar gregoriancalendar = new GregorianCalendar();
        gregoriancalendar.setTimeZone(makeTimeZone(k1));
        gregoriancalendar.set(i, j - 1, k, l, i1, j1);
        gregoriancalendar.set(14, l1);
        return gregoriancalendar;
    }

    public String decodeGraphicString()
        throws ASN1Exception
    {
        checkNextTag(25);
        return decodeString("ISO8859_1");
    }

    public String decodeIA5String()
        throws ASN1Exception
    {
        checkNextTag(22);
        return decodeString("ISO8859_1");
    }

    private int decodeInt(int i)
        throws ASN1Exception
    {
        checkNextTag(i);
        int j = valueLength;
        int k = (4 - j) * 8;
        int l;
        for(l = 0; j-- > 0; l = (l << 8) + getOctet());
        if(k < 0)
            k = 0;
        return (l << k) >> k;
    }

    public BigInteger decodeInteger()
        throws ASN1Exception
    {
        checkNextTag(2);
        byte abyte0[] = new byte[valueLength];
        for(int i = 0; i < valueLength; i++)
            abyte0[i] = (byte)getOctet();

        return new BigInteger(abyte0);
    }

    public int decodeIntegerAsInt()
        throws ASN1Exception
    {
        return decodeInt(2);
    }

    public long decodeIntegerAsLong()
        throws ASN1Exception
    {
        checkNextTag(2);
        int i = valueLength;
        int j = (8 - i) * 8;
        long l;
        for(l = 0L; i-- > 0; l = (l << 8) + (long)getOctet());
        if(j < 0)
            j = 0;
        return (l << j) >> j;
    }

    public void decodeNull()
        throws ASN1Exception
    {
        checkNextTag(5);
        while(valueLength-- > 0) 
            getOctet();

    }

    public String decodeNumericString()
        throws ASN1Exception
    {
        checkNextTag(18);
        return decodeString("ISO8859_1");
    }

    public ASN1OID decodeObjectIdentifier()
        throws ASN1Exception
    {
        checkNextTag(6);
        int i = getOctet();
        idlist[0] = i / 40;
        idlist[1] = i % 40;
        int k = 2;
        int l = 0;
        while(--valueLength > 0) 
        {
            int j = getOctet();
            if(l >= 0x10000000)
                throwASN1Exception("Component of an object identifier doesn't fit into an int");
            l = (l << 7) + (j & 0x7f);
            if((j & 0x80) == 0)
            {
                if(k >= idlist.length)
                    throwASN1Exception("Object identifier has more than " + idlist.length + " components");
                idlist[k++] = l;
                l = 0;
            }
        }

        return new ASN1OID(null, idlist, 0, k);
    }

    public byte[] decodeOctetString()
        throws ASN1Exception
    {
        checkNextTag(4);
        return startDecodeOctetString();
    }

    public int decodeOctetString(byte abyte0[], int i)
        throws ASN1Exception
    {
        checkNextTag(4);
        octetStringLength = i;
        octetStringByteArray = abyte0;
        decodeOctetStringContents();
        if(abyte0 != octetStringByteArray)
            throw new ArrayIndexOutOfBoundsException("Byte array too small to hold ASN.1 octet string");
        else
            return octetStringLength;
    }

    public ASN1Any decodeOctetStringAsAny()
        throws ASN1Exception
    {
        checkNextTag(4);
        octetStringLength = 0;
        octetStringByteArray = new byte[Math.max(0, valueLength)];
        decodeOctetStringContents();
        BERAny berany = new BERAny();
        berany.tag = 4;
        berany.data = octetStringByteArray;
        berany.begin = berany.valueBegin = 0;
        berany.length = octetStringLength;
        return berany;
    }

    private synchronized void decodeOctetStringContents()
        throws ASN1Exception
    {
        if(constructed)
        {
            for(; decodeTagAndLength(); decodeOctetStringContents())
                if(constructed)
                    push(tag);

            pop();
        }
        else
        {
            int i = valueLength;
            if(octetStringLength + i > octetStringByteArray.length)
            {
                byte abyte0[] = new byte[octetStringLength + i];
                if(octetStringLength > 0)
                    System.arraycopy(octetStringByteArray, 0, abyte0, 0, octetStringLength);
                octetStringByteArray = abyte0;
            }
            while(i-- > 0) 
                octetStringByteArray[octetStringLength++] = (byte)getOctet();

        }
    }

    public String decodePrintableString()
        throws ASN1Exception
    {
        checkNextTag(19);
        return decodeString("ISO8859_1");
    }

    public double decodeReal()
        throws ASN1Exception
    {
        throw new Error("Read decoding not yet implemented");
    }

    public int decodeSequence()
        throws ASN1Exception
    {
        checkNextTag(16);
        return stackPos;
    }

    public int decodeSequenceOf()
        throws ASN1Exception
    {
        checkNextTag(16);
        return stackPos;
    }

    public int decodeSet()
        throws ASN1Exception
    {
        checkNextTag(17);
        return stackPos;
    }

    public int decodeSetOf()
        throws ASN1Exception
    {
        checkNextTag(17);
        return stackPos;
    }

    protected String decodeString(String s)
        throws ASN1Exception
    {
        try
        {
            return new String(startDecodeOctetString(), s);
        }
        catch(UnsupportedEncodingException _ex)
        {
            throw new ASN1Exception("Character encoding <" + s + "> not supported");
        }
    }

    public String decodeT61String()
        throws ASN1Exception
    {
        checkNextTag(20);
        return decodeString("ISO8859_1");
    }

    protected boolean decodeTagAndLength()
        throws ASN1Exception
    {
        if(pushedBack)
        {
            pushedBack = false;
            return true;
        }
        int i = currStack.stack[stackIndex];
        if(i >= 0 && readOctets >= i || tag == 0)
            return false;
        byte byte0 = 0;
        int j = getOctet();
        constructed = (j & 0x20) != 0;
        tlbIndex = 1;
        tagLenBuf[0] = (byte)j;
        switch(j & 0xc0)
        {
        case 0: // '\0'
            byte0 = 0;
            break;

        case 64: // '@'
            byte0 = 1;
            break;

        case 128: 
            byte0 = 2;
            break;

        case 192: 
            byte0 = 3;
            break;

        }
        tag = j & 0x1f;
        if(tag == 31)
        {
            long l1 = 0L;
            int k;
            do
            {
                tagLenBuf[tlbIndex++] = (byte)(k = getOctet());
                l1 = (l1 << 7) + (long)(k & 0x7f);
                if(l1 > 0x3fffffffL)
                    throwASN1Exception("Tag number too big");
            }
            while((k & 0x80) != 0);
            tag = (int)l1;
        }
        tag = ASN1Tag.makeTag(byte0, tag);
        tagLenBuf[tlbIndex++] = (byte)(valueLength = getOctet());
        if(valueLength >= 128)
        {
            if(valueLength == 128)
            {
                valueLength = -1;
                return true;
            }
            long l2 = 0L;
            for(int i1 = valueLength & 0x7f; i1 > 0; i1--)
            {
                int l;
                tagLenBuf[tlbIndex++] = (byte)(l = getOctet());
                l2 = l2 << 8 | (long)l;
                if(l2 >= 0x80000000L)
                    throwASN1Exception("Length value too big");
            }

            valueLength = (int)l2;
        }
        return tag != 0;
    }

    public Calendar decodeUTCTime()
        throws ASN1Exception
    {
        int j1 = 0;
        int k1 = 0;
        byte byte0 = 10;
        checkNextTag(23);
        String s = decodeString("ISO8859_1");
        int i = get2AsciiDigits(s, 0);
        int j = get2AsciiDigits(s, 2);
        int k = get2AsciiDigits(s, 4);
        int l = get2AsciiDigits(s, 6);
        int i1 = get2AsciiDigits(s, 8);
        i = i >= 50 ? 1900 + i : 2000 + i;
        char c = s.charAt(10);
        if(c >= '0' && c <= '9')
        {
            k1 = get2AsciiDigits(s, 10);
            byte0 = 12;
        }
        c = s.charAt(byte0);
        if(c == '+' || c == '-')
        {
            byte byte1 = c != '-' ? ((byte) (1)) : -1;
            j1 = (get2AsciiDigits(s, byte0 + 1) * byte1 * 60 + get2AsciiDigits(s, byte0 + 3) * byte1) * 60000;
        }
        else
        if(c != 'Z')
            throwASN1Exception("Garbled UTC time - no trailing Z");
        GregorianCalendar gregoriancalendar = new GregorianCalendar(i, j - 1, k, l, i1, k1);
        gregoriancalendar.setTimeZone(makeTimeZone(j1));
        return gregoriancalendar;
    }

    public String decodeVideotexString()
        throws ASN1Exception
    {
        checkNextTag(21);
        return decodeString("ISO8859_1");
    }

    public String decodeVisibleString()
        throws ASN1Exception
    {
        checkNextTag(26);
        return decodeString("ISO8859_1");
    }

    public boolean endOf(int i)
        throws ASN1Exception
    {
        if(i != stackPos)
            throwASN1Exception("Mismatch of termination of constructed encodings");
        if(decodeTagAndLength())
        {
            pushBack();
            return false;
        }
        else
        {
            pop();
            return true;
        }
    }

    private final int expectedTag()
    {
        return implicitTag == 0 ? tag : implicitTag;
    }

    private final int get2AsciiDigits(String s, int i)
        throws ASN1Exception
    {
        int j = 0;
        int k = 0;
        if(i + 2 <= s.length())
        {
            j = s.charAt(i);
            k = s.charAt(i + 1);
        }
        if(j < 48 || j > 57 || k < 48 || k > 57)
            throwASN1Exception("Malformed time - expecting digits but got: " + (char)j + (char)k);
        return (j - 48) * 10 + (k - 48);
    }

    protected int getOctet()
        throws ASN1Exception
    {
        try
        {
            int i = input.read();
            if(i < 0)
            {
                throw new ASN1Exception(new EOFException("Unexpected end of input data"));
            }
            else
            {
                readOctets++;
                return i;
            }
        }
        catch(IOException ioexception)
        {
            throw new ASN1Exception(ioexception);
        }
    }

    public int getTLLength()
        throws ASN1Exception
    {
        peekNextTag();
        return tlbIndex;
    }

    public int getTLVLength()
        throws ASN1Exception
    {
        peekNextTag();
        return tlbIndex + valueLength;
    }

    public int getTLVOffset()
        throws ASN1Exception
    {
        peekNextTag();
        return readOctets - tlbIndex;
    }

    public int getValueLength()
        throws ASN1Exception
    {
        peekNextTag();
        return valueLength;
    }

    public int getValueOffset()
        throws ASN1Exception
    {
        peekNextTag();
        return readOctets;
    }

    private static TimeZone makeTimeZone(int i)
    {
        Object obj = null;
        if(i == 0)
            return UTC_ZONE;
        String as[] = TimeZone.getAvailableIDs(i);
        if(as == null || as.length == 0 || !((TimeZone) (obj = TimeZone.getTimeZone(as[0]))).getID().equals(as[0]))
            obj = new SimpleTimeZone(i, (i <= 0 ? "" : "+") + i);
        return ((TimeZone) (obj));
    }

    public void nextHasAlphabetConstraints(String s)
        throws ASN1Exception
    {
    }

    public void nextHasSizeConstraints(long l, long l1)
        throws ASN1Exception
    {
    }

    public boolean nextIsDefault(int i)
        throws ASN1Exception
    {
        return peekNextTag() != i;
    }

    public void nextIsImplicit(int i)
    {
        if(implicitTag == 0)
            implicitTag = i;
    }

    public boolean nextIsOptional(int i)
        throws ASN1Exception
    {
        return peekNextTag() != i;
    }

    public boolean nextTagConstructed()
        throws ASN1Exception
    {
        if(!decodeTagAndLength())
        {
            return false;
        }
        else
        {
            pushBack();
            return constructed;
        }
    }

    public int peekNextTag()
        throws ASN1Exception
    {
        if(!decodeTagAndLength())
        {
            return 0;
        }
        else
        {
            pushBack();
            return tag;
        }
    }

    public int peekNextValueLength()
        throws ASN1Exception
    {
        if(pushedBack)
            return valueLength;
        if(!decodeTagAndLength())
        {
            return -1;
        }
        else
        {
            pushBack();
            return valueLength;
        }
    }

    protected void pop()
        throws ASN1Exception
    {
        int i = currStack.stack[stackIndex];
        if(i >= 0 && i != readOctets || i < 0 && tag != 0)
            throwASN1Exception("Contents octets not consumed");
        if((stackIndex -= 2) < 0)
        {
            currStack = currStack.prev;
            stackIndex = currStack.stack.length - 2;
        }
        tag = -1;
        stackPos--;
    }

    protected void push(int i)
    {
        if((stackIndex += 2) >= currStack.stack.length)
        {
            if(currStack.next == null)
            {
                DecStack decstack = currStack.next = new DecStack();
                decstack.prev = currStack;
                decstack.next = null;
                decstack.stack = new int[currStack.stack.length];
            }
            currStack = currStack.next;
            stackIndex = 0;
        }
        currStack.stack[stackIndex] = valueLength < 0 ? -1 : readOctets + valueLength;
        currStack.stack[stackIndex + 1] = i;
        stackPos++;
    }

    protected final void pushBack()
    {
        pushedBack = true;
    }

    public InputStream setInputStream(InputStream inputstream)
    {
        InputStream inputstream1 = input;
        input = inputstream;
        pushedBack = false;
        readOctets = 0;
        for(; currStack.prev != null; currStack = currStack.prev);
        currStack.stack[0] = -1;
        currStack.stack[1] = 16;
        stackIndex = stackPos = 0;
        tag = 16;
        implicitTag = 0;
        return inputstream1;
    }

    public void skipNext()
        throws ASN1Exception
    {
        if(!decodeTagAndLength())
            throwASN1Exception("End of (nested) encoding - can't skip next element");
        if(valueLength >= 0)
        {
            for(int i = valueLength; i-- > 0;)
                getOctet();

        }
        else
        {
            int j = 0;
            push(tag);
            while(stackPos > j) 
            {
                while(decodeTagAndLength()) 
                {
                    if(valueLength < 0)
                        push(tag);
                    for(int k = valueLength; k-- > 0;)
                        getOctet();

                }

                pop();
            }

        }
    }

    public byte[] startDecodeOctetString()
        throws ASN1Exception
    {
        octetStringLength = 0;
        octetStringByteArray = new byte[Math.max(0, valueLength)];
        decodeOctetStringContents();
        if(octetStringLength == octetStringByteArray.length)
        {
            return octetStringByteArray;
        }
        else
        {
            byte abyte0[] = new byte[octetStringLength];
            System.arraycopy(octetStringByteArray, 0, abyte0, 0, octetStringLength);
            return abyte0;
        }
    }

    protected void throwASN1Exception(String s)
        throws ASN1Exception
    {
        throw new ASN1Exception(s + " (offset " + readOctets + ")");
    }

    public String toString()
    {
        return "BER";
    }

    protected InputStream input;
    protected DecStack currStack;
    protected int stackIndex;
    protected int stackPos;
    protected int readOctets;
    protected boolean pushedBack;
    protected int implicitTag;
    protected int tag;
    protected boolean constructed;
    protected int valueLength;
    protected int tlbIndex;
    protected byte tagLenBuf[];
    private static final TimeZone UTC_ZONE = new SimpleTimeZone(0, "UTC");
    private int idlist[];
    private int octetStringLength;
    private byte octetStringByteArray[];

}
