// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DEREncoder.java

package com.ibm.asn1;

import com.ibm.util.Comparator;

// Referenced classes of package com.ibm.asn1:
//            CompareContext, DEREncoder, DERPos, TagsComparator

class OctetStringComparator extends Comparator
{

    OctetStringComparator(DEREncoder derencoder)
    {
        encoder = derencoder;
    }

    public int compare(Object obj, Object obj1)
    {
        CompareContext comparecontext = new CompareContext(encoder, (DERPos)obj);
        CompareContext comparecontext1 = new CompareContext(encoder, (DERPos)obj1);
        int i;
        do
        {
            i = comparecontext.nextOctet();
            int j = comparecontext1.nextOctet();
            int k;
            if((k = i - j) != 0)
                return k;
        }
        while(i != -1);
        return 0;
    }

    DEREncoder encoder;
}
