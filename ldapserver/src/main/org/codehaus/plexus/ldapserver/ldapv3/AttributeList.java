package org.codehaus.plexus.ldapserver.ldapv3;

//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/org/codehaus/ldapv3/AttributeList.java
//
//   Java class for ASN.1 definition AttributeList as defined in
//   module LDAPv3.
//   This file was generated by Snacc for Java at Fri Jul  2 18:01:43 1999
//   Snacc for Java - Andreas Schade (SAN/ZRL)
//-----------------------------------------------------------------------------

// Import PrintStream class for print methods

import com.ibm.asn1.*;
import com.ibm.util.*;

import java.io.PrintStream;

/** This class represents the ASN.1 SEQUENCE OF type <tt>AttributeList</tt>.
 * Sequence-of classes inherit from java.util.Vector.
 * As a subtype of the Vector class this class also
 * preserves the order of the contained elements.
 * @author Snacc for Java
 * @version Fri Jul  2 18:01:43 1999

 */

public class AttributeList extends java.util.Vector implements LDAPv3
{

    /** default constructor */
    public AttributeList()
    {
    }

    /** copy constructor */
    public AttributeList( AttributeList arg )
    {
        for ( java.util.Enumeration e = arg.elements(); e.hasMoreElements(); )
        {
            addElement( ( (AttributeListSeq) ( e.nextElement() ) ) );
        }
    }

    /** decoding method.
     * @param dec
     *        decoder object derived from com.ibm.asn1.ASN1Decoder
     * @exception com.ibm.asn1.ASN1Exception
     *            decoding error
     */
    public void decode( ASN1Decoder dec ) throws ASN1Exception
    {
        int seq_of_nr = dec.decodeSequenceOf();
        while ( !dec.endOf( seq_of_nr ) )
        {
            AttributeListSeq tmp = new AttributeListSeq();
            tmp.decode( dec );
            addElement( tmp );
        }
    }

    /** encoding method.
     * @param enc
     *        encoder object derived from com.ibm.asn1.ASN1Encoder
     * @exception com.ibm.asn1.ASN1Exception
     *            encoding error
     */
    public void encode( ASN1Encoder enc ) throws ASN1Exception
    {
        int seq_of_nr = enc.encodeSequenceOf();
        for ( java.util.Enumeration e = elements(); e.hasMoreElements(); )
        {
            ( (AttributeListSeq) ( e.nextElement() ) ).encode( enc );
        }
        enc.endOf( seq_of_nr );
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
        boolean nonePrinted = true;
        os.println( "{ -- SEQUENCE OF --" );
        for ( java.util.Enumeration e = elements(); e.hasMoreElements(); )
        {
            if ( nonePrinted == false )
                os.println( ',' );
            nonePrinted = false;
            for ( int ii = 0; ii < indent + 2; ii++ ) os.print( ' ' );
            ( (AttributeListSeq) ( e.nextElement() ) ).print( os, indent + 2 );
            if ( !e.hasMoreElements() )
                os.println();
        }
        for ( int ii = 0; ii < indent; ii++ ) os.print( ' ' );
        os.print( '}' );
    }
}
