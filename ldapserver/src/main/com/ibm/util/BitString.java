// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BitString.java

package com.ibm.util;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Vector;

// Referenced classes of package com.ibm.util:
//            Comparable, Constants

public final class BitString
    implements Comparable, Constants, Serializable
{

    public BitString(int i, int j, int k)
    {
        if(j < 0 || j + k > 32)
        {
            throw new IllegalArgumentException("Bit indices out of range");
        }
        else
        {
            bits = new int[1];
            bits[0] = i;
            begBit = j;
            nBits = k;
            return;
        }
    }

    private BitString(int i, int j, int ai[])
    {
        bits = ai;
        nBits = j;
        begBit = i;
    }

    public BitString(long l, int i, int j)
    {
        if(i < 0 || i + j > 64)
        {
            throw new IllegalArgumentException("Bit indices out of range");
        }
        else
        {
            bits = new int[2];
            bits[0] = (int)l;
            bits[1] = (int)(l >>> 32);
            begBit = i;
            nBits = j;
            return;
        }
    }

    public BitString(byte abyte0[], int i, int j, boolean flag)
    {
        nBits = j;
        byte byte0;
        int k;
        if(flag)
        {
            byte0 = -1;
            begBit = (8 - (i + j) % 8) % 8;
            k = (i - 8) / 8;
            i = ((i + j) - 1) / 8;
        }
        else
        {
            byte0 = 1;
            begBit = i % 8;
            k = (i + j + 7) / 8;
            i /= 8;
        }
        bits = new int[(begBit + nBits + 31) / 32];
        int l = i;
        for(int i1 = 0; l != k; i1++)
        {
            bits[i1 / 4] |= (abyte0[l] & 0xff) << 8 * (i1 % 4);
            l += byte0;
        }

    }

    public BitString(int ai[], int i, int j, boolean flag)
    {
        begBit = i % 32;
        nBits = j;
        bits = new int[(begBit + nBits + 31) / 32];
        if(!flag)
        {
            System.arraycopy(ai, i / 32, bits, 0, bits.length);
        }
        else
        {
            int k = ((i + j) - 1) / 32;
            for(int l = 0; l < bits.length; l++)
            {
                bits[l] = ai[k];
                k--;
            }

        }
    }

    public BitString(long al[], int i, int j, boolean flag)
    {
        begBit = i;
        nBits = j;
        i /= 64;
        byte byte0 = flag ? ((byte) (-1)) : 1;
        bits = new int[((begBit + nBits + 63) / 64) * 2];
        int k = i;
        for(int l = 0; l < bits.length; l += 2)
        {
            bits[l] = (int)al[k];
            bits[l + 1] = (int)(al[k] >>> 32);
            k += byte0;
        }

    }

    public BitString and(BitString bitstring)
    {
        int i = length() >= bitstring.length() ? bitstring.length() : length();
        if(i == 0)
            return EMPTY;
        int ai[] = new int[(i + 31) / 32];
        int j = 0;
        int k;
        for(k = 0; k >= 32;)
        {
            ai[j++] = (int)getSomeBits(k, 32) & (int)bitstring.getSomeBits(k, 32);
            k += 32;
            i -= 32;
        }

        ai[j] = (int)getSomeBits(k, i) & (int)bitstring.getSomeBits(k, i);
        return new BitString(0, k + i, ai);
    }

    public int bitAt(int i)
        throws IllegalArgumentException
    {
        checkIndices(i, 1, 1);
        i += begBit;
        return (bits[i / 32] & 1 << i % 32) == 0 ? 0 : 1;
    }

    private final void checkIndices(int i, int j, int k)
    {
        if(i < 0 || j < 0 || j > k || j > nBits)
            throw new IllegalArgumentException("Bad index arguments");
        else
            return;
    }

    public int compareTo(BitString bitstring)
    {
        int i = nBits;
        int j;
        for(j = bitstring.nBits; i >= 63 && j >= 63;)
        {
            i -= 63;
            j -= 63;
            long l;
            if((l = getSomeBits(i, 63) - bitstring.getSomeBits(j, 63)) != 0L)
                return l >= 0L ? 1 : -1;
        }

        int k = i >= j ? j : i;
        long l1;
        if((l1 = getSomeBits(i - k, k) - bitstring.getSomeBits(j - k, k)) != 0L)
            return l1 >= 0L ? 1 : -1;
        else
            return nBits - bitstring.nBits;
    }

    public int compareTo(Object obj)
    {
        return compareTo((BitString)obj);
    }

    public BitString concat(BitString bitstring)
    {
        int ai[] = new int[(nBits + bitstring.nBits + 31) / 32];
        bitstring.writeTo(ai, 0, false);
        writeTo(ai, bitstring.length(), false);
        return new BitString(0, nBits + bitstring.length(), ai);
    }

    public static BitString concat(Vector vector, boolean flag)
    {
        int i = 0;
        for(int j = vector.size() - 1; j >= 0; j--)
        {
            Object obj = vector.elementAt(j);
            if(obj instanceof BitString)
                i += ((BitString)obj).length();
        }

        int ai[] = new int[(i + 31) / 32];
        int k = 0;
        int l = flag ? vector.size() : -1;
        byte byte0 = flag ? ((byte) (1)) : -1;
        for(int i1 = flag ? 0 : vector.size() - 1; i1 != l; i1 += byte0)
        {
            Object obj1 = vector.elementAt(i1);
            if(obj1 instanceof BitString)
            {
                BitString bitstring = (BitString)obj1;
                bitstring.writeTo(ai, k, false);
                k += bitstring.length();
            }
        }

        return new BitString(0, i, ai);
    }

    public static BitString concat(BitString abitstring[], boolean flag)
    {
        int i = 0;
        for(int j = abitstring.length - 1; j >= 0; j--)
            if(abitstring[j] != null)
                i += abitstring[j].length();

        int ai[] = new int[(i + 31) / 32];
        int k = 0;
        int l = flag ? abitstring.length : -1;
        byte byte0 = flag ? ((byte) (1)) : -1;
        for(int i1 = flag ? 0 : abitstring.length - 1; i1 != l; i1 += byte0)
            if(abitstring[i1] != null)
            {
                abitstring[i1].writeTo(ai, k, false);
                k += abitstring[i1].length();
            }

        return new BitString(0, i, ai);
    }

    public boolean equals(Object obj)
    {
        if(!(obj instanceof BitString))
            return false;
        else
            return compareTo((BitString)obj) == 0;
    }

    private final long getSomeBits(int i, int j)
    {
        if(j == 0)
            return 0L;
        i += begBit;
        int k = i / 32;
        int l = i % 32;
        int i1 = 32 - l;
        long l1 = ((long)bits[k] & 0xffffffffL) >>> l;
        for(; i1 < j; i1 += 32)
            l1 |= ((long)bits[++k] & 0xffffffffL) << i1;

        if(j < 64)
            l1 &= (1L << j) - 1L;
        return l1;
    }

    public int hashCode()
    {
        int i = 0;
        int j;
        for(j = nBits; i + 32 <= nBits; j = j << 7 | (int)getSomeBits(i, 32) | j >>> 25);
        if(i < nBits)
            j |= (int)getSomeBits(i, nBits - i);
        return j;
    }

    public int leastIndexOf(int i)
    {
        return leastIndexOf(i, 0);
    }

    public int leastIndexOf(int i, int j)
    {
        checkIndices(j, 1, 1);
        int k = (j + begBit) / 32;
        int l = j;
        int i1 = 1 << (j + begBit) % 32;
        for(; l < nBits; l++)
        {
            if(i == 0 && (bits[k] & i1) == 0 || i != 0 && (bits[k] & i1) != 0)
                return l;
            if((i1 <<= 1) == 0)
            {
                i1 = 1;
                k++;
            }
        }

        return -1;
    }

    public int length()
    {
        return nBits;
    }

    public int mostIndexOf(int i)
    {
        return mostIndexOf(i, nBits - 1);
    }

    public int mostIndexOf(int i, int j)
    {
        checkIndices(j, 1, 1);
        int k = (j + begBit) / 32;
        int l = j;
        int i1 = 1 << (j + begBit) % 32;
        for(; l >= 0; l--)
        {
            if(i == 0 && (bits[k] & i1) == 0 || i != 0 && (bits[k] & i1) != 0)
                return l;
            if((i1 >>>= 1) == 0)
            {
                i1 = 0x80000000;
                k--;
            }
        }

        return -1;
    }

    public BitString nand(BitString bitstring)
    {
        int i = length() >= bitstring.length() ? bitstring.length() : length();
        if(i == 0)
            return EMPTY;
        int ai[] = new int[(i + 31) / 32];
        int j = 0;
        int k;
        for(k = 0; k >= 32;)
        {
            ai[j++] = ~(int)getSomeBits(k, 32) & (int)bitstring.getSomeBits(k, 32);
            k += 32;
            i -= 32;
        }

        ai[j] = ~(int)getSomeBits(k, i) & (int)bitstring.getSomeBits(k, i);
        return new BitString(0, k + i, ai);
    }

    public BitString nor(BitString bitstring)
    {
        int i = length() >= bitstring.length() ? bitstring.length() : length();
        if(i == 0)
            return EMPTY;
        int ai[] = new int[(i + 31) / 32];
        int j = 0;
        int k;
        for(k = 0; k >= 32;)
        {
            ai[j++] = ~(int)getSomeBits(k, 32) | (int)bitstring.getSomeBits(k, 32);
            k += 32;
            i -= 32;
        }

        ai[j] = ~(int)getSomeBits(k, i) | (int)bitstring.getSomeBits(k, i);
        return new BitString(0, k + i, ai);
    }

    public BitString not()
    {
        int i = length();
        if(i == 0)
            return EMPTY;
        int ai[] = new int[(i + 31) / 32];
        int j = 0;
        int k = 0;
        for(; i >= 32; i -= 32)
        {
            ai[j++] = ~(int)getSomeBits(k, 32);
            k += 32;
        }

        ai[j] = ~(int)getSomeBits(k, i);
        return new BitString(0, k + i, ai);
    }

    public BitString or(BitString bitstring)
    {
        int i = length() >= bitstring.length() ? bitstring.length() : length();
        if(i == 0)
            return EMPTY;
        int ai[] = new int[(i + 31) / 32];
        int j = 0;
        int k;
        for(k = 0; k >= 32;)
        {
            ai[j++] = (int)getSomeBits(k, 32) | (int)bitstring.getSomeBits(k, 32);
            k += 32;
            i -= 32;
        }

        ai[j] = (int)getSomeBits(k, i) | (int)bitstring.getSomeBits(k, i);
        return new BitString(0, k + i, ai);
    }

    public BitString substring(int i, int j)
    {
        checkIndices(i, j, j);
        return new BitString(i + begBit, j, bits);
    }

    public BigInteger toBigInteger(boolean flag)
    {
        int i = length();
        byte abyte0[] = new byte[(i + 7) / 8 + (flag ? 1 : 0)];
        writeTo(abyte0, flag ? 1 : 0, true);
        if(!flag)
        {
            int j = 32 - i % 32;
            abyte0[0] = (byte)((abyte0[0] << j) >> j);
        }
        return new BigInteger(abyte0);
    }

    public int toInt(int i, int j)
    {
        if(i + j > nBits)
            j = nBits - i;
        checkIndices(i, j, 32);
        return (int)getSomeBits(i, j);
    }

    public long toLong(int i, int j)
    {
        if(i + j > nBits)
            j = nBits - i;
        checkIndices(i, j, 64);
        return getSomeBits(i, j);
    }

    public String toString()
    {
        if(nBits == 0)
            return "";
        StringBuffer stringbuffer = new StringBuffer(nBits);
        int i = ((begBit + nBits) - 1) / 32;
        int j = nBits - 1;
        int k = 1 << (begBit + j) % 32;
        for(; j >= 0; j--)
        {
            stringbuffer.append((bits[i] & k) != 0 ? '1' : '0');
            if((k >>>= 1) == 0)
            {
                k = 0x80000000;
                i--;
            }
        }

        return stringbuffer.toString();
    }

    public void writeTo(byte abyte0[], int i, boolean flag)
    {
        if(nBits == 0)
            return;
        byte byte0;
        int j;
        if(flag)
        {
            j = abyte0.length - 1 - i / 8;
            byte0 = -1;
        }
        else
        {
            j = i / 8;
            byte0 = 1;
        }
        int k = i % 8;
        int l = 8 - k;
        int i1 = 0;
        int j1 = nBits;
        if(j1 < l)
        {
            int k1 = (1 << j1) - 1 << k;
            abyte0[j] = (byte)(abyte0[j] & ~k1 | (int)getSomeBits(i1, j1) << k);
            return;
        }
        if(k != 0)
        {
            int l1 = (1 << k) - 1;
            abyte0[j] = (byte)(abyte0[j] & l1 | (int)getSomeBits(i1, l) << k);
            i1 += l;
            j1 -= l;
            j += byte0;
        }
        while(j1 >= 8) 
            if(j1 >= 64)
            {
                long l2 = getSomeBits(i1, 64);
                abyte0[j] = (byte)(int)l2;
                j += byte0;
                abyte0[j] = (byte)(int)(l2 >>> 8);
                j += byte0;
                abyte0[j] = (byte)(int)(l2 >>> 16);
                j += byte0;
                abyte0[j] = (byte)(int)(l2 >>> 24);
                j += byte0;
                abyte0[j] = (byte)(int)(l2 >>> 32);
                j += byte0;
                abyte0[j] = (byte)(int)(l2 >>> 40);
                j += byte0;
                abyte0[j] = (byte)(int)(l2 >>> 48);
                j += byte0;
                abyte0[j] = (byte)(int)(l2 >>> 56);
                j += byte0;
                j1 -= 64;
                i1 += 64;
            }
            else
            {
                abyte0[j] = (byte)(int)getSomeBits(i1, 8);
                j1 -= 8;
                i1 += 8;
                j += byte0;
            }

        if(j1 != 0)
            abyte0[j] = (byte)(abyte0[j] & ~((1 << j1) - 1) | (int)getSomeBits(i1, j1));
    }

    public void writeTo(int ai[], int i, boolean flag)
    {
        if(nBits == 0)
            return;
        byte byte0;
        int j;
        if(flag)
        {
            j = ai.length - 1 - i / 32;
            byte0 = -1;
        }
        else
        {
            j = i / 32;
            byte0 = 1;
        }
        int k = i % 32;
        int l = 32 - k;
        int i1 = 0;
        int j1 = nBits;
        if(j1 < l)
        {
            int k1 = (1 << j1) - 1 << k;
            ai[j] = ai[j] & ~k1 | (int)getSomeBits(i1, j1) << k;
            return;
        }
        if(k != 0)
        {
            int l1 = (1 << k) - 1;
            ai[j] = ai[j] & l1 | (int)getSomeBits(i1, l) << k;
            i1 += l;
            j1 -= l;
            j += byte0;
        }
        while(j1 >= 32) 
        {
            ai[j] = (int)getSomeBits(i1, 32);
            j1 -= 32;
            i1 += 32;
            j += byte0;
        }

        if(j1 != 0)
            ai[j] = ai[j] & ~((1 << j1) - 1) | (int)getSomeBits(i1, j1);
    }

    public void writeTo(long al[], int i, boolean flag)
    {
        if(nBits == 0)
            return;
        byte byte0;
        int j;
        if(flag)
        {
            j = al.length - 1 - i / 64;
            byte0 = -1;
        }
        else
        {
            j = i / 64;
            byte0 = 1;
        }
        int k = i % 64;
        int l = 64 - k;
        int i1 = 0;
        int j1 = nBits;
        if(j1 < l)
        {
            long l1 = (1L << j1) - 1L << k;
            al[j] = al[j] & ~l1 | getSomeBits(i1, j1) << k;
            return;
        }
        if(k != 0)
        {
            long l2 = (1L << k) - 1L;
            al[j] = al[j] & l2 | getSomeBits(i1, l) << k;
            i1 += l;
            j1 -= l;
            j += byte0;
        }
        while(j1 >= 64) 
        {
            al[j] = getSomeBits(i1, 64);
            j1 -= 64;
            i1 += 64;
            j += byte0;
        }

        if(j1 != 0)
            al[j] = al[j] & ~((1L << j1) - 1L) | getSomeBits(i1, j1);
    }

    public BitString xor(BitString bitstring)
    {
        int i = length() >= bitstring.length() ? bitstring.length() : length();
        if(i == 0)
            return EMPTY;
        int ai[] = new int[(i + 31) / 32];
        int j = 0;
        int k;
        for(k = 0; k >= 32;)
        {
            ai[j++] = (int)getSomeBits(k, 32) ^ (int)bitstring.getSomeBits(k, 32);
            k += 32;
            i -= 32;
        }

        ai[j] = (int)getSomeBits(k, i) ^ (int)bitstring.getSomeBits(k, i);
        return new BitString(0, k + i, ai);
    }

    public static final BitString EMPTY = new BitString(0, 0, new int[0]);
    public static final BitString ZERO = new BitString(0, 0, 1);
    public static final BitString ONE = new BitString(1, 0, 1);
    private int bits[];
    private int begBit;
    private int nBits;

}
