import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class HomePage extends JFrame {
    private ImageIcon backgroundImage;
    // Map to store exam schedule (subject -> exam date)
    private Map<LocalDate, String> examSchedule;

    public HomePage() {
        setTitle("Reminder App - Home Page");

        // Load the background image from the web
        try {
            URL imageUrl = new URL("https://od.lk/s/MTlfNTQ1MDk5Nzdf/9a61d646-bd74-4750-b6eb-231108858c65.jpeg");
            Image backgroundImg = ImageIO.read(imageUrl);
            backgroundImage = new ImageIcon(backgroundImg);
        } catch (IOException e) {
            // Handle the exception gracefully, for example, by using a fallback image
            e.printStackTrace();
            backgroundImage = new ImageIcon("fallback_background_image.jpg"); // Provide a local fallback image
        }

        // Set layout manager
        setLayout(new BorderLayout());

        // Create and customize header label for upcoming class test
        JLabel upcomingClassTestLabel = new JLabel("Upcoming Class Test");
        upcomingClassTestLabel.setFont(new Font("Arial", Font.BOLD, 36));
        upcomingClassTestLabel.setHorizontalAlignment(JLabel.CENTER);
        upcomingClassTestLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        upcomingClassTestLabel.setForeground(Color.WHITE); // Set font color to white
        upcomingClassTestLabel.setBackground(new Color(0, 100, 0)); // Set background color to blue
        upcomingClassTestLabel.setOpaque(true); // Make the background color visible
        upcomingClassTestLabel.setPreferredSize(new Dimension(400, 60)); // Set preferred size
        add(upcomingClassTestLabel, BorderLayout.NORTH);

        // Create and customize content panel
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPanel.setLayout(new BorderLayout()); // Use BorderLayout for better layout
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(contentPanel, BorderLayout.CENTER);

        // Set window properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(backgroundImage.getIconWidth(), backgroundImage.getIconHeight()); // Set the size based on the
                                                                                  // background image dimensions
        setLocationRelativeTo(null);
        setVisible(true);

        // Initialize and fetch exam schedule from the system
        initializeExamSchedule();

        // Display upcoming exams
        displayUpcomingExams(contentPanel);

        // Check for upcoming exams when the homepage is displayed
        checkUpcomingExams();

        // Start a separate thread for displaying notifications every 5 seconds
        Thread notificationThread = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(5000); // Wait for 5 seconds
                    checkUpcomingExams(); // Check for upcoming exams and display notifications
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        notificationThread.start();

        // Create a menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Create a "Tests" menu
        JMenu testsMenu = new JMenu("Menu");
        menuBar.add(testsMenu);

        // Add "Previous Test" menu item
        JMenuItem previousTestItem = new JMenuItem("Previous Test");
        testsMenu.add(previousTestItem);
        // Add action listener to "Previous Test" menu item
        previousTestItem.addActionListener(e -> showPreviousTestsDialog());

    }

    private void initializeExamSchedule() {
        // Initialize exam schedule
        examSchedule = new TreeMap<>();

        // Example: Add exams
        examSchedule.put(LocalDate.of(2024, 3, 21), "English"); // Example exam date
        examSchedule.put(LocalDate.of(2024, 3, 22), "Math"); // Example exam date
        examSchedule.put(LocalDate.of(2024, 3, 12), "Bangla"); // Example exam date
        // Add more exams
    }

    private void displayUpcomingExams(JPanel contentPanel) {

        // Create a panel for displaying the upcoming exams
        JPanel examsPanel = new JPanel(new BorderLayout());
        examsPanel.setOpaque(false); // Make the panel transparent

        // Create table headers
        String[] columnNames = { "Index", "Subject", "Date", "Details" };

        // Create data for the table
        List<Object[]> upcomingExamsData = new ArrayList<>();
        int index = 0;
        LocalDate currentDate = LocalDate.now();
        for (Map.Entry<LocalDate, String> entry : examSchedule.entrySet()) {
            LocalDate examDate = entry.getKey();
            String examName = entry.getValue();

            if (examDate.isAfter(currentDate)) {
                String formattedDate = formatDate(examDate);
                Object[] rowData = { index + 1, examName, formattedDate, "" };
                upcomingExamsData.add(rowData);
                index++;
            }
        }
        // Convert the list to a 2D array
        Object[][] data = new Object[upcomingExamsData.size()][4];
        for (int i = 0; i < upcomingExamsData.size(); i++) {
            data[i] = upcomingExamsData.get(i);
        }

        // Create the table
        JTable table = new JTable(data, columnNames);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.setRowHeight(30);

        // Set alignment for table columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Index
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Subject
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Date
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Details

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);

        // Add the scroll pane to the panel
        examsPanel.add(scrollPane, BorderLayout.CENTER);

        // Add the panel to the contentPanel
        contentPanel.add(examsPanel, BorderLayout.CENTER);

        // Create a panel for the button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Make the panel transparent
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // Align button to the right

        // Create a button with the class icon for adding new tests
        JButton addButton = new JButton("Add Test", getPlusIcon());
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to handle adding a new test
                // Prompt the user for input
                String subject = JOptionPane.showInputDialog(HomePage.this, "Enter subject for the new exam:");

                // Check if subject is provided and not empty
                if (subject != null && !subject.isEmpty()) {
                    // Prompt the user for the exam date
                    String dateStr = JOptionPane.showInputDialog(HomePage.this,
                            "Enter date for the exam (YYYY-MM-DD):");
                    try {
                        // Parse the date string
                        LocalDate examDate = LocalDate.parse(dateStr);

                        // Add the new exam schedule to the map
                        examSchedule.put(examDate, subject);

                        // Refresh the panel to display the updated exam schedules
                        refreshExamSchedulePanel(contentPanel);
                    } catch (DateTimeParseException ex) {
                        JOptionPane.showMessageDialog(HomePage.this,
                                "Invalid date format. Please enter date in YYYY-MM-DD format.",
                                "Invalid Date", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Add the button to the buttonPanel
        buttonPanel.add(addButton);

        // Add the buttonPanel to the contentPanel
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void showPreviousTestsDialog() {
        // Create dialog
        JDialog dialog = new JDialog(this, "Previous Tests", true);

        // Create content panel for the dialog
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Iterate through
        // Iterate through each exam
        for (Map.Entry<LocalDate, String> entry : examSchedule.entrySet()) {
            LocalDate examDate = entry.getKey();
            String examName = entry.getValue();

            // Check if the exam date is in the past
            if (examDate.isBefore(currentDate)) {
                // Create label to display exam information
                JLabel examLabel = new JLabel("Subject: " + examName + " | Date: " + formatDate(examDate));

                // Add label to the content panel
                contentPanel.add(examLabel);
            }
        }

        // Add content panel to the dialog
        dialog.add(contentPanel);

        // Set dialog properties
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private ImageIcon getPlusIcon() {
        try {
            // URL of the plus icon image
            URL url = new URL("https://od.lk/s/MTlfNTQ0NTU4NzNf/%2B%20icon.png"); // Replace with the actual URL of the
                                                                                  // image

            // Fetch the image from the URL
            Image img = ImageIO.read(url);

            // Scale the image to fit the button and make it round
            int size = 40; // Set the desired size
            Image scaledImg = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            BufferedImage roundedImg = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = roundedImg.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setClip(new Ellipse2D.Float(0, 0, size, size));
            g2d.drawImage(scaledImg, 0, 0, size, size, null);
            g2d.dispose();

            // Create ImageIcon from the scaled and rounded image
            return new ImageIcon(roundedImg);
        } catch (IOException e) {
            e.printStackTrace();
            // Return null if an error occurs
            return null;
        }
    }

    private void refreshExamSchedulePanel(JPanel contentPanel) {
        // Clear the content of the panel
        contentPanel.removeAll();

        // Display the updated upcoming exams
        displayUpcomingExams(contentPanel);

        // Revalidate and repaint the panel to reflect the changes
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void checkUpcomingExams() {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Iterate through each exam
        for (Map.Entry<LocalDate, String> entry : examSchedule.entrySet()) {
            LocalDate examDate = entry.getKey();
            // Check if the exam is scheduled exactly two days from the current date
            if (examDate.equals(currentDate.plusDays(2))) {
                // If the exam is in 2 days, show the notification
                String examName = entry.getValue();
                showNotification(examName, examDate);
            }
        }
    }

    private String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        return date.format(formatter);
    }

    private void showNotification(String exam, LocalDate examDate) {
        // Play notification sound
        playNotificationSound();

        // Display a pop-up notification
        JOptionPane.showMessageDialog(this,
                "Reminder: " + exam + " test is in 2 days on " + formatDate(examDate) + ". Be prepared!",
                "Exam Reminder", JOptionPane.INFORMATION_MESSAGE);
    }

    private void playNotificationSound() {
        try {
            // Provide a direct URL to the sound file
            URL soundUrl = new URL("https://od.lk/s/MTlfNTQ0NTU3NTlf/mixkit-happy-bells-notification-937.wav");

            // Open the audio input stream
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundUrl);

            // Get a clip and open it with the audio input stream
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // Start playing the clip
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new HomePage();
    }
}