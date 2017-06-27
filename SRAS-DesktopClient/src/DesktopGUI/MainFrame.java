package DesktopGUI;
import javax.swing.*;
import java.awt.*;

//This is to contruct the frame that every panel will be swapped out onto.
public class MainFrame {

    JFrame MainFrame;

    public MainFrame()
    {
        MainFrame = new JFrame();
        MainFrame.setResizable(false);
        MainFrame.setSize(350,500);
        MainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        MainFrame.setLocationByPlatform(true);
        MainFrame.setVisible(true);
    }

    public MainFrame(JPanel CurrentPanel)
    {
        MainFrame = new JFrame();
        MainFrame.setResizable(false);
        MainFrame.setSize(350,500);
        MainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        MainFrame.add(CurrentPanel);
        MainFrame.setVisible(true);
    }

    public void renameFrame(String name)
    {
        MainFrame.setTitle(name);
    }

    public void replacePanel(JPanel panelInput)
    {
        MainFrame.getContentPane().removeAll();
        MainFrame.add(panelInput);
        panelInput.setVisible(true);
        MainFrame.getContentPane().repaint();
        MainFrame.setSize(351,500);
        MainFrame.setSize(350,500);
    }
}
