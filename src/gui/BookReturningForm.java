package gui;

import database.DatabaseConnection;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

public class BookReturningForm extends JFrame {
    private JTextField bookIdField, borrowerIdField;
    private JLabel messageLabel;

    public BookReturningForm() {

        if (!LoginForm.isLoggedIn) {
            JOptionPane.showMessageDialog(null, "You must log in first!");
            dispose();
            return;
        }
        setTitle("Book Returning");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(5, 2));

        JLabel bookIdLabel = new JLabel("Book ID:");
        bookIdField = new JTextField();
        JLabel borrowerIdLabel = new JLabel("Borrower ID:");
        borrowerIdField = new JTextField();
        messageLabel = new JLabel("");
        JButton returnButton = new JButton("Return");

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnBook();
            }
        });

        add(bookIdLabel);
        add(bookIdField);
        add(borrowerIdLabel);
        add(borrowerIdField);
        add(new JLabel());  // Empty cell
        add(returnButton);
        add(messageLabel);  // Display message

    }

    private void returnBook() {
        int bookId = Integer.parseInt(bookIdField.getText());
        int borrowerId = Integer.parseInt(borrowerIdField.getText());

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if the book was borrowed by the borrower
            String borrowQuery = "SELECT id, date_borrowed, due_date, date_returned FROM borrow_records WHERE book_id = ? AND borrower_id = ? AND date_returned IS NULL";
            PreparedStatement borrowStmt = conn.prepareStatement(borrowQuery);
            borrowStmt.setInt(1, bookId);
            borrowStmt.setInt(2, borrowerId);
            ResultSet borrowRs = borrowStmt.executeQuery();

            if (borrowRs.next()) {
                int borrowRecordId = borrowRs.getInt("id");
                Date dueDate = borrowRs.getDate("due_date");
                Date dateBorrowed = borrowRs.getDate("date_borrowed");

                long diffInMillies = Math.abs(new Date(System.currentTimeMillis()).getTime() - dueDate.getTime());
                long diff = diffInMillies / (24 * 60 * 60 * 1000);

                if (diff > 0) {
                    // Calculate penalty
                    double penaltyAmount = diff * 20;  // 20 pesos per day for students
                    String penaltyQuery = "INSERT INTO penalties (borrow_record_id, amount_due, amount_paid) VALUES (?, ?, 0)";
                    PreparedStatement penaltyStmt = conn.prepareStatement(penaltyQuery);
                    penaltyStmt.setInt(1, borrowRecordId);
                    penaltyStmt.setDouble(2, penaltyAmount);
                    penaltyStmt.executeUpdate();

                    messageLabel.setText("Book returned with a penalty of " + penaltyAmount + " pesos.");
                } else {
                    messageLabel.setText("Book returned on time.");
                }

                // Update borrow record
                String updateBorrowQuery = "UPDATE borrow_records SET date_returned = NOW() WHERE id = ?";
                PreparedStatement updateBorrowStmt = conn.prepareStatement(updateBorrowQuery);
                updateBorrowStmt.setInt(1, borrowRecordId);
                updateBorrowStmt.executeUpdate();

                // Update book status
                String updateBookQuery = "UPDATE books SET status = 'returned' WHERE id = ?";
                PreparedStatement updateBookStmt = conn.prepareStatement(updateBookQuery);
                updateBookStmt.setInt(1, bookId);
                updateBookStmt.executeUpdate();
            } else {
                messageLabel.setText("No matching borrow record found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Error returning book.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BookReturningForm().setVisible(true);
        });
    }
}
