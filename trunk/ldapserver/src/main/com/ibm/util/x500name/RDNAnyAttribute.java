// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RDNAnyAttribute.java

package com.ibm.util.x500name;

import com.ibm.asn1.*;
import com.ibm.util.Hex;
import com.ibm.util.Util;
import java.io.Serializable;

// Referenced classes of package com.ibm.util.x500name:
//            RDNAttribute, RDNAnyAttributeFactory, RDNAttributeFactory

public final class RDNAnyAttribute extends RDNAttribute
    implements Serializable
{

    public RDNAnyAttribute(ASN1OID asn1oid, ASN1Any asn1any)
    {
        super(null);
        oid = asn1oid;
        any = asn1any;
    }

    public RDNAnyAttribute(ASN1OID asn1oid, String s)
    {
        super(null);
        oid = asn1oid;
        int i = 0;
        int j = s.length();
        byte abyte0[] = new byte[s.length() / 2];
        int k = 0;
        if(s.startsWith("#"))
            i = 1;
        char c;
        while(i < j) 
            if((c = s.charAt(i++)) != ' ' && c != '\n' && c != '\r')
                if(c >= '0' && c <= '9' || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F')
                {
                    abyte0[k / 2] |= Character.digit(c, 16) << ((k & 0x1) != 0 ? 0 : 4);
                    k++;
                }
                else
                {
                    throw new IllegalArgumentException("Illegal character: pos. " + (i - 1));
                }

        if((k & 0x1) != 0)
        {
            throw new IllegalArgumentException("Odd number of hex digits");
        }
        else
        {
            any = new BERAny();
            any.data = abyte0;
            any.begin = any.valueBegin = 0;
            any.length = k / 2;
            return;
        }
    }

    public RDNAnyAttribute(RDNAttributeFactory rdnattributefactory, ASN1Decoder asn1decoder)
        throws ASN1Exception
    {
        super(rdnattributefactory);
        oid = null;
        any = asn1decoder.decodeAny();
    }

    public void encodeValue(ASN1Encoder asn1encoder)
        throws ASN1Exception
    {
        asn1encoder.encodeAny(any);
    }

    public boolean equals(Object obj)
    {
        if(obj == null || !getClass().equals(obj.getClass()))
        {
            return false;
        }
        else
        {
            RDNAnyAttribute rdnanyattribute = (RDNAnyAttribute)obj;
            return keyToASN1OID().equals(rdnanyattribute.keyToASN1OID()) && Util.arraycmp(any.data, any.begin, any.length, rdnanyattribute.any.data, rdnanyattribute.any.begin, rdnanyattribute.any.length) == 0;
        }
    }

    public RDNAttributeFactory factory()
    {
        if(theFactory != null)
            return theFactory;
        else
            return theFactory = new RDNAnyAttributeFactory(this);
    }

    public int hashCode()
    {
        int i = keyToASN1OID().hashCode();
        for(int j = any.begin; j < any.length; j++)
            i = i << 7 ^ any.data[j] ^ i >>> 25;

        return i;
    }

    public ASN1OID keyToASN1OID()
    {
        if(oid != null)
            return oid;
        else
            return theFactory.keyASN1OID();
    }

    public String keyToString()
    {
        if(oid != null)
            return "OID." + oid.toString();
        else
            return theFactory.keyString();
    }

    public String toString()
    {
        return keyToString() + "=" + valueToString();
    }

    public String valueToString()
    {
        return "#" + Hex.toString(any.data, any.begin, any.length);
    }

    private ASN1OID oid;
    private ASN1Any any;
}
