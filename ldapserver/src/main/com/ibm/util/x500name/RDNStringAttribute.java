// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RDNStringAttribute.java

package com.ibm.util.x500name;

import com.ibm.asn1.*;
import java.io.Serializable;

// Referenced classes of package com.ibm.util.x500name:
//            RDNAttribute, RDNAttributeFactory

public class RDNStringAttribute extends RDNAttribute
    implements Serializable
{

    public RDNStringAttribute(RDNAttributeFactory rdnattributefactory, ASN1Decoder asn1decoder)
        throws ASN1Exception
    {
        super(rdnattributefactory);
        stringTag = asn1decoder.peekNextTag();
        if(stringTag == 19)
            value = asn1decoder.decodePrintableString();
        else
        if(stringTag == 20)
            value = asn1decoder.decodeT61String();
        else
        if(stringTag == 22)
        {
            value = asn1decoder.decodeIA5String();
        }
        else
        {
            asn1decoder.nextIsImplicit(stringTag);
            value = asn1decoder.decodeIA5String();
        }
    }

    public RDNStringAttribute(RDNAttributeFactory rdnattributefactory, String s, int i)
    {
        super(rdnattributefactory);
        value = s;
        stringTag = i;
    }

    public void encodeValue(ASN1Encoder asn1encoder)
        throws ASN1Exception
    {
        if(stringTag == 19)
            asn1encoder.encodePrintableString(value);
        else
        if(stringTag == 20)
            asn1encoder.encodeT61String(value);
        else
        if(stringTag == 22)
        {
            asn1encoder.encodeIA5String(value);
        }
        else
        {
            asn1encoder.nextIsImplicit(stringTag);
            asn1encoder.encodeIA5String(value);
        }
    }

    public ASN1OID keyToASN1OID()
    {
        return theFactory.keyASN1OID();
    }

    public String keyToString()
    {
        return theFactory.keyString();
    }

    public String valueToString()
    {
        return value;
    }

    protected String value;
    protected int stringTag;
}
