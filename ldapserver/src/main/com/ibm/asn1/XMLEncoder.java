// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XMLEncoder.java

package com.ibm.asn1;

import com.ibm.util.BitString;
import com.ibm.util.Hex;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.*;

// Referenced classes of package com.ibm.asn1:
//            ASN1Encoder, ASN1Exception, ASN1OID, ASN1Tag

public class XMLEncoder extends ASN1Encoder
{
    private class Tag
    {

        String type;
        String tclass;
        int value;

        Tag(String s, int i)
        {
            type = s;
            switch(ASN1Tag.getTagClass(i))
            {
            case 0: // '\0'
                tclass = "UNIVERSAL";
                break;

            case 1: // '\001'
                tclass = "APPLICATION";
                break;

            case 2: // '\002'
                tclass = "CONTEXT";
                break;

            case 3: // '\003'
                tclass = "PRIVATE";
                break;

            }
            value = ASN1Tag.getTagNumber(i);
        }
    }


    public XMLEncoder()
    {
        acc = new StringBuffer();
        tagList = new Vector();
        specialTag = false;
        tagName = "";
        id_nr = -1;
        scope = new Hashtable();
        indentLevel = 0;
    }

    public void dec()
    {
        indentLevel--;
    }

    public void encodeAny(byte abyte0[], int i, int j)
        throws ASN1Exception
    {
        write("<OCTETSTRING value=\"" + Hex.toString(abyte0) + "\">");
        writeTag();
        write("</OCTETSTRING>");
    }

    public void encodeBMPString(String s)
        throws ASN1Exception
    {
        write("<BMPSTRING value=\"" + s + "\">");
        writeTag();
        write("</BMPSTRING>");
    }

    public void encodeBitString(BitString bitstring)
        throws ASN1Exception
    {
        write("<BITSTRING value=\"" + bitstring.toString() + "\">");
        writeTag();
        write("</BITSTRING>");
    }

    public void encodeBoolean(boolean flag)
        throws ASN1Exception
    {
        write("<BOOLEAN value=\"" + flag + "\">");
        writeTag();
        write("</BOOLEAN>");
    }

    public int encodeChoice(int i, int ai[])
    {
        return newScope();
    }

    public boolean encodeDefault()
    {
        return true;
    }

    public void encodeEnumeration(int i)
        throws ASN1Exception
    {
        write("<ENUMERATION value=\"" + i + "\">");
        writeTag();
        write("</ENUMERATION>");
    }

    public int encodeExplicit(int i)
    {
        tagList.addElement(new Tag("EXPLICIT", i));
        specialTag = false;
        int j = ++id_nr;
        return j;
    }

    public void encodeGeneralString(String s)
        throws ASN1Exception
    {
        write("<GENERALSTRING value=\"" + s + "\">");
        writeTag();
        write("</GENERALSTRING>");
    }

    public void encodeGeneralizedTime(String s)
        throws ASN1Exception
    {
        write("<GENERALIZEDTIME value=\"" + s + "\">");
        writeTag();
        write("</GENERALIZEDTIME>");
    }

    public void encodeGeneralizedTime(Calendar calendar)
        throws ASN1Exception
    {
        encodeGeneralizedTime(calendar.toString());
    }

    public void encodeGraphicString(String s)
        throws ASN1Exception
    {
        write("<GRAPHICSTRING value=\"" + s + "\">");
        writeTag();
        write("</GRAPHICSTRING>");
    }

    public void encodeIA5String(String s)
        throws ASN1Exception
    {
        write("<IA5STRING value=\"" + s + "\">");
        writeTag();
        write("</IA5STRING>");
    }

    public void encodeInteger(int i)
        throws ASN1Exception
    {
        write("<INTEGER value=\"" + i + "\">");
        writeTag();
        write("</INTEGER>");
    }

    public void encodeInteger(long l)
        throws ASN1Exception
    {
        write("<INTEGER value=\"" + l + "\">");
        writeTag();
        write("</INTEGER>");
    }

    public void encodeInteger(BigInteger biginteger)
        throws ASN1Exception
    {
        encodeInteger(biginteger.intValue());
    }

    public void encodeNull()
        throws ASN1Exception
    {
        write("<NULL>");
        writeTag();
        write("</NULL>");
    }

