package org.codehaus.plexus.ldapserver.server.util;



/**
 * General logging class for use in printing debug output
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 * @deprecated Removed in favour of log4j
 */
public class Logger
{
    private static Logger instance;
    private int logLevel = 0;
    private java.io.PrintStream logStream = System.out;

    public static final int LOG_NORMAL = 0;
    public static final int LOG_DEBUG = 9;

    private Logger()
    {
        Object value;
        
        value = ServerConfig.getInstance().get( ServerConfig.JAVALDAP_DEBUG );
        if(value == null) {
            logLevel = LOG_DEBUG;
            return;
        }
        
        try {
            logLevel = new Integer(value.toString()).intValue();
        }
        catch(NumberFormatException ex) {
            logLevel = LOG_DEBUG;
        }
    }

    public static Logger getInstance()
    {
        if ( instance == null )
        {
            instance = new Logger();
        }
        return instance;
    }

    public void log( int level, String message )
    {
        if ( logLevel >= level )
        {
            logStream.println( message );
        }
    }

    public void setLogLevel( int level )
    {
        this.logLevel = level;
    }

    public void setLogWriter( java.io.PrintStream logStream )
    {
        this.logStream = logStream;
    }
}
