// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EXTERNALChoice.java

package com.ibm.asn1;

import com.ibm.util.BitString;
import com.ibm.util.Hex;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

// Referenced classes of package com.ibm.asn1:
//            ASN1Decoder, ASN1Encoder, ASN1Exception, ASN1Tag, 
//            ASN1Type

public class EXTERNALChoice
    implements ASN1Type
{

    public EXTERNALChoice()
    {
        single_ASN1_type = null;
        octet_aligned = null;
        arbitrary = null;
    }

    public EXTERNALChoice(EXTERNALChoice externalchoice)
    {
        single_ASN1_type = null;
        octet_aligned = null;
        arbitrary = null;
        choiceId = externalchoice.choiceId;
        switch(choiceId)
        {
        case -2147483648: 
            single_ASN1_type = new byte[externalchoice.single_ASN1_type.length];
            System.arraycopy(externalchoice.single_ASN1_type, 0, single_ASN1_type, 0, externalchoice.single_ASN1_type.length);
            break;

        case -2147483647: 
            octet_aligned = new byte[externalchoice.octet_aligned.length];
            System.arraycopy(externalchoice.octet_aligned, 0, octet_aligned, 0, externalchoice.octet_aligned.length);
            break;

        case -2147483646: 
            arbitrary = externalchoice.arbitrary;
            break;

        }
    }

    public void decode(ASN1Decoder asn1decoder)
        throws ASN1Exception
    {
        choiceId = asn1decoder.decodeChoice(tag_list);
        switch(choiceId)
        {
        case -2147483648: 
            int i = asn1decoder.decodeExplicit(ASN1Tag.makeTag(2, 0));
            single_ASN1_type = asn1decoder.decodeOctetString();
            asn1decoder.endOf(i);
            break;

        case -2147483647: 
            asn1decoder.nextIsImplicit(ASN1Tag.makeTag(2, 1));
            octet_aligned = asn1decoder.decodeOctetString();
            break;

        case -2147483646: 
            asn1decoder.nextIsImplicit(ASN1Tag.makeTag(2, 2));
            arbitrary = asn1decoder.decodeBitString();
            break;

        }
    }

    public void encode(ASN1Encoder asn1encoder)
        throws ASN1Exception
    {
        asn1encoder.setDefinedName("EXTERNALChoice");
        int i = asn1encoder.encodeChoice(choiceId, tag_list);
        switch(choiceId)
        {
        case -2147483648: 
            int j = asn1encoder.encodeExplicit(ASN1Tag.makeTag(2, 0));
            asn1encoder.encodeOctetString(single_ASN1_type);
            asn1encoder.endOf(j);
            break;

        case -2147483647: 
            asn1encoder.nextIsImplicit(ASN1Tag.makeTag(2, 1));
            asn1encoder.encodeOctetString(octet_aligned);
            break;

        case -2147483646: 
            asn1encoder.nextIsImplicit(ASN1Tag.makeTag(2, 2));
            asn1encoder.encodeBitString(arbitrary);
            break;

        }
        asn1encoder.endOf(i);
    }

    public void print(PrintStream printstream)
    {
        print(printstream, 0);
    }

    public void print(PrintStream printstream, int i)
    {
        printstream.println("{ -- CHOICE --");
        switch(choiceId)
        {
        default:
            break;

        case -2147483648: 
            for(int j = 0; j < i + 2; j++)
                printstream.print(' ');

            printstream.print("single-ASN1-type = ");
            printstream.print(Hex.toString(single_ASN1_type));
            break;

        case -2147483647: 
            for(int k = 0; k < i + 2; k++)
                printstream.print(' ');

            printstream.print("octet-aligned = ");
            printstream.print(Hex.toString(octet_aligned));
            break;

        case -2147483646: 
            for(int l = 0; l < i + 2; l++)
                printstream.print(' ');

            printstream.print("arbitrary = ");
            printstream.print(arbitrary.toString());
            break;

        }
        for(int i1 = 0; i1 < i; i1++)
            printstream.print(' ');

        printstream.print("}");
    }

    public String toString()
    {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        PrintStream printstream = new PrintStream(bytearrayoutputstream);
        print(printstream);
        printstream.close();
        return bytearrayoutputstream.toString();
    }

    public static final int SINGLE_ASN1_TYPE_CID = 0x80000000;
    public static final int OCTET_ALIGNED_CID = 0x80000001;
    public static final int ARBITRARY_CID = 0x80000002;
    int tag_list[] = {
        0x80000000, 0x80000001, 0x80000002
    };
    public int choiceId;
    public byte single_ASN1_type[];
    public byte octet_aligned[];
    public BitString arbitrary;
}
