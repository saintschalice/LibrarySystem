package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import database.DatabaseConnection;

public class PenaltyForm extends JFrame {
    private JTextField penaltyIdField, amountPaidField;
    private JLabel messageLabel;

    public PenaltyForm() {

        if (!LoginForm.isLoggedIn) {
            JOptionPane.showMessageDialog(null, "You must log in first!");
            dispose();
            return;
        }
        setTitle("Penalty Payment");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(5, 2));

        JLabel penaltyIdLabel = new JLabel("Penalty ID:");
        penaltyIdField = new JTextField();
        JLabel amountPaidLabel = new JLabel("Amount Paid:");
        amountPaidField = new JTextField();
        messageLabel = new JLabel("");
        JButton payButton = new JButton("Pay");

        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                payPenalty();
            }
        });

        add(penaltyIdLabel);
        add(penaltyIdField);
        add(amountPaidLabel);
        add(amountPaidField);
        add(new JLabel());  // Empty cell
        add(payButton);
        add(messageLabel);  // Display message
    }

    private void payPenalty() {
        int penaltyId = Integer.parseInt(penaltyIdField.getText());
        double amountPaid = Double.parseDouble(amountPaidField.getText());

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if the penalty exists and is not yet fully paid
            String penaltyQuery = "SELECT amount_due, amount_paid FROM penalties WHERE id = ?";
            PreparedStatement penaltyStmt = conn.prepareStatement(penaltyQuery);
            penaltyStmt.setInt(1, penaltyId);
            ResultSet penaltyRs = penaltyStmt.executeQuery();

            if (penaltyRs.next()) {
                double amountDue = penaltyRs.getDouble("amount_due");
                double currentAmountPaid = penaltyRs.getDouble("amount_paid");

                if (currentAmountPaid + amountPaid > amountDue) {
                    messageLabel.setText("Amount exceeds the penalty due.");
                } else {
                    double newAmountPaid = currentAmountPaid + amountPaid;
                    String updatePenaltyQuery = "UPDATE penalties SET amount_paid = ? WHERE id = ?";
                    PreparedStatement updatePenaltyStmt = conn.prepareStatement(updatePenaltyQuery);
                    updatePenaltyStmt.setDouble(1, newAmountPaid);
                    updatePenaltyStmt.setInt(2, penaltyId);
                    updatePenaltyStmt.executeUpdate();

                    messageLabel.setText("Penalty paid successfully.");
                }
            } else {
                messageLabel.setText("Penalty not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Error processing payment.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PenaltyForm().setVisible(true);
        });
    }
}
