// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BEREncoder.java

package com.ibm.asn1;

import java.io.*;

// Referenced classes of package com.ibm.asn1:
//            BaseBEREncoder, ASN1Exception, ASN1Tag, BERConstants, 
//            EncBuf, EncPos, EncPosTable

public class BEREncoder extends BaseBEREncoder
{

    public BEREncoder()
    {
        this(1024, 64);
    }

    public BEREncoder(int i, int j)
    {
        if(i < 32)
            i = 32;
        if(j < 16)
            j = 16;
        firstTable = new EncPosTable();
        firstTable.table = new EncPos[j];
        firstBuf = new EncBuf(i);
        firstTable.table[0] = new EncPos();
        init();
    }

    public int codeLength()
    {
        return firstTable.table[0].length;
    }

    public void debug()
    {
        System.err.println("currBuf:    " + currBuf);
        System.err.println("currBufPos: " + currBufPos);
        System.err.println("offset:     " + offset);
        System.err.println("currTable:  " + currTable);
        System.err.println("currPos:    " + currPos);
        System.err.println("parentPos:  " + parentPos);
        System.err.println("tableIndex: " + tableIndex);
    }

    public void encodeAny(byte abyte0[], int i, int j)
        throws ASN1Exception
    {
        encodeTag(false, 0);
        putOctets(abyte0, i, j);
    }

    public int encodeConstructedString(int i)
        throws ASN1Exception
    {
        return encodeTag(true, i);
    }

    public int encodeExplicit(int i)
        throws ASN1Exception
    {
        return encodeTag(true, i);
    }

    public int encodeSequence()
        throws ASN1Exception
    {
        return encodeTag(true, 16);
    }

    public int encodeSet()
        throws ASN1Exception
    {
        return encodeTag(true, 17);
    }

    protected int encodeTag(boolean flag, int i)
        throws ASN1Exception
    {
        parentPos.oneMoreChild();
        if(currPos.isPrimitive() && currPos.length == 0)
            parentPos.length += currPos.length = offset - currPos.offset;
        newCurrPos();
        currPos.parent = parentPos;
        currPos.setTag(i, specialTag ? implicitTag : -1);
        if(flag)
        {
            currPos.offset = nContiguousBytes(10);
            specialTag = false;
            parentPos = currPos;
        }
        else
        {
            currPos.offset = offset;
            currPos.setPrimitive();
            if(i != 0)
                super.encodeTag(false, i);
        }
        return currPos.offset;
    }

    public void endOf(int i)
        throws ASN1Exception
    {
        if(i == -1)
        {
            return;
        }
        else
        {
            endOfConstructed(i);
            return;
        }
    }

    protected void endOfConstructed(int i)
        throws ASN1Exception
    {
        if(parentPos == firstTable.table[0] || i != parentPos.offset)
            throw new ASN1Exception("Mismatch of constructed encoding terminations");
        if(currPos.isPrimitive() && currPos.length == 0)
            parentPos.length += currPos.length = offset - currPos.offset;
        EncBuf encbuf = currBuf;
        int j = currBufPos;
        int k = offset;
        setToPos(parentPos.offset);
        super.encodeTag(true, parentPos.getEncodedTag());
        super.encodeLength(parentPos.length);
        int l = offset - parentPos.offset;
        parentPos.setTLLength(l);
        parentPos.parent.length += l + parentPos.length;
        parentPos = parentPos.parent;
        currBuf = encbuf;
        currBufPos = j;
        offset = k;
    }

    public void finish()
        throws ASN1Exception
    {
        if(parentPos != firstTable.table[0])
            throw new ASN1Exception("Unfinished constructed encodings");
        if(currPos.isPrimitive() && currPos.length == 0)
            parentPos.length += currPos.length = offset - currPos.offset;
    }

    public void init()
    {
        parentPos = firstTable.table[0];
        parentPos.offset = 0;
        parentPos.parent = null;
        currPos = parentPos;
        currBuf = firstBuf;
        currTable = firstTable;
        tableIndex = 1;
        currBufPos = offset = 0;
        specialTag = false;
    }

    protected int nContiguousBytes(int i)
    {
        int j = currBuf.data.length - currBufPos;
        if(i > j)
        {
            offset += j;
            if(currBuf.next == null)
                currBuf.next = new EncBuf(currBuf);
            currBuf = currBuf.next;
            currBufPos = 0;
        }
        currBufPos += i;
        offset += i;
        return offset - i;
    }

