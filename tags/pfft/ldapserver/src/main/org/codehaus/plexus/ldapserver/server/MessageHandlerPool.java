package org.codehaus.plexus.ldapserver.server;

import org.codehaus.plexus.ldapserver.server.util.ObjectPool;

/**
 * MessageHandlers involve some heavy setup (BER encoder/decoder, Connection
 * object, etc...), so we pool them using the MessageHandlerPool.
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */
class MessageHandlerPool extends ObjectPool
{
    private static MessageHandlerPool mhp = null;

    /**
     * ConnectionHandlerPool constructor comment.
     */
    private MessageHandlerPool()
    {
        super();
    }

    /**
     * create method comment.
     */
    public Object create() throws Exception
    {
        return new MessageHandler();
    }

    /**
     * expire method comment.
     */
    public void expire( Object o )
    {
    }

    public static MessageHandlerPool getInstance()
    {
        if ( mhp == null )
        {
            mhp = new MessageHandlerPool();
        }
        return mhp;
    }

    /**
     * validate method comment.
     */
    public boolean validate( Object o )
    {
        return false;
    }
}
