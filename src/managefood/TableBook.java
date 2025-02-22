package managefood;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TableBook extends JFrame {
    private JPanel contentPane;
    private DBConnection dbConnection;
    private JTextField txtConsumerName;
    private JComboBox<String> cmbAvailability, cmbTableNo;

    public TableBook() {
        dbConnection = new DBConnection(); // Initialize dbConnection object
        this.setTitle("Table Booking");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(250, 200, 1000, 540);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        JLabel lblTableNo = new JLabel("Table No:");
        lblTableNo.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblTableNo.setBounds(570, 50, 100, 25);
        contentPane.add(lblTableNo);

        cmbTableNo = new JComboBox<>();
        cmbTableNo.setEditable(true);
        cmbTableNo.setFont(new Font("Tahoma", Font.BOLD, 15));
        cmbTableNo.setBounds(720, 50, 100, 25);
        contentPane.add(cmbTableNo);

        JLabel lblConsumerName = new JLabel("Consumer Name:");
        lblConsumerName.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblConsumerName.setBounds(570, 80, 150, 25);
        contentPane.add(lblConsumerName);

        txtConsumerName = new JTextField();
        txtConsumerName.setFont(new Font("Tahoma", Font.BOLD, 15));
        txtConsumerName.setBounds(720, 80, 100, 25);
        contentPane.add(txtConsumerName);

        JLabel lblAvailability = new JLabel("Availability:");
        lblAvailability.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblAvailability.setBounds(570, 110, 100, 25);
        contentPane.add(lblAvailability);

        cmbAvailability = new JComboBox<>();
        cmbAvailability.setFont(new Font("Tahoma", Font.BOLD, 15));
        cmbAvailability.addItem("Available");
        cmbAvailability.addItem("Occupied");
        cmbAvailability.setBounds(720, 110, 100, 25);
        contentPane.add(cmbAvailability);

        JButton btnBook = new JButton("Book");
        btnBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bookOrUnbookTable();
            }
        });
        btnBook.setFont(new Font("Tahoma", Font.BOLD, 25));
        btnBook.setBounds(570, 140, 100, 30);
        btnBook.setBackground(Color.DARK_GRAY);
        btnBook.setForeground(Color.GREEN);
        contentPane.add(btnBook);

        JButton btnUnbook = new JButton("Unbook");
        btnUnbook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bookOrUnbookTable();
            }
        });
        btnUnbook.setFont(new Font("Tahoma", Font.BOLD, 25));
        btnUnbook.setBounds(720, 140, 130, 30);
        btnUnbook.setBackground(Color.DARK_GRAY);
        btnUnbook.setForeground(Color.GREEN);
        contentPane.add(btnUnbook);

        JButton btnLoadData = new JButton("Load Data");
        btnLoadData.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (e.getSource() == btnLoadData) {

                        refreshTableData();
                    }
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        });

        btnLoadData.setFont(new Font("Tahoma", Font.BOLD, 25));
        btnLoadData.setBounds(100, 400, 200, 30);
        btnLoadData.setBackground(Color.DARK_GRAY);
        btnLoadData.setForeground(Color.GREEN);
        contentPane.add(btnLoadData);

        JButton btnRemoveTable = new JButton("Remove Table");
        btnRemoveTable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String tableNumber = JOptionPane.showInputDialog(null, "Enter the table number to remove:");
                    if (Validation.isValidTableNo(tableNumber)) {
                        Connection con = dbConnection.mkDataBase();
                        String deleteQuery = "DELETE FROM foodtable WHERE table_number=?";
                        PreparedStatement deleteStatement = con.prepareStatement(deleteQuery);
                        deleteStatement.setString(1, tableNumber);
                        int rowsAffected = deleteStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "Table removed successfully.");
                            refreshTableData();
                        } else {
                            JOptionPane.showMessageDialog(null, "Table not found.");
                        }

                        deleteStatement.close();
                        con.close();
                    } else {
                        JOptionPane.showMessageDialog(null, "Please enter a valid table number.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error removing table.");
                }
            }
        });
        btnRemoveTable.setFont(new Font("Tahoma", Font.BOLD, 25));
        btnRemoveTable.setBounds(330, 400, 250, 30);
        btnRemoveTable.setBackground(Color.DARK_GRAY);
        btnRemoveTable.setForeground(Color.GREEN);
        contentPane.add(btnRemoveTable);

        setVisible(true);
        getContentPane().setBackground(Color.LIGHT_GRAY);
    }

    private void refreshTableData() {
        String[] columnNames = { "Consumer", "Table Number", "Availability", "Table Type" };
        Object[][] row = new Object[50][4];

        PreparedStatement pst;
        ResultSet rs;
        DBConnection con = new DBConnection();
        try {
            pst = con.mkDataBase().prepareStatement("select * from foodtable");
            rs = pst.executeQuery();
            int i = 0;
            while (rs.next()) {
                row[i][0] = rs.getString("consumer");
                row[i][1] = rs.getString("table_number");
                row[i][2] = rs.getString("availability");
                row[i][3] = rs.getString("table_type");
                i++;
            }

            rs.close();
            pst.close();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Error");
            JOptionPane.showMessageDialog(null, "Error !");
        }

        JTable table = new JTable(row, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 0, 550, 300); // Adjust the bounds as needed
        contentPane.add(scrollPane);

        // Populate cmbTableNo with table numbers where availability is 'Available'
        try {
            cmbTableNo.removeAllItems(); // Clear previous items
            Connection con2 = dbConnection.mkDataBase();
            String query = "select table_number from foodtable where availability='Available'";
            PreparedStatement pst2 = con2.prepareStatement(query);
            ResultSet rs2 = pst2.executeQuery();
            while (rs2.next()) {
                cmbTableNo.addItem(rs2.getString("table_number"));
            }
            rs2.close();
            pst2.close();
            con2.close();
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    private void bookOrUnbookTable() {
        try {
            String tableNo = cmbTableNo.getSelectedItem().toString();
            String consumerName = txtConsumerName.getText();
            String availability = cmbAvailability.getSelectedItem().toString();

            Connection con = dbConnection.mkDataBase();
            String updateQuery = "update foodtable set consumer=?, availability=? where table_number=?";
            PreparedStatement updateStatement = con.prepareStatement(updateQuery);
            updateStatement.setString(1, consumerName);
            updateStatement.setString(2, availability);
            updateStatement.setString(3, tableNo);
            updateStatement.executeUpdate();

            refreshTableData();

            updateStatement.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TableBook();
    }
}
