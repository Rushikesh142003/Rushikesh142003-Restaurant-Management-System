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
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EnterFood {

   private JFrame mainFrame;
   private JLabel headerLabel;
   private JPanel controlPanel;
   private JLabel id, name, price;

   GridLayout experimentLayout = new GridLayout(0, 2);
   ResultSet rs;

   EnterFood() {
      prepareGUI();
   }

   private void prepareGUI() {
      mainFrame = new JFrame("Insert a new food item!");
      mainFrame.setSize(700, 600);
      mainFrame.setLayout(new GridLayout(3, 1));
      mainFrame.getContentPane().setBackground(Color.pink);
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

      id = new JLabel("Food id");
      JTextField foodid = new JTextField();
      foodid.setSize(100, 40);

      name = new JLabel("Food Name");
      JTextField foodname = new JTextField();
      foodname.setSize(100, 40);

      price = new JLabel("Food Price");
      JTextField foodprize = new JTextField();
      foodprize.setSize(100, 40);

      JButton okButton = new JButton("save");

      okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
               int foodId = Integer.parseInt(foodid.getText());
               String foodName = foodname.getText();
               double foodPrice = Double.parseDouble(foodprize.getText());

               if (foodId <= 0) {
                  throw new IllegalArgumentException("Food ID must be a positive integer.");
               }
               if (foodid.getText().isEmpty()) {
                  throw new IllegalArgumentException("Food ID cannot be empty.");
               }
               if (foodName.isBlank()) {
                  throw new IllegalArgumentException("Food name cannot be empty.");
               }
               if (foodPrice <= 0) {
                  throw new IllegalArgumentException("Food price must be a positive number.");
               }
               if (foodprize.getText().isEmpty()) {
                  throw new IllegalArgumentException("Food price cannot be empty.");
               }

               PreparedStatement pst;
               DBConnection con = new DBConnection();
               try {
                  pst = con.mkDataBase().prepareStatement("insert into food(id,food_name,price) values (?,?,?)");
                  pst.setInt(1, foodId);
                  pst.setString(2, foodName);
                  pst.setDouble(3, foodPrice);
                  pst.execute();

                  JOptionPane.showMessageDialog(null, "Done Inserting " + foodName);
                  // Clear text fields after successful insertion
                  foodid.setText("");
                  foodname.setText("");
                  foodprize.setText("");
               } catch (SQLException ex) {
                  JOptionPane.showMessageDialog(null, "Food id already used " + ex.getMessage());
               }
            } catch (NumberFormatException ex) {
               JOptionPane.showMessageDialog(null, "Please enter valid numeric values for ID and Price.");
            } catch (IllegalArgumentException ex) {
               JOptionPane.showMessageDialog(null, ex.getMessage());
            } catch (Exception ex) {
               System.out.println(ex);
               JOptionPane.showMessageDialog(null, "Inserting Error: " + ex.getMessage());
            }
         }
      });

      JPanel jp = new JPanel(null);
      jp.add(id);
      jp.add(foodid);
      jp.add(name);
      jp.add(foodname);
      jp.add(price);
      jp.add(foodprize);
      jp.setSize(100, 600);
      jp.setLayout(experimentLayout);
      controlPanel.add(jp);
      jp.add(okButton);
      mainFrame.setLocationRelativeTo(null);
      mainFrame.setVisible(true);
   }

   public static void main(String[] args) {
      EnterFood swingControlDemo = new EnterFood();
      swingControlDemo.showButtonDemo();
   }
}