    private void newCurrPos()
    {
        if(tableIndex >= currTable.table.length)
        {
            if(currTable.next == null)
            {
                EncPosTable encpostable = new EncPosTable();
                encpostable.table = new EncPos[currTable.table.length];
                currTable.next = encpostable;
            }
            currTable = currTable.next;
            tableIndex = 0;
        }
        if((currPos = currTable.table[tableIndex++]) == null)
            currPos = currTable.table[tableIndex - 1] = new EncPos();
        else
            currPos.init();
    }

    protected void putOctet(int i)
    {
        if(currBufPos >= currBuf.data.length)
        {
            if(currBuf.next == null)
                currBuf.next = new EncBuf(currBuf);
            currBuf = currBuf.next;
            currBufPos = 0;
        }
        currBuf.data[currBufPos++] = (byte)i;
        offset++;
    }

    protected void putOctets(byte abyte0[], int i, int j)
    {
        int k = currBuf.data.length - currBufPos;
        offset += j;
        int l = j <= k ? j : k;
        for(; j > k; k = currBuf.data.length)
        {
            System.arraycopy(abyte0, i, currBuf.data, currBufPos, k);
            j -= k;
            i += k;
            if(currBuf.next == null)
                currBuf.next = new EncBuf(currBuf);
            currBuf = currBuf.next;
            currBufPos = 0;
        }

        if(j > 0)
        {
            System.arraycopy(abyte0, i, currBuf.data, currBufPos, j);
            currBufPos += j;
        }
    }

    protected void setToPos(int i)
    {
        int j = 0;
        if(offset > i)
        {
            currBuf = firstBuf;
            currBufPos = j = 0;
        }
        else
        {
            j = offset - currBufPos;
        }
        do
        {
            int k = currBuf.data.length;
            if(j + k > i)
            {
                currBufPos = i - j;
                offset = i;
                return;
            }
            j += k;
            currBuf = currBuf.next;
        }
        while(true);
    }

    public byte[] toByteArray()
        throws ASN1Exception
    {
        try
        {
            finish();
            byte abyte0[] = new byte[firstTable.table[0].length];
            writeTo(null, abyte0, 0);
            return abyte0;
        }
        catch(IOException _ex)
        {
            throw new Error("A <Should never happen> DID HAPPEN!");
        }
    }

    public int toByteArray(byte abyte0[], int i)
        throws ASN1Exception
    {
        try
        {
            return writeTo(null, abyte0, i) - i;
        }
        catch(IOException _ex)
        {
            throw new Error("A <Should never happen> DID HAPPEN!");
        }
    }

    public String toString()
    {
        return "BER";
    }

    protected int writeBlock(OutputStream outputstream, byte abyte0[], int i, int j, int k)
        throws IOException
    {
        setToPos(j);
        while(k > 0) 
        {
            int l;
            if((l = currBuf.data.length - currBufPos) > k)
                l = k;
            if(outputstream != null)
            {
                outputstream.write(currBuf.data, currBufPos, l);
            }
            else
            {
                System.arraycopy(currBuf.data, currBufPos, abyte0, i, l);
                i += l;
            }
            if((currBufPos += l) >= currBuf.data.length)
            {
                currBufPos = 0;
                currBuf = currBuf.next;
            }
            k -= l;
            offset += l;
        }

        return i;
    }

    public int writeTo(OutputStream outputstream)
        throws ASN1Exception, IOException
    {
        return writeTo(outputstream, null, 0);
    }

    protected int writeTo(OutputStream outputstream, byte abyte0[], int i)
        throws ASN1Exception, IOException
    {
        finish();
        EncPosTable encpostable = currTable;
        int j = tableIndex;
        currTable = firstTable;
        for(tableIndex = 0; ++tableIndex < j || currTable != encpostable;)
            if(tableIndex >= currTable.table.length)
            {
                currTable = currTable.next;
                tableIndex = -1;
            }
            else
            {
                EncPos encpos = currTable.table[tableIndex];
                if(encpos.isPrimitive())
                    i = writeBlock(outputstream, abyte0, i, encpos.offset, encpos.length);
                else
                    i = writeBlock(outputstream, abyte0, i, encpos.offset, encpos.getTLLength());
            }

        return i;
    }

    public static final String RULES_NAME = "BER";
    protected EncBuf firstBuf;
    protected EncBuf currBuf;
    protected int currBufPos;
    protected int offset;
    protected EncPosTable firstTable;
    protected EncPosTable currTable;
    protected EncPos currPos;
    protected EncPos parentPos;
    protected int tableIndex;
}
