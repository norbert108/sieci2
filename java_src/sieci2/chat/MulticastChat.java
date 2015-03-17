package sieci2.chat;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MulticastChat {

    private InetAddress address;
    private int port;

    private String nick;

    public MulticastChat (String address, int port,  String nick){
        try{
            this.address = InetAddress.getByName(address);
            this.port = port;
            this.nick = nick;
        } catch (UnknownHostException e){
            e.printStackTrace();
        }
    }

    public void run() {
        InputListener inputListener = new InputListener(address, port, nick);
        NetworkListener networkListener = new NetworkListener(address, port);

        (new Thread(inputListener)).start();
        (new Thread(networkListener)).start();
    }
}
