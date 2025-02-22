package managefood;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ItemInfo {
   private JFrame mainFrame;
   private JLabel headerLabel;
   private JPanel controlPanel;

   ItemInfo() {
      prepareGUI();
   }

   private void prepareGUI() {
      mainFrame = new JFrame("Showing all items");
      mainFrame.setBounds(200, 200, 700, 500);
      mainFrame.getContentPane().setBackground(Color.pink);
      mainFrame.setLayout(new FlowLayout());

      headerLabel = new JLabel("", JLabel.CENTER);
      controlPanel = new JPanel();
      controlPanel.setBounds(200, 200, 700, 500);
      controlPanel.setLayout(new FlowLayout());

      mainFrame.add(headerLabel);
      mainFrame.add(controlPanel);

      mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 

      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent) {
            mainFrame.dispose(); // Dispose the JFrame to release associated resources
         }
      });
   }

   public void showButtonDemo(){
      headerLabel.setText("Restaurant Management System");
      headerLabel.setFont(new Font(null, Font.BOLD, 25));

      String[] columnNames = { "Food ID", "Food Name", "Price" };
      Object[][] data = new Object[100][3];

      PreparedStatement pst;
      ResultSet rs;
      DBConnection con = new DBConnection();
      try {
         pst = con.mkDataBase().prepareStatement("select * from food");
         rs = pst.executeQuery();
         int i = 0;
         while (rs.next()) {
            data[i][0] = rs.getInt("id");
            data[i][1] = rs.getString("food_name");
            data[i][2] = rs.getInt("price");
            i++;
         }

         // mainFrame.setVisible(false);
      } catch (Exception ex) {
         System.out.println(ex);
         System.out.println("Error");
         JOptionPane.showMessageDialog(null, "Error !");
      }

      JTable table = new JTable(data, columnNames);
      table.setSize(400, 400);
      table.setVisible(true);
      controlPanel.add(
            new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

      mainFrame.setVisible(true);
      mainFrame.setLocationRelativeTo(null);

   }

   public static void main(String[] args) {
         ItemInfo swingControlDemo = new ItemInfo();
         swingControlDemo.showButtonDemo();
      
   }
}
