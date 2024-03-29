package org.codehaus.plexus.ldapserver.ldapv3;

//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/org/codehaus/ldapv3/ExtendedRequest.java
//
//   Java class for ASN.1 definition ExtendedRequest as defined in
//   module LDAPv3.
//   This file was generated by Snacc for Java at Fri Jul  2 18:01:43 1999
//   Snacc for Java - Andreas Schade (SAN/ZRL)
//-----------------------------------------------------------------------------

// Import PrintStream class for print methods

import com.ibm.asn1.*;
import com.ibm.util.*;

import java.io.PrintStream;

/** This class represents the ASN.1 SEQUENCE type <tt>ExtendedRequest</tt>.
 * For each sequence member, sequence classes contain a
 * public member variable of the corresponding Java type.
 * @author Snacc for Java
 * @version Fri Jul  2 18:01:43 1999

 */

public class ExtendedRequest implements LDAPv3
{

    /** member variable representing the sequence member requestName of type byte[] */
    public byte[] requestName;
    /** member variable representing the sequence member requestValue of type byte[] */
    public byte[] requestValue = null;

    /** default constructor */
    public ExtendedRequest()
    {
    }

    /** copy constructor */
    public ExtendedRequest( ExtendedRequest arg )
    {
        requestName = new byte[arg.requestName.length];
        System.arraycopy( arg.requestName, 0, requestName, 0, arg.requestName.length );
        requestValue = new byte[arg.requestValue.length];
        System.arraycopy( arg.requestValue, 0, requestValue, 0, arg.requestValue.length );
    }

    /** decoding method.
     * @param dec
     *        decoder object derived from com.ibm.asn1.ASN1Decoder
     * @exception com.ibm.asn1.ASN1Exception
     *            decoding error
     */
    public void decode( ASN1Decoder dec ) throws ASN1Exception
    {
        dec.nextIsImplicit( dec.makeTag( dec.APPLICATION_TAG_CLASS, 23 ) );
        int seq_nr = dec.decodeSequence();
        dec.nextIsImplicit( dec.makeTag( dec.CONTEXT_TAG_CLASS, 0 ) );
        requestName = dec.decodeOctetString();
        if ( !dec.nextIsOptional( dec.makeTag( dec.CONTEXT_TAG_CLASS, 1 ) ) )
        {
            dec.nextIsImplicit( dec.makeTag( dec.CONTEXT_TAG_CLASS, 1 ) );
            requestValue = dec.decodeOctetString();
        }
        dec.endOf( seq_nr );
    }

    /** encoding method.
     * @param enc
     *        encoder object derived from com.ibm.asn1.ASN1Encoder
     * @exception com.ibm.asn1.ASN1Exception
     *            encoding error
     */
    public void encode( ASN1Encoder enc ) throws ASN1Exception
    {
        enc.nextIsImplicit( enc.makeTag( enc.APPLICATION_TAG_CLASS, 23 ) );
        int seq_nr = enc.encodeSequence();
        enc.nextIsImplicit( enc.makeTag( enc.CONTEXT_TAG_CLASS, 0 ) );
        enc.encodeOctetString( requestName );
        if ( requestValue != null )
        {
            enc.nextIsImplicit( enc.makeTag( enc.CONTEXT_TAG_CLASS, 1 ) );
            enc.encodeOctetString( requestValue );
        }
        enc.endOf( seq_nr );
    }

    /** default print method (variable indentation)
     * @param os
     *        PrintStream representing the print destination (file, etc)
     */
    public void print( PrintStream os )
    {
        print( os, 0 );
    }

    /** print method (variable indentation)
     * @param os
     *        PrintStream representing the print destination (file, etc)
     * @param indent
     *        number of blanks that preceed each output line.
     */
    public void print( PrintStream os, int indent )
    {
        os.println( "{ -- SEQUENCE --" );
        for ( int ii = 0; ii < indent + 2; ii++ ) os.print( ' ' );
        os.print( "requestName = " );
        try
        {
            ( new HexOutputStream( os ) ).write( requestName );
        }
        catch ( java.io.IOException ex )
        {
            os.print( "( unprintable OCTET STRING value )" );
        }

        os.println( ',' );
        if ( requestValue != null )
        {
            for ( int ii = 0; ii < indent + 2; ii++ ) os.print( ' ' );
            os.print( "requestValue = " );
            try
            {
                ( new HexOutputStream( os ) ).write( requestValue );
            }
            catch ( java.io.IOException ex )
            {
                os.print( "( unprintable OCTET STRING value )" );
            }
        }

        os.println();
        for ( int ii = 0; ii < indent; ii++ ) os.print( ' ' );
        os.print( '}' );
    }
}
