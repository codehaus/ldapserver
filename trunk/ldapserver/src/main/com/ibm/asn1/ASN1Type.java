// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ASN1Type.java

package com.ibm.asn1;


// Referenced classes of package com.ibm.asn1:
//            ASN1EncType, ASN1Exception, ASN1Decoder

public interface ASN1Type
    extends ASN1EncType
{

    public abstract void decode(ASN1Decoder asn1decoder)
        throws ASN1Exception;
}
