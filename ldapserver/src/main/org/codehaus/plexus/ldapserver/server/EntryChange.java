package org.codehaus.plexus.ldapserver.server;



/**
 * Class containing a single LDAP modification
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.server.syntax.DirectoryString;

import java.util.Vector;

public class EntryChange
{

    private int modType;
    public DirectoryString attr;
    public java.util.Vector values;

    /**
     * EntryChange constructor comment.
     */
    public EntryChange()
    {
        super();
    }

    /**
     * EntryChange constructor comment.
     */
    public EntryChange( int modType, DirectoryString attr, Vector values )
    {
        super();
        setModType( modType );
        setAttr( attr );
        setValues( values );
    }

    /**
     * @return java.lang.String
     */
    public DirectoryString getAttr()
    {
        return attr;
    }

    /**
     * @return int
     */
    public int getModType()
    {
        return modType;
    }

    /**
     * @return java.util.Vector
     */
    public java.util.Vector getValues()
    {
        return values;
    }

    /**
     * @param newAttr java.lang.String
     */
    public void setAttr( DirectoryString newAttr )
    {
        attr = newAttr;
    }

    /**
     * @param newModType int
     */
    public void setModType( int newModType )
    {
        modType = newModType;
    }

    /**
     * @param newValues java.util.Vector
     */
    public void setValues( java.util.Vector newValues )
    {
        values = newValues;
    }
}
