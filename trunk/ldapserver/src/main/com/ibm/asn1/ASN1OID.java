// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ASN1OID.java

package com.ibm.asn1;

import com.ibm.util.Comparable;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

// Referenced classes of package com.ibm.asn1:
//            ASN1Decoder, ASN1Encoder, ASN1Exception, ASN1Type, 
//            PrefixLookupHack

public final class ASN1OID
    implements ASN1Type, Comparable, Serializable
{

    public ASN1OID()
    {
        ids = null;
    }

    public ASN1OID(String s, ASN1OID asn1oid)
    {
        ids = (int[])asn1oid.ids.clone();
        nm = s;
    }

    public ASN1OID(String s, ASN1OID asn1oid, int i)
    {
        int j = asn1oid.ids.length;
        ids = new int[j + 1];
        System.arraycopy(asn1oid.ids, 0, ids, 0, j);
        ids[j] = i;
        nm = s;
    }

    public ASN1OID(String s, ASN1OID asn1oid, int ai[])
    {
        this(s, asn1oid, ai, 0, ai.length);
    }

    public ASN1OID(String s, ASN1OID asn1oid, int ai[], int i, int j)
    {
        int k = asn1oid.ids.length;
        ids = new int[k + j];
        System.arraycopy(asn1oid.ids, 0, ids, 0, k);
        System.arraycopy(ai, 0, ids, k, j);
        nm = s;
    }

    public ASN1OID(String s, String s1)
    {
        int i = s1.length();
        int j = 0;
        int k = 0;
        int l = 0;
        char c = '\0';
        int ai[] = new int[100];
        String s2 = null;
        if(s1.charAt(j) == '{')
        {
            if(s1.charAt(i - 1) != '}')
                throw new IllegalArgumentException("No terminating }");
            j++;
            i--;
        }
        k = j;
        do
        {
            while(k < i && ((c = s1.charAt(k)) == ' ' || c == '\t' || c == '\n' || c == '\r')) 
                k++;

            int i1 = k;
            char c1 = 'X';
            if(k < i && ((c = s1.charAt(k)) == '-' || c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z'))
            {
                for(; k < i && (c = s1.charAt(k)) != '(' && c != ',' && c != '.' && c != ' ' && c != '\t' && c != '\r' && c != '\n'; k++);
                if(k <= i && c == '(')
                {
                    k++;
                    c1 = c;
                }
                else
                {
                    if(s2 != null)
                        throw new IllegalArgumentException("Only one ASN.1 OID prefix name allowed: " + i1);
                    s2 = s1.substring(i1, k);
                    ASN1OID asn1oid = find(s2);
                    if(asn1oid == null)
                        throw new IllegalArgumentException("No such ASN.1 OID name registered: " + s2);
                    for(int k1 = 0; k1 < asn1oid.ids.length; k1++)
                        ai[l++] = asn1oid.ids[k1];

                    c1 = '\0';
                }
            }
            if(c1 != 0)
            {
                long l1 = 0L;
                int j1 = k;
                while(k < i && (c = s1.charAt(k)) >= '0' && c <= '9') 
                {
                    k++;
                    if((l1 = l1 * 10L + (long)(c - 48)) > 0x7fffffffL)
                        throw new IllegalArgumentException("Components may not exceed Integer.MAX_VALUE: pos " + k);
                }

                if(j1 == k)
                    throw new IllegalArgumentException("Illegal ASN.1 OID format: " + k);
                if(c1 == '(')
                {
                    if(k >= i || c != ')')
                        throw new IllegalArgumentException("Can't find closing (: pos " + j1);
                    k++;
                }
                ai[l++] = (int)l1;
            }
            for(; k < i && ((c = s1.charAt(k)) == ' ' || c == '\t' || c == '\n' || c == '\r'); k++);
            if(k >= i)
                break;
            if((c = s1.charAt(k)) == '.' || c == ',')
                k++;
        }
        while(true);
        if(l == 0)
        {
            throw new IllegalArgumentException("Empty ASN.1 OID");
        }
        else
        {
            ids = new int[l];
            System.arraycopy(ai, 0, ids, 0, l);
            nm = s;
            return;
        }
    }

    public ASN1OID(String s, int ai[])
    {
        this(s, ai, 0, ai.length);
    }

    public ASN1OID(String s, int ai[], int i, int j)
    {
        ids = new int[j];
        System.arraycopy(ai, i, ids, 0, j);
        nm = s;
    }

    public int compareTo(ASN1OID asn1oid)
    {
        int i = ids.length;
        int j = asn1oid.ids.length;
        int k = i >= j ? j : i;
        for(int l = 0; l < k; l++)
        {
            int i1 = ids[l] - asn1oid.ids[l];
            if(i1 != 0)
                return i1 >= 0 ? 1 : -1;
        }

        if(i == j)
            return 0;
        else
            return i >= j ? 2 : -2;
    }

    public int compareTo(Object obj)
    {
        return compareTo((ASN1OID)obj);
    }

    public void decode(ASN1Decoder asn1decoder)
        throws ASN1Exception
    {
        if(ids != null)
        {
            throw new SecurityException("Initialized ASN1OID objects are immutable");
        }
        else
        {
            ASN1OID asn1oid = asn1decoder.decodeObjectIdentifier();
            ids = asn1oid.ids;
            nm = asn1oid.nm;
            return;
        }
    }

    public void encode(ASN1Encoder asn1encoder)
        throws ASN1Exception
    {
        asn1encoder.encodeObjectIdentifier(this);
    }

    public static Enumeration enumerateInternedOIDs()
    {
        return internedOIDs.keys();
    }

    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        if(!(obj instanceof ASN1OID))
            return obj.equals(this);
        else
            return compareTo((ASN1OID)obj) == 0;
    }

    public static ASN1OID find(String s)
    {
        return (ASN1OID)namedInterns.get(s);
    }

    public ASN1OID findLongestInternedPrefix()
    {
        PrefixLookupHack prefixlookuphack = new PrefixLookupHack();
        prefixlookuphack.ids = ids;
        for(prefixlookuphack.nValid = ids.length; prefixlookuphack.nValid > 0; prefixlookuphack.nValid--)
        {
            prefixlookuphack.hashCode = 0;
            for(int i = 0; i < prefixlookuphack.nValid; i++)
                prefixlookuphack.hashCode = hashNextComponent(prefixlookuphack.hashCode, prefixlookuphack.ids[i]);

            ASN1OID asn1oid;
            if((asn1oid = (ASN1OID)internedOIDs.get(prefixlookuphack)) != null)
                return asn1oid;
        }

        return null;
    }

    public int hashCode()
    {
        int i = 0;
        for(int j = 0; j < ids.length; j++)
            i = hashNextComponent(i, ids[j]);

        return i;
    }

    private static int hashNextComponent(int i, int j)
    {
        return i << 3 ^ j ^ i >>> 29;
    }

    public ASN1OID intern()
    {
        ASN1OID asn1oid;
        if((asn1oid = (ASN1OID)internedOIDs.get(this)) != null)
            return asn1oid;
        internedOIDs.put(this, this);
        if(nm != null && namedInterns.get(nm) == null)
            namedInterns.put(nm, this);
        return this;
    }

    public ASN1OID internedOrIdentity()
    {
        ASN1OID asn1oid;
        if((asn1oid = (ASN1OID)internedOIDs.get(this)) != null)
            return asn1oid;
        else
            return this;
    }

    public int level()
    {
        return ids.length;
    }

    public String name()
    {
        return nm;
    }

    public int nthComponent(int i)
    {
        return ids[i];
    }

    public ASN1OID prefix(String s, int i)
    {
        return new ASN1OID(s, ids, 0, i);
    }

    public String toASN1String()
    {
        StringBuffer stringbuffer = new StringBuffer(ids.length * 6);
        stringbuffer.append("{ ");
        for(int i = 0; i < ids.length; i++)
        {
            stringbuffer.append(Integer.toString(ids[i]));
            stringbuffer.append(' ');
        }

        stringbuffer.append('}');
        return stringbuffer.toString();
    }

    public String toPrettyString()
    {
        if(nm != null)
            return nm;
        for(ASN1OID asn1oid = this; (asn1oid = asn1oid.findLongestInternedPrefix()) != null;)
            if(asn1oid.name() != null)
            {
                StringBuffer stringbuffer = new StringBuffer(asn1oid.nm.length() + (ids.length - asn1oid.ids.length) * 6);
                stringbuffer.append(asn1oid.name());
                for(int i = asn1oid.ids.length; i < ids.length; i++)
                {
                    stringbuffer.append('.');
                    stringbuffer.append(Integer.toString(ids[i]));
                }

                return stringbuffer.toString();
            }

        return toString();
    }

    public String toString()
    {
        StringBuffer stringbuffer = new StringBuffer(ids.length * 6);
        stringbuffer.append(Integer.toString(ids[0]));
        for(int i = 1; i < ids.length; i++)
        {
            stringbuffer.append('.');
            stringbuffer.append(Integer.toString(ids[i]));
        }

        return stringbuffer.toString();
    }

    public static final int PREFIX = -2;
    public static final int LESS = -1;
    public static final int EQUAL = 0;
    public static final int GREATER = 1;
    public static final int EXTENSION = 2;
    private int ids[];
    private String nm;
    private static Hashtable internedOIDs = new Hashtable();
    private static Hashtable namedInterns = new Hashtable();

}
