package Main;

import CommModels.*;
import Database.DBHelper;
import Networking.ClientManager;
import Networking.ServerManager;
import com.pi4j.io.gpio.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args) throws InterruptedException, IOException
    {
        //TODO: Should probably take in port number and max clients as params

        System.out.println("> [" + Main.getDate() + "] Starting new server... type command 'help' for usage");
        ServerManager serverManager = new ServerManager();
        Scanner scanner = new Scanner(System.in);

        initializePins();

        // Listen for commands from the server administrator.
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

            else if (command.equals("help"))
            {
                System.out.println("adduser:[username] --- Adds a new user with the specified username");
                System.out.println("adddevice:[devicename] --- Adds a new device with the specified name");
                System.out.println("showclients --- Prints a list of connected clients");
                System.out.println("shutdown --- Terminates the server after updating the database");
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

            else if (command.equals("showclients"))
            {
                ClientManager[] clientConnections = serverManager.getClientConnections();

                String divider = " ---------------------------------------------------------------------------";

                System.out.println(divider);
                System.out.printf("%-12s %-22s %-16s %-22s %s %n",
                        "| ThreadId", "| Username", "| Role", "| Address", "|");
                System.out.println(divider);

                for (ClientManager cm: clientConnections)
                {
                    if(cm != null)
                    {
                        System.out.printf("%-12s %-22s %-16s %-22s %s %n",
                                "| " + cm.getThreadId(),
                                "| " + cm.getAuthenticatedUserName(),
                                "| " + cm.getAuthenticatedUserRole(),
                                "| " + cm.getAuthenticatedUserAddress(), "|");

                        System.out.println(divider);
                    }
                }
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

    private static void initializePins()
    {
        GpioController gpio = GpioFactory.getInstance();
        Devices devices = DBHelper.selectAllDevices();

        for (Device device : devices.getDevices())
        {
            GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(getGpioPin((int)device.getDevicePin()), device.getDeviceName(), PinState.LOW);
            pin.setShutdownOptions(true, PinState.LOW);
            pin.setState(PinState.LOW);
            gpio.unprovisionPin(pin);
        }
    }

    /**
     * Converts Device.pin (int) to GPIO.pin (Pin).
     * @param x pin int from Device to be converted
     * @return GPIO Pin used for issuing commands
     */
    private static Pin getGpioPin(int x)
    {
        Pin resultPin = null;
        Pin[] p = RaspiPin.allPins();
        ArrayList<Pin> pins = new ArrayList<>(Arrays.asList(p));
        Collections.sort(pins);

        for (Pin pin : pins)
        {
            String pinString = pin.toString();

            if(Integer.toString(x).length() == 2)
            {
                if (pinString.substring(pinString.length() - 2).equals(Integer.toString(x)))
                {
                    resultPin = pin;
                    break;
                }
            }
            else if (Integer.toString(x).length() == 1)
            {
                if (pinString.substring(pinString.length() - 1).equals(Integer.toString(x)))
                {
                    resultPin = pin;
                    break;
                }
            }
        }
        return resultPin;
    }
}
