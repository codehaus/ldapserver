// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EncPosTable.java

package com.ibm.asn1;


// Referenced classes of package com.ibm.asn1:
//            EncPos

final class EncPosTable
{

    EncPosTable()
    {
        next = null;
    }

    EncPos table[];
    EncPosTable next;
}
