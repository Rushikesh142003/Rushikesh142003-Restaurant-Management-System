package managefood;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddEmployee extends JFrame implements ActionListener {

    // Components declaration
    JTextField empIdText, empNameText, empAgeText, empSalaryText, empPhoneText, empAadharText, empEmailText;
    JLabel empIdLabel, empNameLabel, empAgeLabel, empGenderLabel, empJobLabel, empSalaryLabel, empPhoneLabel,
            empAadharLabel,
            empEmailLabel, label;
    JRadioButton male, female;
    JButton save, back;
    JComboBox<String> comboBox;
    ButtonGroup group;
    String[] course = { "Kitchen Staff", "Waiter/Waitress", "Manager", "Accountant", "Chef" };
    DBConnection dbConnection; // Instance of DBConnection class

    public AddEmployee() {
        // Initialize DBConnection instance
        dbConnection = new DBConnection();

        getContentPane().setBackground(Color.LIGHT_GRAY);
        setUndecorated(true);
        setResizable(false);

        empIdLabel = new JLabel("EMPLOYEE ID");
        empIdLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
        empIdLabel.setBounds(60, 30, 150, 27);
        add(empIdLabel);
        empIdText = new JTextField();
        empIdText.setBounds(200, 30, 150, 27);
        add(empIdText);

        empNameLabel = new JLabel("NAME");
        empNameLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
        empNameLabel.setBounds(60, 80, 150, 27);
        add(empNameLabel);
        empNameText = new JTextField();
        empNameText.setBounds(200, 80, 150, 27);
        add(empNameText);

        empAgeLabel = new JLabel("AGE");
        empAgeLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
        empAgeLabel.setBounds(60, 120, 150, 27);
        add(empAgeLabel);
        empAgeText = new JTextField();
        empAgeText.setBounds(200, 120, 150, 27);
        add(empAgeText);

        empGenderLabel = new JLabel("GENDER");
        empGenderLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
        empGenderLabel.setBounds(60, 170, 150, 27);
        add(empGenderLabel);
        male = new JRadioButton("MALE");
        male.setBackground(Color.WHITE);
        male.setBounds(200, 170, 70, 27);
        add(male);
        female = new JRadioButton("FEMALE");
        female.setBackground(Color.WHITE);
        female.setBounds(270, 170, 80, 27);
        add(female);

        group = new ButtonGroup();
        group.add(male);
        group.add(female);

        empJobLabel = new JLabel("JOB");
        empJobLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
        empJobLabel.setBounds(60, 220, 150, 27);
        add(empJobLabel);
        comboBox = new JComboBox<>(course);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBounds(200, 220, 150, 30);
        add(comboBox);

        empSalaryLabel = new JLabel("SALARY");
        empSalaryLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
        empSalaryLabel.setBounds(60, 270, 150, 27);
        add(empSalaryLabel);
        empSalaryText = new JTextField();
        empSalaryText.setBounds(200, 270, 150, 27);
        add(empSalaryText);

        empPhoneLabel = new JLabel("PHONE");
        empPhoneLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
        empPhoneLabel.setBounds(60, 320, 150, 27);
        add(empPhoneLabel);
        empPhoneText = new JTextField();
        empPhoneText.setBounds(200, 320, 150, 27);
        add(empPhoneText);

        empAadharLabel = new JLabel("AADHAR");
        empAadharLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
        empAadharLabel.setBounds(60, 370, 150, 27);
        add(empAadharLabel);
        empAadharText = new JTextField();
        empAadharText.setBounds(200, 370, 150, 27);
        add(empAadharText);

        empEmailLabel = new JLabel("EMAIL");
        empEmailLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
        empEmailLabel.setBounds(60, 420, 150, 27);
        add(empEmailLabel);
        empEmailText = new JTextField();
        empEmailText.setBounds(200, 420, 150, 27);
        add(empEmailText);

        label = new JLabel("ADD EMPLOYEE DETAILS");
        label.setForeground(Color.BLUE);
        label.setFont(new Font("Tahoma", Font.BOLD, 31));
        label.setBounds(450, 24, 442, 35);
        add(label);

        ImageIcon i1 = null;
        URL imageUrl = getClass().getResource("\\images\\employee.jpg"); // Adjusted relative path
        if (imageUrl != null) {
            i1 = new ImageIcon(imageUrl);
            Image i3 = i1.getImage().getScaledInstance(500, 500, Image.SCALE_DEFAULT);
            ImageIcon i2 = new ImageIcon(i3);
            JLabel image = new JLabel(i2);
            image.setBounds(410, 80, 480, 410);
            add(image);
        } else {
            System.out.println("Image not found");
        }

        save = new JButton("SAVE");
        save.addActionListener(this);
        save.setFont(new Font("Tahoma", Font.BOLD, 20));
        save.setBounds(60, 470, 130, 30);
        save.setBackground(Color.DARK_GRAY);
        save.setForeground(Color.GREEN);
        add(save);

        back = new JButton("Back");
        back.addActionListener(this);
        back.setFont(new Font("Tahoma", Font.BOLD, 20));
        back.setBounds(210, 470, 130, 30);
        back.setBackground(Color.DARK_GRAY);
        back.setForeground(Color.GREEN);
        add(back);

        setBounds(350, 200, 900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            if (ae.getSource() == save) {
                String id = empIdText.getText();
                String name = empNameText.getText();
                String age = empAgeText.getText();
                String salary = empSalaryText.getText();
                String phone = empPhoneText.getText();
                String aadhar = empAadharText.getText();
                String email = empEmailText.getText();
                String gender = null;
                if (male.isSelected()) {
                    gender = "male";
                } else if (female.isSelected()) {
                    gender = "female";
                }
                String job = (String) comboBox.getSelectedItem();

                if (Validation.isValidInput(id, name, age, gender, job, salary, phone, aadhar, email)) {
                    try {
                        // Establish connection to the database
                        Connection conn = dbConnection.mkDataBase();

                        // Prepare SQL statement
                        String sql = "INSERT INTO employee VALUES (?,?, ?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement pstmt = conn.prepareStatement(sql);

                        // Set parameters for the prepared statement
                        pstmt.setString(1, "EID000" + id);
                        pstmt.setString(2, name);
                        pstmt.setString(3, age);
                        pstmt.setString(4, gender);
                        pstmt.setString(5, job);
                        pstmt.setString(6, salary);
                        pstmt.setString(7, phone);
                        pstmt.setString(8, aadhar);
                        pstmt.setString(9, email);

                        // Execute the SQL statement
                        pstmt.executeUpdate();

                        JOptionPane.showMessageDialog(null, "Employee Added Successfully");

                        // Close the prepared statement and database connection
                        pstmt.close();
                        conn.close();

                        // Reset all components
                        resetComponents();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter valid data in all fields.");
                }
            } else if (ae.getSource() == back) {
                dispose();
            }
        } catch (Exception aee) {
            aee.printStackTrace();
        }
    }

    // Reset method to clear all fields
    private void resetComponents() {
        empIdText.setText("");
        empNameText.setText("");
        empAgeText.setText("");
        empSalaryText.setText("");
        empPhoneText.setText("");
        empAadharText.setText("");
        empEmailText.setText("");
        comboBox.removeAllItems();
        for (String item : course) {
            comboBox.addItem(item);
        }
        group.clearSelection();
    }

    // Main method
    public static void main(String[] args) {
        new AddEmployee();
    }
}
