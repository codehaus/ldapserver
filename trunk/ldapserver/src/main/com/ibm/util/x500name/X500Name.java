// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   X500Name.java

package com.ibm.util.x500name;

import com.ibm.asn1.*;
import com.ibm.util.Sorter;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.*;

// Referenced classes of package com.ibm.util.x500name:
//            RDNAnyAttribute, RDNAttribute, RDNAttributeFactory, RDNStringAttributeFactory, 
//            X500NameEnumerator1, X500NameEnumerator2

public final class X500Name
    implements Serializable
{

    public X500Name(ASN1Decoder asn1decoder)
        throws ASN1Exception
    {
        Vector vector = new Vector();
        Vector vector1;
        for(int i = asn1decoder.decodeSequenceOf(); !asn1decoder.endOf(i); vector.addElement(vector1))
        {
            int j = asn1decoder.decodeSetOf();
            vector1 = new Vector();
            int k;
            for(; !asn1decoder.endOf(j); asn1decoder.endOf(k))
            {
                k = asn1decoder.decodeSequence();
                ASN1OID asn1oid = asn1decoder.decodeObjectIdentifier().intern();
                RDNAttributeFactory rdnattributefactory = (RDNAttributeFactory)oid2Factory.get(asn1oid);
                vector1.addElement(rdnattributefactory == null ? ((Object) (new RDNAnyAttribute(asn1oid, asn1decoder.decodeAny()))) : ((Object) (rdnattributefactory.makeAttribute(asn1decoder))));
            }

        }

        setAttributes(vector);
    }

    public X500Name(String s)
    {
        this(s, null);
    }

    public X500Name(String s, int ai[])
    {
        Vector vector = new Vector();
        int i = ai != null ? ai[0] : 0;
        int j = s.length();
        if(j > 0 && s.charAt(0) == '<')
            i = 1;
        do
        {
            Vector vector1 = new Vector();
            do
            {
                ASN1OID asn1oid = null;
                RDNAttributeFactory rdnattributefactory = null;
                String s1 = null;
                BERAny berany = null;
                if((i = skipWhiteSpace(s, i)) >= j)
                    break;
                if("OID.".regionMatches(0, s, i, 4) || "oid.".regionMatches(0, s, i, 4))
                {
                    int k = (i += 3) + 1;
                    while(i < j && s.charAt(i) == '.') 
                    {
                        int l1 = ++i;
                        char c;
                        for(; i < j && (c = s.charAt(i)) >= '0' && c <= '9'; i++);
                        if(l1 == i)
                            throw new IllegalArgumentException("Illegal syntax: oid...: " + l1);
                    }

                    asn1oid = new ASN1OID(null, s.substring(k, i));
                    rdnattributefactory = (RDNAttributeFactory)oid2Factory.get(asn1oid);
                }
                else
                {
                    int j1;
                    int l = j1 = i;
                    char c1;
                    while(i < j && ((c1 = s.charAt(i)) >= '0' && c1 <= '9' || c1 >= 'a' && c1 <= 'z' || c1 >= 'A' && c1 <= 'Z' || c1 == ' ')) 
                    {
                        i++;
                        if(c1 != ' ')
                            j1 = i;
                    }

                    if(l == j1)
                        throw new IllegalArgumentException("Missing key: " + l);
                    String s2 = s.substring(l, j1).toLowerCase();
                    if((rdnattributefactory = (RDNAttributeFactory)string2Factory.get(s2)) == null)
                        throw new IllegalArgumentException("Unknown key: " + s2);
                }
                if((i = skipWhiteSpace(s, i)) >= j || s.charAt(i) != '=')
                    throw new IllegalArgumentException("Missing '=' between key and value: " + i);
                if((i = skipWhiteSpace(s, ++i)) >= j)
                    throw new IllegalArgumentException("Missing value");
                int i1;
                switch(s.charAt(i))
                {
                case 34: // '"'
                    i1 = ++i;
                    do
                    {
                        if(i >= j)
                            throw new IllegalArgumentException("Missing closing \"");
                        char c2;
                        if((c2 = s.charAt(i)) == '"')
                            break;
                        if(c2 == '\\' && ++i > j)
                            throw new IllegalArgumentException("Orphan backslash");
                        i++;
                    }
                    while(true);
                    s1 = s.substring(i1, i++);
                    break;

                case 35: // '#'
                    i1 = ++i;
                    char c3;
                    for(; i < j && ((c3 = s.charAt(i)) >= '0' && c3 <= '9' || c3 >= 'a' && c3 <= 'f' || c3 >= 'A' && c3 <= 'F'); i++);
                    if(i1 == i || (i - i1 & 0x1) != 0)
                        throw new IllegalArgumentException("Odd or zero number of hex digits: " + i1);
                    byte abyte0[] = new byte[(i - i1) / 2];
                    int j2 = i1;
                    for(int k2 = 0; j2 < i; k2++)
                    {
                        abyte0[k2] = (byte)(Character.digit(s.charAt(j2), 16) << 4 | Character.digit(s.charAt(j2 + 1), 16));
                        j2 += 2;
                    }

                    berany = new BERAny();
                    berany.data = abyte0;
                    berany.begin = berany.valueBegin = 0;
                    berany.length = abyte0.length;
                    break;

                default:
                    int k1;
                    i1 = k1 = i;
                    char c4;
label0:
                    while(i < j) 
                        switch(c4 = s.charAt(i))
                        {
                        default:
                            if(c4 == '\\' && ++i > j)
                                throw new IllegalArgumentException("Orphan backslash");
                            i++;
                            if(c4 != ' ')
                                k1 = i;
                            break;

                        case 10: // '\n'
                        case 13: // '\r'
                        case 35: // '#'
                        case 43: // '+'
                        case 44: // ','
                        case 59: // ';'
                        case 60: // '<'
                        case 61: // '='
                        case 62: // '>'
                            break label0;

                        }

                    s1 = s.substring(i1, k1);
                    break;

                }
                if(s1 != null && s1.indexOf(92) >= 0)
                {
                    int i2 = s1.length();
                    StringBuffer stringbuffer = new StringBuffer(i2);
                    for(int l2 = 0; l2 < i2; l2++)
                    {
                        if(s1.charAt(l2) == '\\')
                            l2++;
                        stringbuffer.append(s1.charAt(l2));
                    }

                    s1 = stringbuffer.toString();
                }
                if(rdnattributefactory != null)
                {
                    if(s1 != null)
                        vector1.addElement(rdnattributefactory.makeAttribute(s1));
                    else
                        try
                        {
                            vector1.addElement(rdnattributefactory.makeAttribute(berany.getDecoder()));
                        }
                        catch(ASN1Exception asn1exception)
                        {
                            throw new IllegalArgumentException("Bad hex-data (" + asn1exception.getMessage() + "): " + i1);
                        }
                }
                else
                {
                    if(s1 != null)
                        throw new IllegalArgumentException("Anonymous key with concrete value: " + s1);
                    vector1.addElement(new RDNAnyAttribute(asn1oid, berany));
                }
                char c5;
                if(i >= j || (c5 = s.charAt(i)) != '+')
                    break;
                i++;
            }
            while(true);
            if(vector1.size() > 0)
                vector.addElement(vector1);
            char c6;
            if(i >= j || (c6 = s.charAt(i)) != ',' && c6 != ';')
                break;
            i++;
        }
        while(true);
        if(vector.size() == 0)
            throw new IllegalArgumentException("Empty sequence of name components");
        if(s.charAt(0) == '<')
        {
            char c7;
            if(i >= j || (c7 = s.charAt(i)) != '>')
                throw new IllegalArgumentException("Expecting terminating '>': " + i);
            i++;
        }
        else
        if(ai == null && i < j)
            throw new IllegalArgumentException("Trailing garbage: " + i);
        if(ai != null)
            ai[0] = i;
        setAttributes(vector);
    }

    public X500Name(Vector vector)
    {
        setAttributes(vector);
    }

    public X500Name(byte abyte0[])
        throws ASN1Exception
    {
        this(new BERDecoder(new ByteArrayInputStream(abyte0)));
    }

    public X500Name(RDNAttribute ardnattribute[])
    {
        attributes = new RDNAttribute[ardnattribute.length][1];
        for(int i = 0; i < ardnattribute.length; i++)
            attributes[i][0] = ardnattribute[i];

    }

    public X500Name(RDNAttribute ardnattribute[][])
    {
label0:
        while(ardnattribute.length != 0) 
        {
            attributes = new RDNAttribute[ardnattribute.length][];
            for(int i = 0; i < ardnattribute.length; i++)
            {
                if(ardnattribute[i].length == 0)
                    break label0;
                attributes[i] = new RDNAttribute[ardnattribute[i].length];
                System.arraycopy(ardnattribute[i], 0, attributes[i], 0, ardnattribute[i].length);
                Sorter.comparableSorter.sortDestructive(ardnattribute[i]);
            }

            return;
        }

    }

    public RDNAttribute attribute(ASN1OID asn1oid)
    {
        return attribute(findRDNAttributeFactory(asn1oid));
    }

    public RDNAttribute attribute(RDNAttributeFactory rdnattributefactory)
    {
        for(int i = 0; i < attributes.length; i++)
        {
            int j = 0;
            for(int k = attributes[i].length; j < k; j++)
                if(attributes[i][j].factory() == rdnattributefactory)
                    return attributes[i][j];

        }

        return null;
    }

    public RDNAttribute attribute(String s)
    {
        return attribute(findRDNAttributeFactory(s));
    }

    public RDNAttribute[][] attributes()
    {
        RDNAttribute ardnattribute[][] = new RDNAttribute[attributes.length][];
        for(int i = 0; i < ardnattribute.length; i++)
        {
            ardnattribute[i] = new RDNAttribute[attributes[i].length];
            System.arraycopy(attributes[i], 0, ardnattribute[i], 0, attributes[i].length);
        }

        return ardnattribute;
    }

    public byte[] encode()
    {
        try
        {
            BEREncoder berencoder = new BEREncoder();
            encode(berencoder);
            berencoder.finish();
            return berencoder.toByteArray();
        }
        catch(ASN1Exception _ex)
        {
            return null;
        }
    }

    public void encode(ASN1Encoder asn1encoder)
        throws ASN1Exception
    {
        int i = asn1encoder.encodeSequenceOf();
        for(int j = 0; j < attributes.length; j++)
        {
            int k = asn1encoder.encodeSetOf();
            for(int l = 0; l < attributes[j].length; l++)
            {
                int i1 = asn1encoder.encodeSequenceOf();
                RDNAttribute rdnattribute = attributes[j][l];
                rdnattribute.encodeKey(asn1encoder);
                rdnattribute.encodeValue(asn1encoder);
                asn1encoder.endOf(i1);
            }

            asn1encoder.endOf(k);
        }

        asn1encoder.endOf(i);
    }

    public static Enumeration enumerateRDNAttributeFactories()
    {
        return allFactories.elements();
    }

    public Enumeration enumerateRDNAttributes()
    {
        return new X500NameEnumerator1(this, null);
    }

    public boolean equals(Object obj)
    {
        if(obj == null || !getClass().equals(obj.getClass()))
            return false;
        X500Name x500name = (X500Name)obj;
        if(attributes.length != x500name.attributes.length)
            return false;
        for(int i = 0; i < attributes.length; i++)
        {
            if(attributes[i].length != x500name.attributes[i].length)
                return false;
            for(int j = 0; j < attributes[i].length; j++)
                if(!attributes[i][j].equals(x500name.attributes[i][j]))
                    return false;

        }

        return true;
    }

    public static RDNAttributeFactory findRDNAttributeFactory(ASN1OID asn1oid)
    {
        return (RDNAttributeFactory)oid2Factory.get(asn1oid);
    }

    public static RDNAttributeFactory findRDNAttributeFactory(String s)
    {
        return (RDNAttributeFactory)string2Factory.get(s.toLowerCase());
    }

    public Enumeration findRDNAttributeSet(RDNAttributeFactory rdnattributefactory)
    {
        return new X500NameEnumerator1(this, rdnattributefactory);
    }

    public int hashCode()
    {
        int i = 0;
        for(int j = 0; j < attributes.length; j++)
        {
            for(int k = 0; k < attributes[j].length; k++)
                i = i << 7 ^ attributes[j][k].hashCode() ^ i >>> 25;

        }

        return i;
    }

    public boolean isEmpty()
    {
        return attributes.length == 0;
    }

    public static boolean registerRDNAttributeFactory(RDNAttributeFactory rdnattributefactory)
    {
        String s = rdnattributefactory.keyString();
        ASN1OID asn1oid = rdnattributefactory.keyASN1OID();
        if(string2Factory.get(s) != null || oid2Factory.get(asn1oid) != null)
        {
            return false;
        }
        else
        {
            allFactories.addElement(rdnattributefactory);
            string2Factory.put(s, rdnattributefactory);
            oid2Factory.put(asn1oid, rdnattributefactory);
            return true;
        }
    }

    private void sanityCheck()
    {
        for(int i = 0; i < attributes.length; i++)
            if(attributes[i].length == 0)
                throw new IllegalArgumentException("Bad distinguished name: RDNAttribute[][]: Array with zero length");

    }

    private void setAttributes(Vector vector)
    {
        attributes = new RDNAttribute[vector.size()][];
        for(int i = 0; i < attributes.length; i++)
        {
            Vector vector1 = (Vector)vector.elementAt(i);
            attributes[i] = new RDNAttribute[vector1.size()];
            vector1.copyInto(attributes[i]);
            Sorter.comparableSorter.sortDestructive(attributes[i]);
        }

        sanityCheck();
    }

    private int skipWhiteSpace(String s, int i)
    {
        char c;
        for(int j = s.length(); i < j && ((c = s.charAt(i)) == ' ' || c == '\n' || c == '\r'); i++);
        return i;
    }

    public String toString()
    {
        StringBuffer stringbuffer = new StringBuffer();
        for(int i = 0; i < attributes.length; i++)
        {
            stringbuffer.append(attributes[i][0].toString());
            for(int j = 1; j < attributes[i].length; j++)
            {
                stringbuffer.append(" + ");
                stringbuffer.append(attributes[i][j].toString());
            }

            if(i + 1 < attributes.length)
                stringbuffer.append(", ");
        }

        return stringbuffer.toString();
    }

    private static Hashtable string2Factory = new Hashtable();
    private static Hashtable oid2Factory = new Hashtable();
    private static Vector allFactories = new Vector();
    private static ASN1OID baseOID;
    public static final ASN1OID COMMON_NAME;
    public static final ASN1OID SERIAL_NUMBER;
    public static final ASN1OID COUNTRY;
    public static final ASN1OID LOCATION;
    public static final ASN1OID STATE;
    public static final ASN1OID STREET;
    public static final ASN1OID ORGANIZATION;
    public static final ASN1OID ORGANIZATIONAL_UNIT;
    public static final ASN1OID TITLE;
    public static final ASN1OID ZIP;
    public static final ASN1OID EMAIL;
    private static ASN1OID oids[];
    private static String keyNames[] = {
        "cn", "serialNumber", "c", "l", "st", "street", "o", "ou", "t", "zip", 
        "email"
    };
    RDNAttribute attributes[][];

    static 
    {
        baseOID = new ASN1OID(null, "2 5 4");
        COMMON_NAME = new ASN1OID("commonName", baseOID, 3);
        SERIAL_NUMBER = new ASN1OID("serialNumber", baseOID, 5);
        COUNTRY = new ASN1OID("country", baseOID, 6);
        LOCATION = new ASN1OID("location", baseOID, 7);
        STATE = new ASN1OID("state", baseOID, 8);
        STREET = new ASN1OID("street", baseOID, 9);
        ORGANIZATION = new ASN1OID("organization", baseOID, 10);
        ORGANIZATIONAL_UNIT = new ASN1OID("organizationalUnit", baseOID, 11);
        TITLE = new ASN1OID("title", baseOID, 12);
        ZIP = new ASN1OID("postalCode", baseOID, 17);
        EMAIL = new ASN1OID("emailAddress", "iso(1) member-body(2) US(840) rsadsi(113549) pkcs(1) pkcs-9(9) emailAddress(1)");
        oids = (new ASN1OID[] {
            COMMON_NAME, SERIAL_NUMBER, COUNTRY, LOCATION, STATE, STREET, ORGANIZATION, ORGANIZATIONAL_UNIT, TITLE, ZIP, 
            EMAIL
        });
        for(int i = 0; i < keyNames.length; i++)
            registerRDNAttributeFactory(new RDNStringAttributeFactory(oids[i].intern(), keyNames[i], 19));

    }
}
