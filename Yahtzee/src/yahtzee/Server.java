package yahtzee;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author enesb
 */
public class Server {
    
    public ServerSocket socket;
    public int port;
    
    public Server(int port){
        try {
            this.port = port;
            this.socket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Listen(){
        
        try {
            Socket nClient  = this.socket.accept();
            ObjectInputStream cInput = new ObjectInputStream(nClient.getInputStream());
            ObjectOutputStream cOutput = new ObjectOutputStream(nClient.getOutputStream());
            
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
