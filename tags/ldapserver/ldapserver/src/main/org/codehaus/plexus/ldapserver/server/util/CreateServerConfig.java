package org.codehaus.plexus.ldapserver.server.util;

/**
 * Insert the type's description here.
 * Creation date: (6/10/2000 11:28:01 AM)
 * @author: Administrator
 */
public class CreateServerConfig
{
    /**
     * CreateServerConfig constructor comment.
     */
    public CreateServerConfig()
    {
        super();
    }

    public static void main( String args[] )
    {
        ServerConfig sc = ServerConfig.getInstance();
        sc.put( "codehaus.server.name", "localhost" );
        sc.put( "codehaus.server.port", "10389" );
        sc.put( "codehaus.server.schema.std", "std.oc.xml" );
        sc.put( "codehaus.server.schema.user", "user.oc.xml" );
        sc.write();
    }
}
