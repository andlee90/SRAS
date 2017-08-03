package Controller;
import javax.swing.*;
import DesktopGUI.*;
import CommModels.*;

public class DesktopClientController {
    static MainFrame frame1;
    public final static int maxServers = 20;
    public static int currentServer;
    public static Object[][] data = new Object[maxServers][2];
    public static int currentRow = 0;
    public static User userIn;
    public static Devices devices;
    public static void main(String [] args)
    {
        devices = new Devices();
        new AuthenticationPanel();
        data[0][0] = "localhost";
        data[0][1]="50873";
        data[1][0] = "192.168.1.3";
        data[1][1]="50873";
        currentRow=2;
        frame1 = new MainFrame(new ServerListPanel().getServerListPanel());
        frame1.renameFrame("SRAS - Server List");
    }


    public static void replacePanel(JPanel panelInput, String title)
    {
        frame1.replacePanel(panelInput,title);
    }




}
