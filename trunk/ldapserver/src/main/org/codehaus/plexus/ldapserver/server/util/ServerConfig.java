package org.codehaus.plexus.ldapserver.server.util;

/**
 * Class for reading and retrieving the server's file-based configuration
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;

public class ServerConfig extends java.util.Properties
{

    private static final String JAVALDAP_PROP = "javaldap.prop";
    private static final String JAVALDAP_PROP_DESC = "JavaLDAP Server Properties";

    public static final String JAVALDAP_SERVER_NAME = "javaldap.server.name";
    public static final String JAVALDAP_SERVER_PORT = "javaldap.server.port";
    public static final String JAVALDAP_STDSCHEMA = "javaldap.schema.std";
    public static final String JAVALDAP_USERSCHEMA = "javaldap.schema.user";
    public static final String JAVALDAP_SERVER_THREADS = "javaldap.server.threads";
    public static final String JAVALDAP_SERVER_BACKENDS = "javaldap.server.backends";
    public static final String JAVALDAP_SCHEMACHECK = "javaldap.schemacheck";
    public static final String JAVALDAP_ACLCHECK = "javaldap.aclcheck";
    public static final String JAVALDAP_ACLPROPS = "javaldap.acl.props";
    public static final String JAVALDAP_ROOTUSER = "javaldap.rootuser";
    public static final String JAVALDAP_ROOTPW = "javaldap.rootpw";
    public static final String JAVALDAP_DEBUG = "javaldap.debug";

    // Backend Config should go in the backend config, not this...however this will do for now.
    public static final String JAVALDAP_BACKENDJDBC_LONGVARCHAR = "javaldap.backendjdbc.longvarchar";
    public static final String JAVALDAP_BACKENDJDBC_CREATETABLE = "javaldap.backendjdbc.createtable";
    public static final String JAVALDAP_BACKENDJDBC_DBDRIVER = "javaldap.backendjdbc.dbdriver";
    public static final String JAVALDAP_BACKENDJDBC_DBURL = "javaldap.backendjdbc.dburl";
    public static final String JAVALDAP_BACKENDJDBC_DBUSER = "javaldap.backendjdbc.dbuser";
    public static final String JAVALDAP_BACKENDJDBC_DBPASS = "javaldap.backendjdbc.dbpass";

    private static ServerConfig instance;

    /**
     * ServerConfig constructor comment.
     */
    private ServerConfig()
    {
        super();
    }

    /**
     * ServerConfig constructor comment.
     * @param defaults java.util.Properties
     */
    private ServerConfig( java.util.Properties defaults )
    {
        super( defaults );
    }

    public static ServerConfig getInstance()
    {
        if ( instance == null )
        {
            instance = new ServerConfig();
        }
        return instance;
    }

    public void init()
    {
        InputStream is;
        
        try
        {
            is = new FileInputStream( JAVALDAP_PROP );
            load( is );
            is.close();
        }
        catch ( java.io.FileNotFoundException fnfe )
        {
            is = this.getClass().getResourceAsStream("/" + JAVALDAP_PROP);
            if(is == null) {
                Logger.getInstance().log( Logger.LOG_NORMAL, "Configuration Not Found: " + JAVALDAP_PROP );
            }
            else {
                try {
                    load(is);
                    is.close();
                }
                catch(IOException ex) {
                    Logger.getInstance().log( Logger.LOG_NORMAL, "IO Error Reading /" + JAVALDAP_PROP + " from class.");
                }
            }
        }
        catch ( java.io.IOException ioe )
        {
            Logger.getInstance().log( Logger.LOG_NORMAL, "IO Error Reading " + JAVALDAP_PROP );
        }
    }

    public void write()
    {
        try
        {
            FileOutputStream os = new FileOutputStream( JAVALDAP_PROP );
            save( os, JAVALDAP_PROP_DESC );
            os.close();
        }
        catch ( java.io.IOException ioe )
        {
            Logger.getInstance().log( Logger.LOG_NORMAL, "IO Error Writing " + JAVALDAP_PROP );
        }
    }
}
