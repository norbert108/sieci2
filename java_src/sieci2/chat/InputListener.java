package sieci2.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InputListener implements Runnable{

    private BufferedReader bufferedIO;

    private InetAddress address;
    private int port;
    private String nick;

    public InputListener(InetAddress address, int port, String nick){
        this.bufferedIO = new BufferedReader(new InputStreamReader(System.in));

        this.address = address;
        this.port = port;
        this.nick = nick.substring(0, 6); // nicks longer than 6 characters are truncated
    }

    @Override
    public void run() {
        try{
            DatagramSocket serverSocket = new DatagramSocket();

            while (true){
                String message = bufferedIO.readLine();

                // truncate message to 20 bytes
                message = String.format("%1$20s", message);
                message = message.substring(0, 20);

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                String time = timeFormat.format(new Date());

                // build useful part of message
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(nick);
                stringBuilder.append(message);
                stringBuilder.append(time);
                stringBuilder.append(calculateChecksum(stringBuilder.toString()));
                byte[] messageBytes = stringBuilder.toString().getBytes();

                DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, address, port);
                serverSocket.send(packet);
            }
        } catch (IOException  e){
            e.printStackTrace();
        }
    }

    String calculateChecksum(String messageData){
//        int checksum[] = new int[10];
//
//        for(int i = 0; i < messageData.length(); i++){
//            checksum[i % 10] += checksum[i % 10]/messageData.charAt(i);
//        }

//        return Arrays.toString(checksum);
        return "1234567890";
    }
}
