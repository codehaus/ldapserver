// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ASN1Tag.java

package com.ibm.asn1;


// Referenced classes of package com.ibm.asn1:
//            ASN1Exception

public class ASN1Tag
{

    public ASN1Tag()
    {
    }

    public static int getTagClass(int i)
    {
        return i >>> 30;
    }

    public static int getTagNumber(int i)
    {
        return i & 0x3fffffff;
    }

    public static boolean isUniversalStringTag(int i)
    {
        if(getTagClass(i) != 0)
            throw new IllegalArgumentException("Tag is not a universal tag");
        if(i >= 64)
            return false;
        else
            return (stringTags & (long)(1 << i)) != 0L;
    }

    public static int makeApplicationTag(int i)
        throws ASN1Exception
    {
        return makeTag(1, i);
    }

    public static int makeContextTag(int i)
        throws ASN1Exception
    {
        return makeTag(2, i);
    }

    public static int makeTag(int i, int j)
    {
        if(j < 0 || j > 0x3fffffff)
            throw new IllegalArgumentException("Tag number too big");
        else
            return i << 30 | j;
    }

    public static int makeUniversalTag(int i)
        throws ASN1Exception
    {
        return makeTag(0, i);
    }

    public static String tagClassToString(int i)
    {
        return tagClasses[getTagClass(i)];
    }

    public static String tagToString(int i)
    {
        if(getTagClass(i) != 0)
            return tagClassToString(i) + " " + Integer.toString(getTagNumber(i));
        else
            return tagClassToString(i) + " " + universalTagNumberToString(i);
    }

    public static String universalTagNumberToString(int i)
    {
        if(getTagClass(i) != 0)
            throw new IllegalArgumentException("Tag is not a universal tag");
        if(i >= universalTagNames.length)
            return Integer.toString(i);
        else
            return universalTagNames[i];
    }

    public static final int UNIVERSAL_TAG_CLASS = 0;
    public static final int APPLICATION_TAG_CLASS = 1;
    public static final int CONTEXT_TAG_CLASS = 2;
    public static final int PRIVATE_TAG_CLASS = 3;
    public static final int INVALID = 0;
    public static final int BOOLEAN = 1;
    public static final int INTEGER = 2;
    public static final int BITSTRING = 3;
    public static final int OCTETSTRING = 4;
    public static final int NULL = 5;
    public static final int OBJECTIDENTIFIER = 6;
    public static final int OD = 7;
    public static final int EXTERNAL = 8;
    public static final int REAL = 9;
    public static final int ENUMERATION = 10;
    public static final int SEQUENCE = 16;
    public static final int SEQUENCEOF = 16;
    public static final int SET = 17;
    public static final int SETOF = 17;
    public static final int NUMERICSTRING = 18;
    public static final int PRINTABLESTRING = 19;
    public static final int T61STRING = 20;
    public static final int VIDEOTEXSTRING = 21;
    public static final int IA5STRING = 22;
    public static final int UTCTIME = 23;
    public static final int GENERALIZEDTIME = 24;
    public static final int GRAPHICSTRING = 25;
    public static final int VISIBLESTRING = 26;
    public static final int GENERALSTRING = 27;
    public static final int BMPSTRING = 30;
    private static final int TAG_CLASS_SHIFT = 30;
    private static final int TAG_NUMBER_MASK = 0x3fffffff;
    private static final int TAG_CLASS_MASK = 0xc0000000;
    public static final int MAX_TAG_NUMBER = 0x3fffffff;
    private static final String tagClasses[] = {
        "Universal", "Application", "Context", "Private"
    };
    private static final String universalTagNames[] = {
        "End-of-Indefinite-Encoding", "Boolean", "Integer", "BitString", "OctetString", "Null", "ObjectIdentifier", "OD", "External", "Real", 
        "Enumeration", "11", "12", "13", "14", "15", "Sequence (of)", "Set (of)", "NumericString", "PrintableString", 
        "T61String", "TeletexString", "IA5String", "UTCTime", "GeneralizedTime", "GraphicString", "VisibleString", "GeneralString"
    };
    private static long stringTags = 0xe7c0018L;

}
