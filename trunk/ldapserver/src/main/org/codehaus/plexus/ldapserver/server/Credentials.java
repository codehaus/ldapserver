package org.codehaus.plexus.ldapserver.server;

/**
 * Class holding a session's credentials
 */

import org.codehaus.plexus.ldapserver.server.syntax.DirectoryString;

public class Credentials
{

    private DirectoryString user = null;
    private byte authType = AUTH_NONE;
    private String saslMech = null;

    public static final byte AUTH_NONE = 0;
    public static final byte AUTH_SIMPLE = 1;
    public static final byte AUTH_SASL = 2;

    /**
     * Credentials constructor comment.
     */
    public Credentials()
    {
        super();
        setUser( new DirectoryString( "" ) );
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/18/2000 9:03:58 AM)
     * @return byte
     */
    public byte getAuthType()
    {
        return authType;
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/18/2000 9:03:58 AM)
     * @return java.lang.String
     */
    public java.lang.String getSaslMech()
    {
        return saslMech;
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/18/2000 9:03:58 AM)
     * @return org.codehaus.server.syntax.DirectoryString
     */
    public DirectoryString getUser()
    {
        return user;
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/18/2000 9:03:58 AM)
     * @param newAuthType byte
     */
    public void setAuthType( byte newAuthType )
    {
        authType = newAuthType;
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/18/2000 9:03:58 AM)
     * @param newSaslMech java.lang.String
     */
    public void setSaslMech( java.lang.String newSaslMech )
    {
        saslMech = newSaslMech;
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/18/2000 9:03:58 AM)
     * @param newUser org.codehaus.server.syntax.DirectoryString
     */
    public void setUser( DirectoryString newUser )
    {
        user = newUser;
    }
}
