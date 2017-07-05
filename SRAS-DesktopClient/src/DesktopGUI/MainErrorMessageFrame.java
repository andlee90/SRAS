package DesktopGUI;

/**
 * Created by Tim on 7/5/2017.
 */
import javax.swing.*;

/**
 * Creates a frame to display messages for the purpose of general error handling.
 */
public class MainErrorMessageFrame
{
    private JPanel message;
    private JFrame frame;

    public MainErrorMessageFrame(String error)
    {
        frame = new JFrame();
        createPanel(error);
        createDialog();
        JOptionPane.showConfirmDialog(frame, message, "Error Message:", JOptionPane.CANCEL_OPTION);
    }

    private void createDialog()
    {
        JDialog dialog = new JDialog(frame, "Error Message:");
        dialog.setModal(true);
        dialog.setContentPane(message);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
    }

    private void createPanel(String error)
    {
        message = new JPanel();
        message.add(new JLabel(error));
    }
}
