package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import database.DatabaseConnection;
import models.Borrower;

public class BookBorrowingForm extends JFrame {
    private JTextField borrowerIdField, bookIdField;
    private JLabel messageLabel;

    public BookBorrowingForm() {
        setTitle("Book Borrowing");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(5, 2));

        JLabel borrowerIdLabel = new JLabel("Borrower ID:");
        borrowerIdField = new JTextField();
        JLabel bookIdLabel = new JLabel("Book ID:");
        bookIdField = new JTextField();
        messageLabel = new JLabel("");
        JButton borrowButton = new JButton("Borrow");

        borrowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrowBook();
            }
        });

        add(borrowerIdLabel);
        add(borrowerIdField);
        add(bookIdLabel);
        add(bookIdField);
        add(new JLabel());  // Empty cell
        add(borrowButton);
        add(messageLabel);  // Display message

    }

    private void borrowBook() {
        int borrowerId = Integer.parseInt(borrowerIdField.getText());
        int bookId = Integer.parseInt(bookIdField.getText());
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check book availability
            String bookQuery = "SELECT status, category FROM books WHERE id = ?";
            PreparedStatement bookStmt = conn.prepareStatement(bookQuery);
            bookStmt.setInt(1, bookId);
            ResultSet bookRs = bookStmt.executeQuery();
            
            if (bookRs.next()) {
                String status = bookRs.getString("status");
                String category = bookRs.getString("category");
                
                if ("borrowed".equals(status)) {
                    messageLabel.setText("Book is already borrowed.");
                    return;
                }

                if ("Academic".equals(category)) {
                    messageLabel.setText("Academic books cannot be borrowed.");
                    return;
                }
            } else {
                messageLabel.setText("Book not found.");
                return;
            }
            
            // Check borrower details and borrowing limits
            String borrowerQuery = "SELECT borrower_type FROM borrowers WHERE id = ?";
            PreparedStatement borrowerStmt = conn.prepareStatement(borrowerQuery);
            borrowerStmt.setInt(1, borrowerId);
            ResultSet borrowerRs = borrowerStmt.executeQuery();
            
            if (borrowerRs.next()) {
                String borrowerType = borrowerRs.getString("borrower_type");

                String borrowCountQuery = "SELECT COUNT(*) AS borrow_count FROM borrow_records WHERE borrower_id = ? AND date_returned IS NULL";
                PreparedStatement borrowCountStmt = conn.prepareStatement(borrowCountQuery);
                borrowCountStmt.setInt(1, borrowerId);
                ResultSet borrowCountRs = borrowCountStmt.executeQuery();
                
                if (borrowCountRs.next()) {
                    int borrowCount = borrowCountRs.getInt("borrow_count");
                    if ("Student".equals(borrowerType) && borrowCount >= 2) {
                        messageLabel.setText("Student has exceeded the borrowing limit.");
                        return;
                    }
                    if ("Teacher".equals(borrowerType) && borrowCount >= 5) {
                        messageLabel.setText("Teacher has exceeded the borrowing limit.");
                        return;
                    }
                }
                
                // Borrow book
                String borrowQuery = "INSERT INTO borrow_records (borrower_id, book_id, date_borrowed, due_date) VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 3 DAY))";
                PreparedStatement borrowStmt = conn.prepareStatement(borrowQuery);
                borrowStmt.setInt(1, borrowerId);
                borrowStmt.setInt(2, bookId);
                borrowStmt.executeUpdate();

                // Update book status
                String updateBookQuery = "UPDATE books SET status = 'borrowed' WHERE id = ?";
                PreparedStatement updateBookStmt = conn.prepareStatement(updateBookQuery);
                updateBookStmt.setInt(1, bookId);
                updateBookStmt.executeUpdate();

                messageLabel.setText("Book borrowed successfully.");
            } else {
                messageLabel.setText("Borrower not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Error borrowing book.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BookBorrowingForm().setVisible(true);
        });
    }
}
