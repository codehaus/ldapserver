package org.codehaus.plexus.ldapserver.ldapv3;

//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/org/codehaus/ldapv3/SearchRequest.java
//
//   Java class for ASN.1 definition SearchRequest as defined in
//   module LDAPv3.
//   This file was generated by Snacc for Java at Fri Jul  2 18:01:43 1999
//   Snacc for Java - Andreas Schade (SAN/ZRL)
//-----------------------------------------------------------------------------

// Import PrintStream class for print methods

import com.ibm.asn1.*;
import com.ibm.util.*;

import java.io.PrintStream;

/** This class represents the ASN.1 SEQUENCE type <tt>SearchRequest</tt>.
 * For each sequence member, sequence classes contain a
 * public member variable of the corresponding Java type.
 * @author Snacc for Java
 * @version Fri Jul  2 18:01:43 1999

 */

public class SearchRequest implements LDAPv3
{

    /** member variable representing the sequence member baseObject of type byte[] */
    public byte[] baseObject;
    /** member variable representing the sequence member scope of type SearchRequestEnum */
    public SearchRequestEnum scope = new SearchRequestEnum();
    /** member variable representing the sequence member derefAliases of type SearchRequestEnum1 */
    public SearchRequestEnum1 derefAliases = new SearchRequestEnum1();
    /** member variable representing the sequence member sizeLimit of type java.math.BigInteger */
    public java.math.BigInteger sizeLimit;
    /** member variable representing the sequence member timeLimit of type java.math.BigInteger */
    public java.math.BigInteger timeLimit;
    /** member variable representing the sequence member typesOnly of type boolean */
    public boolean typesOnly;
    /** member variable representing the sequence member filter of type Filter */
    public Filter filter = new Filter();
    /** member variable representing the sequence member attributes of type AttributeDescriptionList */
    public AttributeDescriptionList attributes = new AttributeDescriptionList();

    /** default constructor */
    public SearchRequest()
    {
    }

    /** copy constructor */
    public SearchRequest( SearchRequest arg )
    {
        baseObject = new byte[arg.baseObject.length];
        System.arraycopy( arg.baseObject, 0, baseObject, 0, arg.baseObject.length );
        scope = new SearchRequestEnum( arg.scope );
        derefAliases = new SearchRequestEnum1( arg.derefAliases );
        sizeLimit = arg.sizeLimit;
        timeLimit = arg.timeLimit;
        typesOnly = arg.typesOnly;
        filter = new Filter( arg.filter );
        attributes = new AttributeDescriptionList( arg.attributes );
    }

    /** decoding method.
     * @param dec
     *        decoder object derived from com.ibm.asn1.ASN1Decoder
     * @exception com.ibm.asn1.ASN1Exception
     *            decoding error
     */
    public void decode( ASN1Decoder dec ) throws ASN1Exception
    {
        dec.nextIsImplicit( dec.makeTag( dec.APPLICATION_TAG_CLASS, 3 ) );
        int seq_nr = dec.decodeSequence();
        baseObject = dec.decodeOctetString();
        scope.decode( dec );
        derefAliases.decode( dec );
        sizeLimit = dec.decodeInteger();
        timeLimit = dec.decodeInteger();
        typesOnly = dec.decodeBoolean();
        filter.decode( dec );
        attributes.decode( dec );
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
        enc.nextIsImplicit( enc.makeTag( enc.APPLICATION_TAG_CLASS, 3 ) );
        int seq_nr = enc.encodeSequence();
        enc.encodeOctetString( baseObject );
        scope.encode( enc );
        derefAliases.encode( enc );
        enc.encodeInteger( sizeLimit );
        enc.encodeInteger( timeLimit );
        enc.encodeBoolean( typesOnly );
        filter.encode( enc );
        attributes.encode( enc );
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
        os.print( "baseObject = " );
        try
        {
            ( new HexOutputStream( os ) ).write( baseObject );
        }
        catch ( java.io.IOException ex )
        {
            os.print( "( unprintable OCTET STRING value )" );
        }
        os.println( ',' );

        for ( int ii = 0; ii < indent + 2; ii++ ) os.print( ' ' );
        os.print( "scope = " );
        scope.print( os, indent + 2 );
        os.println( ',' );

        for ( int ii = 0; ii < indent + 2; ii++ ) os.print( ' ' );
        os.print( "derefAliases = " );
        derefAliases.print( os, indent + 2 );
        os.println( ',' );

        for ( int ii = 0; ii < indent + 2; ii++ ) os.print( ' ' );
        os.print( "sizeLimit = " );
        os.print( sizeLimit.toString() );
        os.println( ',' );

        for ( int ii = 0; ii < indent + 2; ii++ ) os.print( ' ' );
        os.print( "timeLimit = " );
        os.print( timeLimit.toString() );
        os.println( ',' );

        for ( int ii = 0; ii < indent + 2; ii++ ) os.print( ' ' );
        os.print( "typesOnly = " );
        os.print( typesOnly );
        os.println( ',' );

        for ( int ii = 0; ii < indent + 2; ii++ ) os.print( ' ' );
        os.print( "filter = " );
        filter.print( os, indent + 2 );
        os.println( ',' );

        for ( int ii = 0; ii < indent + 2; ii++ ) os.print( ' ' );
        os.print( "attributes = " );
        attributes.print( os, indent + 2 );

        os.println();
        for ( int ii = 0; ii < indent; ii++ ) os.print( ' ' );
        os.print( '}' );
    }
}