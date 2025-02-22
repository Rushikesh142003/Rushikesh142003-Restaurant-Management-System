package managefood;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FoodOrderRecord {

    private JFrame recordFrame;
    private Connection connection;

    FoodOrderRecord() {
        // Create a database connection
        DBConnection dbConnection = new DBConnection();
        connection = dbConnection.mkDataBase();
        if (connection == null) {
            JOptionPane.showMessageDialog(null, "Error connecting to the database", "Database Connection Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        prepareGUI();
    }

    public void prepareGUI() {
        recordFrame = new JFrame("Food Order Record");
        JPanel panel = new JPanel(new GridBagLayout());

        JLabel recordFetchLabel = new JLabel("Display record by using");
        JButton billNumberButton = new JButton("Fetch by Bill Number");
        JButton dateButton = new JButton("Fetch by Date");
        JButton dateTimeButton = new JButton("Fetch by Date and Time");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(10, 10, 10, 10); // Add some padding
        panel.add(recordFetchLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 10); // Adjust spacing as needed
        panel.add(billNumberButton, gbc);

        gbc.gridx = 1;
        panel.add(dateButton, gbc);

        gbc.gridx = 2;
        panel.add(dateTimeButton, gbc);

        // NORTH the panel using BorderLayout
        recordFrame.add(panel, BorderLayout.NORTH);
        recordFrame.setSize(500, 400);
        recordFrame.setLocationRelativeTo(null);
        recordFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        recordFrame.setVisible(true);

        // Set preferred size for buttons
        Dimension buttonSize = new Dimension(150, 30);
        billNumberButton.setPreferredSize(buttonSize);
        dateButton.setPreferredSize(buttonSize);
        dateTimeButton.setPreferredSize(buttonSize);

        // Button action for fetching by bill number
        billNumberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String billNumber = JOptionPane.showInputDialog("Enter Bill Number:");
                fetchRecordsByBillNumber(billNumber);
            }
        });

        // Button action for fetching by date
        dateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInputDate = JOptionPane.showInputDialog("Enter Date (DD-MM-YYYY):");

                // Convert the user input to the format used in the database
                String targetDateDatabaseFormat = convertToDatabaseFormat(userInputDate);

                // Fetch data based on the converted date
                fetchRecordsByDate(targetDateDatabaseFormat);
            }
        });

        // Button action for fetching by date and time
        dateTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = JOptionPane.showInputDialog("Enter Date (DD-MM-YYYY):");
                date = convertToDatabaseFormat(date);
                System.out.println(date);
                String Time = JOptionPane.showInputDialog("Enter Time (HH:MM am/pm):");
                fetchRecordsByDateTime(date, Time);
            }
        });
    }

    private void fetchRecordsByBillNumber(String billNumber) {
        try {
            String query = "SELECT * FROM orderfoodrecord WHERE bill_number = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(billNumber));
            ResultSet resultSet = preparedStatement.executeQuery();

            displayResultInTable(resultSet);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching records from the database", "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fetchRecordsByDate(String date) {
        try {
            String query = "SELECT * FROM orderfoodrecord WHERE date = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, date);
            ResultSet resultSet = preparedStatement.executeQuery();

            displayResultInTable(resultSet);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching records from the database", "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fetchRecordsByDateTime(String date, String Time) {
        try {
            String query = "SELECT * FROM orderfoodrecord WHERE date = ? AND time = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, date);
            preparedStatement.setString(2, Time);

            ResultSet resultSet = preparedStatement.executeQuery();

            displayResultInTable(resultSet);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching records from the database", "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to convert user input date to database format
    private String convertToDatabaseFormat(String userInputDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            return outputFormat.format(inputFormat.parse(userInputDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to format date in "dd-MM-yyyy" format
    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }

    private void displayResultInTable(ResultSet resultSet) throws SQLException {
        DefaultTableModel tableModel = new DefaultTableModel();

        tableModel.addColumn("Bill Number");
        tableModel.addColumn("Food ID");
        tableModel.addColumn("Food Name");
        tableModel.addColumn("Food Prize");
        tableModel.addColumn("Food Quantity");
        tableModel.addColumn("Date");
        tableModel.addColumn("Time");

        while (resultSet.next()) {
            Object[] row = {
                    resultSet.getInt("bill_number"),
                    resultSet.getInt("food_id"),
                    resultSet.getString("foodname"),
                    resultSet.getDouble("foodprize"),
                    resultSet.getInt("food_quantity"),
                    formatDate(resultSet.getDate("date")), // Format the date before adding to the table
                    resultSet.getString("time")
            };
            tableModel.addRow(row);
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JFrame resultFrame = new JFrame("Result");
        resultFrame.getContentPane().add(scrollPane);
        resultFrame.setSize(800, 600);
        resultFrame.setLocationRelativeTo(null);
        resultFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FoodOrderRecord();
        });
    }
}
