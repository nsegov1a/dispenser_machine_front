package model;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.io.InputStream;

public class SSHSession {
    
    private final int port;
    private final String hostIP;
    private final String username;
    private final String password;
    private static JSch jsch;
    private static Session jschSession;
    private static ChannelExec channelExec;
    private static SSHSession instancia;
    private static int TIMEOUT = 10000;
    private static int CHANNEL_TIMEOUT = 30000;

    private SSHSession(String hostIP, String username, String password) throws Exception{
        this.hostIP = hostIP;
        this.username = username;
        this.password = password;
        this.port = 22;
        this.connect();
    }
    
     private SSHSession(String hostIP, String username, String password, int port) throws Exception {
        this.hostIP = hostIP;
        this.username = username;
        this.password = password;
        this.port = port;
        this.connect();
    }
     
    private void connect() throws Exception {
        jsch = new JSch();
        jsch.setKnownHosts("~/.ssh/known_hosts");
        jschSession = jsch.getSession(this.username, this.hostIP, this.port);
        jschSession.setPassword(this.password);
        jschSession.connect(TIMEOUT);
        System.out.println("CONECTADO!!!");
    }
    
    public static SSHSession get(String hostIP, String username, String password) throws Exception {
        if (instancia == null || !jschSession.isConnected()) {
            instancia = new SSHSession(hostIP, username, password);
        }
        return instancia;
    }
    
    public static SSHSession get(String hostIP, String username, String password, int port) throws Exception {
        if (instancia == null || !jschSession.isConnected()) {
            instancia = new SSHSession(hostIP, username, password, port);
        }
        return instancia;
    }

    public static String issueCommand(String command) throws IOException, JSchException, InterruptedException {
        String retorno = "";
        channelExec = (ChannelExec) jschSession.openChannel("exec");
        channelExec.setCommand(command);
        channelExec.setErrStream(System.err);
        InputStream in = channelExec.getInputStream();
        channelExec.connect(CHANNEL_TIMEOUT);
        byte[] temp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(temp, 0, 1024);
                if (i < 0) break;
                retorno += new String(temp, 0, i);
            }
            if (channelExec.isClosed()) {
                if (in.available() > 0) continue;
                break;
            }
            Thread.sleep(100);
        }
        channelExec.disconnect();
        return retorno;
    }
    
    public String getHostIP() {
        return hostIP;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public static boolean isConnected() {
        return jschSession.isConnected();
    }
    
    public static boolean isInitialized() {
        return instancia != null;
    }
    
    public static void close() {
        jschSession.disconnect();
    }
}
