package org.codehaus.plexus.ldapserver.server.util;



/**
 * DNUtility contains methods for manipulating distinguished names
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.server.syntax.DirectoryString;

import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

public class DNUtility
{
    private static DNUtility instance = null;

    private DNUtility()
    {
    }

    public DirectoryString createDN( Vector rdnComponents ) throws InvalidDNException
    {
        StringBuffer dn = new StringBuffer( 64 );
        Enumeration rdnEnum = rdnComponents.elements();

        while ( rdnEnum.hasMoreElements() )
        {
            String rdn = (String) rdnEnum.nextElement();
            if ( rdn.indexOf( "=" ) < 0 )
            {
                throw new InvalidDNException();
            }
            dn.append( rdn.trim() );
            if ( rdnEnum.hasMoreElements() )
            {
                dn.append( "," );
            }
        }
        return new DirectoryString( dn.toString() );
    }

    public Vector explodeDN( DirectoryString dn )
    {
        Vector rdnComponents = new Vector();

        StringTokenizer dnTok = new StringTokenizer( dn.toString(), "," );

        while ( dnTok.hasMoreTokens() )
        {
            String rdn = dnTok.nextToken();
            while ( rdn.endsWith( "\\" ) && !rdn.endsWith( "\\\\" ) && dnTok.hasMoreTokens() )
            {
                rdn = rdn.concat( "," );
                rdn = rdn.concat( dnTok.nextToken() );
            }
            rdn.trim();
            rdnComponents.addElement( rdn );
        }
        return rdnComponents;
    }

    public static DNUtility getInstance()
    {
        if ( instance == null )
        {
            instance = new DNUtility();
        }
        return instance;
    }

    public DirectoryString normalize( DirectoryString dn ) throws InvalidDNException
    {
        return createDN( explodeDN( dn ) );
    }
}
