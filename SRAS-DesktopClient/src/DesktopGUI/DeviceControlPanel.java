package DesktopGUI;
import CommModels.Command;
import CommModels.Device;
import CommModels.Devices;
import CommModels.Led;
import Controller.ClientManager;
import Controller.DesktopClientController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class DeviceControlPanel
{
    JPanel mainDevicePanel;
    ArrayList<Device> devices  = DesktopClientController.devices.getDevices();
    public DeviceControlPanel()
    {
        createMainPanel();
    }
    public void createMainPanel()
    {
        GridLayout grid = new GridLayout(DesktopClientController.devices.getDevices().size()+1,1,0,0);
        mainDevicePanel = new JPanel(grid);
        mainDevicePanel.setBackground(Color.WHITE);
        mainDevicePanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        for(int cntr = 0; cntr < DesktopClientController.devices.getDevices().size(); cntr++)
            mainDevicePanel.add(createDevicePanel((devices.get(cntr).getDeviceName()),devices.get(cntr)));
        mainDevicePanel.add(controlButtonsPanel());
    }

    public JPanel getPanel()
    {
        return mainDevicePanel;
    }
    public JPanel controlButtonsPanel()
    {
        FlowLayout f1 = new FlowLayout(10,45,20);
        JPanel buttonPanel = new JPanel(f1);
        JButton statusButton = new JButton("Status");
        JButton disconnectButton = new JButton("Disconnect");
        disconnectButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
              DesktopClientController.replacePanel(new ServerListPanel().getServerListPanel(),"SRAS - Server List");
                try {
                    ClientManager.socket.close();
                } catch (IOException e1) {
                    new MainErrorMessageFrame(e1.getLocalizedMessage());
                }
            }});

        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        buttonPanel.add(disconnectButton);
        buttonPanel.add(statusButton);
        return buttonPanel;
    }


    public JPanel createDevicePanel(String deviceName, Device device)
    {

        FlowLayout f1 = new FlowLayout(10,70,20);
        JPanel devicePanel = new JPanel(f1);
        devicePanel.setBackground(Color.WHITE);
        devicePanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        JButton deviceButton = new JButton("Control");



        JLabel deviceLabel = new JLabel(deviceName);
        devicePanel.add(deviceLabel);
        devicePanel.add(deviceButton);
        deviceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DesktopClientController.replacePanel(new LEDPanel(device).getLEDPanel(),"SRAS - "+device.getDeviceName());

            }
        });
        return devicePanel;
    }
}
