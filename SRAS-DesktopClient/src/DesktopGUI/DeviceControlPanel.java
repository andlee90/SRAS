package DesktopGUI;
import javax.swing.*;
import java.awt.*;


public class MainFrame
{
    JFrame mainFrame;
    JPanel LabelPanel;
    JPanel ButtonPanel;
    JPanel MainPanel;
    JLabel Label1;
    JButton Button1;

    public MainFrame(){
        createButtons();
        createLabel();
        createPanels();
        createMainFrame();
    }

    public void createButtons()
    {
        Button1 = new JButton("OFF");
        Button1.setBackground(Color.RED);
    }

    public void createLabel()
    {
        Label1 = new JLabel("Device 1");
    }

    public void createPanels(){

        LabelPanel = new JPanel();
        ButtonPanel = new JPanel();
        MainPanel = new JPanel();

        MainPanel.setSize(300,500);
        LabelPanel.setSize(175,500);
        ButtonPanel.setSize(175,500);
        MainPanel.add(LabelPanel, BorderLayout.WEST);
        MainPanel.add(ButtonPanel, BorderLayout.EAST);

        LabelPanel.add(Label1);

        ButtonPanel.add(Button1);

    }
    public void createMainFrame()
    {
        mainFrame=new JFrame();
        mainFrame.setSize(350,500);
        mainFrame.add(LabelPanel);
        mainFrame.add(ButtonPanel);
        mainFrame.setVisible(true);
    }

    public JPanel getpanel(){
        return MainPanel;
    }

}

