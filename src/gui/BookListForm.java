package gui;

import database.DatabaseConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class BookListForm extends JFrame {
    private JTable bookTable;

    public BookListForm() {

        if (!LoginForm.isLoggedIn) {
            JOptionPane.showMessageDialog(null, "You must log in first!");
            dispose();
            return;
        }

        setTitle("Book List");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"Title", "ISBN", "Category", "Author", "Copyright Year", "Publisher", "Status"};
        Object[][] data = fetchBookData();

        bookTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private Object[][] fetchBookData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT title, isbn, category, author, copyright_year, publisher, status FROM books";
            PreparedStatement stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery();
            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();
            Object[][] data = new Object[rowCount][7];
            int rowIndex = 0;
            while (rs.next()) {
                data[rowIndex][0] = rs.getString("title");
                data[rowIndex][1] = rs.getString("isbn");
                data[rowIndex][2] = rs.getString("category");
                data[rowIndex][3] = rs.getString("author");
                data[rowIndex][4] = rs.getInt("copyright_year");
                data[rowIndex][5] = rs.getString("publisher");
                data[rowIndex][6] = rs.getString("status");
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
            new BookListForm().setVisible(true);
        });
    }
}