// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RDNAttribute.java

package com.ibm.util.x500name;

import com.ibm.asn1.*;
import com.ibm.util.Comparable;
import java.io.Serializable;

// Referenced classes of package com.ibm.util.x500name:
//            RDNAttributeFactory

public abstract class RDNAttribute
    implements Comparable, Serializable
{

    public RDNAttribute(RDNAttributeFactory rdnattributefactory)
    {
        theFactory = rdnattributefactory;
    }

    public int compareTo(Object obj)
    {
        RDNAttribute rdnattribute = (RDNAttribute)obj;
        int i;
        if((i = keyToASN1OID().compareTo(rdnattribute.keyToASN1OID())) != 0)
            return i;
        else
            return valueToString().compareTo(rdnattribute.valueToString());
    }

    public void encode(ASN1Encoder asn1encoder)
        throws ASN1Exception
    {
        int i = asn1encoder.encodeSequence();
        encodeKey(asn1encoder);
        encodeValue(asn1encoder);
        asn1encoder.endOf(i);
    }

    public void encodeKey(ASN1Encoder asn1encoder)
        throws ASN1Exception
    {
        asn1encoder.encodeObjectIdentifier(keyToASN1OID());
    }

    abstract void encodeValue(ASN1Encoder asn1encoder)
        throws ASN1Exception;

    public boolean equals(RDNAttribute rdnattribute)
    {
        if(rdnattribute == null || !getClass().equals(rdnattribute.getClass()))
        {
            return false;
        }
        else
        {
            RDNAttribute rdnattribute1 = rdnattribute;
            return keyToASN1OID().equals(rdnattribute1.keyToASN1OID()) && valueToString().compareTo(rdnattribute1.valueToString()) == 0;
        }
    }

    public RDNAttributeFactory factory()
    {
        return theFactory;
    }

    public int hashCode()
    {
        return keyToASN1OID().hashCode() ^ valueToString().hashCode();
    }

    public ASN1OID keyToASN1OID()
    {
        return theFactory.keyASN1OID();
    }

    public String keyToString()
    {
        return theFactory.keyString();
    }

    public String toString()
    {
        String s = valueToString();
        boolean flag = false;
        boolean flag1 = false;
        if(s.startsWith(" ") || s.endsWith(" "))
        {
            flag = true;
        }
        else
        {
            for(int i = s.length() - 1; i >= 0;)
                switch(s.charAt(i))
                {
                case 10: // '\n'
                case 13: // '\r'
                case 35: // '#'
                case 43: // '+'
                case 44: // ','
                case 59: // ';'
                case 60: // '<'
                case 61: // '='
                case 62: // '>'
                    flag = true;
                    // fall through

                case 34: // '"'
                case 92: // '\\'
                    flag1 = true;
                    // fall through

                default:
                    i--;
                    break;

                }

        }
        if(flag1)
        {
            StringBuffer stringbuffer = new StringBuffer();
            int j = 0;
            for(int k = s.length(); j < k; j++)
            {
                char c;
                if((c = s.charAt(j)) == '"' || c == '\\')
                    stringbuffer.append('\\');
                stringbuffer.append(c);
            }

            s = stringbuffer.toString();
            flag = true;
        }
        return keyToString() + "=" + (flag ? '"' + s + '"' : s);
    }

    public abstract String valueToString();

    protected RDNAttributeFactory theFactory;
}
