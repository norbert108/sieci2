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
            int msgSize;
            long n;

            while (true){
                if(inputStream.available() > 0){
                    msgSize = inputStream.readByte();

                    byte[] byteArray = new byte[msgSize];
                    inputStream.read(byteArray); //error check

                    n = byteArrayToInt(byteArray);

                    System.out.println("Received request for " + n + " digit.");

                    // calculate...

                    // send back result
                    byte result = calculateNthPIDigit(n);
                    outputStream.write(result);
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

    private byte calculateNthPIDigit(long n){
       return 7;
    }
}