package org.codehaus.plexus.ldapserver.server.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class for reading and retrieving the server's file-based configuration
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */
public class ServerConfig extends java.util.Properties
{
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ServerConfig.class);

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

    public static ServerConfig getInstance()
    {
        if ( instance == null )
        {
            instance = new ServerConfig();
        }
        return instance;
    }

    public boolean init()
    {
        if(!loadFromFile()) {
            if(!loadFromClass()) {
                return false;
            }
        }
        return true;
    }
    
    private boolean loadFromFile() {
        InputStream is;
        
        try {
            is = new FileInputStream(JAVALDAP_PROP);
            load(is);
            is.close();
        }
        catch(FileNotFoundException fnfe) {
            return false;
        }
        catch(IOException ioe) {
            LOGGER.warn("IO Error Reading " + JAVALDAP_PROP );
        }
        
        return true;
    }
    
    private boolean loadFromClass() {
        InputStream is = this.getClass().getResourceAsStream("/" + JAVALDAP_PROP);
        
        if (is == null) {
            LOGGER.info("Configuration not found: " + JAVALDAP_PROP);
            return false;
        }

        try {
            load(is);
            return true;
        }
        catch(IOException ex) 
        {
            LOGGER.warn("Error reading /" + JAVALDAP_PROP + " from class path.");
            return false;
        } 
        finally 
        {
            StreamUtility.close(is);
        }
    }

    public void write()
    {
        try
        {
            FileOutputStream os = new FileOutputStream( JAVALDAP_PROP );
            store( os, JAVALDAP_PROP_DESC );
            StreamUtility.close(os);
        }
        catch ( java.io.IOException ioe )
        {
            LOGGER.info("IO Error Writing " + JAVALDAP_PROP );
        }
    }
}
