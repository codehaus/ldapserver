package org.codehaus.plexus.ldapserver.server;

/**
 * @author <a href="mailto:trygvis@inamo.no">Trygve Laugst&oslash;</a>
 * @version $Id: EmbeddedLDAPServer.java,v 1.1 2003-11-27 12:09:40 trygvis Exp $
 */
public class EmbeddedLDAPServer {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(EmbeddedLDAPServer.class);
    private ServerThread server;
    private int port;
    
    private class ServerThread extends Thread {
        private LDAPServer server = new LDAPServer();
        
        public void run() {
            try {
                server.run();
            }
            catch(Exception ex) {
                // ignore
            }
        }
        
        public void setDone() {
            server.setDone();
        }
        
        public void setPort(int port) {
            server.setPort(port);
        }
        
        public void initialize() throws Exception {
            server.initialize();
        }
    }
    
    public EmbeddedLDAPServer(int port) {
        this.port = port;
        
        server = new ServerThread();
    }
    
    public void start() throws Exception {
        if(isRunning())
            throw new IllegalStateException("The server must be stopped before it can be starte.");
        
        LOGGER.info("Initalizing server");
        server.setPort(port);
        server.initialize();
        server.setDaemon(true);
        server.start();
    }
    
    public void sendStopSignal() {
        if(!server.isAlive())
            throw new IllegalStateException("The server isn't running.");
        
        server.setDone();
    }

    public boolean isRunning() {
        return server.isAlive();
    }
    
    public void waitForShutdown(long timeout) {
        long start;
        
        start = System.currentTimeMillis();

        while(System.currentTimeMillis() - start < timeout) {
            if(!isRunning())
                return;

            try {
                Thread.sleep(100);
            }
            catch(InterruptedException ex) {
                // ignore
            }
        }
    }
}
