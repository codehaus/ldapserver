// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RDNAttributeFactory.java

package com.ibm.util.x500name;

import com.ibm.asn1.*;
import java.io.Serializable;

// Referenced classes of package com.ibm.util.x500name:
//            RDNAttribute

public abstract class RDNAttributeFactory
    implements Serializable
{

    public RDNAttributeFactory()
    {
    }

    public abstract ASN1OID keyASN1OID();

    public abstract String keyString();

    public abstract RDNAttribute makeAttribute(ASN1Decoder asn1decoder)
        throws ASN1Exception;

    public abstract RDNAttribute makeAttribute(String s);
}
