package org.codehaus.plexus.ldapserver.server.schema;

/*
The JavaLDAP Server
Copyright (C) 2000  Clayton Donley (donley@linc-dev.com) - All Rights Reserved

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/

/**
 * Class for Initializing the LDAP Server's Schema
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.server.util.ServerConfig;
import org.xml.sax.HandlerBase;
import org.xml.sax.Parser;
import org.xml.sax.helpers.ParserFactory;

public class InitSchema
{

    private static final String
        DEFAULT_PARSER_NAME = "org.apache.xerces.parsers.SAXParser";

    /**
     * InitSchema constructor comment.
     */
    public InitSchema()
    {
        super();
    }

    public void init()
    {

        String parserName = DEFAULT_PARSER_NAME;
        try
        {
            HandlerBase handler = new SchemaXMLHandler();

            Parser parser = ParserFactory.makeParser( parserName );
            parser.setDocumentHandler( handler );
            parser.setErrorHandler( handler );
            parser.parse( (String) ServerConfig.getInstance().get( ServerConfig.JAVALDAP_STDSCHEMA ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace( System.err );
        }
    }
}
