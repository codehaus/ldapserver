// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BERAny.java

package com.ibm.asn1;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

// Referenced classes of package com.ibm.asn1:
//            ASN1Any, ASN1Tag, BERDecoder, BEREncoder, 
//            ASN1Decoder

public class BERAny extends ASN1Any
    implements Serializable
{

    public BERAny()
    {
    }

    public BERAny(BERAny berany)
    {
        super(berany);
    }

    public BERAny(byte abyte0[])
    {
        this(abyte0, 0, abyte0.length);
    }

    public BERAny(byte abyte0[], int i, int j)
    {
        data = abyte0;
        begin = i;
        valueBegin = i;
        length = j;
        tag = 0;
    }

    public Object clone()
    {
        return new BERAny(this);
    }

    public ASN1Decoder getDecoder()
    {
        return new BERDecoder(new ByteArrayInputStream(data, begin, length));
    }

    public String getEncoding()
    {
        return "BER";
    }

    static final long serialVersionUID = 0x622c15989676818fL;
}
