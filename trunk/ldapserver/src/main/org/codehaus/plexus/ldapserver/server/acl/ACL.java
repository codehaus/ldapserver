package org.codehaus.plexus.ldapserver.server.acl;



/**
 * Represents an LDAP Access Control List
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.server.syntax.DirectoryString;
import org.codehaus.plexus.ldapserver.server.util.DNUtility;

import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

public class ACL
{
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ACL.class);

    private boolean scopeSubtree = false;
    private boolean grant = false;
    private Vector permission = null;
    private Vector attr = null;
    private String authnLevel = null;
    private boolean authzDN = true;
    private byte subjectType = 0;
    private DirectoryString subject = null;

    public static final byte SUBJECT_INVALID = 0;
    public static final byte SUBJECT_SUBTREE = 2;
    public static final byte SUBJECT_PUBLIC = 4;
    public static final byte SUBJECT_ROLE = 8;
    public static final byte SUBJECT_GROUP = 16;
    public static final byte SUBJECT_THIS = 32;
    public static final byte SUBJECT_AUTHZID = 64;
    public static final byte SUBJECT_IPADDRESS = 96;


    /**
     * DirectoryACL constructor comment.
     */
    public ACL()
    {
        super();
    }

    /**
     * DirectoryACL constructor comment.
     */
    public ACL( String strACL )
    {
        super();
        StringTokenizer aclTok = new StringTokenizer( strACL, "#" );
        if ( aclTok.nextToken().equalsIgnoreCase( "subtree" ) )
        {
            setScopeSubtree( true );
        }
        else
        {
            setScopeSubtree( false );
        }

        StringTokenizer rightsTok = new StringTokenizer( aclTok.nextToken(), ":" );
        if ( rightsTok.nextToken().equalsIgnoreCase( "grant" ) )
        {
            setGrant( true );
        }
        else
        {
            setGrant( false );
        }

        if ( rightsTok.hasMoreTokens() )
        {
            Vector permVec = new Vector();
            StringTokenizer permTok = new StringTokenizer( rightsTok.nextToken(), "," );
            while ( permTok.hasMoreTokens() )
            {
                permVec.addElement( new Character( permTok.nextToken().charAt( 0 ) ) );
            }
            setPermission( permVec );
        }

        Vector attrVec = new Vector();
        StringTokenizer attrTok = new StringTokenizer( aclTok.nextToken(), "," );
        while ( attrTok.hasMoreTokens() )
        {
            attrVec.addElement( new DirectoryString( attrTok.nextToken() ) );
        }
        setAttr( attrVec );

        StringTokenizer subjectTok = new StringTokenizer( aclTok.nextToken(), ":" );
        String authn = subjectTok.nextToken();
        if ( authn.equalsIgnoreCase( "authnLevel" ) )
        {
            String authnlevel = subjectTok.nextToken();
            if ( authnlevel.equalsIgnoreCase( "sasl" ) )
            {
                authnlevel = authnlevel.concat( ":" + subjectTok.nextToken() );
            }
            setAuthnLevel( authnlevel );
        }

        String subType = null;
        if ( getAuthnLevel() == null )
        {
            subType = authn;
        }
        else
        {
            subType = subjectTok.nextToken();
        }
        if ( subType.startsWith( "authz" ) )
        {
            setSubjectType( SUBJECT_AUTHZID );
            StringTokenizer subTypeTok = new StringTokenizer( subType, "-" );
            subTypeTok.nextToken();
            if ( subTypeTok.nextToken().equalsIgnoreCase( "dn" ) )
            {
                setAuthzDN( true );
            }
            else
            {
                setAuthzDN( false );
            }
            try
            {
                setSubject( new DirectoryString( subjectTok.nextToken() ) );
            }
            catch ( org.codehaus.plexus.ldapserver.server.util.InvalidDNException ide )
            {
                LOGGER.info("Invalid DN Specified in ACL" );
            }
        }
        else if ( subType.equalsIgnoreCase( "public" ) )
        {
            setSubjectType( SUBJECT_PUBLIC );
        }
        else if ( subType.equalsIgnoreCase( "this" ) )
        {
            setSubjectType( SUBJECT_THIS );
        }
        else
        {
            if ( subType.equalsIgnoreCase( "role" ) )
            {
                setSubjectType( SUBJECT_ROLE );
            }
            else if ( subType.equalsIgnoreCase( "group" ) )
            {
                setSubjectType( SUBJECT_GROUP );
            }
            else if ( subType.equalsIgnoreCase( "subtree" ) )
            {
                setSubjectType( SUBJECT_SUBTREE );
            }
            else if ( subType.equalsIgnoreCase( "ipAddress" ) )
            {
                setSubjectType( SUBJECT_IPADDRESS );
            }
            try
            {
                setSubject( new DirectoryString( subjectTok.nextToken() ) );
            }
            catch ( org.codehaus.plexus.ldapserver.server.util.InvalidDNException ide )
            {
                LOGGER.info("Invalid DN Specified in ACL" );
            }
        }

    }

    /**
     * Insert the method's description here.
     * Creation date: (8/17/2000 7:00:41 PM)
     * @return java.util.Vector
     */
    public java.util.Vector getAttr()
    {
        return attr;
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/17/2000 7:00:41 PM)
     * @return java.lang.String
     */
    public java.lang.String getAuthnLevel()
    {
        return authnLevel;
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/17/2000 7:00:41 PM)
     * @return java.util.Vector
     */
    public java.util.Vector getPermission()
    {
        return permission;
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/17/2000 7:00:41 PM)
     * @return org.codehaus.server.syntax.DirectoryString
     */
    public org.codehaus.plexus.ldapserver.server.syntax.DirectoryString getSubject()
    {

        return subject;
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/17/2000 7:18:14 PM)
     * @return byte
     */
    public byte getSubjectType()
    {
        return subjectType;
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/17/2000 8:12:07 PM)
     * @return boolean
     */
    public boolean isAuthzDN()
    {
        return authzDN;
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/17/2000 7:00:41 PM)
     * @return boolean
     */
    public boolean isGrant()
    {
        return grant;
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/17/2000 8:41:17 PM)
     * @return boolean
     */
    public boolean isScopeSubtree()
    {
        return scopeSubtree;
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/17/2000 7:00:41 PM)
     * @param newAttr java.util.Vector
     */
    public void setAttr( java.util.Vector newAttr )
    {
        attr = newAttr;
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/17/2000 7:00:41 PM)
     * @param newAuthnLevel java.lang.String
     */
    public void setAuthnLevel( java.lang.String newAuthnLevel )
    {
        authnLevel = newAuthnLevel;
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/17/2000 8:12:07 PM)
     * @param newAuthzDN boolean
     */
    public void setAuthzDN( boolean newAuthzDN )
    {
        authzDN = newAuthzDN;
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/17/2000 7:00:41 PM)
     * @param newGrant boolean
     */
    public void setGrant( boolean newGrant )
    {
        grant = newGrant;
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/17/2000 7:00:41 PM)
     * @param newPermission java.util.Vector
     */
    public void setPermission( java.util.Vector newPermission )
    {
        permission = newPermission;
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/17/2000 8:41:17 PM)
     * @param newScopeSubtree boolean
     */
    public void setScopeSubtree( boolean newScopeSubtree )
    {
        scopeSubtree = newScopeSubtree;
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/17/2000 7:00:41 PM)
     * @param newSubject org.codehaus.server.syntax.DirectoryString
     */
    public void setSubject( org.codehaus.plexus.ldapserver.server.syntax.DirectoryString newSubject )
        throws org.codehaus.plexus.ldapserver.server.util.InvalidDNException
    {
        switch ( getSubjectType() )
        {
            case SUBJECT_ROLE:
            case SUBJECT_GROUP:
            case SUBJECT_SUBTREE:
                subject = DNUtility.getInstance().normalize( newSubject );
                break;
            default:
                subject = newSubject;
                break;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (8/17/2000 7:18:14 PM)
     * @param newSubjectType byte
     */
    public void setSubjectType( byte newSubjectType )
    {
        subjectType = newSubjectType;
    }

    /**
     * Returns a String that represents the value of this object.
     * @return a string representation of the receiver
     */
    public String toString()
    {
        StringBuffer aclString = new StringBuffer();
        if ( isScopeSubtree() )
        {
            aclString.append( "subtree#" );
        }
        else
        {
            aclString.append( "entry#" );
        }

        if ( isGrant() == true )
        {
            aclString.append( "grant:" );
        }
        else
        {
            aclString.append( "deny:" );
        }

        if ( getPermission() != null )
        {
            for ( Enumeration permEnum = getPermission().elements(); permEnum.hasMoreElements(); )
            {
                Byte permByte = (Byte) permEnum.nextElement();
                aclString.append( permByte );
                if ( permEnum.hasMoreElements() )
                {
                    aclString.append( "," );
                }
            }

        }

        aclString.append( "#" );
        if ( getAttr() != null )
        {
            for ( Enumeration attrEnum = getAttr().elements(); attrEnum.hasMoreElements(); )
            {
                DirectoryString oneAttr = (DirectoryString) attrEnum.nextElement();
                aclString.append( oneAttr );
                if ( attrEnum.hasMoreElements() )
                {
                    aclString.append( "," );
                }
            }
        }

        aclString.append( "#" );

        if ( getAuthnLevel() != null )
        {
            aclString.append( "authnLevel:" ).append( getAuthnLevel() ).append( ":" );
        }

        switch ( getSubjectType() )
        {
            case SUBJECT_AUTHZID:
                aclString.append( "authzID-" );
                if ( isAuthzDN() )
                {
                    aclString.append( "dn:" );
                }
                else
                {
                    aclString.append( "u:" );
                }
                aclString.append( getSubject() );
                break;
            case SUBJECT_ROLE:
                aclString.append( "role:" ).append( getSubject() );
                break;
            case SUBJECT_GROUP:
                aclString.append( "group:" ).append( getSubject() );
                break;
            case SUBJECT_SUBTREE:
                aclString.append( "subtree:" ).append( getSubject() );
                break;
            case SUBJECT_IPADDRESS:
                aclString.append( "ipAddress:" ).append( getSubject() );
                break;
            case SUBJECT_PUBLIC:
                aclString.append( "public:" );
                break;
            case SUBJECT_THIS:
                aclString.append( "this:" );
                break;
            default:
                break;
        }

        return aclString.toString();
    }
}
