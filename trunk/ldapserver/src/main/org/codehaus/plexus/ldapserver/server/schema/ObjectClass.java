package org.codehaus.plexus.ldapserver.server.schema;



/**
 * Represents an LDAP Object Class
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.server.syntax.DirectoryString;

import java.util.Enumeration;
import java.util.Vector;

public class ObjectClass
{

    public static final int OC_ABSTRACT = 0;
    public static final int OC_STRUCTURAL = 1;
    public static final int OC_AUXILIARY = 2;


    private String oid = null;
    private DirectoryString name = null;
    private String description = null;
    private boolean obsolete = false;
    private DirectoryString superior = null;
    private int type = OC_STRUCTURAL;
    private Vector must = null;
    private Vector may = null;

    /**
     * ObjectClass constructor comment.
     */
    public ObjectClass()
    {
        super();
        setMay( new Vector() );
        setMust( new Vector() );
    }

    public void addMay( DirectoryString aMay )
    {
        Vector may = getMay();
        if ( may == null )
        {
            may = new Vector();
        }
        may.addElement( aMay );
        this.may = may;
    }

    public void addMust( DirectoryString aMust )
    {
        Vector must = getMust();
        if ( must == null )
        {
            must = new Vector();
        }
        must.addElement( aMust );
        this.must = must;
    }

    public String getDescription()
    {
        return this.description;
    }

    public Vector getMay()
    {
        return this.may;
    }

    public Vector getMust()
    {
        return this.must;
    }

    public DirectoryString getName()
    {
        return this.name;
    }

    public String getOid()
    {
        return this.oid;
    }

    public DirectoryString getSuperior()
    {
        return this.superior;
    }

    public int getType()
    {
        return this.type;
    }

    public boolean isObsolete()
    {
        return this.obsolete;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public void setMay( Vector may )
    {
        this.may = may;
    }

    public void setMust( Vector must )
    {
        this.must = must;
    }

    public void setName( DirectoryString name )
    {
        this.name = name;
    }

    public void setObsolete( boolean obsolete )
    {
        this.obsolete = obsolete;
    }

    public void setOid( String oid )
    {
        this.oid = oid;
    }

    public void setSuperior( DirectoryString superior )
    {
        this.superior = superior;
    }

    public void setType( int type )
    {
        this.type = type;
    }

    public String toString()
    {
        String objClass = new String();
        objClass = objClass.concat( "( " + getOid() + " NAME " + getName().toString() );
        if ( getDescription() != null && !getDescription().equals( "" ) )
        {
            objClass = objClass.concat( " DESC " + getDescription() );
        }
        if ( isObsolete() == true )
        {
            objClass = objClass.concat( " OBSOLETE" );
        }
        if ( getSuperior() != null )
        {
            objClass = objClass.concat( " SUP " + getSuperior().toString() );
        }
        if ( getType() == OC_ABSTRACT )
        {
            objClass = objClass.concat( " ABSTRACT" );
        }
        else if ( getType() == OC_AUXILIARY )
        {
            objClass = objClass.concat( " AUXILIARY" );
        }

        if ( getMust() != null && !getMust().isEmpty() )
        {
            if ( getMust().size() > 1 )
            {
                objClass = objClass.concat( " MUST ( " );
                Enumeration enum = getMust().elements();
                while ( enum.hasMoreElements() )
                {
                    objClass = objClass.concat( ( (DirectoryString) enum.nextElement() ).toString() );
                    if ( enum.hasMoreElements() )
                    {
                        objClass = objClass.concat( " $ " );
                    }
                }
                objClass = objClass.concat( " )" );
            }
            else
            {
                objClass = objClass.concat( " MUST " + ( (DirectoryString) getMust().elementAt( 0 ) ).toString() );
            }
        }
        if ( getMay() != null && !getMay().isEmpty() )
        {
            if ( getMay().size() > 1 )
            {
                objClass = objClass.concat( " MAY ( " );
                Enumeration enum = getMay().elements();
                while ( enum.hasMoreElements() )
                {
                    objClass = objClass.concat( ( (DirectoryString) enum.nextElement() ).toString() );
                    if ( enum.hasMoreElements() )
                    {
                        objClass = objClass.concat( " $ " );
                    }
                }
                objClass = objClass.concat( " )" );
            }
            else
            {
                objClass = objClass.concat( " MAY " + (DirectoryString) getMay().elementAt( 0 ) );
            }
        }
        objClass = objClass.concat( " )" );
        return objClass;
    }
}
