// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BERIndefiniteEncoder.java

package com.ibm.asn1;

import java.io.IOException;
import java.io.OutputStream;

// Referenced classes of package com.ibm.asn1:
//            BaseBEREncoder, ASN1Exception, ASN1Tag, BERConstants

public class BERIndefiniteEncoder extends BaseBEREncoder
{

    public BERIndefiniteEncoder(OutputStream outputstream)
    {
        output = outputstream;
    }

    public void encodeAny(byte abyte0[], int i, int j)
        throws ASN1Exception
    {
        putOctets(abyte0, i, j);
    }

    public int encodeConstructedString(int i)
        throws ASN1Exception
    {
        return encodeConstructedWithIndefiniteLength(i);
    }

    private int encodeConstructedWithIndefiniteLength(int i)
        throws ASN1Exception
    {
        encodeTag(true, i);
        putOctet(128);
        return ++level;
    }

    public int encodeExplicit(int i)
        throws ASN1Exception
    {
        return encodeConstructedWithIndefiniteLength(i);
    }

    public int encodeSequence()
        throws ASN1Exception
    {
        return encodeConstructedWithIndefiniteLength(16);
    }

    public int encodeSet()
        throws ASN1Exception
    {
        return encodeConstructedWithIndefiniteLength(17);
    }

    public void endOf(int i)
        throws ASN1Exception
    {
        if(level != i)
        {
            throw new ASN1Exception("endOf mismatch");
        }
        else
        {
            level--;
            putOctet(0);
            putOctet(0);
            return;
        }
    }

    public void finish()
        throws ASN1Exception
    {
        int i = level;
        level = 0;
        if(i != 0)
            throw new ASN1Exception("Outstanding endOfs");
        else
            return;
    }

    public OutputStream getOutputStream()
    {
        return output;
    }

    protected void putOctet(int i)
        throws ASN1Exception
    {
        try
        {
            output.write(i);
        }
        catch(IOException ioexception)
        {
            throw new ASN1Exception(ioexception);
        }
    }

    protected void putOctets(byte abyte0[], int i, int j)
        throws ASN1Exception
    {
        try
        {
            output.write(abyte0, i, j);
        }
        catch(IOException ioexception)
        {
            throw new ASN1Exception(ioexception);
        }
    }

    public OutputStream setOutputStream(OutputStream outputstream)
    {
        OutputStream outputstream1 = output;
        output = outputstream;
        return outputstream1;
    }

    public String toString()
    {
        return "BER (indefinite encoding)";
    }

    public static final String RULES_NAME = "BER (indefinite encoding)";
    private OutputStream output;
    private int level;
}
