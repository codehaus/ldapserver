/*
 * (c) Copyright 2001 MyCorporation.
 * All Rights Reserved.
 */
package org.codehaus.plexusl.ldapserver;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;

/**
 * @version 	1.0
 * @author
 */
public class LDAPConnection {
    /** log4j logger */
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(LDAPConnection.class);

    Hashtable environment = new Hashtable();
    public LDAPConnection() {
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        environment.put("com.sun.jndi.ldap.connect.pool", "true");

    }

    private Ldap ldap;
    public void loadConfiguration(Ldap newldap) throws IOException {
        this.ldap = newldap;
    }

    InitialDirContext initDirContext = null;
    public void connect() throws NamingException {
        environment.put(
            Context.PROVIDER_URL,
            "ldap://" + ldap.getServer() + ":" + ldap.getPort() + "/" + ldap.getBaseDN());
        environment.put(Context.SECURITY_PRINCIPAL, ldap.getUser());
        environment.put(Context.SECURITY_CREDENTIALS, ldap.getPassword());

        Enumeration keys = environment.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            LOGGER.info("Environment[" + key + "] = " + environment.get(key));

        }

        LOGGER.info("Connecting...");

        initDirContext = new InitialDirContext(environment);

    }

    public InitialDirContext getDirContext() {
        return this.initDirContext;
    }

    /*private void dumpDirContext(DirContext dctx, String indent)
    	throws NamingException {
    	NamingEnumeration bindings = dctx.listBindings("");
    	while (bindings.hasMore()) {
    		Binding bd = (Binding) bindings.next();
    		DirContext sctx = (DirContext) bd.getObject();
    		logger.info(indent + "<B>" + bd.getName() + "</B>");
    
    		Attributes a = sctx.getAttributes("");
    		NamingEnumeration attrlist = a.getAll();
    		while (attrlist.hasMore()) {
    			Attribute att = (Attribute) attrlist.next();
    			logger.info(indent + "&nbsp;&nbsp;&nbsp;&nbsp;" + att.toString());
    		}
    		dumpDirContext(sctx, indent + "    ");
    	}
    
    }*/

    public void findZFD() throws NamingException {
        SearchControls cons = new SearchControls();

        cons.setSearchScope(SearchControls.SUBTREE_SCOPE);

        NamingEnumeration searchResults = initDirContext.search("", "(ou=zfd)", cons);

        while (searchResults.hasMore()) {
            Binding bd = (Binding) searchResults.next();
            //DirContext sctx = (DirContext) bd.getObject();
            LOGGER.info("bd:" + bd.getName());
            //logger.info("Binding : " + bd.getClassName());
            //	logger.info("  " + bd.getName() + "  ");
            Attributes a = initDirContext.getAttributes(bd.getName());

            if (a != null) {
                NamingEnumeration attrlist = a.getAll();
                while (attrlist.hasMore()) {
                    Attribute att = (Attribute) attrlist.next();
                    LOGGER.info("Attribute: " + att.toString());
                }
            }
        }

    }

    public void dump() throws NamingException {
        dump(initDirContext, "");
    }

    public static void dumpNamingEnumeration(NamingEnumeration enum) throws NamingException {

        while (enum.hasMore()) {
            Binding bd = (Binding) enum.next();
            //DirContext sctx = (DirContext) bd.getObject();
            LOGGER.info("  " + bd.getName() + "  ");

        }

    }

    public static void dump(InitialDirContext dctx, String indent) throws NamingException {
        NamingEnumeration bindings = dctx.listBindings("");

        while (bindings.hasMore()) {
            Binding bd = (Binding) bindings.next();
            //DirContext sctx = (DirContext) bd.getObject();
            LOGGER.info(indent + "<B>" + bd.getName() + "</B>");

            //Attributes a = sctx.getAttributes("");
            //NamingEnumeration attrlist = a.getAll();
            //while (attrlist.hasMore()) {
            //Attribute att = (Attribute) attrlist.next();
            //logger.info(indent + att.toString());
            //}
            //dumpDirContext(sctx, indent + "&nbsp;&nbsp;");
        }

    }

}