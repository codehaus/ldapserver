package org.codehaus.plexus.ldapserver.server.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author  Ben Walding
 * @version $Id: StreamUtility.java,v 1.1 2003-12-18 03:47:58 bwalding Exp $
 */
public class StreamUtility
{
    public static void close(InputStream is)
    {
        if (is != null)
        {
            try
            {
                is.close();
            }
            catch (IOException e)
            {
                //Don't care
            }
        }
    }

    public static void close(OutputStream os)
    {
        if (os != null)
        {
            try
            {
                os.close();
            }
            catch (IOException e)
            {
                //Don't care
            }
        }
    }
}
