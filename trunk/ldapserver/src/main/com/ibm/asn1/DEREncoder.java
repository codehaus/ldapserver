// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DEREncoder.java

package com.ibm.asn1;

import com.ibm.util.Sorter;
import java.io.IOException;
import java.io.OutputStream;

// Referenced classes of package com.ibm.asn1:
//            BEREncoder, ASN1Exception, ASN1Tag, BaseBEREncoder, 
//            DERPos, EncPos, EncPosTable, OctetStringComparator, 
//            TagsComparator

public class DEREncoder extends BEREncoder
{

    public DEREncoder()
    {
        tagSorter = new Sorter(new TagsComparator());
        octetSorter = new Sorter(new OctetStringComparator(this));
        finishedRoot = null;
    }

    public DEREncoder(int i, int j)
    {
        super(i, j);
        tagSorter = new Sorter(new TagsComparator());
        octetSorter = new Sorter(new OctetStringComparator(this));
        finishedRoot = null;
    }

    protected DERPos builtDERNode(EncPos encpos)
    {
        DERPos derpos = new DERPos(encpos);
        int i = 0;
        int j = encpos.howManyChildren();
        int k = encpos.getUnderLyingTag();
        derpos.children = new DERPos[j];
        while(i < j) 
        {
            nextTableIndex();
            EncPos encpos1 = currTable.table[tableIndex];
            derpos.children[i++] = encpos1.isPrimitive() ? new DERPos(encpos1) : builtDERNode(encpos1);
        }

        if(k == 17)
            (encpos.isSetOf() ? octetSorter : tagSorter).sortDestructive(derpos.children);
        return derpos;
    }

    public int encodeConstructedString(int i)
        throws ASN1Exception
    {
        throw new ASN1Exception("Constructed strings are illegal under DER encoding");
    }

    public boolean encodeDefault()
    {
        return false;
    }

    public int encodeSetOf()
        throws ASN1Exception
    {
        int i = super.encodeSetOf();
        currPos.setSetOfFlag();
        return i;
    }

    public void finish()
        throws ASN1Exception
    {
        super.finish();
        if(finishedRoot != null)
        {
            return;
        }
        else
        {
            EncPosTable encpostable = currTable;
            int i = tableIndex;
            currTable = firstTable;
            tableIndex = 0;
            finishedRoot = builtDERNode(firstTable.table[0]);
            return;
        }
    }

    private final void nextTableIndex()
    {
        if(++tableIndex >= currTable.table.length)
        {
            tableIndex = 0;
            currTable = currTable.next;
        }
    }

    protected int outputNode(DERPos derpos, OutputStream outputstream, byte abyte0[], int i)
        throws IOException
    {
        int j = 0;
        for(int k = derpos.children.length; j < k;)
        {
            DERPos derpos1 = derpos.children[j++];
            if(derpos1.pos.isPrimitive())
            {
                i = writeBlock(outputstream, abyte0, i, derpos1.pos.offset, derpos1.pos.length);
            }
            else
            {
                i = writeBlock(outputstream, abyte0, i, derpos1.pos.offset, derpos1.pos.getTLLength());
                i = outputNode(derpos1, outputstream, abyte0, i);
            }
        }

        return i;
    }

    public String toString()
    {
        return "DER";
    }

    protected int writeTo(OutputStream outputstream, byte abyte0[], int i)
        throws IOException
    {
        return outputNode(finishedRoot, outputstream, abyte0, i) - i;
    }

    public static final String RULES_NAME = "DER";
    private Sorter tagSorter;
    private Sorter octetSorter;
    private DERPos finishedRoot;
}
