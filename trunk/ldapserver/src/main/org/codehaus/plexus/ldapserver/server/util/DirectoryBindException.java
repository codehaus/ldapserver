package org.codehaus.plexus.ldapserver.server.util;

/**
 * Exception thrown when the Bind operation is unsuccessful
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */
public class DirectoryBindException extends DirectoryException
{
    /**
     * DirectoryBindException constructor comment.
     */
    public DirectoryBindException()
    {
        super();
        setLDAPErrorCode( 49 );
    }

    /**
     * DirectoryBindException constructor comment.
     * @param s java.lang.String
     */
    public DirectoryBindException( String s )
    {
        super( s );
        setLDAPErrorCode( 49 );
    }
}
