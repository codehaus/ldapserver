package org.codehaus.plexus.ldapserver.server.operation;


/**
 * The Compare operation compares an attribute value to those stored within
 * a particular LDAP entry. Method not completed.
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.ldapv3.LDAPMessage;

public class CompareOperation implements Operation
{

    LDAPMessage request = null;
    LDAPMessage response = null;

    public CompareOperation( LDAPMessage request )
    {
        this.request = request;
    }

    public LDAPMessage getResponse()
    {
        return this.response;
    }

    public void perform()
    {

        // Not Completed
    }
}
