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
    private JButton bookListButton;
    private JButton borrowerListButton;
    private JButton bookBorrowingButton;
    private JButton bookReturningButton;
    private JButton penaltyButton;
    private JButton loginButton;
    private JButton registerButton;

    public Main() {
        setTitle("Library System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        bookListButton = new JButton("Book List");
        borrowerListButton = new JButton("Borrower List");
        bookBorrowingButton = new JButton("Book Borrowing");
        bookReturningButton = new JButton("Book Returning");
        penaltyButton = new JButton("Penalty");
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

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

        add(loginButton);
        add(registerButton);
        add(bookListButton);
        add(borrowerListButton);
        add(bookBorrowingButton);
        add(bookReturningButton);
        add(penaltyButton);

        updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        boolean isLoggedIn = LoginForm.isLoggedIn;
        bookListButton.setVisible(isLoggedIn);
        borrowerListButton.setVisible(isLoggedIn);
        bookBorrowingButton.setVisible(isLoggedIn);
        bookReturningButton.setVisible(isLoggedIn);
        penaltyButton.setVisible(isLoggedIn);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main mainFrame = new Main();
            mainFrame.setVisible(true);

            // Add a listener to update button visibility after login
            LoginForm.addLoginListener(() -> {
                mainFrame.updateButtonVisibility();
            });
        });
    }
}