import java.io.*;
import java.net.*;

public class PIGenServer {

    private ServerSocket serverSocket;
    private DataInputStream inputStream;

    private Socket clientSocket;
    private DataOutputStream outputStream;

    public void run() {
        try{
            serverSocket = new ServerSocket(9999);

            clientSocket = serverSocket.accept();
            inputStream = new DataInputStream((clientSocket.getInputStream()));
            outputStream = new DataOutputStream(clientSocket.getOutputStream());

            // start receiving messages from client
            int n = 0, msgSize;
            while (true){
                if(inputStream.available() > 0){
                    msgSize = inputStream.readByte();

                    byte[] byteArray = new byte[msgSize];
                    inputStream.read(byteArray); //error check

                    System.out.println(byteArrayToInt(byteArray));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long byteArrayToInt(byte[] bytes){
        long value = 0;
        for (int i = 0; i < bytes.length; i++)
        {
            value += ((long) bytes[i] & 0xffL) << (8 * i);
        }

        return value;
    }
}