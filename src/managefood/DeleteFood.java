package managefood;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DeleteFood {
   private JFrame mainFrame;
   private JLabel headerLabel;
   private JPanel controlPanel;
   private JLabel idLabel;

   GridLayout experimentLayout = new GridLayout(1, 1);
   ResultSet rs;

   DeleteFood() {
      prepareGUI();
   }

   private void prepareGUI() {
      mainFrame = new JFrame("Delete food item!");
      mainFrame.setSize(700, 400);
      mainFrame.getContentPane().setBackground(Color.pink);

      mainFrame.setLayout(new GridLayout(3, 1));
      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent) {
            mainFrame.dispose();
         }
      });
      headerLabel = new JLabel("", JLabel.CENTER);
      controlPanel = new JPanel();
      controlPanel.setLayout(new FlowLayout());

      mainFrame.add(headerLabel);
      mainFrame.add(controlPanel);
      mainFrame.setVisible(true);
   }

   public void showButtonDemo() {
      headerLabel.setText("Restaurant Management System");
      headerLabel.setFont(new Font(null, Font.BOLD, 27));
      headerLabel.setForeground(Color.white);

      idLabel = new JLabel("Enter Food id");
      JTextField foodid = new JTextField();
      foodid.setSize(100, 30);

      JButton okButton = new JButton("DELETE");

      okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
               int id = Integer.parseInt(foodid.getText());
               if (id <= 0) {
                  throw new IllegalArgumentException("Food ID must be a positive integer.");
               }

               PreparedStatement pst;
               DBConnection con = new DBConnection();
               try {
                  pst = con.mkDataBase().prepareStatement("DELETE FROM food WHERE id = ?");
                  pst.setInt(1, id);

                  // Retrieve food_name before deleting
                  String deletedFoodName = getFoodNameById(String.valueOf(id));

                  int rowsAffected = pst.executeUpdate();
                  if (rowsAffected > 0) {
                     JOptionPane.showMessageDialog(null, "Item Deleted: " + deletedFoodName);
                  } else {
                     JOptionPane.showMessageDialog(null, "Item not found: " + id);
                  }
                  // Clear text field after successful delete
                  foodid.setText("");
               } catch (Exception ex) {
                  JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
               }
            } catch (NumberFormatException ex) {
               JOptionPane.showMessageDialog(null, "Please enter a valid numeric ID.");
            } catch (IllegalArgumentException ex) {
               JOptionPane.showMessageDialog(null, ex.getMessage());
            }
         }
      });

      JPanel jp = new JPanel();
      jp.add(idLabel);
      jp.add(foodid);
      jp.setSize(700, 400);
      jp.setLayout(experimentLayout);
      controlPanel.add(jp);
      jp.add(okButton);
      mainFrame.setVisible(true);
      mainFrame.setLocationRelativeTo(null);
   }

   // Method to retrieve food_name by id
   private String getFoodNameById(String id) {
      PreparedStatement pst;
      ResultSet rs;
      DBConnection con = new DBConnection();
      try {
         pst = con.mkDataBase().prepareStatement("SELECT food_name FROM food WHERE id = ?");
         pst.setString(1, id);
         rs = pst.executeQuery();

         if (rs.next()) {
            return rs.getString("food_name");
         }
      } catch (Exception ex) {
         ex.printStackTrace();
      }
      return null; // Return null if not found
   }

   public static void main(String[] args) {
      DeleteFood swingControlDemo = new DeleteFood();
      swingControlDemo.showButtonDemo();
   }

}
