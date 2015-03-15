import java.io.*;
import java.net.*;
import java.nio.*;

public class FileServer {

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
            String n;

            while (true){
                if(inputStream.available() > 0){
                    msgSize = inputStream.readByte();

                    byte[] byteArray = new byte[msgSize];
                    inputStream.read(byteArray);

                    String requestedFileName = new String(byteArray);

                    System.out.println("Received request for file \"" + requestedFileName + "\"");
                    sendFile(requestedFileName);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendFile(String fileName) throws IOException{
        // open and read file
        try{
            File file = new File(fileName);


            int fileLength = (int)file.length();
            byte[] fileLengthBy = ByteBuffer.allocate(4).putInt(fileLength).array();

            BufferedInputStream fileBuffer = new BufferedInputStream(new FileInputStream(file));

            byte[] fileBytes = new byte[(int)file.length()];
            fileBuffer.read(fileBytes, 0, fileLength);

            outputStream.write(0); // 0 - OK
            outputStream.write(fileLengthBy); // 4 bytes
            outputStream.write(fileBytes);
        } catch (IOException e){
            System.err.println("File \"" + fileName +"\" not found!");

            outputStream.write(1); // 1 - file not found on server
        }
    }
}