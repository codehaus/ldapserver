// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ObjectDescriptor.java

package com.ibm.asn1;

import com.ibm.util.Hex;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

// Referenced classes of package com.ibm.asn1:
//            ASN1Decoder, ASN1Encoder, ASN1Exception, ASN1Tag, 
//            ASN1Type

public class ObjectDescriptor
    implements ASN1Type
{

    public ObjectDescriptor()
    {
    }

    public ObjectDescriptor(ObjectDescriptor objectdescriptor)
    {
        value = new byte[objectdescriptor.value.length];
        System.arraycopy(objectdescriptor.value, 0, value, 0, objectdescriptor.value.length);
    }

    public ObjectDescriptor(byte abyte0[])
    {
        value = abyte0;
    }

    public void decode(ASN1Decoder asn1decoder)
        throws ASN1Exception
    {
        asn1decoder.nextIsImplicit(ASN1Tag.makeTag(0, 7));
        value = asn1decoder.decodeOctetString();
    }

    public void encode(ASN1Encoder asn1encoder)
        throws ASN1Exception
    {
        asn1encoder.nextIsImplicit(ASN1Tag.makeTag(0, 7));
        asn1encoder.encodeOctetString(value);
    }

    public void print(PrintStream printstream)
    {
        print(printstream, 0);
    }

    public void print(PrintStream printstream, int i)
    {
        printstream.print(Hex.toString(value));
    }

    public String toString()
    {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        PrintStream printstream = new PrintStream(bytearrayoutputstream);
        print(printstream);
        printstream.close();
        return bytearrayoutputstream.toString();
    }

    public byte value[];
}
