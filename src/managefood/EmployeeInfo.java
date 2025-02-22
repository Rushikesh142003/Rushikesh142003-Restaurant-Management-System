package managefood;
import java.awt.*;
import javax.swing.border.EmptyBorder; 
import java.sql.*;
import javax.swing.*;
import java.awt.event.ActionListener; 
import java.awt.event.ActionEvent;

public class EmployeeInfo extends JFrame {
    private JPanel contentPane; 
    private JLabel headerLabel;
    private JPanel controlPanel;

    public EmployeeInfo() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setBounds(300, 150, 1000, 600);
        setUndecorated(true);
        contentPane = new JPanel(); 
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane); 
        contentPane.setLayout(null); 

        headerLabel = new JLabel();
        headerLabel.setBounds(300, 10, 400, 30);
        contentPane.add(headerLabel);

        controlPanel = new JPanel();
        controlPanel.setBounds(0, 50, 1000, 450);
        contentPane.add(controlPanel);

        JButton btnLoadData = new JButton("Load Data"); 
        btnLoadData.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { 
                showButtonDemo();
            }
        });
        btnLoadData.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnLoadData.setBounds(310, 520, 120, 30); 
        btnLoadData.setBackground(Color.DARK_GRAY); 
        btnLoadData.setForeground(Color.GREEN);
        contentPane.add(btnLoadData);

        JButton btnExit = new JButton("close"); 
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { 
                dispose();
            }
        });
        btnExit.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnExit.setBounds(470, 520, 120, 30); 
        btnExit.setBackground(Color.DARK_GRAY); 
        btnExit.setForeground(Color.GREEN);
        contentPane.add(btnExit); 

        JButton btnRemoveEmployee = new JButton("Remove Employee");
        btnRemoveEmployee.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String eid = JOptionPane.showInputDialog(null, "Enter Employee ID to remove:");
                if (eid != null && !eid.isEmpty()) {
                    removeEmployee(eid);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a valid Employee ID.");
                }
            }
        });
        btnRemoveEmployee.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnRemoveEmployee.setBounds(610, 520, 200, 30);
        btnRemoveEmployee.setBackground(Color.DARK_GRAY);
        btnRemoveEmployee.setForeground(Color.GREEN);
        contentPane.add(btnRemoveEmployee);

        setVisible(true);
        getContentPane().setBackground(Color.LIGHT_GRAY);
    }

    public void showButtonDemo() {
        headerLabel.setText("Employee Information");
        headerLabel.setFont(new Font(null, Font.BOLD, 25));
    
        String[] columnNames = { "id","Name", "Age", "Gender", "Job", "Salary", "Phone", "Aadhar", "Email" };
        Object[][] data = new Object[100][9]; // Assuming maximum 100 employees for simplicity
    
        PreparedStatement pst;
        ResultSet rs;
        DBConnection con = new DBConnection();
        try {
            pst = con.mkDataBase().prepareStatement("SELECT * FROM employee");
            rs = pst.executeQuery();
            int i = 0;
            while (rs.next() && i < 100) { // Limiting to 100 rows for simplicity
                data[i][0] = rs.getString("eid");
                data[i][1] = rs.getString("name");
                data[i][2] = rs.getString("age");
                data[i][3] = rs.getString("gender");
                data[i][4] = rs.getString("job");
                data[i][5] = rs.getString("salary");
                data[i][6] = rs.getString("phone");
                data[i][7] = rs.getString("aadhar");
                data[i][8] = rs.getString("email");
                i++;
            }
    
            JTable table = new JTable(data, columnNames);
            table.setPreferredScrollableViewportSize(new Dimension(900, 400)); // Set preferred size
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Prevent automatic resizing of columns
            // Set column widths
            table.getColumnModel().getColumn(0).setPreferredWidth(150);//eid
            table.getColumnModel().getColumn(1).setPreferredWidth(150); // Name
            table.getColumnModel().getColumn(2).setPreferredWidth(50);  // Age
            table.getColumnModel().getColumn(3).setPreferredWidth(80);  // Gender
            table.getColumnModel().getColumn(4).setPreferredWidth(150); // Job
            table.getColumnModel().getColumn(5).setPreferredWidth(100); // Salary
            table.getColumnModel().getColumn(6).setPreferredWidth(100); // Phone
            table.getColumnModel().getColumn(7).setPreferredWidth(120); // Aadhar
            table.getColumnModel().getColumn(8).setPreferredWidth(200); // Email
    
            table.setVisible(true);
            controlPanel.removeAll(); // Clear previous content
            controlPanel.add(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
    
            // Refresh the content pane
            controlPanel.revalidate();
            controlPanel.repaint();
        } catch (Exception ex) {
            ex.printStackTrace(); // Print stack trace for debugging
            JOptionPane.showMessageDialog(null, "Error fetching data from the database: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void removeEmployee(String eid) {
        try {
            DBConnection con = new DBConnection();
            PreparedStatement pst = con.mkDataBase().prepareStatement("DELETE FROM employee WHERE eid = ?");
            pst.setString(1, eid);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Employee with ID " + eid + " removed successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "No employee found with ID " + eid, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error removing employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        new EmployeeInfo();
    }
}
