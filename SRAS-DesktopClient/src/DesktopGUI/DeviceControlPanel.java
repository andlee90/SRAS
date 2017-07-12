package DesktopGUI;
import CommModels.Devices;

import javax.swing.*;
import java.awt.*;

public class DeviceControlPanel
{
    JPanel mainDevicePanel;
    Devices.Device device;
    public DeviceControlPanel()
    {
        createMainPanel();
    }

    public void createMainPanel()
    {
        GridLayout grid = new GridLayout(5,1,10,5);
        mainDevicePanel = new JPanel(grid);
        mainDevicePanel.setBackground(Color.WHITE);
        mainDevicePanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        mainDevicePanel.add(createDevicePanel("Device 1"));
        mainDevicePanel.add(createDevicePanel("Device 2"));
        mainDevicePanel.add(createDevicePanel("Device 3"));
        mainDevicePanel.add(createDevicePanel("Device 4"));
        mainDevicePanel.add(createDevicePanel("Device 5"));
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
