package org.codehaus.plexus.ldapserver.server;

/**
 * The Entry class represents an LDAP entry in Java.
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.server.syntax.DirectoryString;
import org.codehaus.plexus.ldapserver.server.util.DNUtility;
import org.codehaus.plexus.ldapserver.server.util.InvalidDNException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class Entry extends Hashtable implements Serializable
{

    private DirectoryString name = null;
    private DirectoryString base = null;
    private long id = -1;

    public Entry()
    {
        super();
    }

    public Entry( byte[] entryBytes )
    {
        ByteArrayInputStream bais = new ByteArrayInputStream( entryBytes );
        int len = bais.read();
        byte[] nameBytes = new byte[len];
        bais.read( nameBytes, 0, len );
        this.name = new DirectoryString( nameBytes );

        len = bais.read();
        byte[] baseBytes = new byte[len];
        bais.read( baseBytes, 0, len );
        this.base = new DirectoryString( baseBytes );

        len = bais.read();
        byte[] idBytes = new byte[len];
        bais.read( idBytes, 0, len );
        this.id = new Long( new String( idBytes ) ).longValue();

        int numKeys = bais.read();
        for ( int keyCount = 0; keyCount < numKeys; keyCount++ )
        {
            len = bais.read();
            byte[] keyBytes = new byte[len];
            bais.read( keyBytes, 0, len );
            Vector values = new Vector();
            int numValues = bais.read();
            for ( int valCount = 0; valCount < numValues; valCount++ )
            {
                len = bais.read();
                byte[] valBytes = new byte[len];
                bais.read( valBytes, 0, len );
                values.addElement( new DirectoryString( valBytes ) );
            }
            put( new DirectoryString( keyBytes ), values );
        }
    }

    public Entry( DirectoryString name ) throws InvalidDNException
    {

        setName( name );
    }

    public byte[] getAsByteArray()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int nameLen = getName().length();
        baos.write( nameLen );
        baos.write( getName().getBytes(), 0, nameLen );

        int baseLen = getBase().length();
        baos.write( baseLen );
        baos.write( getBase().getBytes(), 0, baseLen );

        String idString = new Long( getID() ).toString();
        int idLen = idString.length();
        baos.write( idLen );
        baos.write( idString.getBytes(), 0, idLen );

        baos.write( size() );
        Enumeration keyEnum = keys();
        while ( keyEnum.hasMoreElements() )
        {
            DirectoryString aKey = (DirectoryString) keyEnum.nextElement();
            baos.write( aKey.length() );
            baos.write( aKey.getBytes(), 0, aKey.length() );
            Vector vals = (Vector) get( aKey );
            baos.write( vals.size() );
            Enumeration valEnum = vals.elements();
            while ( valEnum.hasMoreElements() )
            {
                DirectoryString aVal = (DirectoryString) valEnum.nextElement();
                baos.write( aVal.length() );
                baos.write( aVal.getBytes(), 0, aVal.length() );
            }
        }
        byte[] entryBytes = baos.toByteArray();
        try
        {
            baos.close();
        }
        catch ( java.io.IOException ioe )
        {
            ioe.printStackTrace();
        }
        return entryBytes;
    }

    public DirectoryString getBase()
    {
        return this.base;
    }

    public long getID()
    {
        return this.id;
    }

    public DirectoryString getName()
    {
        return this.name;
    }

    public void setBase( DirectoryString base )
    {
        this.base = base;
    }

    public void setID( long id )
    {
        this.id = id;
    }

    public void setName( DirectoryString name ) throws InvalidDNException
    {

        Vector rdnComponents = DNUtility.getInstance().explodeDN( name );

        this.name = DNUtility.getInstance().createDN( rdnComponents );

        if ( !rdnComponents.isEmpty() )
        {
            rdnComponents.removeElementAt( 0 );
        }
        setBase( DNUtility.getInstance().createDN( rdnComponents ) );

        //Logger.getInstance().log(Logger.LOG_DEBUG,"New Entry Name: " + getName());
        //Logger.getInstance().log(Logger.LOG_DEBUG,"New Entry Base: " + getBase());

    }
}
