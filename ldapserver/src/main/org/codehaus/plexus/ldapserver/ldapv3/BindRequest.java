package org.codehaus.plexus.ldapserver.ldapv3;

//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/org/codehaus/ldapv3/BindRequest.java
//
//   Java class for ASN.1 definition BindRequest as defined in
//   module LDAPv3.
//   This file was generated by Snacc for Java at Fri Jul  2 18:01:43 1999
//   Snacc for Java - Andreas Schade (SAN/ZRL)
//-----------------------------------------------------------------------------

// Import PrintStream class for print methods

import com.ibm.asn1.*;
import com.ibm.util.*;

import java.io.PrintStream;

/** This class represents the ASN.1 SEQUENCE type <tt>BindRequest</tt>.
 * For each sequence member, sequence classes contain a
 * public member variable of the corresponding Java type.
 * @author Snacc for Java
 * @version Fri Jul  2 18:01:43 1999

 */

public class BindRequest implements LDAPv3
{

    /** member variable representing the sequence member version of type java.math.BigInteger */
    public java.math.BigInteger version;
    /** member variable representing the sequence member name of type byte[] */
    public byte[] name;
    /** member variable representing the sequence member authentication of type AuthenticationChoice */
    public AuthenticationChoice authentication = new AuthenticationChoice();

    /** default constructor */
    public BindRequest()
    {
    }

    /** copy constructor */
    public BindRequest( BindRequest arg )
    {
        version = arg.version;
        name = new byte[arg.name.length];
        System.arraycopy( arg.name, 0, name, 0, arg.name.length );
        authentication = new AuthenticationChoice( arg.authentication );
    }

    /** decoding method.
     * @param dec
     *        decoder object derived from com.ibm.asn1.ASN1Decoder
     * @exception com.ibm.asn1.ASN1Exception
     *            decoding error
     */
    public void decode( ASN1Decoder dec ) throws ASN1Exception
    {
        dec.nextIsImplicit( dec.makeTag( dec.APPLICATION_TAG_CLASS, 0 ) );
        int seq_nr = dec.decodeSequence();
        version = dec.decodeInteger();
        name = dec.decodeOctetString();
        authentication.decode( dec );
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
        enc.nextIsImplicit( enc.makeTag( enc.APPLICATION_TAG_CLASS, 0 ) );
        int seq_nr = enc.encodeSequence();
        enc.encodeInteger( version );
        enc.encodeOctetString( name );
        authentication.encode( enc );
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
        os.print( "version = " );
        os.print( version.toString() );
        os.println( ',' );

        for ( int ii = 0; ii < indent + 2; ii++ ) os.print( ' ' );
        os.print( "name = " );
        try
        {
            ( new HexOutputStream( os ) ).write( name );
        }
        catch ( java.io.IOException ex )
        {
            os.print( "( unprintable OCTET STRING value )" );
        }
        os.println( ',' );

        for ( int ii = 0; ii < indent + 2; ii++ ) os.print( ' ' );
        os.print( "authentication = " );
        authentication.print( os, indent + 2 );

        os.println();
        for ( int ii = 0; ii < indent; ii++ ) os.print( ' ' );
        os.print( '}' );
    }
}
