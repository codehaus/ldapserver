// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ASN1OID.java

package com.ibm.asn1;


// Referenced classes of package com.ibm.asn1:
//            ASN1OID

class PrefixLookupHack
{

    PrefixLookupHack()
    {
    }

    public boolean equals(Object obj)
    {
        if(!(obj instanceof ASN1OID))
            return false;
        ASN1OID asn1oid = (ASN1OID)obj;
        if(asn1oid.level() < nValid)
            return false;
        for(int i = 0; i < nValid; i++)
            if(ids[i] != asn1oid.nthComponent(i))
                return false;

        return true;
    }

    public int hashCode()
    {
        return hashCode;
    }

    int hashCode;
    int ids[];
    int nValid;
}
