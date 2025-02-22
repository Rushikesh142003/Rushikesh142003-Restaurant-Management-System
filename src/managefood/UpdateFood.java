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

public class UpdateFood {
   private JFrame mainFrame;
   private JLabel headerLabel;
   private JLabel statusLabel;
   private JPanel controlPanel;
   private JLabel id, name, price;
   GridLayout experimentLayout = new GridLayout(0, 2);
   ResultSet rs;

   UpdateFood() {
      prepareGUI();
   }

   private void prepareGUI() {
      mainFrame = new JFrame("Update!");
      mainFrame.setSize(700, 500);
      mainFrame.setLayout(new GridLayout(3, 1));
      mainFrame.getContentPane().setBackground(Color.pink);
      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent) {
            mainFrame.dispose();
         }
      });
      headerLabel = new JLabel("", JLabel.CENTER);
      statusLabel = new JLabel("", JLabel.CENTER);
      statusLabel.setSize(350, 400);
      controlPanel = new JPanel();
      controlPanel.setLayout(new FlowLayout());
      mainFrame.add(headerLabel);
      mainFrame.add(controlPanel);
      mainFrame.add(statusLabel);
      mainFrame.setVisible(true);
   }

   public void showButtonDemo() {
      headerLabel.setText("Restaurant Management System");
      headerLabel.setFont(new Font(null, Font.BOLD, 27));

      id = new JLabel("Food id");
      JTextField foodId = new JTextField();
      foodId.setSize(100, 40);

      name = new JLabel("Food Name");
      JTextField foodName = new JTextField();
      foodName.setSize(100, 30);

      price = new JLabel("Food Price");
      JTextField foodPrize = new JTextField();
      foodPrize.setSize(100, 30);

      JButton updateButton = new JButton("UPDATE");

      updateButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
               int id = Integer.parseInt(foodId.getText());
               if (id <= 0) {
                  throw new IllegalArgumentException("Food ID must be a positive integer.");
               }

               String name = foodName.getText();
               if (name.isBlank()) {
                  throw new IllegalArgumentException("Food name cannot be empty.");
               }

               double price = Double.parseDouble(foodPrize.getText());
               if (price <= 0) {
                  throw new IllegalArgumentException("Food price must be a positive number.");
               }

               try {
                  PreparedStatement pst;
                  DBConnection con = new DBConnection();
                  pst = con.mkDataBase().prepareStatement("UPDATE food SET food_name= ? ,price=? where id = ?");
                  pst.setString(1, name);
                  pst.setDouble(2, price);
                  pst.setInt(3, id);
                  pst.execute();

                  JOptionPane.showMessageDialog(null, "Done Updating " + name);
                  // Clear text fields after successful updation
                  foodName.setText("");
                  foodPrize.setText("");
                  foodId.setText("");
               } catch (Exception ex) {
                  JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage());
               }
            } catch (NumberFormatException ex) {
               JOptionPane.showMessageDialog(null, "Please enter valid numeric values for ID and Price.");
            } catch (IllegalArgumentException ex) {
               JOptionPane.showMessageDialog(null, ex.getMessage());
            }
         }
      });

      JPanel jp = new JPanel();
      jp.add(id);
      jp.add(foodId);
      jp.add(name);
      jp.add(foodName);
      jp.add(price);
      jp.add(foodPrize);
      jp.setSize(200, 200);
      jp.setLayout(experimentLayout);
      controlPanel.add(jp);
      jp.add(updateButton);
      mainFrame.setVisible(true);
      mainFrame.setLocationRelativeTo(null);
   }

   public static void main(String[] args) {
      UpdateFood swingControlDemo = new UpdateFood();
      swingControlDemo.showButtonDemo();
   }
}
