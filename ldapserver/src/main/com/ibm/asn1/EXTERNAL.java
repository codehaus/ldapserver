// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EXTERNAL.java

package com.ibm.asn1;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;

// Referenced classes of package com.ibm.asn1:
//            ASN1Decoder, ASN1Encoder, ASN1Exception, ASN1OID, 
//            ASN1Tag, ASN1Type, EXTERNALChoice, ObjectDescriptor

public class EXTERNAL
    implements ASN1Type
{

    public EXTERNAL()
    {
        direct_reference = null;
        indirect_reference = null;
        data_value_descriptor = null;
        encoding = new EXTERNALChoice();
    }

    public EXTERNAL(EXTERNAL external)
    {
        direct_reference = null;
        indirect_reference = null;
        data_value_descriptor = null;
        encoding = new EXTERNALChoice();
        if(external.direct_reference != null)
            direct_reference = external.direct_reference;
        if(external.indirect_reference != null)
            indirect_reference = external.indirect_reference;
        if(external.data_value_descriptor != null)
            data_value_descriptor = new ObjectDescriptor(external.data_value_descriptor);
        encoding = new EXTERNALChoice(external.encoding);
    }

    public void decode(ASN1Decoder asn1decoder)
        throws ASN1Exception
    {
        asn1decoder.nextIsImplicit(ASN1Tag.makeTag(0, 8));
        int i = asn1decoder.decodeSequence();
        if(!asn1decoder.nextIsOptional(ASN1Tag.makeTag(0, 6)))
            direct_reference = asn1decoder.decodeObjectIdentifier();
        if(!asn1decoder.nextIsOptional(ASN1Tag.makeTag(0, 2)))
            indirect_reference = asn1decoder.decodeInteger();
        if(!asn1decoder.nextIsOptional(ASN1Tag.makeTag(0, 7)))
        {
            data_value_descriptor = new ObjectDescriptor();
            asn1decoder.nextIsImplicit(ASN1Tag.makeTag(0, 7));
            data_value_descriptor.decode(asn1decoder);
        }
        encoding.decode(asn1decoder);
        asn1decoder.endOf(i);
    }

    public void encode(ASN1Encoder asn1encoder)
        throws ASN1Exception
    {
        asn1encoder.nextIsImplicit(ASN1Tag.makeTag(0, 8));
        asn1encoder.setDefinedName("EXTERNAL");
        int i = asn1encoder.encodeSequence();
        if(direct_reference != null)
            asn1encoder.encodeObjectIdentifier(direct_reference);
        if(indirect_reference != null)
            asn1encoder.encodeInteger(indirect_reference);
        if(data_value_descriptor != null)
        {
            asn1encoder.nextIsImplicit(ASN1Tag.makeTag(0, 7));
            data_value_descriptor.encode(asn1encoder);
        }
        encoding.encode(asn1encoder);
        asn1encoder.endOf(i);
    }

    public void print(PrintStream printstream)
    {
        print(printstream, 0);
    }

    public void print(PrintStream printstream, int i)
    {
        printstream.println("{ -- SEQUENCE --");
        if(direct_reference != null)
        {
            for(int j = 0; j < i + 2; j++)
                printstream.print(' ');

            printstream.print("direct-reference = ");
            printstream.print(direct_reference.toString());
        }
        printstream.println(',');
        if(indirect_reference != null)
        {
            for(int k = 0; k < i + 2; k++)
                printstream.print(' ');

            printstream.print("indirect-reference = ");
            printstream.print(indirect_reference.toString());
        }
        printstream.println(',');
        if(data_value_descriptor != null)
        {
            for(int l = 0; l < i + 2; l++)
                printstream.print(' ');

            printstream.print("data-value-descriptor = ");
            data_value_descriptor.print(printstream, i + 2);
        }
        printstream.println(',');
        for(int i1 = 0; i1 < i + 2; i1++)
            printstream.print(' ');

        printstream.print("encoding = ");
        encoding.print(printstream, i + 2);
        printstream.println();
        for(int j1 = 0; j1 < i; j1++)
            printstream.print(' ');

        printstream.print('}');
    }

    public String toString()
    {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        PrintStream printstream = new PrintStream(bytearrayoutputstream);
        print(printstream);
        printstream.close();
        return bytearrayoutputstream.toString();
    }

    public ASN1OID direct_reference;
    public BigInteger indirect_reference;
    public ObjectDescriptor data_value_descriptor;
    public EXTERNALChoice encoding;
}
