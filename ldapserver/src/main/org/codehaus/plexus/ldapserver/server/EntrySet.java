package org.codehaus.plexus.ldapserver.server;




/**
 * Backends have different ways of managing a set of LDAP entries returned in
 * a search. This interface is implemented by classes that know how to retrieve
 * the next entry in a result.
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */
public interface EntrySet
{

    abstract public Entry getNext();

    abstract public boolean hasMore();
}
