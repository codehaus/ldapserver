package org.codehaus.plexus.ldapserver.server.util;


/**
 * Exception Thrown when the Distinguished Name is Invalid
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */
public class InvalidDNException extends DirectoryException
{
    /**
     * InvalidDNException constructor comment.
     */
    public InvalidDNException()
    {
        super();
        setLDAPErrorCode( 34 );
    }

    /**
     * InvalidDNException constructor comment.
     * @param s java.lang.String
     */
    public InvalidDNException( String s )
    {
        super( s );
        setLDAPErrorCode( 34 );
    }
}
