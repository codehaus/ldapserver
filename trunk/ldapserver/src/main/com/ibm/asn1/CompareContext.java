// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DEREncoder.java

package com.ibm.asn1;


// Referenced classes of package com.ibm.asn1:
//            BEREncoder, ContextData, DERPos, EncBuf, 
//            EncPos, TagsComparator, DEREncoder

class CompareContext
{

    CompareContext(DEREncoder derencoder, DERPos derpos)
    {
        enc = derencoder;
        chunkRest = 0;
        stack = new ContextData();
        stack.pos = derpos;
        stack.nthChild = -1;
    }

    int nextOctet()
    {
        while(chunkRest <= 0) 
        {
            if(stack == null)
                return -1;
            DERPos derpos;
            if(stack.nthChild == -1)
            {
                derpos = stack.pos;
                stack.nthChild = 0;
            }
            else
            {
                if(stack.pos.children == null || stack.nthChild >= stack.pos.children.length)
                {
                    stack = stack.next;
                    continue;
                }
                if(!(derpos = stack.pos.children[stack.nthChild++]).pos.isPrimitive())
                {
                    ContextData contextdata = new ContextData();
                    contextdata.pos = stack.pos.children[stack.nthChild++];
                    contextdata.nthChild = -1;
                    contextdata.next = stack;
                    stack = contextdata;
                    continue;
                }
            }
            chunkRest = derpos.pos.isPrimitive() ? derpos.pos.length : derpos.pos.getTLLength();
            enc.setToPos(derpos.pos.offset);
            stack.buf = enc.currBuf;
            stack.bufpos = enc.currBufPos;
        }

        if(stack.bufpos >= stack.buf.data.length)
        {
            stack.buf = stack.buf.next;
            stack.bufpos = 0;
        }
        chunkRest--;
        return stack.buf.data[stack.bufpos++] & 0xff;
    }

    ContextData stack;
    int chunkRest;
    DEREncoder enc;
}
