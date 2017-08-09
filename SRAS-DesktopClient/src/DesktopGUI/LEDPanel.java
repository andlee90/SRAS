package DesktopGUI;
import CommModels.Command;
import CommModels.Device;
import CommModels.LedState;
import Controller.ClientManager;
import Controller.DesktopClientController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class LEDPanel {
    JPanel LEDPanel;
    JLabel imageLabel;
    boolean isOn;
    LEDPanel(Device device)
    {


        GridLayout grid = new GridLayout(3,1,15,0);
        LEDPanel = new JPanel(grid);
        ImageIcon logo = new ImageIcon("Images/SRAS LOGOmd.png");
        ImageIcon imageOn = new ImageIcon("Images/led_on.png");
        ImageIcon imageOff = new ImageIcon("Images/led_off.png");
        JLabel logoLabel = new JLabel("", logo, JLabel.CENTER);

        LEDPanel.add(logoLabel);
        LEDPanel.setBackground(Color.WHITE);

        //creates an led picture of current LED state.

        if((device.getDeviceState()== LedState.ON)){
            imageLabel = new JLabel("", imageOn, JLabel.CENTER);
            LEDPanel.add(imageLabel);
            isOn=true;
        }
        else if(device.getDeviceState()==LedState.OFF){
            imageLabel = new JLabel("", imageOff, JLabel.CENTER);
            LEDPanel.add(imageLabel);
            isOn=false;
        }
        else
        {
            imageLabel = new JLabel("", imageOn, JLabel.CENTER);
            LEDPanel.add(imageLabel);
            isOn=true;
        }
        FlowLayout flow = new FlowLayout();
        JPanel buttonPanel = new JPanel(flow);
        JButton toggleButton = new JButton("Toggle on/off");
        JButton blinkButton = new JButton("Blink");
        JButton backButton = new JButton("Back");

        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Command command = new Command(Command.CommandType.TOGGLE);
                if(isOn)
                {
                    imageLabel = new JLabel("", imageOff, JLabel.CENTER);

                    isOn=false;
                }
                else
                {
                    imageLabel = new JLabel("", imageOn, JLabel.CENTER);
                    isOn=true;
                }
                LEDPanel.revalidate();
                LEDPanel.repaint();
                try {
                    ClientManager.sendCommand(device,command);
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }

            }
        });

        blinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Command command = new Command(Command.CommandType.BLINK);
                try {
                    ClientManager.sendCommand(device,command);
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
                DesktopClientController.replacePanel(new LEDPanel(device).getLEDPanel(), "SRAS - " + device.getDeviceName());
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientManager.loseControl(device);
                ClientManager.updateDevices();
                DesktopClientController.replacePanel(new DeviceControlPanel().getPanel(),"SRAS - Device List");
            }
        });


        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(toggleButton);
        buttonPanel.add(blinkButton);
        buttonPanel.add(backButton);



        LEDPanel.add(buttonPanel);
    }
    public JPanel getLEDPanel(){
        return LEDPanel;
    }
}
