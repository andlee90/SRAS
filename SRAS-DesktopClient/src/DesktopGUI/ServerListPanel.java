package DesktopGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Created by Tim on 7/5/2017.
 */
public class ServerListPanel
{
    JPanel serverListPanel;
    JPanel serverListButtonPanel;
    JPanel mainListPanel;

    JButton cancelButton;
    JButton connectButton;
    JButton addServerButton;

    FlowLayout flow = new FlowLayout(10,22,10);
    GridLayout grid = new GridLayout(2,1,25,25);
    GridLayout grid2 = new GridLayout(1,1,25,25);
    public ServerListPanel()
    {
        ImageIcon image = new ImageIcon("Images/pi_logo2.png");
        JLabel imageLabel = new JLabel("", image, JLabel.CENTER);

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
        Object[][] data =
                {
                        {"SERVER", "PORT"},
                        {"SERVER", "PORT"},
                        {"SERVER", "PORT"},
                        {"SERVER", "PORT"},
                        {"SERVER", "PORT"},
                        {"SERVER", "PORT"},
                        {"SERVER", "PORT"},
                        {"SERVER", "PORT"},
                        {"SERVER", "PORT"},

                };
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable( model )
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
        serverListButtonPanel.add(connectButton);
        serverListButtonPanel.add(addServerButton);
        serverListButtonPanel.add(cancelButton);


    }
}
