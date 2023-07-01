package model.network;

import com.google.gson.Gson;
import model.App;
import model.User;
import view.shape.profile.RankScroll;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ClientTest {
    static DataOutputStream dataOutputStream;
    static DataInputStream dataInputStream;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Socket socket = connect(scanner);
        dataOutputStream = new DataOutputStream (socket.getOutputStream());
        dataInputStream = new DataInputStream (socket.getInputStream());

        HashMap<String,String> data = new HashMap<>();
        data.put("menu","profile");
        data.put("command","getAllUsers");
        String dataStr = new Gson().toJson(data);
        try {
            dataOutputStream.writeUTF(dataStr);
            dataStr = dataInputStream.readUTF();
            ArrayList<User> users = new ArrayList<>();
            while (!dataStr.equals("EOF")){
                User user = new Gson().fromJson(dataStr, User.class);
                users.add(user);
                dataStr = dataInputStream.readUTF();
            }


            for (Object user : users) {
                System.out.println(user.toString());
            }
        } catch (Exception e){

        }
    }

    private static Socket connect(Scanner scanner) {
        Socket socket;
        try {
            socket = new Socket("localhost",8000);
        } catch (ConnectException e){
            System.out.println("no server found enter something to reconnect");

            while (!scanner.nextLine().equals("reconnect")) System.out.println("no");
            socket = connect(scanner);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  socket;
    }
}
