// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BERDecoder.java

package com.ibm.asn1;


// Referenced classes of package com.ibm.asn1:
//            BERDecoder

final class DecStack
{

    DecStack()
    {
    }

    int stack[];
    DecStack prev;
    DecStack next;
}
