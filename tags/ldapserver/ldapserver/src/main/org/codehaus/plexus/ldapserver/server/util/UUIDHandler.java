package org.codehaus.plexus.ldapserver.server.util;

public class UUIDHandler
{

    private static UUIDHandler handler = null;
    private String hardwareAddress = null;

    /**
     * UUIDHandler constructor comment.
     */
    private UUIDHandler()
    {
        super();
    }

    public UUID create()
    {
        long time = getTime();
        UUID uuid = new UUID();
        return uuid;
    }

    public UUIDHandler getInstance()
    {
        if ( handler == null )
        {
            handler = new UUIDHandler();
        }
        return handler;
    }

    private synchronized long getTime()
    {
        long time = new java.util.Date().getTime();
        time = time + 0;//0x01B21DD213814000;
        return time;
    }

    public void setHardwareAddress( String hardwareAddress )
    {
        this.hardwareAddress = hardwareAddress;
    }
}
