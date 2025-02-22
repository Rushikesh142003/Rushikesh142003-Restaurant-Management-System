package managefood;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Home {
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JPanel controlPanel;
    private JLabel backgroundLabel;
    private BufferedImage originalImage;

    public Home() {
        prepareGUI();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("Restaurant Management System");
        mainFrame.setBounds(100, 100, 1150, 800);

        // Load the original background image
        try {
            // Assuming the image is located in the "images" directory relative to your
            // classpath
            originalImage = ImageIO.read(getClass().getResource("\\images\\HomePageImage.jpg"));
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
        backgroundLabel.setBounds(0, 0, mainFrame.getWidth(), mainFrame.getHeight());

        mainFrame.setContentPane(backgroundLabel);

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        headerLabel = new JLabel("Restaurant Management System", JLabel.CENTER);
        headerLabel.setFont(new Font(null, Font.BOLD, 27));
        headerLabel.setForeground(Color.white);
        headerLabel.setBounds(20, 0, mainFrame.getWidth(), 50);

        controlPanel = new JPanel();
        controlPanel.setLayout(null);
        controlPanel.setOpaque(false);
        controlPanel.setBounds(0, 0, mainFrame.getWidth(), mainFrame.getHeight());

        backgroundLabel.add(controlPanel);
        controlPanel.add(headerLabel);

        mainFrame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                int frameWidth = mainFrame.getWidth();
                int frameHeight = mainFrame.getHeight();

                backgroundLabel.setBounds(0, 0, frameWidth, frameHeight);
                headerLabel.setBounds(20, 0, frameWidth, 50);
                controlPanel.setBounds(0, 0, frameWidth, frameHeight);

                // Repaint to update the background image size
                backgroundLabel.repaint();
            }
        });

        mainFrame.setVisible(true);
        controlPanel.setVisible(true);
    }

    public void showButtonDemo() {
        JButton profileButton = new JButton("Profile");
        JButton billButton = new JButton("Create Bill ");
        JButton addfoodButton = new JButton("Add Food");
        JButton updatefoodButton = new JButton("Update Food");
        JButton deletefoodButton = new JButton("Delete Food");
        JButton foodinfoButton = new JButton("Food Information");
        JButton recordButton = new JButton("Order Record");
        JButton addTableButton = new JButton("Add table");
        JButton tableBookButton = new JButton("Book table");
        JButton revenueRecordButton = new JButton("Revenue Record");

        profileButton.setBounds(20, 100, 120, 50);
        billButton.setBounds(20, 150, 120, 50);
        foodinfoButton.setBounds(20, 200, 120, 50);
        addfoodButton.setBounds(20, 250, 120, 50);
        updatefoodButton.setBounds(20, 300, 120, 50);
        deletefoodButton.setBounds(20, 350, 120, 50);
        recordButton.setBounds(20, 400, 120, 50);
        addTableButton.setBounds(20,450,120,50);
        tableBookButton.setBounds(20,500,120,50);
        revenueRecordButton.setBounds(20,550,120,50);
        controlPanel.add(foodinfoButton);
        controlPanel.add(billButton);
        controlPanel.add(addfoodButton);
        controlPanel.add(updatefoodButton);
        controlPanel.add(deletefoodButton);
        controlPanel.add(profileButton);
        controlPanel.add(recordButton);
        controlPanel.add(addTableButton);
        controlPanel.add(tableBookButton);
        controlPanel.add(revenueRecordButton);

        foodinfoButton.addActionListener(e -> {
            ItemInfo ii = new ItemInfo();
            ii.showButtonDemo();
        });

        billButton.addActionListener(e -> {
            BillFrame billFrame = new BillFrame();
            billFrame.showBillFrame();
        });

        addfoodButton.addActionListener(e -> {
            EnterFood ef = new EnterFood();
            ef.showButtonDemo();
        });

        updatefoodButton.addActionListener(e -> {
            UpdateFood uf = new UpdateFood();
            uf.showButtonDemo();
        });

        deletefoodButton.addActionListener(e -> {
            DeleteFood df = new DeleteFood();
            df.showButtonDemo();
        });

        profileButton.addActionListener(e -> {
            new Profile();
        });
        recordButton.addActionListener(e -> {
            new FoodOrderRecord();
        });
        addTableButton.addActionListener(e -> {
            new AddTable();
        });
        tableBookButton.addActionListener(e -> new TableBook());
        revenueRecordButton.addActionListener(event -> new RevenueCalculate());

        mainFrame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Home home = new Home();
            home.showButtonDemo();
        });
    }
}
