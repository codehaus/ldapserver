// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RDNAnyAttribute.java

package com.ibm.util.x500name;

import com.ibm.asn1.*;

// Referenced classes of package com.ibm.util.x500name:
//            RDNAttributeFactory, RDNAnyAttribute, RDNAttribute

class RDNAnyAttributeFactory extends RDNAttributeFactory
{

    RDNAnyAttributeFactory(RDNAnyAttribute rdnanyattribute)
    {
        attribute = rdnanyattribute;
    }

    public ASN1OID keyASN1OID()
    {
        return attribute.keyToASN1OID();
    }

    public String keyString()
    {
        return attribute.keyToString();
    }

    public RDNAttribute makeAttribute(ASN1Decoder asn1decoder)
        throws ASN1Exception
    {
        return null;
    }

    public RDNAttribute makeAttribute(String s)
    {
        return null;
    }

    private RDNAnyAttribute attribute;
}
