package Controller;
import javax.swing.*;
import DesktopGUI.*;
/**
 * Created by Tim on 6/21/2017.
 */
public class DesktopClientController {
    static MainFrame frame1;
    static JPanel DevicePanel;
    public static void main(String [] args)
    {
        new AuthenticationPanel();
        frame1 = new MainFrame(new AuthenticationPanel().getAuthenticationPanel());
        frame1.renameFrame("SRAS Server List");
    }

    public static void replacePanel(JPanel panelInput){
        frame1.replacePanel(panelInput);
    }
    public static boolean isValidUser(){
        boolean isValid = true;
        if (isValid){
            return true;
        }
        else{
            System.out.println("INVALID USERNAME OR PASSWORD!");
            return false;
        }
    }


}
