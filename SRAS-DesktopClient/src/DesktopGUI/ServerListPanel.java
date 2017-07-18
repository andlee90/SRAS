package DesktopGUI;
import CommModels.*;
import Controller.DesktopClientController;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerListPanel
{
    private JPanel serverListPanel;
    private JPanel serverListButtonPanel;
    private JPanel mainListPanel;

    private JButton cancelButton;
    private JButton connectButton;
    private JButton addServerButton;

    private JTable table;

    FlowLayout flow = new FlowLayout(10,22,10);
    GridLayout grid = new GridLayout(2,1,25,25);
    GridLayout grid2 = new GridLayout(1,1,25,25);


    ServerListPanel()
    {
        ImageIcon image = new ImageIcon("Images/pi_logo2.png");
        JLabel imageLabel;
        imageLabel = new JLabel("", image, JLabel.CENTER);

        mainListPanel = new JPanel(grid);
        serverListButtonPanel = new JPanel(flow);
        serverListPanel = new JPanel(grid2);


        serverListPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        serverListButtonPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        mainListPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        serverListButtonPanel.setBackground(Color.WHITE);
        serverListPanel.setBackground(Color.WHITE);
        mainListPanel.setBackground(Color.WHITE);



        mainListPanel.add(serverListPanel);
        mainListPanel.add(serverListButtonPanel);

        createJTable();
        createButtons();
        serverListButtonPanel.add(imageLabel);
        serverListPanel.setVisible(true);
        mainListPanel.setVisible(true);
        serverListButtonPanel.setVisible(true);
    }

    public JPanel getServerListPanel()
    {
        return mainListPanel;
    }

    public void createJTable()
    {
        String[] columnNames = {"SERVER NAME: ", "PORT NUMBER: "};

        DefaultTableModel model = new DefaultTableModel(DesktopClientController.data, columnNames);
        table = new JTable( model )
        {
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
        };
        table.setSize(300,300);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setFillsViewportHeight(true);
        JScrollPane tableSP = new JScrollPane(table);
        serverListPanel.add(tableSP);
    }

    public void createButtons()
    {
        cancelButton = new JButton("Cancel");
        connectButton = new JButton("Connect");
        addServerButton = new JButton("Add Server");

        cancelButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                DesktopClientController.replacePanel(new AuthenticationPanel().getAuthenticationPanel(),"SRAS - Login");
            }});

        connectButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                System.out.println("The selected row is: "+table.getSelectedRow());
                String ip = DesktopClientController.data[table.getSelectedRow()][0].toString();
                int port = Integer.parseInt(DesktopClientController.data[table.getSelectedRow()][1].toString());
                System.out.println(ip+ " "+ port);
                try{
                    Socket socket = new Socket(ip,port);
                    System.out.println("Socket created");
                    socket.setSoTimeout(5000);
                    Message message = new Message("Hello there");
                    try {


                        System.out.println("Output stream...");
                        ObjectOutputStream clientOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        System.out.println("Sending user....");
                        clientOutputStream.writeObject(DesktopClientController.userIn);
                        System.out.println("user sent");
                        System.out.println("Input stream...");
                        ObjectInputStream clientInputStream = new ObjectInputStream(socket.getInputStream());
                        DesktopClientController.userIn = (User) clientInputStream.readObject();
                        System.out.println("user sent");
                        clientOutputStream.writeObject(message);
                        System.out.println("message sent");
                        message = (Message) clientInputStream.readObject();
                        DesktopClientController.devices = (Devices) clientInputStream.readObject();

                    }catch (ClassNotFoundException e2) {
                        e2.printStackTrace();
                        System.out.println("This aint workin'");
                    }
                    socket.close();
                    DesktopClientController.replacePanel(new DeviceControlPanel().getPanel(), "SRAS - Device Control Panel");
                }catch (UnknownHostException exception)
                {
                    exception.printStackTrace();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }



            }});

       addServerButton.addActionListener(new ActionListener() {
           @Override public void actionPerformed(ActionEvent e) {
               DesktopClientController.replacePanel(new AddServerPanel().getAddServerPanel(),"SRAS - Add Server");
           }});

        serverListButtonPanel.add(connectButton);
        serverListButtonPanel.add(addServerButton);
        serverListButtonPanel.add(cancelButton);


    }
    private static void initialize(Socket socket ) throws IOException{
        //OutputStream os = socket.getObjectOutputStream();

    }
}
