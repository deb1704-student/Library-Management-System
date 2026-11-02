import javax.swing.SwingUtilities;
import java.sql.SQLException;

public class LibraryManagementSystem {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryModel model = new LibraryModel();

            try {
                model.createTables();
                System.out.println("Database initialized successfully.");

                LibraryView view = new LibraryView();
                LibraryController controller = new LibraryController(model, view);

                view.getBooksButton().addActionListener(e -> controller.showBooks());
                view.getMembersButton().addActionListener(e -> controller.showMembers());
                view.getLoansButton().addActionListener(e -> controller.showLoans());

                view.getIssueButton().addActionListener(e -> view.showIssueBookPage());
                view.getReturnButton().addActionListener(e -> view.showReturnBookPage());
                view.getSearchButton().addActionListener(e -> view.showSearchBooksPage());
                view.getSearchSubmitButton().addActionListener(e -> controller.searchBooks());

                view.getAddBookButton().addActionListener(e -> view.showAddBookPage());
                view.getRegisterMemberButton().addActionListener(e -> view.showRegisterMemberPage());

                view.getSaveBookButton().addActionListener(e -> controller.addBook());
                view.getSaveMemberButton().addActionListener(e -> controller.registerMember());
                view.getSubmitIssueButton().addActionListener(e -> controller.issueBook());
                view.getSubmitReturnButton().addActionListener(e -> controller.returnBook());

            } catch (SQLException e) {
                javax.swing.JOptionPane.showMessageDialog(
                        null,
                        "Database initialization failed: " + e.getMessage(),
                        "Database Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                );
                e.printStackTrace();
            }
        });
    }
}
