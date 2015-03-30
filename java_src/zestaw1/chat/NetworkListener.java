package zestaw1.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;

public class NetworkListener implements Runnable{

    private InetAddress address;
    private int port;

    private DatagramPacket packet;

    public NetworkListener(InetAddress address, int port){
        this.address = address;
        this.port = port;

        byte[] datagramBuffer = new byte[44];
        packet = new DatagramPacket(datagramBuffer, 44);
    }

    @Override
    public void run() {
        try{
            MulticastSocket clientSocket = new MulticastSocket(port);
            clientSocket.joinGroup(address);

            while(true){
                clientSocket.receive(packet);

                byte[] receivedData = packet.getData();

                // slice received message
                String nick, message, time, checksum;
                nick = new String(Arrays.copyOfRange(receivedData, 0, 7));
                message = new String(Arrays.copyOfRange(receivedData, 7, 27));
                time = new String(Arrays.copyOfRange(receivedData, 27, 35));
                checksum = new String(Arrays.copyOfRange(receivedData, 35, 44)); // checksum is 10 bytes

                System.out.println("nick: " + nick + "message" + message + " time: " + time + "checksum" + checksum);
                }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
