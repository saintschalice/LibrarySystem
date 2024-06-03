import gui.BookBorrowingForm;
import gui.BookListForm;
import gui.BookReturningForm;
import gui.BorrowerListForm;
import gui.LoginForm;
import gui.PenaltyForm;
import gui.RegisterForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Main extends JFrame {
    public Main() {
        setTitle("Library System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JButton bookListButton = new JButton("Book List");
        JButton borrowerListButton = new JButton("Borrower List");
        JButton bookBorrowingButton = new JButton("Book Borrowing");
        JButton bookReturningButton = new JButton("Book Returning");
        JButton penaltyButton = new JButton("Penalty");
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        bookListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BookListForm().setVisible(true);
            }
        });

        borrowerListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BorrowerListForm().setVisible(true);
            }
        });

        bookBorrowingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BookBorrowingForm().setVisible(true);
            }
        });

        bookReturningButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BookReturningForm().setVisible(true);
            }
        });

        penaltyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PenaltyForm().setVisible(true);
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginForm().setVisible(true);
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterForm().setVisible(true);
            }
        });
       

        add(bookListButton);
        add(borrowerListButton);
        add(bookBorrowingButton);
        add(bookReturningButton);
        add(penaltyButton);
        add(loginButton);
        add(registerButton);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}