// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ASN1Exception.java

package com.ibm.asn1;


public class ASN1Exception extends Exception
{

    public ASN1Exception(Exception exception)
    {
        cause = exception;
    }

    public ASN1Exception(String s)
    {
        super(s);
    }

    public Throwable getCause()
    {
        return cause;
    }

    public String getMessage()
    {
        if(cause != null)
            return cause.getMessage();
        else
            return super.getMessage();
    }

    protected Exception cause;
}
