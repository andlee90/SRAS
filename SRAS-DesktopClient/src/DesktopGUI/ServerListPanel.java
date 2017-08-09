package DesktopGUI;
import CommModels.*;
import Controller.ClientManager;
import Controller.DesktopClientController;
import sun.security.krb5.internal.crypto.Des;

import java.io.*;
import java.net.Socket;
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

    private JButton removeButton;
    private JButton connectButton;
    private JButton addServerButton;
    private static Socket socket;
    private static JTable table;

    FlowLayout flow = new FlowLayout(10,22,10);
    GridLayout grid = new GridLayout(2,1,25,25);
    GridLayout grid2 = new GridLayout(1,1,25,25);


    public ServerListPanel()
    {
        ImageIcon image = new ImageIcon("Images/SRAS LOGOmd.png");
        JLabel imageLabel;
        imageLabel = new JLabel("", image, JLabel.CENTER);

        mainListPanel = new JPanel(grid);
        serverListButtonPanel = new JPanel(flow);
        serverListPanel = new JPanel(grid2);

        DefaultTableModel model;
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
        removeButton = new JButton("Remove");
        connectButton = new JButton("Connect");
        addServerButton = new JButton("   Add   ");

        removeButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
              try {
                    for(int i = table.getSelectedRow();i<DesktopClientController.data.length-1;i++)
                    {
                        DesktopClientController.data[i][0] = DesktopClientController.data[i+1][0];
                        DesktopClientController.data[i][1] = DesktopClientController.data[i+1][1];
                    }
                  DesktopClientController.data[DesktopClientController.maxServers-1][0]="";
                  DesktopClientController.data[DesktopClientController.maxServers-1][1]="";
                  DesktopClientController.currentRow--;

                  DesktopClientController.replacePanel(new ServerListPanel().getServerListPanel(),"SRAS - Server List");
                }
                catch(Exception e1)
                {
                   new MainErrorMessageFrame("Please select a valid server to remove from the list.");
                }
            }});

        connectButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {

                try {
                    ClientManager.createSocket(table.getSelectedRow());

                } catch (Exception e1) {
                    new MainErrorMessageFrame("Please select a valid Server from the list.");
                }


            }});

           addServerButton.addActionListener(new ActionListener()
           {
               @Override public void actionPerformed(ActionEvent e)
               {
                   DesktopClientController.replacePanel(new AddServerPanel().getAddServerPanel(),"SRAS - Add Server");
               }
           });

        serverListButtonPanel.add(connectButton);
        serverListButtonPanel.add(addServerButton);
        serverListButtonPanel.add(removeButton);


    }

}
