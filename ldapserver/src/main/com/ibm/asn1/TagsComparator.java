// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DEREncoder.java

package com.ibm.asn1;

import com.ibm.util.Comparator;

// Referenced classes of package com.ibm.asn1:
//            ASN1Tag, CompareContext, DERPos, EncPos, 
//            OctetStringComparator

class TagsComparator extends Comparator
{

    TagsComparator()
    {
    }

    public int compare(Object obj, Object obj1)
    {
        int i = ((DERPos)obj).pos.getEncodedTag();
        int j = ((DERPos)obj1).pos.getEncodedTag();
        int k = ASN1Tag.getTagClass(i) - ASN1Tag.getTagClass(j);
        return k == 0 ? ASN1Tag.getTagNumber(i) - ASN1Tag.getTagNumber(j) : k;
    }
}
