package org.codehaus.plexus.ldapserver.server.schema;

/**
 * An XML Handler for use with the Xerces XML Parser to Parse DSML Schema
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.server.syntax.DirectoryString;
import org.xml.sax.AttributeList;

public class SchemaXMLHandler extends org.xml.sax.HandlerBase
{

    private static final int DSML_OC = 0;
    private static final int DSML_AT = 1;
    private int currentType = DSML_OC;

    private static final int OP_IGNORE = 0;
    private static final int OP_NAME = 1;
    private static final int OP_DESC = 2;
    private static final int OP_OID = 3;

    private int currentOp = OP_IGNORE;

    private ObjectClass currentOC = null;
    private AttributeType currentAT = null;

    public void characters( char[] ch, int start, int length )
    {
        String text = new String( ch, start, length );
        if ( currentType == DSML_OC && text != null )
        {
            if ( currentOp == OP_NAME )
            {
                currentOC.setName( new DirectoryString( text ) );
            }
            if ( currentOp == OP_DESC )
            {
                currentOC.setDescription( text );
            }
            if ( currentOp == OP_OID )
            {
                currentOC.setOid( text );
            }
        }
        if ( currentType == DSML_AT && text != null )
        {
            if ( currentOp == OP_NAME )
            {
                currentAT.setName( new DirectoryString( text ) );
            }
            if ( currentOp == OP_DESC )
            {
                currentAT.setDescription( text );
            }
            if ( currentOp == OP_OID )
            {
                currentAT.setOid( text );
            }
        }
        currentOp = OP_IGNORE;
    }

    public void endElement( String name )
    {
        if ( name.equals( "dsml:class" ) )
        {
            SchemaChecker.getInstance().addObjectClass( currentOC );
        }
        if ( name.equals( "dsml:attribute-type" ) )
        {
            SchemaChecker.getInstance().addAttributeType( currentAT );
        }
        currentOp = OP_IGNORE;
    }

    public void startElement( String name, AttributeList atts )
    {
        if ( name.equals( "dsml:class" ) )
        {
            currentType = DSML_OC;
            currentOC = new ObjectClass();
            String superior = atts.getValue( "superior" );
            if ( superior != null )
            {
                currentOC.setSuperior( new DirectoryString( superior.substring( 1 ) ) );
            }
            String ocType = atts.getValue( "type" );
            if ( ocType != null )
            {
                if ( ocType.equals( "abstract" ) )
                {
                    currentOC.setType( ObjectClass.OC_ABSTRACT );
                }
                else if ( ocType.equals( "auxilary" ) )
                {
                    currentOC.setType( ObjectClass.OC_AUXILIARY );
                }
                else
                {
                    currentOC.setType( ObjectClass.OC_STRUCTURAL );
                }
            }
        }
        if ( name.equals( "dsml:name" ) )
        {
            currentOp = OP_NAME;
        }

        if ( name.equals( "dsml:description" ) )
        {
            currentOp = OP_DESC;
        }

        if ( name.equals( "dsml:object-identifier" ) )
        {
            currentOp = OP_OID;
        }
        if ( name.equals( "dsml:attribute" ) && currentType == DSML_OC )
        {
            String ref = atts.getValue( "ref" );
            String req = atts.getValue( "required" );
            if ( req != null && ref != null && req.equals( "true" ) )
            {
                currentOC.addMust( new DirectoryString( ref.substring( 1 ) ) );
            }
            else if ( ref != null )
            {
                currentOC.addMay( new DirectoryString( ref.substring( 1 ) ) );
            }
        }

        if ( name.equals( "dsml:attribute-type" ) )
        {
            currentType = DSML_AT;
            currentAT = new AttributeType();
            String superior = atts.getValue( "superior" );
            if ( superior != null )
            {
                currentAT.setSuperior( new DirectoryString( superior.substring( 1 ) ) );
            }
        }

    }
}
