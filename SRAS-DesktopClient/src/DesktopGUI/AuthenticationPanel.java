package DesktopGUI;

import Controller.DesktopClientController;
import DesktopGUI.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.*;
public class AuthenticationPanel
{
    //JFrame AuthenticationFrame;

    JPanel AuthenticationPanel;

    JButton AuthenticationLogin;
    JButton AuthenticationCancel;

    JLabel usernameLabel;
    JLabel passwordLabel;

    JTextField username;
    JTextField password;


    public AuthenticationPanel()
    {
        AuthenticationPanel = new JPanel();
        AuthenticationPanel.setSize(350,350);
        AuthenticationPanel.setLayout(null);
        createAuthenticationButtons();
        createTextFields();
    }



    public JPanel getAuthenticationPanel()
    {
        return AuthenticationPanel;

    }

    public void createTextFields()
    {
        username = new JTextField();
        username.setBounds(100,25,150,30);
        AuthenticationPanel.add(username);

        password = new JTextField();
        password.setBounds(100,75,150,30);
        AuthenticationPanel.add(password);

    }

    public void createJlabels()
    {
        usernameLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");
    }

    public void createAuthenticationButtons()
    {
        AuthenticationLogin = new JButton("Login");
        AuthenticationLogin.setBounds(50,150,100,30);
        AuthenticationLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(DesktopClientController.isValidUser()){
                    DesktopClientController.replacePanel(new DeviceControlPanel().getPanel());
                }
                else
                    System.out.println("Username or Password are incorrect");
            }
        });



        AuthenticationCancel= new JButton("Close");
        AuthenticationCancel.setBounds(200,150,100,30);
        AuthenticationCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        AuthenticationPanel.add(AuthenticationLogin);
        AuthenticationPanel.add(AuthenticationCancel);

    }

}
