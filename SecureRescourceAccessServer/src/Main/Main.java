package Main;

import CommModels.*;
import Database.DBHelper;
import Networking.ServerManager;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args) throws InterruptedException, IOException
    {
        System.out.println("> [" + Main.getDate() + "] Starting new server... Ctrl+C to shutdown.");
        new ServerManager();

        Scanner scanner = new Scanner(System.in);

        while(scanner.hasNext())
        {
            String fullCommand = scanner.next();
            String[] commandTokens = fullCommand.split(":");
            String command = commandTokens[0];

            if(command.equals("adduser"))
            {
                String user = commandTokens[commandTokens.length-1];

                System.out.println("> [" + Main.getDate() + "] Enter password for new user " + user + ": ");
                String password = scanner.next();
                System.out.println("> [" + Main.getDate() + "] Enter " + user + "'s email address: ");
                String email = scanner.next();
                System.out.println("> [" + Main.getDate() + "] Enter " + user + "'s first name: ");
                String fname = scanner.next();
                System.out.println("> [" + Main.getDate() + "] Enter " + user + "'s last name: ");
                String lname = scanner.next();
                System.out.println("> [" + Main.getDate() + "] Enter " + user + "'s role (1 for admin): ");
                int role = scanner.nextInt();

                DBHelper.insertUser(commandTokens[1], password, email, fname, lname, role);
                System.out.println("> [" + Main.getDate() + "] New user " + user + " saved.");
            }

            else if (command.equals("adddevice"))
            {
                String device = commandTokens[commandTokens.length-1];

                System.out.println("> [" + Main.getDate() + "] Enter pin for new device " + device + ": ");
                int pin = scanner.nextInt();
                System.out.println("> [" + Main.getDate() + "] Enter type for new device " + device + ": ");
                String type = scanner.next();

                DBHelper.insertDevice(pin, device, type, "AVAILABLE", "OFF");
                System.out.println("> [" + Main.getDate() + "] New device " + device + " saved.");
            }

            else if (command.equals("shutdown"))
            {
                Devices devices = DBHelper.selectAllDevices();

                for (Device d: devices.getDevices())
                {
                    if(d instanceof Led)
                    {
                        d.setDeviceState(LedState.OFF);

                        DBHelper.updateDevice(d.getDeviceId(),
                                (int)d.getDevicePin(),
                                d.getDeviceName(),
                                "LED",
                                d.getDeviceStatus().toString(),
                                d.getDeviceState().toString());
                    }
                    else if (d instanceof Arm)
                    {
                        d.setDeviceState(ArmState.OFF);
                        DBHelper.updateDevice(d.getDeviceId(),
                                (int)d.getDevicePin(),
                                d.getDeviceName(),
                                "ARM",
                                d.getDeviceStatus().toString(),
                                d.getDeviceState().toString());
                    }
                }
                System.exit(0);
            }

            else
            {
                System.out.println("> [" + Main.getDate() + "] Invalid command.");
            }
        }
    }

    /**
     * Gets the current date and time.
     */
    public static String getDate()
    {
        java.util.Date date = new java.util.Date();
        String dateString = date.toString();

        return dateString.substring(4,19);
    }
}
