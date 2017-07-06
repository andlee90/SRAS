package DesktopGUI;

import CommModels.*;
import Controller.DesktopClientController;
import com.sun.security.ntlm.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.*;

public class AddServerPanel
{

    JLabel imageLabel;
    JPanel picturePanel;

    JPanel AddServerPanel;
    JPanel GridAddServer;
    JButton AddServerButton;
    JButton AuthenticationCancel;

    JLabel HostnameLabel;
    JLabel PortNumberLabel;

    JTextField hostName;
    JTextField portNumber;



    public AddServerPanel()
    {
        GridLayout layout = new GridLayout(2,1,100,0);
        GridAddServer = new JPanel();

        AddServerPanel = new JPanel();
        AddServerPanel.setSize(350,350);
        AddServerPanel.setLayout(null);
        AddServerPanel.setBackground(Color.WHITE);

        AddServerPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        createAuthenticationButtons();
        createTextFields();
        createJLabels();
        createPicturePanel();

        AddServerPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));


        GridAddServer.setLayout(layout);

        GridAddServer.add(AddServerPanel);
        GridAddServer.add(picturePanel);
    }

    public void createPicturePanel()
    {
        ImageIcon image = new ImageIcon("Images/pi_logo.png");

        imageLabel = new JLabel("", image, JLabel.CENTER);
        picturePanel = new JPanel(new BorderLayout());
        picturePanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        picturePanel.setBackground(Color.WHITE);
        picturePanel.add( imageLabel, BorderLayout.CENTER );
    }


    public JPanel getAddServerPanel()
    {
        return GridAddServer;
    }

    public void createTextFields()
    {

        hostName = new JTextField();
        hostName.setBounds(100,25,150,30);
        AddServerPanel.add(hostName);



        portNumber = new JTextField();
        portNumber.setBounds(100,75,150,30);
        AddServerPanel.add(portNumber);

    }

    public void createJLabels()
    {
        HostnameLabel = new JLabel("Host Name: ");
        HostnameLabel.setBounds(100,5,150,20);
        HostnameLabel.setVisible(true);

        PortNumberLabel = new JLabel("Port Number");
        PortNumberLabel.setBounds(100,55,150,20);
        PortNumberLabel.setVisible(true);

        AddServerPanel.add(HostnameLabel);
        AddServerPanel.add(PortNumberLabel);
    }

    public void createAuthenticationButtons()
    {

        Message messageBack = new Message("");
        AddServerButton = new JButton("Add Server");
        AddServerButton.setBounds(50,150,100,30);
        AddServerButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                DesktopClientController.data[DesktopClientController.currentRow][0]=hostName.getText();
                DesktopClientController.data[DesktopClientController.currentRow][1] = portNumber.getText();
                DesktopClientController.currentRow++;
                DesktopClientController.replacePanel(new ServerListPanel().getServerListPanel(), "SRAS - Server List");
            }
        });



        AuthenticationCancel= new JButton("Cancel");
        AuthenticationCancel.setBounds(200,150,100,30);
        AuthenticationCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DesktopClientController.replacePanel(new ServerListPanel().getServerListPanel(), "SRAS - Server List");
            }
        });

        AddServerPanel.add(AddServerButton);
        AddServerPanel.add(AuthenticationCancel);

    }

}
