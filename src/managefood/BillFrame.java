package managefood;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BillFrame {
    private JFrame mainFrame;
    private DefaultTableModel tableModel;
    private JTable orderTable;

    private String selectedFood;
    private double selectedFoodPrice;
    private int selectedFoodId;

    public BillFrame() {
        mainFrame = new JFrame();
        mainFrame.setTitle("Bill page");
        mainFrame.setSize(800, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                mainFrame.dispose();
            }
        });

        JLabel titleLabel = new JLabel("Restaurant Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        JButton generateBillButton = new JButton("Generate Bill");
        JButton addItemButton = new JButton("Add Item");

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Food ID");
        tableModel.addColumn("Item");
        tableModel.addColumn("Price");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Total");
        orderTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(orderTable);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(generateBillButton);
        buttonPanel.add(addItemButton);

        mainFrame.add(titleLabel, BorderLayout.NORTH);
        mainFrame.add(scrollPane, BorderLayout.CENTER);
        mainFrame.add(buttonPanel, BorderLayout.SOUTH);

        generateBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateBill();
                clearTable();
            }
        });

        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItem();
            }
        });

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                mainFrame.dispose();
            }
        });
    }

    private int fetchFoodIdFromDatabase(String itemName) {
        try {
            DBConnection con = new DBConnection();
            Connection connection = con.mkDataBase();

            String query = "SELECT id FROM food WHERE food_name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, itemName);

            ResultSet resultSet = preparedStatement.executeQuery();

            int foodId = 0;
            if (resultSet.next()) {
                foodId = resultSet.getInt("id");
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

            return foodId;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void addItem() {
        JDialog addItemDialog = new JDialog(mainFrame, "Add Item", true);
        addItemDialog.setLayout(new GridLayout(3, 2));

        JComboBox<String> itemComboBox = new JComboBox<>();
        JTextField quantityField = new JTextField();
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");

        fetchFoodNames(itemComboBox);

        addItemDialog.add(new JLabel("Select food :"));
        addItemDialog.add(itemComboBox);
        addItemDialog.add(new JLabel("Enter Quantity:"));
        addItemDialog.add(quantityField);
        addItemDialog.add(addButton);
        addItemDialog.add(cancelButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedFood = (String) itemComboBox.getSelectedItem();
                selectedFoodId = fetchFoodIdFromDatabase(selectedFood);
                selectedFoodPrice = fetchPriceFromDatabase(selectedFood);

                int quantity = Integer.parseInt(quantityField.getText());
                tableModel.addRow(new Object[] { selectedFoodId, selectedFood, selectedFoodPrice, quantity,
                        selectedFoodPrice * quantity });

                addItemDialog.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItemDialog.dispose();
            }
        });

        addItemDialog.setSize(300, 150);
        addItemDialog.setLocationRelativeTo(mainFrame);
        addItemDialog.setVisible(true);
    }

    private double fetchPriceFromDatabase(String itemName) {
        try {
            DBConnection con = new DBConnection();
            Connection connection = con.mkDataBase();

            String query = "SELECT price FROM food WHERE food_name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, itemName);

            ResultSet resultSet = preparedStatement.executeQuery();

            double price = 0.0;
            if (resultSet.next()) {
                price = resultSet.getDouble("price");
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

            return price;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    private void fetchFoodNames(JComboBox<String> itemComboBox) {
        try {
            DBConnection con = new DBConnection();
            Connection connection = con.mkDataBase();

            String query = "SELECT food_name FROM food";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String foodName = resultSet.getString("food_name");
                itemComboBox.addItem(foodName);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearTable() {
        tableModel.setRowCount(0);
    }

    private void printBill(Component component) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new Printable() {
            public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
                if (page > 0) {
                    return NO_SUCH_PAGE;
                }

                Graphics2D g2 = (Graphics2D) g;
                g2.translate(pf.getImageableX(), pf.getImageableY());

                component.printAll(g2);

                return PAGE_EXISTS;
            }
        });

        boolean ok = job.printDialog();
        if (ok) {
            try {
                job.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void generateBill() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(mainFrame, "No items added to the bill.", "Empty Bill",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int billNumberIncrement = 0;
        try {
            // Create a database connection
            DBConnection con = new DBConnection();
            Connection connection = con.mkDataBase();

            // Fetch the current bill_number_increment from the billno table
            billNumberIncrement = getBillNumberIncrement(connection);
            billNumberIncrement += 1;

            // Prepare the query to insert data into orderfoodrecord table
            String insertQuery = "INSERT INTO orderfoodrecord (bill_number, food_id, foodname, foodprize, food_quantity, date, time) VALUES (?, ?, ?, ?, ?, CURRENT_DATE,?)";

            // Iterate over the rows in the tableModel and insert data into orderfoodrecord
            // table
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                int foodId = (int) tableModel.getValueAt(row, 0);
                String foodName = (String) tableModel.getValueAt(row, 1);
                double foodPrice = (double) tableModel.getValueAt(row, 2);
                int quantity = (int) tableModel.getValueAt(row, 3);

                // Get the current time in "hh:mm a" format
                String currentTime = getCurrentTimeInFormat();

                // Create a prepared statement
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, billNumberIncrement);
                insertStatement.setInt(2, foodId);
                insertStatement.setString(3, foodName);
                insertStatement.setDouble(4, foodPrice);
                insertStatement.setInt(5, quantity);
                insertStatement.setString(6, currentTime);

                // Execute the insert statement
                insertStatement.executeUpdate();

                // Close the statement
                insertStatement.close();
            }

            // Update the billno table with the incremented bill_number_increment
            updateBillNumberIncrement(connection, billNumberIncrement);

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately in a real-world application
        }

        // Create the bill summary dialog
        JDialog billSummaryDialog = new JDialog(mainFrame, "Food Receipt", true);
        billSummaryDialog.setLayout(null);

        JPanel headerPanel1 = new JPanel();
        headerPanel1.setBounds(5, 0, 100, 100);

        // logo image
        ImageIcon i1 = null;
        URL imageUrl = getClass().getResource("\\images\\logo.png"); // Adjusted relative path
        if (imageUrl != null) {
            i1 = new ImageIcon(imageUrl);
            Image i3 = i1.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
            ImageIcon i2 = new ImageIcon(i3);
            JLabel logoLabel = new JLabel(i2);
            logoLabel.setBounds(0, 0, 100, 100);
            headerPanel1.add(logoLabel);
        } else {
            System.out.println("Image not found");
        }

        // Add headerPanel1 to billSummaryDialog
        billSummaryDialog.add(headerPanel1);

        // Restaurant Information
        JLabel restaurantNameL = new JLabel("|Restaurant: LiteFood");
        restaurantNameL.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel restaurantOwnerL = new JLabel("|Owner:Rushikesh Patil |");
        restaurantOwnerL.setFont(new Font("Arial", Font.PLAIN, 16));
        JLabel restaurantMobileL = new JLabel("|MobileNo:9665393259|");
        restaurantMobileL.setFont(new Font("Arial", Font.PLAIN, 16));
        JLabel restaurantAddressl = new JLabel("|Address:kadlas,tal-sangola 413309|");
        restaurantAddressl.setFont(new Font("Arial", Font.PLAIN, 16));
        restaurantNameL.setBounds(10, 0, 140, 20);
        restaurantOwnerL.setBounds(10, 30, 100, 20);
        restaurantMobileL.setBounds(120, 30, 100, 20);
        restaurantAddressl.setBounds(10, 60, 140, 20);

        JPanel headerPanel2 = new JPanel();
        headerPanel2.setBounds(70, 0, 520, 80);

        headerPanel2.add(restaurantNameL);
        headerPanel2.add(restaurantOwnerL);
        headerPanel2.add(restaurantMobileL);
        headerPanel2.add(restaurantAddressl);

        // Add headerPanel2 to billSummaryDialog
        billSummaryDialog.add(headerPanel2);

        // Info Panel
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        // HeaderPanel3
        JPanel headerPanel3 = new JPanel();
        headerPanel3.setBounds(115, 80, 400, 20);

        JLabel dateLabel = new JLabel("| Date: " + java.time.LocalDate.now().format(dateFormatter));
        dateLabel.setBounds(0, 0, 100, 20);
        headerPanel3.add(dateLabel);

        JLabel timeLabel = new JLabel(" | Time: " + java.time.LocalTime.now().format(timeFormatter));
        timeLabel.setBounds(100, 0, 100, 20);
        headerPanel3.add(timeLabel);

        JLabel billNumberLabel = new JLabel(" | Bill Number: " + billNumberIncrement + " |");
        billNumberLabel.setBounds(200, 0, 100, 20);
        headerPanel3.add(billNumberLabel);

        // Add headerPanel3 to billSummaryDialog
        billSummaryDialog.add(headerPanel3);

        // Summary Table
        DefaultTableModel summaryTableModel = new DefaultTableModel();
        summaryTableModel.addColumn("Food ID");
        summaryTableModel.addColumn("Item");
        summaryTableModel.addColumn("Price");
        summaryTableModel.addColumn("Quantity");
        summaryTableModel.addColumn("Total");

        JTable summaryTable = new JTable(summaryTableModel);

        for (int row = 0; row < tableModel.getRowCount(); row++) {
            Object[] rowData = new Object[5];
            for (int col = 0; col < 5; col++) {
                rowData[col] = tableModel.getValueAt(row, col);
            }
            summaryTableModel.addRow(rowData);
        }

        JScrollPane summaryScrollPane = new JScrollPane(summaryTable);
        summaryScrollPane.setBounds(10, 120, 570, 350);
        billSummaryDialog.add(summaryScrollPane);

        // Total Panel
        double totalPrice = 0.0;
        int totalQuantity = 0;

        for (int row = 0; row < summaryTableModel.getRowCount(); row++) {
            double total = (double) summaryTableModel.getValueAt(row, 4);
            int quantity = (int) summaryTableModel.getValueAt(row, 3);

            totalPrice += total;
            totalQuantity += quantity;
        }

        JLabel totalPriceLabel = new JLabel(" Total Price: Rs. " + totalPrice);
        totalPriceLabel.setBounds(60, 475, 200, 20);
        billSummaryDialog.add(totalPriceLabel);

        JLabel totalQuantityLabel = new JLabel("     Total Quantity: " + totalQuantity + "                         ");
        totalQuantityLabel.setBounds(210, 475, 200, 20);
        billSummaryDialog.add(totalQuantityLabel);
        JButton printButton = new JButton("Print");
        printButton.setBounds(470, 475, 100, 20);
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printBill(billSummaryDialog);
            }
        });

        billSummaryDialog.add(printButton);

        // Set size, location, and visibility of the dialog
        billSummaryDialog.setSize(600, 550);
        billSummaryDialog.setLocationRelativeTo(mainFrame);
        billSummaryDialog.setVisible(true);

    }

    // Method to get the current time in "hh:mm a" format
    private String getCurrentTimeInFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return LocalTime.now().format(formatter);
    }

    private int getBillNumberIncrement(Connection connection) throws SQLException {
        // Fetch the current bill_number_increment from the billno table
        String selectQuery = "SELECT bill_number_increment FROM billno";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectQuery);

        int billNumberIncrement1;

        billNumberIncrement1 = 0;
        if (resultSet.next()) {
            billNumberIncrement1 = resultSet.getInt("bill_number_increment");
        }

        resultSet.close();
        statement.close();

        return billNumberIncrement1;
    }

    private void updateBillNumberIncrement(Connection connection, int newBillNumberIncrement) throws SQLException {
        // Update the billno table with the new bill_number_increment
        String updateQuery = "UPDATE billno SET bill_number_increment = ?";
        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
        updateStatement.setInt(1, newBillNumberIncrement);
        updateStatement.executeUpdate();
        updateStatement.close();
    }

    public void showBillFrame() {
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                BillFrame billFrame = new BillFrame();
                billFrame.showBillFrame();
            }
        });
    }
}