    public void encodeNumericString(String s)
        throws ASN1Exception
    {
        write("<NUMERICSTRING value=\"" + s + "\">");
        writeTag();
        write("</NUMERICSTRING>");
    }

    public void encodeObjectIdentifier(ASN1OID asn1oid)
        throws ASN1Exception
    {
        write("<OID value=\"" + asn1oid.toString() + "\">");
        writeTag();
        write("</OID>");
    }

    public void encodeOctetString(byte abyte0[], int i, int j)
        throws ASN1Exception
    {
        write("<OCTETSTRING value=\"" + Hex.toString(abyte0) + "\">");
        writeTag();
        write("</OCTETSTRING>");
    }

    public void encodePrintableString(String s)
        throws ASN1Exception
    {
        write("<PRINTABLESTRING value=\"" + s + "\">");
        writeTag();
        write("</PRINTABLESTRING>");
    }

    public void encodeReal(double d)
        throws ASN1Exception
    {
        write("<REAL value=\"" + d + "\">");
        writeTag();
        write("</REAL>");
    }

    public int encodeSequence()
        throws ASN1Exception
    {
        return newScope();
    }

    public int encodeSequenceOf()
        throws ASN1Exception
    {
        return newScope();
    }

    public int encodeSet()
        throws ASN1Exception
    {
        return newScope();
    }

    public int encodeSetOf()
        throws ASN1Exception
    {
        return newScope();
    }

    public int encodeSimple()
    {
        return newScope();
    }

    public void encodeT61String(String s)
        throws ASN1Exception
    {
        write("<T61STRING value=\"" + s + "\">");
        writeTag();
        write("</T61STRING>");
    }

    public void encodeUTCTime(String s)
        throws ASN1Exception
    {
        write("<UTCTIME value=\"" + s + "\">");
        writeTag();
        write("</UTCTIME>");
    }

    public void encodeUTCTime(Calendar calendar)
        throws ASN1Exception
    {
        encodeUTCTime(calendar.toString());
    }

    public void encodeVideotexString(String s)
        throws ASN1Exception
    {
        write("<VIDEOTEXSTRING value=\"" + s + "\">");
        writeTag();
        write("</VIDEOTEXSTRING>");
    }

    public void encodeVisibleString(String s)
        throws ASN1Exception
    {
        write("<VISIBLESTRING value=\"" + s + "\">");
        writeTag();
        write("</VISIBLESTRING>");
    }

    public void endOf(int i)
    {
        if((tagName = (String)scope.get(new Integer(i))) != null)
        {
            dec();
            write("</" + tagName + ">");
        }
    }

    public void finish()
    {
    }

    public void inc()
    {
        indentLevel++;
    }

    private int newScope()
    {
        write("<" + tagName + ">");
        writeTag();
        inc();
        int i = ++id_nr;
        scope.put(new Integer(i), tagName);
        return i;
    }

    public void nextHasAlphabetConstraint(String s)
    {
    }

    public void nextHasSizeConstraints(long l, long l1)
    {
    }

    public void nextIsImplicit(int i)
    {
        if(!specialTag)
        {
            specialTag = true;
            tagList.addElement(new Tag("IMPLICIT", i));
        }
    }

    public void setDefinedName(String s)
    {
        tagName = s;
    }

    public String toString()
    {
        return "XML";
    }

    private void write(String s)
    {
        for(int i = 0; i < indentLevel; i++)
            acc.append("  ");

        acc.append(s);
        acc.append("\n");
    }

    private void writeTag()
    {
        inc();
        for(int i = 0; i < tagList.size(); i++)
        {
            Tag tag = (Tag)tagList.elementAt(i);
            write("<ASN1Tag type=\"" + tag.type + "\" class=\"" + tag.tclass + "\" value=\"" + tag.value + "\"/>");
        }

        tagList.clear();
        dec();
    }

    public void writeTo(OutputStream outputstream)
        throws ASN1Exception, IOException
    {
        byte abyte0[] = acc.toString().getBytes();
        outputstream.write(abyte0, 0, abyte0.length);
    }

    private StringBuffer acc;
    private Vector tagList;
    private boolean specialTag;
    private String tagName;
    private int id_nr;
    public Hashtable scope;
    private int indentLevel;
}
