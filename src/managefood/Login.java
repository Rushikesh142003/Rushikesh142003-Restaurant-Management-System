package managefood;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public Login() {
        // Set up the frame
        setTitle("LOGIN");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JPanel panel = new JPanel();
        setContentPane(panel);
        panel.setBackground(Color.CYAN);
        setLayout(null);

        // Create components with increased font size
        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);

        JLabel loginLabel = new JLabel("Food Management");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton closeButton = new JButton("Close");

        // With these lines
        loginLabel = new JLabel("Food Management");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 20));

        usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(labelFont);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);

        usernameField = new JTextField();
        usernameField.setFont(labelFont);

        passwordField = new JPasswordField();
        passwordField.setFont(labelFont);

        loginButton = new JButton("Login");
        loginButton.setFont(buttonFont);

        closeButton = new JButton("Close");
        closeButton.setFont(buttonFont);
        // Set bounds for each component
        loginLabel.setBounds(130, 10, 190, 50);
        usernameLabel.setBounds(70, 60, 90, 35);
        passwordLabel.setBounds(70, 100, 90, 35);

        usernameField.setBounds(170, 60, 200, 35);
        passwordField.setBounds(170, 100, 200, 35);

        loginButton.setBounds(100, 170, 80, 35);
        closeButton.setBounds(200, 170, 80, 35);

        // Add components to the frame
        add(loginLabel);
        add(usernameLabel);
        add(passwordLabel);
        add(usernameField);
        add(passwordField);
        add(loginButton);
        add(closeButton);

        // Add action listener to the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkLogin()) {
                    dispose(); // Close the login frame
                    Home home = new Home();
                    home.showButtonDemo();
                } else {
                    JOptionPane.showMessageDialog(Login.this, "Invalid username or password");
                }
            }
        });

        // Add action listener to the close button
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the login frame
            }
        });
    }

    // Method to check login credentials in the database
    private boolean checkLogin() {
        String username = usernameField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        DBConnection con = new DBConnection();
        PreparedStatement preparedStatement;
        try {
            String query = "SELECT * FROM admin WHERE userid = ? AND userpassword = ?";
            preparedStatement = con.mkDataBase().prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            // It returns a boolean value
            return resultSet.next(); // If there is a matching row, login is successful

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Login login = new Login();
                login.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
