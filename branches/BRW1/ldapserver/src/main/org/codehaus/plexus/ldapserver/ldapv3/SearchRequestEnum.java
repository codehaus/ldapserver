package org.codehaus.plexus.ldapserver.ldapv3;

//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/org/codehaus/ldapv3/SearchRequestEnum.java
//
//   Java class for ASN.1 definition SearchRequestEnum as defined in
//   module LDAPv3.
//   This file was generated by Snacc for Java at Fri Jul  2 18:01:43 1999
//   Snacc for Java - Andreas Schade (SAN/ZRL)
//-----------------------------------------------------------------------------

// Import PrintStream class for print methods

import com.ibm.asn1.*;
import com.ibm.util.*;

import java.io.PrintStream;

/** This class represents the ASN.1 simple definition <tt>SearchRequestEnum</tt>.
 * Simple classes contain a member variable <tt>value</tt> of the
 * type that is FINALLY referred to.
 * @author Snacc for Java
 * @version Fri Jul  2 18:01:43 1999

 */

public class SearchRequestEnum implements LDAPv3
{

    public int value;

    public static final int BASEOBJECT = 0;
    public static final int SINGLELEVEL = 1;
    public static final int WHOLESUBTREE = 2;

    /** default constructor */
    public SearchRequestEnum()
    {
    }

    public SearchRequestEnum( int arg )
    {
        value = arg;
    }

    /** copy constructor */
    public SearchRequestEnum( SearchRequestEnum arg )
    {
        value = arg.value;
    }

    /** decoding method.
     * @param dec
     *        decoder object derived from com.ibm.asn1.ASN1Decoder
     * @exception com.ibm.asn1.ASN1Exception
     *            decoding error
     */
    public void decode( ASN1Decoder dec ) throws ASN1Exception
    {
        value = dec.decodeEnumeration();
    }

    /** encoding method.
     * @param enc
     *        encoder object derived from com.ibm.asn1.ASN1Encoder
     * @exception com.ibm.asn1.ASN1Exception
     *            encoding error
     */
    public void encode( ASN1Encoder enc ) throws ASN1Exception
    {
        enc.encodeEnumeration( value );
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
        os.print( value );
    }
}
