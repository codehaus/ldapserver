package org.codehaus.plexus.ldapserver.server.schema;


/**
 * Class for Checking Schema
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.server.Entry;
import org.codehaus.plexus.ldapserver.server.syntax.DirectoryString;
import org.codehaus.plexus.ldapserver.server.util.ServerConfig;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class SchemaChecker
{
    private static Hashtable attributeTypes = null;
    private static Hashtable objectClasses = null;
    private static Hashtable fullOCTable = null;
    private static SchemaChecker instance;
    private static boolean schemaCheckOn = true;
    private static DirectoryString objClassAttr = new DirectoryString( "objectclass" );

    /**
     * SchemaChecker constructor comment.
     */
    private SchemaChecker()
    {
        super();
        objectClasses = new Hashtable();
        attributeTypes = new Hashtable();
        fullOCTable = new Hashtable();

        if ( ServerConfig.getInstance().get( ServerConfig.JAVALDAP_SCHEMACHECK ).equals( "0" ) )
        {
            this.schemaCheckOn = false;
        }
    }

    public void addAttributeType( AttributeType attributeType )
    {
        attributeTypes.put( attributeType.getName(), attributeType );
    }

    public void addObjectClass( ObjectClass objectClass )
    {
        objectClasses.put( objectClass.getName(), objectClass );
    }

    public void checkEntry( Entry entry ) throws org.codehaus.plexus.ldapserver.server.util.DirectorySchemaViolation
    {
        if ( isSchemaCheckOn() == false )
        {
            return;
        }
        Vector objectClass = (Vector) entry.get( objClassAttr );
        if ( objectClass == null )
        {
            throw new org.codehaus.plexus.ldapserver.server.util.DirectorySchemaViolation( "Missing Object Class" );
        }
        Vector missingAttr = new Vector();
        Vector invalidAttr = new Vector();
        Enumeration ocEnum = objectClass.elements();
        while ( ocEnum.hasMoreElements() )
        {
            ObjectClass oc = getObjectClass( (DirectoryString) ocEnum.nextElement() );
            Vector must = null;
            if ( oc != null )
            {
                must = oc.getMust();
            }
            if ( must != null )
            {
                Enumeration mustEnum = must.elements();
                while ( mustEnum.hasMoreElements() )
                {
                    DirectoryString aMust = (DirectoryString) mustEnum.nextElement();
                    if ( !entry.containsKey( aMust ) || ( (Vector) entry.get( aMust ) ).isEmpty() )
                    {
                        missingAttr.addElement( aMust );
                    }
                }
            }
            while ( oc != null && oc.getSuperior() != null && !objectClass.contains( oc.getSuperior() ) )
            {
                DirectoryString aSup = oc.getSuperior();
                objectClass.addElement( aSup );
                oc = getObjectClass( aSup );
            }
        }

        ocEnum = objectClass.elements();
        boolean firstOC = true;

        while ( ocEnum.hasMoreElements() )
        {
            ObjectClass oc = getObjectClass( (DirectoryString) ocEnum.nextElement() );
            Vector tmpInvalidAttr = null;
            Enumeration attrEnum = null;
            if ( firstOC )
            {
                attrEnum = entry.keys();
                firstOC = false;
            }
            else
            {
                tmpInvalidAttr = (Vector) invalidAttr.clone();
                attrEnum = tmpInvalidAttr.elements();
            }
            while ( attrEnum.hasMoreElements() )
            {
                DirectoryString attr = (DirectoryString) attrEnum.nextElement();
                if ( oc != null && ( oc.getMust().contains( attr ) || oc.getMay().contains( attr ) ) )
                {
                    if ( invalidAttr.contains( attr ) )
                    {
                        invalidAttr.removeElement( attr );
                    }
                }
                else
                {
                    if ( !invalidAttr.contains( attr ) )
                    {
                        invalidAttr.addElement( attr );
                    }
                }
            }
        }
        if ( !invalidAttr.isEmpty() || !missingAttr.isEmpty() )
        {
            throw new org.codehaus.plexus.ldapserver.server.util.DirectorySchemaViolation( "Missing Attributes: " + missingAttr.toString() + ", Not Allowed Attributes: " + invalidAttr.toString() );
        }
    }

    private AttributeType getAttributeType( DirectoryString attributeType )
    {
        return (AttributeType) this.attributeTypes.get( attributeType );
    }

    public static SchemaChecker getInstance()
    {
        if ( instance == null )
        {
            instance = new SchemaChecker();
        }
        return instance;
    }

    private ObjectClass getObjectClass( DirectoryString objectClass )
    {
        return (ObjectClass) this.objectClasses.get( objectClass );
    }

    public Hashtable getObjectClasses()
    {
        return this.objectClasses;
    }

    public boolean isSchemaCheckOn()
    {
        return this.schemaCheckOn;
    }

    public void removeAttributeType( DirectoryString name )
    {
        attributeTypes.remove( name );
    }

    public void removeObjectClass( DirectoryString name )
    {
        objectClasses.remove( name );
    }

    public void setSchemaCheckOn( boolean schemaCheckOn )
    {
        this.schemaCheckOn = schemaCheckOn;
    }
}
