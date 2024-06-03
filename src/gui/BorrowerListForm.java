package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import database.DatabaseConnection;

public class BorrowerListForm extends JFrame {
    private JTable borrowerTable;

    public BorrowerListForm() {
        setTitle("Borrower List");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"Name", "Borrower Type", "Student ID", "Employee ID", "Year Level", "Section", "Department"};
        Object[][] data = fetchBorrowerData();

        borrowerTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(borrowerTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private Object[][] fetchBorrowerData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT name, borrower_type, student_id, employee_id, year_level, section, department FROM borrowers";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();
            Object[][] data = new Object[rowCount][7];
            int rowIndex = 0;
            while (rs.next()) {
                data[rowIndex][0] = rs.getString("name");
                data[rowIndex][1] = rs.getString("borrower_type");
                data[rowIndex][2] = rs.getString("student_id");
                data[rowIndex][3] = rs.getString("employee_id");
                data[rowIndex][4] = rs.getInt("year_level");
                data[rowIndex][5] = rs.getString("section");
                data[rowIndex][6] = rs.getString("department");
                rowIndex++;
            }
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Object[0][0];
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BorrowerListForm().setVisible(true);
        });
    }
}
