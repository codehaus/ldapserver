package org.codehaus.plexus.ldapserver.server.util;



/**
 * Exception thrown when someone attempts to add or update an entry that does
 * not conform to the defined LDAP schema.
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */
public class DirectorySchemaViolation extends DirectoryException
{
    /**
     * DirectorySchemaViolation constructor comment.
     */
    public DirectorySchemaViolation()
    {
        super();
        setLDAPErrorCode( 65 );
    }

    /**
     * DirectorySchemaViolation constructor comment.
     * @param s java.lang.String
     */
    public DirectorySchemaViolation( String s )
    {
        super( s );
        setLDAPErrorCode( 65 );
    }
}
