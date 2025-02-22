package managefood;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.Properties;

public class RevenueCalculate extends JFrame {

    private JLabel lblTitle;
    private JLabel lblRevenue;
    private JButton btnDay;
    private JButton btnMonth;
    private JButton btnYear;
    private JDatePickerImpl datePicker;

    private Connection con;

    public RevenueCalculate() {
        setTitle("Revenue Record");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        setLocationRelativeTo(null); // Center the frame

        lblTitle = new JLabel("Revenue Calculate");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        add(lblTitle, gbc);

        lblRevenue = new JLabel("Total Revenue: ");
        lblRevenue.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        add(lblRevenue, gbc);

        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        add(datePicker, gbc);

        btnDay = new JButton("Revenue of Day");
        btnDay.setFont(new Font("Arial", Font.PLAIN, 20));
        btnDay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate date = convertToLocalDate(datePicker.getModel().getValue());
                if (date != null) {
                    fetchAndCalculateRevenue(date, date.plusDays(1));
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        add(btnDay, gbc);

        btnMonth = new JButton("Revenue of Month");
        btnMonth.setFont(new Font("Arial", Font.PLAIN, 20));
        btnMonth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate date = convertToLocalDate(datePicker.getModel().getValue());
                if (date != null) {
                    YearMonth yearMonth = YearMonth.of(date.getYear(), date.getMonth());
                    fetchAndCalculateRevenue(yearMonth.atDay(1), yearMonth.plusMonths(1).atDay(1));
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        add(btnMonth, gbc);

        btnYear = new JButton("Revenue of Year");
        btnYear.setFont(new Font("Arial", Font.PLAIN, 20));
        btnYear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate date = convertToLocalDate(datePicker.getModel().getValue());
                if (date != null) {
                    LocalDate startOfYear = date.withDayOfYear(1);
                    LocalDate startOfNextYear = date.plusYears(1).withDayOfYear(1);
                    fetchAndCalculateRevenue(startOfYear, startOfNextYear);
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        add(btnYear, gbc);

        setVisible(true);
        DBConnection dbConnection = new DBConnection();
        this.con = dbConnection.mkDataBase();
    }

    // Method to convert java.util.Date to java.time.LocalDate
    private LocalDate convertToLocalDate(Object value) {
        if (value instanceof java.util.Date) {
            return ((java.util.Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return null;
    }

    private void fetchAndCalculateRevenue(LocalDate startDate, LocalDate endDate) {
        try {
            String query = "SELECT foodprize, food_quantity FROM orderfoodrecord WHERE date >= ? AND date < ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setDate(1, java.sql.Date.valueOf(startDate));
            pst.setDate(2, java.sql.Date.valueOf(endDate));
            ResultSet rs = pst.executeQuery();

            double total = 0;
            while (rs.next()) {
                double price = rs.getDouble("foodprize");
                int quantity = rs.getInt("food_quantity");
                total += price * quantity;
            }
            lblRevenue.setText("Total Revenue: " + total);

            rs.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RevenueCalculate();
            }
        });
    }

    class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

        private String datePattern = "yyyy-MM-dd";
        private DateTimeFormatter dateTimeFormatter;

        public DateLabelFormatter() {
            dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);
        }

        @Override
        public Object stringToValue(String text) throws ParseException {
            return LocalDate.parse(text, dateTimeFormatter);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value instanceof GregorianCalendar) {
                LocalDate localDate = ((GregorianCalendar) value).toZonedDateTime().toLocalDate();
                return dateTimeFormatter.format(localDate);
            } else {
                return "";
            }
        }
    }
}
