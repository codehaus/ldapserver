package org.codehaus.plexus.ldapserver;

/*
 * LICENSE
 */

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import junit.framework.TestCase;
import org.codehaus.plexus.ldapserver.server.EmbeddedLDAPServer;

/**
 * @author <a href="mailto:trygvis@inamo.no">Trygve Laugst&oslash;l</a>
 * @version $Id: AbstractLdapTest.java,v 1.1 2003-12-23 19:24:14 trygvis Exp $
 */
public abstract class AbstractLdapTest extends TestCase {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(AbstractLdapTest.class);
    
    private EmbeddedLDAPServer server;
    private DirContext context;
    private Ldap ldap;
    
    public AbstractLdapTest() {
    }

    public void start() throws Exception {
        int port = 6666;
        String baseDN = "dc=javaldap,dc=com";

        server = new EmbeddedLDAPServer(port);
        server.start();

        ldap = new Ldap();
        ldap.setBaseDN(baseDN);
        ldap.setServer("127.0.0.1");
        ldap.setPort("" + port);
        ldap.setUser("cn=admin");
        ldap.setPassword("manager");

        LDAPConnection con = new LDAPConnection();

        con.loadConfiguration(ldap);
        con.connect();

        context = con.getDirContext();
    }
    
    public void stop() {
        server.sendStopSignal();
        server.waitForShutdown(10000);
    }

    public void addPosixAccount(String username, String cn, int uid, int gid) 
        throws NamingException {
        Attribute attribute;
        Attributes attributes = new BasicAttributes();

        attribute = new BasicAttribute("objectClass");
        attribute.add("top");
        attribute.add("posixAccount");
        attributes.put(attribute);
        attributes.put("uid", username);
        attributes.put("cn", cn);
        attributes.put("uidNumber", Integer.toString(uid));
        attributes.put("gidNumber", Integer.toString(gid));
        attributes.put("homeDirectory", "/home/" + username);
        context.createSubcontext("uid=" + username + "," + ldap.getBaseDN(), attributes);
    }

    public void assertPosixAccount(String username, String cn, int uid, int gid) 
        throws NamingException {
        SearchControls controls;
        String[] attributeIds;
        NamingEnumeration results;
        SearchResult result;
        Attributes attributes;

        attributeIds = new String[]{"objectClass", "cn", "uid", "uidNumber", "gidNumber", "homeDirectory"};
        controls = new SearchControls();
        controls.setReturningAttributes(attributeIds);
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setReturningObjFlag(true);
        controls.setDerefLinkFlag(true);
        results = context.search(ldap.getBaseDN(), "uid={0}", new String[]{username}, controls);

        assertTrue(results.hasMoreElements());
        result = (SearchResult)results.nextElement();
        attributes = result.getAttributes();
        assertEquals(attributeIds.length, attributes.size());
        assertEquals(username, attributes.get("uid").get());
        assertEquals(cn, attributes.get("cn").get());
        assertEquals(uid, Integer.parseInt(attributes.get("uidNumber").get().toString()));
        assertEquals(gid, Integer.parseInt(attributes.get("gidNumber").get().toString()));
        assertEquals("/home/" + username, attributes.get("homeDirectory").get());
        assertTrue(attributes.get("objectClass").contains("top"));
        assertTrue(attributes.get("objectClass").contains("posixAccount"));
        assertFalse(results.hasMoreElements());
    }

    public void assertNoSuchEntry(String dn) throws NamingException {
        NamingEnumeration results;
        
        results = context.search(ldap.getBaseDN(), dn, (SearchControls)null);
        assertFalse(results.hasMoreElements());
    }

    public void assertSingleEntryExists(String dn) throws NamingException {
        NamingEnumeration results;
        
        results = context.search(ldap.getBaseDN(), dn, (SearchControls)null);
        assertTrue(results.hasMoreElements());
        assertNotNull(results.nextElement());
        assertFalse(results.hasMoreElements());
    }

    public void removeEntry(String dn) throws NamingException {
        context.unbind(dn);
    }
}
