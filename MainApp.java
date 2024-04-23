import javax.swing.JFrame;

public class MainApp extends JFrame {
    private LoginPage loginPage;
    private HomePage homePage;

    public MainApp() {
        setTitle("Login Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Initialize the login page
        loginPage = new LoginPage(this); // Pass the MainApp instance to LoginPage
        add(loginPage); // Add the login page to the frame

        // Initialize the homepage, but keep it invisible for now
        homePage = new HomePage();
        add(homePage); // Add the homepage to the frame
        homePage.setVisible(false); // Keep the homepage invisible initially
    }

    // Method called when login is successful
    public void displayHomePage() {
        // Initialize the homepage if not already initialized
        if (homePage == null) {
            homePage = new HomePage();
        }
        // Hide the login page and show the homepage
        loginPage.setVisible(false);
        homePage.setVisible(true);
        setTitle("Homepage");
    }

    public static void main(String[] args) {
        // Run the application
        MainApp mainApp = new MainApp();
        mainApp.setVisible(true); // Make the main application frame visible
    }
}
