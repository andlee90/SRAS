package DesktopGUI;
import CommModels.Device;
import CommModels.Devices;
import Controller.DesktopClientController;

import javax.swing.*;
import java.awt.*;
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
            mainDevicePanel.add(createDevicePanel(devices.get(cntr).getDeviceName()));

    }

    public JPanel getPanel()
    {
        return mainDevicePanel;
    }



    public JPanel createDevicePanel(String deviceName)
    {
        FlowLayout f1 = new FlowLayout(10,70,10);
        JPanel devicePanel = new JPanel(f1);
        devicePanel.setBackground(Color.WHITE);
        devicePanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        JButton deviceButton = new JButton("Control");
        JLabel deviceLabel = new JLabel(deviceName);
        devicePanel.add(deviceLabel);
        devicePanel.add(deviceButton);
        return devicePanel;
    }
}
