package org.codehaus.plexus.ldapserver.server.operation;



import org.codehaus.plexus.ldapserver.ldapv3.LDAPMessage;
import org.codehaus.plexus.ldapserver.server.util.DirectoryException;

public interface Operation
{

    public abstract LDAPMessage getResponse();

    public abstract void perform() throws DirectoryException;
}
