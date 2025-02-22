package managefood;

import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.*;

public class AddTable extends JFrame implements ActionListener {
    private JTextField t1;
    private JComboBox<String> comboBox_1, combobox_2;
    private BufferedImage originalImage;
    JLabel addtablelbl, tableNoL, availableL, typeL, backgroundLabel;
    JButton addTableBtn, backBtn;
    Choice c1;
    String combo1[] = { "Available", "Occupied" };
    String combo3[] = { "private", "public" };

    public AddTable() {
        setBounds(300, 250, 900, 450);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        setLayout(null);
        setResizable(false);
        setUndecorated(true);

        // Load the original background image
        try {
            originalImage = ImageIO.read(getClass().getResource("\\images\\table.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        backgroundLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (originalImage != null) {
                    // Scale the original image based on the current frame size
                    Image scaledImage = originalImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                    g.drawImage(scaledImage, 0, 0, this);
                }
            }
        };

        backgroundLabel.setLayout(null);
        backgroundLabel.setBounds(0, 0, getWidth(), getHeight());

        setContentPane(backgroundLabel);

        addtablelbl = new JLabel("Add Table");
        addtablelbl.setFont(new Font("Tahoma", Font.BOLD, 25));
        addtablelbl.setForeground(Color.BLUE);
        addtablelbl.setBounds(130, 10, 150, 30);
        add(addtablelbl);

        tableNoL = new JLabel("table Number");
        tableNoL.setForeground(new Color(169, 16, 176));
        tableNoL.setFont(new Font("Tahoma", Font.BOLD, 14));
        tableNoL.setBounds(60, 70, 130, 25);
        add(tableNoL);

        t1 = new JTextField();
        t1.setBounds(200, 70, 156, 25);
        add(t1);

        availableL = new JLabel("Availability");
        availableL.setForeground(new Color(169, 16, 176));
        availableL.setFont(new Font("Tahoma", Font.BOLD, 14));
        availableL.setBounds(60, 110, 130, 25);
        add(availableL);

        comboBox_1 = new JComboBox<String>(combo1);
        comboBox_1.setBounds(200, 110, 154, 25);
        add(comboBox_1);

        typeL = new JLabel("Table Type");
        typeL.setForeground(new Color(169, 16, 176));
        typeL.setFont(new Font("Tahoma", Font.BOLD, 14));
        typeL.setBounds(60, 150, 154, 25);
        add(typeL);

        combobox_2 = new JComboBox<String>(combo3);
        combobox_2.setBounds(200, 150, 154, 25);
        add(combobox_2);

        addTableBtn = new JButton("Add");
        addTableBtn.addActionListener(this);
        addTableBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
        addTableBtn.setBounds(60, 250, 130, 33);
        addTableBtn.setBackground(Color.DARK_GRAY);
        addTableBtn.setForeground(Color.GREEN);
        add(addTableBtn);

        backBtn = new JButton("Back");
        backBtn.addActionListener(this);
        backBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
        backBtn.setBounds(220, 250, 130, 33);
        backBtn.setBackground(Color.DARK_GRAY);
        backBtn.setForeground(Color.GREEN);
        add(backBtn);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        DBConnection con = new DBConnection();
        try {
            if (ae.getSource() == addTableBtn) {
                String tableno = t1.getText();
                String available = (String) comboBox_1.getSelectedItem();
                String type = (String) combobox_2.getSelectedItem();

                if (Validation.isValidTableNo(tableno)) {
                    try {
                        con.mkDataBase();
                        Connection connection = con.mkDataBase();

                        // Create a PreparedStatement
                        String sql = "INSERT INTO foodtable (table_number, availability, table_type) VALUES (?, ?, ?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, tableno);
                        preparedStatement.setString(2, available);
                        preparedStatement.setString(3, type);

                        // Execute the query
                        preparedStatement.executeUpdate();

                        JOptionPane.showMessageDialog(null, "Table Successfully Added");

                        // Reset all fields
                        t1.setText("");
                        comboBox_1.setSelectedIndex(0);
                        combobox_2.setSelectedIndex(0);
                    } catch (SQLException ee) {
                        ee.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Table no already used");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a valid table number (e.g., 1, 2, 3, ...)");
                }
            } else if (ae.getSource() == backBtn) {
                this.dispose();
            }
        } catch (Exception eee) {
            eee.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new AddTable();
    }

}
