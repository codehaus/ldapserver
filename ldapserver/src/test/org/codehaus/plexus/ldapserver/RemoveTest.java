package org.codehaus.plexus.ldapserver;

/*
 * LICENSE
 */

/**
 * @author <a href="mailto:trygvis@inamo.no">Trygve Laugst&oslash;l</a>
 * @version $Id: RemoveTest.java,v 1.1 2003-12-23 19:24:14 trygvis Exp $
 */
public class RemoveTest extends AbstractLdapTest {
    public void setUp() throws Exception {
        start();
    }

    public void tearDown() throws Exception {
        stop();
    }
    
    public void testBasic() throws Exception {
        String username = "trygvis";
        String cn = "blah";
        int uid = 5;
        int gid = 7;

        addPosixAccount(username, cn, uid, gid);
        removeEntry("uid=username");
        assertNoSuchEntry("uid=username");
    }
}
