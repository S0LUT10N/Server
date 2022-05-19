package sample.server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Scanner;

public class Server {

   final Scanner sc = new Scanner(new File("C:\\Users\\timat\\Downloads\\ServerFX\\ServerFX\\src\\sample\\server\\ids.txt"));
    String address = "127.0.0.1";
    int port = 12345;
    FileOutputStream fos = new FileOutputStream("C:\\Users\\timat\\Downloads\\ServerFX\\ServerFX\\src\\sample\\server\\ids.txt");
    String filename = "C:\\Users\\timat\\Downloads\\ServerFX\\ServerFX\\src\\sample\\server\\data\\";
    public Server() throws FileNotFoundException {
    }

    public void getIds(HashMap<Integer, String> ids) throws FileNotFoundException {
        String idWithFile;
        while (sc.hasNextLine()){
            idWithFile = sc.nextLine();
            if (idWithFile.indexOf('.') != -1) {
                ids.put(Integer.parseInt(idWithFile.substring(0, idWithFile.indexOf('.'))), idWithFile.substring(idWithFile.indexOf('.') + 1));
            }
        }
    }

    public void saveIds(HashMap<Integer, String> ids) throws IOException {

        for (int i: ids.keySet()) {
            byte[] b = (i + "." + ids.get(i) + "\n").getBytes();
            fos.write(b);
        }
        fos.close();
    }

    public static int saveId(String fileName, HashMap<Integer, String> ids) throws IOException {
        int id = 0;
        while (ids.containsKey(id)){
            id++;
        }
        ids.put(id, fileName);
        return id;
    }

    public static void main(String[] args) throws IOException {
        Server serverOK = new Server();

        ServerSocket server = new ServerSocket(serverOK.port, 50, InetAddress.getByName(serverOK.address));

        HashMap<Integer, String> ids = new HashMap<Integer, String>();
        serverOK.getIds(ids);

        System.out.println("Server started!");


        boolean exit = false;
        String action;
        String fileName, fileServerName;
        int messageLength, id, nameOrId, fileId;
        byte[] message;

        while (!exit){
            try (Socket socket = server.accept();
                 DataInputStream input = new DataInputStream(socket.getInputStream());
                 DataOutputStream output  = new DataOutputStream(socket.getOutputStream())) {

                action = input.readUTF();
                System.out.print(action + ": ");
                if ("2".equals(action)) {
                    System.out.println("Saving action.");
                    fileServerName = input.readUTF();
                    messageLength = input.readInt();
                    message = new byte[messageLength];
                    input.readFully(message, 0, messageLength);

                    File file = new File(serverOK.filename + fileServerName);

                    if (file.exists()){
                        file.delete();
                    }

                    if (!file.createNewFile()) {
                        output.writeInt(403);
                    } else {
                        output.writeInt(200);

                        FileOutputStream fos = new FileOutputStream(file.toPath().toString());
                        fos.write(message);
                        fos.close();

                        try {Thread.sleep(1000);} catch (InterruptedException ignored) {}

                        id = saveId(fileServerName, ids);

                        output.writeInt(id);
                    }
                }

                else if ("1".equals(action)){
                    System.out.println("Getting action.");
                    nameOrId = input.readInt();

                    if (nameOrId == 1) {
                        fileName = input.readUTF();
                    }
                    else {
                        fileId = input.readInt();
                        fileName = ids.get(fileId);
                    }

                    System.out.println("FileName: C:\\Users\\timat\\Downloads\\ServerFX\\ServerFX\\src\\sample\\server\\data\\" + fileName);
                    File file = new File(serverOK.filename + fileName);
                    if (file.exists()) {
                        output.writeInt(200);

                        message = Files.readAllBytes(file.toPath());
                        output.writeInt(message.length);
                        output.write(message);
                    } else {
                        output.writeInt(404);
                    }
                }
                else if ("3".equals(action)){
                    System.out.println("Deletion Action");
                    nameOrId = input.readInt();

                    fileId = -1;
                    if (nameOrId == 1) {
                        fileName = input.readUTF();
                        for (int i: ids.keySet()){
                            if (ids.get(i).equals(fileName)) fileId = i;
                        }
                    }
                    else {
                        fileId = input.readInt();
                        fileName = ids.get(fileId);
                    }

                    System.out.println("FileName: C:\\Users\\timat\\Downloads\\ServerFX\\ServerFX\\src\\sample\\server\\data\\" + fileName);
                    File file = new File(serverOK.filename + fileName);

                    if (file.exists() && file.delete() && fileId != -1){
                        ids.remove(fileId);
                        output.writeInt(200);
                    }
                    else {
                        output.writeInt(404);
                    }
                }
                if ("exit".equals(action)){
                    output.writeInt(200);
                    exit = true;
                }
            }
        }

        serverOK.saveIds(ids);
        server.close();
    }
}

