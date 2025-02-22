package managefood;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Profile {
    private static JFrame profileFrame;
    private static Connection connection;

    Profile() {

        profileFrame = new JFrame("Profile Information");
        profileFrame.setBounds(100, 100, 400, 300);
        profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        profileFrame.setResizable(false);

        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(null);
        profileFrame.setContentPane(profilePanel);

        JButton addEmployeeButton = new JButton("Add Employee"); 
        addEmployeeButton.setBounds(20,40,150,30);

        JButton employeeInfoButton = new JButton("Employee information"); 
        employeeInfoButton.setBounds(20,80,150,30);

        JButton changeUsernameButton = new JButton("Change Username");
        changeUsernameButton.setBounds(20, 130, 150, 30);

        JButton changePasswordButton = new JButton("Change Password");
        changePasswordButton.setBounds(20, 170, 150, 30);

    
        profilePanel.add(addEmployeeButton);
        profilePanel.add(employeeInfoButton);
        profilePanel.add(changeUsernameButton);
        profilePanel.add(changePasswordButton);
        profileFrame.setVisible(true);
        profileFrame.setLocationRelativeTo(null);


        addEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddEmployee();
            }
        });

        employeeInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EmployeeInfo();
            }
        });
    
        changeUsernameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showChangeUsernameForm();
            }
        });
    
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showChangePasswordForm();
            }
        });
        initializeDatabase();
    }

    private static void initializeDatabase() {
        try {
            DBConnection dbConnection = new DBConnection();
            connection = dbConnection.mkDataBase();
        } catch (RuntimeException e) {
            // Handle the exception here if necessary
            e.printStackTrace();
        }
    }

    private static void showChangeUsernameForm() {
        JFrame changeUsernameFrame = new JFrame("Change Username Form");
        changeUsernameFrame.setBounds(200, 200, 400, 200);

        JPanel changeUsernamePanel = new JPanel();
        changeUsernamePanel.setLayout(null);
        changeUsernameFrame.setContentPane(changeUsernamePanel);

        JLabel oldUsernameLabel = new JLabel("Old Username:");
        oldUsernameLabel.setBounds(20, 20, 120, 20);
        JTextField oldUsernameTextField = new JTextField();
        oldUsernameTextField.setBounds(150, 20, 200, 20);

        JLabel newUsernameLabel = new JLabel("New Username:");
        newUsernameLabel.setBounds(20, 50, 120, 20);
        JTextField newUsernameTextField = new JTextField();
        newUsernameTextField.setBounds(150, 50, 200, 20);

        JButton changeUsernameButton = new JButton("Change Username");
        changeUsernameButton.setBounds(150, 90, 150, 30);

        changeUsernamePanel.add(oldUsernameLabel);
        changeUsernamePanel.add(oldUsernameTextField);
        changeUsernamePanel.add(newUsernameLabel);
        changeUsernamePanel.add(newUsernameTextField);
        changeUsernamePanel.add(changeUsernameButton);

        changeUsernameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeUsernameInDatabase(oldUsernameTextField.getText(), newUsernameTextField.getText());
                // Close the form after submitting
                changeUsernameFrame.dispose();
            }
        });

        changeUsernameFrame.setVisible(true);
        changeUsernameFrame.setLocationRelativeTo(null);
    }

    private static void changeUsernameInDatabase(String oldUsername, String newUsername) {
        String updateQuery = "UPDATE admin SET userid = ? WHERE userid = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newUsername);
            preparedStatement.setString(2, oldUsername);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                showMessageDialog("Username updated successfully! New username: " + newUsername);
            } else {
                showMessageDialog("Failed to update username. Either new username or old username is incorrect.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void showChangePasswordForm() {
        JFrame changePasswordFrame = new JFrame("Change Password Form");
        changePasswordFrame.setBounds(200, 200, 400, 200);

        JPanel changePasswordPanel = new JPanel();
        changePasswordPanel.setLayout(null);
        changePasswordFrame.setContentPane(changePasswordPanel);

        JLabel oldPasswordLabel = new JLabel("Old Password:");
        oldPasswordLabel.setBounds(20, 20, 120, 20);
        JPasswordField oldPasswordField = new JPasswordField();
        oldPasswordField.setBounds(150, 20, 200, 20);

        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setBounds(20, 50, 120, 20);
        JPasswordField newPasswordField = new JPasswordField();
        newPasswordField.setBounds(150, 50, 200, 20);

        JButton changePasswordButton = new JButton("Change Password");
        changePasswordButton.setBounds(150, 90, 150, 30);

        changePasswordPanel.add(oldPasswordLabel);
        changePasswordPanel.add(oldPasswordField);
        changePasswordPanel.add(newPasswordLabel);
        changePasswordPanel.add(newPasswordField);
        changePasswordPanel.add(changePasswordButton);

        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePasswordInDatabase(String.valueOf(oldPasswordField.getPassword()),
                        String.valueOf(newPasswordField.getPassword()));
                // Close the form after submitting
                changePasswordFrame.dispose();
            }
        });

        changePasswordFrame.setVisible(true);
        changePasswordFrame.setLocationRelativeTo(null);
    }

    private static void changePasswordInDatabase(String oldPassword, String newPassword) {
        String updateQuery = "UPDATE admin SET userpassword = ? WHERE userpassword = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, oldPassword);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                showMessageDialog("Username updated successfully! New username: " + newPassword);
            } else {
                showMessageDialog("Failed to update password. Either new password or old password is incorrect.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Update", JOptionPane.INFORMATION_MESSAGE);
    }

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Profile();
        });
    }
}
