// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HexOutputStream.java

package com.ibm.util;

import java.io.*;

public class HexOutputStream extends OutputStream
{

    public HexOutputStream()
    {
        print = new PrintWriter(System.out, true);
        n = 0;
        bol = true;
        newline = 16;
        separate = 1;
        offset = 0;
        flushMarker = "";
        lowercase = false;
    }

    public HexOutputStream(OutputStream outputstream)
    {
        print = new PrintWriter(System.out, true);
        n = 0;
        bol = true;
        newline = 16;
        separate = 1;
        offset = 0;
        flushMarker = "";
        lowercase = false;
        print = new PrintWriter(outputstream, true);
    }

    public HexOutputStream(PrintWriter printwriter)
    {
        print = new PrintWriter(System.out, true);
        n = 0;
        bol = true;
        newline = 16;
        separate = 1;
        offset = 0;
        flushMarker = "";
        lowercase = false;
        print = printwriter;
    }

    public void close()
        throws IOException
    {
        resetOffset();
        print.close();
    }

    public void flush()
    {
        print.print(flushMarker);
        print.flush();
    }

    protected void insertBlank()
    {
        print.print(' ');
    }

    protected void insertNewLine()
    {
        print.println();
        bol = true;
    }

    protected void insertOffsetLabel(int i)
    {
        int j = offset;
        char ac[] = lowercase ? hexdigitsLC : hexdigitsUC;
        while(--j >= 0) 
            print.print(ac[i >>> j * 4 & 0xf]);

        print.print(": ");
    }

    public void resetOffset()
    {
        n = 0;
        startNewLine();
    }

    public void startNewLine()
    {
        if(!bol)
            insertNewLine();
    }

    public void write(int i)
        throws IOException
    {
        byte abyte0[] = new byte[1];
        abyte0[0] = (byte)i;
        write(abyte0);
    }

    public void write(byte abyte0[])
        throws IOException
    {
        write(abyte0, 0, abyte0.length);
    }

    public void write(byte abyte0[], int i, int j)
        throws IOException
    {
        char ac[] = lowercase ? hexdigitsLC : hexdigitsUC;
        while(j-- > 0) 
        {
            if(bol & (offset > 0))
                insertOffsetLabel(n);
            bol = false;
            print.print(ac[abyte0[i] >> 4 & 0xf]);
            print.print(ac[abyte0[i] & 0xf]);
            n++;
            if(newline > 0 && n % newline == 0)
                insertNewLine();
            else
            if(separate > 0 && n % separate == 0)
                insertBlank();
            i++;
        }

    }

    protected PrintWriter print;
    private int n;
    private boolean bol;
    public static final char hexdigitsUC[] = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
        'A', 'B', 'C', 'D', 'E', 'F'
    };
    public static final char hexdigitsLC[] = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
        'a', 'b', 'c', 'd', 'e', 'f'
    };
    public int newline;
    public int separate;
    public int offset;
    public String flushMarker;
    public boolean lowercase;

}
