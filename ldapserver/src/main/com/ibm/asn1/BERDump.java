// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BERDump.java

package com.ibm.asn1;

import java.io.*;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

// Referenced classes of package com.ibm.asn1:
//            BERDecoder, ASN1Any, ASN1Exception, ASN1OID, 
//            ASN1Tag

public class BERDump extends BERDecoder
{

    public BERDump(ASN1Any asn1any)
    {
        this(new ByteArrayInputStream(asn1any.data, asn1any.begin, asn1any.length));
    }

    public BERDump(InputStream inputstream)
    {
        super(inputstream);
        indent = "";
        beforeNextNewLine = null;
        octetsThisLine = 0;
        octetPrefix = ' ';
        defaultOctetPrefix = ' ';
        comprehensive = null;
    }

    protected int getOctet()
        throws ASN1Exception
    {
        if(comprehensive != null)
            return super.getOctet();
        char c = octetPrefix;
        octetPrefix = defaultOctetPrefix;
        if(octetsThisLine >= 16)
        {
            newLine();
            c = '+';
        }
        if(octetsThisLine == 0)
        {
            printInt(readOctets, 4, 0);
            out.print(":");
            out.print(indent);
            wasIndentLength = indent.length();
        }
        int i = super.getOctet();
        out.print(c);
        printInt(i, 2, 2);
        octetsThisLine++;
        return i;
    }

    private void indentOut()
    {
        if(indent.length() < 3)
            throw new Error("Garbled encoding?? Stack underflow of constructed encodings.");
        indent = indent.substring(3);
        if(comprehensive == null)
            newLine();
        else
            comprehensive.append(" } ");
    }

    public static void main(String args[])
    {
        if(args.length == 0)
        {
            System.err.println("usage: com.ibm.asn1.BERDump file");
            System.exit(1);
        }
        try
        {
            FileInputStream fileinputstream = new FileInputStream(args[0]);
            BERDump berdump = new BERDump(fileinputstream);
            berdump.print(System.out);
        }
        catch(IOException ioexception)
        {
            System.err.println("Cannot open or read file `" + args[0] + "': " + ioexception.getMessage());
            System.exit(1);
        }
    }

    private void newLine()
    {
        if(octetsThisLine == 0)
            return;
        if(beforeNextNewLine != null)
        {
            for(int i = octetsThisLine * 3 + wasIndentLength; i++ < 40;)
                out.print(' ');

            out.print("   --");
            out.print(beforeNextNewLine);
            beforeNextNewLine = null;
        }
        out.println();
        octetsThisLine = 0;
    }

    private void nextElement(boolean flag)
    {
        if(flag)
            indent = indent + " | ";
        if(comprehensive != null)
        {
            if(beforeNextNewLine != null)
            {
                comprehensive.append(beforeNextNewLine);
                comprehensive.append(flag ? " { " : "; ");
                beforeNextNewLine = null;
            }
        }
        else
        {
            newLine();
        }
    }

    public void print(PrintStream printstream)
    {
        out = printstream;
        int i = 0;
        try
        {
            do
            {
                i = readOctets;
                newLine();
                boolean flag;
                try
                {
                    defaultOctetPrefix = '-';
                    flag = decodeTagAndLength();
                    octetPrefix = defaultOctetPrefix = ' ';
                }
                catch(ASN1Exception asn1exception1)
                {
                    if(asn1exception1.getCause() instanceof EOFException)
                        return;
                    else
                        throw asn1exception1;
                }
                if(!flag)
                {
                    if(stackPos == 0)
                        break;
                    pop();
                    indentOut();
                }
                else
                {
                    StringBuffer stringbuffer = new StringBuffer(1024);
                    stringbuffer.append((constructed ? " Cons. " : " Prim. ") + ASN1Tag.tagToString(tag));
                    stringbuffer.append(" Length=" + (valueLength != -1 ? Integer.toString(valueLength) : "<indefinite>"));
                    switch(tag)
                    {
                    case 23: // '\027'
                    case 24: // '\030'
                        pushBack();
                        stringbuffer.append(" <");
                        stringbuffer.append(tag != 23 ? decodeGeneralizedTime().getTime().toString() : decodeUTCTime().getTime().toString());
                        stringbuffer.append(">");
                        valueLength = 0;
                        break;

                    default:
                        if(constructed)
                        {
                            push(tag);
                            break;
                        }
                        switch(tag)
                        {
                        case 2: // '\002'
                            if(valueLength <= 8)
                            {
                                pushBack();
                                stringbuffer.append(" Value=");
                                stringbuffer.append(decodeInteger().toString());
                                valueLength = 0;
                            }
                            break;

                        case 19: // '\023'
                            pushBack();
                            stringbuffer.append(" PrintableString=\"" + decodePrintableString() + "\"");
                            valueLength = 0;
                            break;

                        case 22: // '\026'
                            pushBack();
                            stringbuffer.append(" IA5String=\"" + decodeIA5String() + "\"");
                            valueLength = 0;
                            break;

                        case 20: // '\024'
                            pushBack();
                            stringbuffer.append(" T61String=\"" + decodeT61String() + "\"");
                            valueLength = 0;
                            break;

                        case 30: // '\036'
                            pushBack();
                            stringbuffer.append(" BMPString=\"" + decodeBMPString() + "\"");
                            valueLength = 0;
                            break;

                        case 3: // '\003'
                            stringbuffer.append(" # of bits=" + Integer.toString(--valueLength * 8 - getOctet()));
                            break;

                        case 6: // '\006'
                            pushBack();
                            stringbuffer.append(" ");
                            stringbuffer.append(decodeObjectIdentifier().toString());
                            valueLength = 0;
                            break;

                        }
                        while(valueLength-- > 0) 
                            getOctet();

                        break;

                    }
                    beforeNextNewLine = stringbuffer.toString();
                    nextElement(constructed);
                }
            }
            while(true);
        }
        catch(ASN1Exception asn1exception)
        {
            Throwable exception = asn1exception.getCause();
            if(exception != null)
                exception = asn1exception.getCause();
            System.err.println(asn1exception.getClass().getName() + ": " + asn1exception.getMessage());
            if(readOctets == i)
                return;
            asn1exception.printStackTrace(printstream);
        }
    }

    private void printInt(int i, int j, int k)
    {
        byte byte0 = 1;
        if(i >= 16)
            byte0 = 2;
        if(i >= 256)
            byte0 = 3;
        if(i >= 4096)
            byte0 = 4;
        if(i >= 0x10000)
            byte0 = 5;
        if(i >= 0x100000)
            byte0 = 6;
        if(i >= 0x1000000)
            byte0 = 7;
        if(i >= 0x10000000)
            byte0 = 8;
        int l = j > byte0 ? j - byte0 : 0;
        for(int i1 = k - byte0 - l; i1-- > 0;)
            out.print(' ');

        while(l-- > 0) 
            out.print('0');

        out.print(Integer.toString(i, 16));
    }

    public String toString()
    {
        comprehensive = new StringBuffer();
        print(null);
        return comprehensive.toString();
    }

    private PrintStream out;
    private String indent;
    private String beforeNextNewLine;
    private int octetsThisLine;
    private int wasIndentLength;
    private char octetPrefix;
    private char defaultOctetPrefix;
    private StringBuffer comprehensive;
}
