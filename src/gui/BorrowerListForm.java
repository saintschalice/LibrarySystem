package gui;

import database.DatabaseConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class BorrowerListForm extends JFrame {
    private JTable borrowerTable;

    public BorrowerListForm() {

        if (!LoginForm.isLoggedIn) {
            JOptionPane.showMessageDialog(null, "You must log in first!");
            dispose();
            return;
        }

        setTitle("Borrower List");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"First Name", "Last Name", "Borrower Type", "Student ID", "Employee ID", "Year Level", "Section", "Department"};
        Object[][] data = fetchBorrowerData();

        borrowerTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(borrowerTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private Object[][] fetchBorrowerData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT first_name, last_name, borrower_type, student_id, employee_id, year_level, section, department FROM borrowers";
            PreparedStatement stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery();
            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();
            Object[][] data = new Object[rowCount][8];
            int rowIndex = 0;
            while (rs.next()) {
                data[rowIndex][0] = rs.getString("first_name");
                data[rowIndex][1] = rs.getString("last_name");
                data[rowIndex][2] = rs.getString("borrower_type");
                data[rowIndex][3] = rs.getString("student_id");
                data[rowIndex][4] = rs.getString("employee_id");
                data[rowIndex][5] = rs.getInt("year_level");
                data[rowIndex][6] = rs.getString("section");
                data[rowIndex][7] = rs.getString("department");
                rowIndex++;
            }
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching data from the database: " + e.getMessage());
            return new Object[0][0];
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BorrowerListForm().setVisible(true);
        });
    }
}
