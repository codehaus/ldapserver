// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RDNStringAttributeFactory.java

package com.ibm.util.x500name;

import com.ibm.asn1.*;
import java.io.Serializable;

// Referenced classes of package com.ibm.util.x500name:
//            RDNAttributeFactory, RDNStringAttribute, RDNAttribute

public class RDNStringAttributeFactory extends RDNAttributeFactory
    implements Serializable
{

    public RDNStringAttributeFactory(ASN1OID asn1oid, String s, int i)
    {
        oid = asn1oid;
        str = s;
        tag = i;
    }

    public final ASN1OID keyASN1OID()
    {
        return oid;
    }

    public final String keyString()
    {
        return str;
    }

    public RDNAttribute makeAttribute(ASN1Decoder asn1decoder)
        throws ASN1Exception
    {
        return new RDNStringAttribute(this, asn1decoder);
    }

    public RDNAttribute makeAttribute(String s)
    {
        return new RDNStringAttribute(this, s, tag);
    }

    private ASN1OID oid;
    private String str;
    private int tag;
}
