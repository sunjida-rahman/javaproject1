import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginPage extends JFrame implements ActionListener {
    private MainApp mainApp; // Reference to the MainApp instance
    private String[] studentRolls = { "BBH2101032F", "BBH2101033F", "BBH2101034F", "BBH2101035F", "BBH2101036F",
            "BBH2101037F" }; // Example rolls
    private JLabel labelRollNumber;
    private JTextField textFieldRollNumber;
    private JButton buttonLogin;
    private JLabel backgroundLabel; // To hold the background image label
    private JLabel smileyLabel; // To hold the smiley icon label

    public LoginPage() {
        setTitle("Login Page");
        setLayout(new BorderLayout());

        // Create background image panel
        ImageIcon backgroundImageIcon = loadImageIcon(
                "https://github.com/sunjida-rahman/logoforjava/blob/main/177346902_padded_logo.png?raw=true");
        backgroundLabel = new JLabel(backgroundImageIcon);
        add(backgroundLabel, BorderLayout.CENTER);

        // Create smiley icon panel
        smileyLabel = new JLabel();
        ImageIcon smileyIcon = loadImageIcon(
                "https://cdn2.iconfinder.com/data/icons/businessman-emotions/267/businessman-unhappy-angry-013-1024.png"); // Load
                                                                                                                           // your
                                                                                                                           // smiley
                                                                                                                           // icon
                                                                                                                           // image
                                                                                                                           // here
        // Scale the icon to make it smaller
        if (smileyIcon != null) {
            Image scaledSmileyImage = smileyIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            smileyIcon = new ImageIcon(scaledSmileyImage);
        }
        smileyLabel.setIcon(smileyIcon);
        smileyLabel.setHorizontalAlignment(JLabel.CENTER);

        // Create login panel
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        labelRollNumber = new JLabel("Roll Number:");
        textFieldRollNumber = new JTextField(15);
        textFieldRollNumber.setBackground(Color.GRAY);
        textFieldRollNumber.setForeground(Color.BLACK);
        textFieldRollNumber.setFont(textFieldRollNumber.getFont().deriveFont(Font.BOLD));
        textFieldRollNumber.setPreferredSize(new Dimension(200, 30)); // Increase height of text field
        inputPanel.add(labelRollNumber);
        inputPanel.add(textFieldRollNumber);

        buttonLogin = new JButton("Login");
        buttonLogin.addActionListener(this);
        buttonLogin.setBackground(Color.GRAY);
        buttonLogin.setForeground(Color.BLACK);
        buttonLogin.setFont(buttonLogin.getFont().deriveFont(Font.BOLD));
        buttonLogin.setPreferredSize(new Dimension(200, 40)); // Increase height of button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(buttonLogin);

        loginPanel.setOpaque(false); // Make login panel transparent
        inputPanel.setOpaque(false); // Make input panel transparent
        buttonPanel.setOpaque(false); // Make button panel transparent

        loginPanel.add(inputPanel);
        loginPanel.add(buttonPanel);

        // Set layout of background label to BorderLayout to center login panel
        backgroundLabel.setLayout(new BorderLayout());
        backgroundLabel.add(smileyLabel, BorderLayout.NORTH); // Add smiley icon above the background image
        backgroundLabel.add(loginPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack(); // Adjust frame size based on components
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public LoginPage(MainApp mainApp) {
        this();
        this.mainApp = mainApp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonLogin) {
            String rollNumber = textFieldRollNumber.getText();
            if (isValidRoll(rollNumber)) {
                // Show login successful message
                JOptionPane.showMessageDialog(this, "Login successful. Welcome, " + rollNumber);

                // Display the homepage
                mainApp.displayHomePage();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid roll number. Please try again.", "Login Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean isValidRoll(String rollNumber) {
        for (String roll : studentRolls) {
            if (roll.equals(rollNumber)) {
                return true;
            }
        }
        return false;
    }

    private ImageIcon loadImageIcon(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            return new ImageIcon(url);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
