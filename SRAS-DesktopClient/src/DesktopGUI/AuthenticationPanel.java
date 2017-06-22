package DesktopGUI;

import CommModels.Message;

import javax.swing.*;

public class MainFrame
{
    JFrame AuthenticationFrame;

    JPanel AuthenticationPanel;

    JButton AuthenticationLogin;
    JButton AuthenticationCancel;

    JLabel usernameLabel;
    JLabel passwordLabel;

    JTextField username;
    JTextField password;


    public MainFrame()
    {
        createAuthenticationFrame();
    }

    public void createAuthenticationFrame()
    {
        AuthenticationFrame = new JFrame();
        AuthenticationFrame.setSize(350,250);
        AuthenticationFrame .setTitle("SRAS Authentication");
        createAuthenticationPanel();
        AuthenticationFrame.add(AuthenticationPanel);
        AuthenticationFrame.setVisible(true);
    }

    public void createAuthenticationPanel()
    {
        AuthenticationPanel = new JPanel();
        AuthenticationPanel.setSize(350,350);
        AuthenticationPanel.setLayout(null);
        createAuthenticationButtons();
        createTextFields();

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

    public void createAuthenticationButtons()
    {
        AuthenticationLogin = new JButton("Login");
        AuthenticationLogin.setBounds(50,150,100,30);
        AuthenticationPanel.add(AuthenticationLogin);

        AuthenticationCancel= new JButton("Cancel");
        AuthenticationCancel.setBounds(200,150,100,30);
        AuthenticationPanel.add(AuthenticationCancel);

    }

}
