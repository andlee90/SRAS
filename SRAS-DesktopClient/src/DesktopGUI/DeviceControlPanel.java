package DesktopGUI;
import javax.swing.*;
import java.awt.*;


public class DeviceControlPanel
{

    JPanel LabelPanel;
    JPanel ButtonPanel;
    JPanel MainPanel;

    JLabel Label1;
    JLabel Label2;
    JLabel Label3;
    JLabel Label4;
    JLabel Label5;

    JButton Button1;
    JButton Button2;
    JButton Button3;
    JButton Button4;
    JButton Button5;


    public DeviceControlPanel(){
        createButtons();
        createLabels();
        createPanels();
    }

    public void createButtons()
    {
        Button1 = new JButton("OFF");
        Button1.setBackground(Color.RED);


        Button2 = new JButton("OFF");
        Button2.setBackground(Color.RED);


        Button3 = new JButton("OFF");
        Button3.setBackground(Color.RED);


        Button4 = new JButton("OFF");
        Button4.setBackground(Color.RED);


        Button5 = new JButton("OFF");
        Button5.setBackground(Color.RED);


    }

    public void createLabels()
    {

        Label1 = new JLabel("Device 1", JLabel.CENTER);

        Label2 = new JLabel("Device 2", JLabel.CENTER);

        Label3 = new JLabel("Device 3", JLabel.CENTER);

        Label4 = new JLabel("Device 4", JLabel.CENTER);

        Label5 = new JLabel("Device 5", JLabel.CENTER);




    }

    //this is for when we make the client side capable of adapting to new devices
    public JPanel createNewDevicePanel(String device)
    {
        JPanel newDevice = new JPanel();
        newDevice.setSize(350,100);

        JButton deviceButton = new JButton("Off");
        JLabel deviceLabel = new JLabel(device);

        deviceButton.setBounds(200,30,25,10);
        deviceLabel.setBounds(50,30,25,10);

        return newDevice;
    }

    public void createPanels(){

        LabelPanel = new JPanel();
        ButtonPanel = new JPanel();
        MainPanel = new JPanel();
        GridLayout layout = new GridLayout(1,5,100,0);
        GridLayout deviceLayout = new GridLayout(5,1,50,50);
        MainPanel.setSize(350,500);
        MainPanel.setLayout(layout);

        LabelPanel.setSize(175,500);
        ButtonPanel.setSize(175,500);

        MainPanel.add(LabelPanel).setLocation(0,1);
        MainPanel.add(ButtonPanel).setLocation(0,3);

        LabelPanel.setLayout(deviceLayout);
        ButtonPanel.setLayout(deviceLayout);

        ButtonPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        LabelPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        MainPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));


        LabelPanel.add(Label1);
        LabelPanel.add(Label2);
        LabelPanel.add(Label3);
        LabelPanel.add(Label4);
        LabelPanel.add(Label5);

        ButtonPanel.add(Button1);
        ButtonPanel.add(Button2);
        ButtonPanel.add(Button3);
        ButtonPanel.add(Button4);
        ButtonPanel.add(Button5);


        MainPanel.add(ButtonPanel, BorderLayout.EAST);



    }


    public JPanel getPanel(){
        return MainPanel;
    }

}

