package org.codehaus.plexus.ldapserver.server.util;


public class DirectoryException extends Exception
{

    int ldapErrorCode = 0;

    /**
     * DirectoryException constructor comment.
     */
    public DirectoryException()
    {
        super();
    }

    public DirectoryException( int code )
    {
        super();
        setLDAPErrorCode( code );
    }

    public DirectoryException( int code, String s )
    {
        super( s );
        setLDAPErrorCode( code );
    }

    /**
     * DirectoryException constructor comment.
     * @param s java.lang.String
     */
    public DirectoryException( String s )
    {
        super( s );
    }

    public int getLDAPErrorCode()
    {
        return this.ldapErrorCode;
    }

    public void setLDAPErrorCode( int code )
    {
        this.ldapErrorCode = code;
    }
}
