package DesktopGUI;
import CommModels.Command;
import CommModels.Device;
import CommModels.Devices;
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
    public DeviceControlPanel()
    {
        createMainPanel();
    }
    ArrayList<Device> devices  = DesktopClientController.devices.getDevices();
    public void createMainPanel()
    {
        GridLayout grid = new GridLayout(5,1,10,0);
        mainDevicePanel = new JPanel(grid);
        mainDevicePanel.setBackground(Color.WHITE);
        mainDevicePanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        for(int cntr = 0; cntr < devices.size() && cntr < 4; cntr++)
            mainDevicePanel.add(createDevicePanel(devices.get(cntr).getDeviceName(),devices.get(cntr)));

    }

    public JPanel getPanel()
    {
        return mainDevicePanel;
    }



    public JPanel createDevicePanel(String deviceName, Device device)
    {

        FlowLayout f1 = new FlowLayout(10,70,10);
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
                Command command = new Command(Command.CommandType.TOGGLE);
                try {
                    ClientManager.sendCommand(device,command);
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
        return devicePanel;
    }
}
