package sample;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;

public class Client {
    String address = "127.0.0.1";
    int port = 12345;
    Socket socket = new Socket(InetAddress.getByName(address), port);
    DataInputStream input = new DataInputStream(socket.getInputStream());
    DataOutputStream output = new DataOutputStream(socket.getOutputStream());

    public Client() throws IOException {
    }

    public  String save(String fileName, String fileServerName) throws IOException {
        if ("".equals(fileServerName) || fileServerName == null){
            fileServerName = fileName;
        }

        output.writeUTF("2");
        output.writeUTF(fileServerName);

        File file = new File("C:\\Users\\timat\\Downloads\\ServerFX\\ServerFX\\src\\sample\\client\\data\\" + fileName);

        byte[] message = Files.readAllBytes(file.toPath());
        output.writeInt(message.length);
        output.write(message);

        int statusCode = input.readInt();
        String response = "";

        if (statusCode == 200){
            int id = input.readInt();
            response = "Response says that file is saved! ID = " + id;
        }
        else if (statusCode == 403){
            response = "The response says that creating the file was forbidden!";
        }

        return response;
    }

    public String get(String fileServerName, String fileName) throws IOException {

        String response = "";

        output.writeUTF("1");
        output.writeInt(1);
        output.writeUTF(fileServerName);

        int statusCode = input.readInt();
        if (statusCode == 200) {
            response = "The file was downloaded!";
            getFile(input, fileName);
        } else if (statusCode == 404) {
            response = "The response says that this file is not found!";
        }

        return response;
    }

    public String get(int fileServerId, String fileName) throws IOException {

        String response = "";

        output.writeUTF("1");
        output.writeInt(2);
        output.writeInt(fileServerId);

        int statusCode = input.readInt();
        if (statusCode == 200) {
            response = "The file was downloaded!";
            getFile(input, fileName);
        } else if (statusCode == 404) {
            response = "The response says that this file is not found!";
        }

        return response;
    }

    private static void getFile(DataInputStream input, String fileName) throws IOException {
        int messageLength;
        byte[] message;
        File file;
        messageLength = input.readInt();
        message = new byte[messageLength];
        input.readFully(message, 0, message.length);

        file = new File("C:\\Users\\timat\\Downloads\\ServerFX\\ServerFX\\src\\sample\\client\\data\\" + fileName);

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(message);
        fos.close();
    }

    public String delete(String fileServerName) throws IOException {

        String response = "";

        output.writeUTF("3");
        output.writeInt(1);
        output.writeUTF(fileServerName);

        int statusCode = input.readInt();
        if (statusCode == 200) {
            response = "The file was deleted!";
        } else if (statusCode == 404) {
            response = "";
        }

        return response;
    }

    public  String delete(int fileServerId) throws IOException {

        String response = "";

        output.writeUTF("3");
        output.writeInt(2);
        output.writeInt(fileServerId);

        int statusCode = input.readInt();
        if (statusCode == 200) {
            response = "The file was deleted!";
        } else if (statusCode == 404) {
            response = "The response says that this file is not found!";
        }

        return response;
    }
}
