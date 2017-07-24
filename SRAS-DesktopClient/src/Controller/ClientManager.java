package Controller;

import CommModels.*;
import DesktopGUI.AuthenticationPanel;
import DesktopGUI.DeviceControlPanel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Tim on 7/23/2017.
 */

public class ClientManager {

    public static Socket socket;
    public static ObjectOutputStream clientOutputStream;
    public static ObjectInputStream clientInputStream;
    public static void createSocket(int row)throws IOException{

        String ip = DesktopClientController.data[row][0].toString();
        int port = Integer.parseInt(DesktopClientController.data[row][1].toString());
        System.out.println("You are connecting to: "+ip+ " on port "+ port+"...");
        try {
            socket = new Socket(ip, port);
            socket.setSoTimeout(5000);
            DesktopClientController.replacePanel(new AuthenticationPanel().getAuthenticationPanel(), "SRAS - Authentication");
        }catch (IOException e1)
        {
            System.out.println("DID NOT PASS CREATE SOCKET METHOD");
            e1.printStackTrace();
        }
    }

    public static void connectToServer() throws IOException {
        try {

            Message message = new Message("Hello there");
            clientOutputStream = new ObjectOutputStream(socket.getOutputStream());

            clientOutputStream.writeObject(DesktopClientController.userIn);
            clientInputStream = new ObjectInputStream(socket.getInputStream());
            DesktopClientController.userIn = (User) clientInputStream.readObject();
            if (DesktopClientController.userIn.getValidity()) {

                clientOutputStream.writeObject(message);
                DesktopClientController.devices = (Devices) clientInputStream.readObject();
                clientOutputStream.writeObject(DesktopClientController.devices.getDevices().get(0));
                message = (Message) clientInputStream.readObject();
                System.out.println(message.getMessage());

                DesktopClientController.replacePanel(new DeviceControlPanel().getPanel(),"SRAS - Device Controller");
            }
        }
        catch(ClassNotFoundException e2){
            e2.printStackTrace();
            System.out.println("This aint workin'");
        }

    }

    public static void sendCommand(Device device, Command command) throws IOException, ClassNotFoundException {

        clientOutputStream.writeObject(command);
        Message message = (Message)clientInputStream.readObject();
        System.out.println(message.getMessage());
    }
}
