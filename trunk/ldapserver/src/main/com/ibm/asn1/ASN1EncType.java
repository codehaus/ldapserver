// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ASN1EncType.java

package com.ibm.asn1;


// Referenced classes of package com.ibm.asn1:
//            ASN1Exception, ASN1Encoder

public interface ASN1EncType
{

    public abstract void encode(ASN1Encoder asn1encoder)
        throws ASN1Exception;
}
