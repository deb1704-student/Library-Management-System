import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class LibraryController {

    private LibraryModel model;
    private LibraryView view;

    public LibraryController(LibraryModel model, LibraryView view) {
        this.model = model;
        this.view = view;

        refreshDashboard();
    }

    // ---------- DASHBOARD ----------

    public void refreshDashboard() {

        try {
            int[] stats = model.getDashboardStats();

            view.updateDashboardStats(
                    stats[0],
                    stats[1],
                    stats[2],
                    stats[3]);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error loading dashboard: " + e.getMessage());
        }
    }

    // ---------- BOOKS ----------

    public void showBooks() {

        try (Connection conn = model.connect();
                ResultSet rs = model.getAllBooks(conn)) {

            view.showBooks(rs);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error loading books: " + e.getMessage());
        }
    }

    // ---------- MEMBERS ----------

    public void showMembers() {

        try (Connection conn = model.connect();
                ResultSet rs = model.getAllMembers(conn)) {

            view.showMembers(rs);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error loading members: " + e.getMessage());
        }
    }

    // ---------- ISSUE BOOK ----------

    public void issueBook() {
        try {
            int[] details = view.getIssueBookDetails();
            int bookId = details[0];
            int memberId = details[1];

            if (bookId <= 0 || memberId <= 0) {
                view.showIssueFormMessage(
                        "Book ID and Member ID must be positive numbers.", false);
                return;
            }

            model.issueBook(bookId, memberId);
            view.clearIssueBookForm();
            view.showIssueFormMessage(
                    "Book issued successfully. Due date is in 7 days.", true);
            refreshDashboard();
            refreshBooksAndLoans();

        } catch (NumberFormatException e) {
            view.showIssueFormMessage(
                    "Enter valid numeric Book ID and Member ID.", false);

        } catch (IllegalArgumentException e) {
            view.showIssueFormMessage(
                    e.getMessage(), false);

        } catch (SQLException e) {
            view.showIssueFormMessage(
                    "Unable to issue book: " + e.getMessage(), false);
        }
    }

    // ---------- RETURN BOOK ----------

    public void returnBook() {
        try {
            int loanId = view.getLoanId();
            if (loanId <= 0) {
                view.showReturnFormMessage(
                        "Loan ID must be a positive number.", false);
                return;
            }

            double fine = model.returnBook(loanId);
            view.clearReturnBookForm();
            view.setReturnFine(fine);
            view.showReturnFormMessage(
                    String.format("Book returned successfully. Fine: ₹%.2f", fine), true);
            refreshDashboard();
            refreshBooksAndLoans();

        } catch (NumberFormatException e) {
            view.showReturnFormMessage(
                    "Enter a valid numeric Loan ID.", false);

        } catch (IllegalArgumentException e) {
            view.showReturnFormMessage(
                    e.getMessage(), false);

        } catch (SQLException e) {
            view.showReturnFormMessage(
                    "Unable to return book: " + e.getMessage(), false);
        }
    }

    // ---------- SEARCH BOOKS ----------

    public void searchBooks() {

        String keyword = view.getSearchKeyword();

        if (keyword == null) {
            keyword = "";
        }

        try (Connection conn = model.connect();
                ResultSet rs = model.searchBooks(conn, keyword)) {

            view.showSearchResults(rs);

        } catch (SQLException e) {
            view.showSearchMessage(
                    "Search failed: " + e.getMessage(), false);
        }
    }

    // ---------- ADD BOOK ----------

    public void addBook() {

        try {
            String[] details = view.getBookDetails();

            String title = details[0].trim();
            String author = details[1].trim();

            if (title.isEmpty()) {
                view.showBookFormMessage(
                        "Book title cannot be empty.", false);
                return;
            }

            if (title.length() > 200) {
                view.showBookFormMessage(
                        "Book title cannot exceed 200 characters.", false);
                return;
            }

            if (title.matches(".*\\s{2,}.*")) {
                view.showBookFormMessage(
                        "Book title cannot contain consecutive spaces.", false);
                return;
            }

            if (author.isEmpty()) {
                view.showBookFormMessage(
                        "Author name cannot be empty.", false);
                return;
            }

            if (author.length() > 100) {
                view.showBookFormMessage(
                        "Author name cannot exceed 100 characters.", false);
                return;
            }

            if (!author.matches("(?U)[\\p{L}\\p{M}][\\p{L}\\p{M} .'-]{0,99}")) {
                view.showBookFormMessage(
                        "Enter a valid author name.", false);
                return;
            }

            int quantity = Integer.parseInt(details[2].trim());

            if (quantity <= 0) {
                view.showBookFormMessage(
                        "Quantity must be greater than zero.", false);
                return;
            }
            if (quantity > 10000) {
                view.showBookFormMessage(
                        "Quantity cannot exceed 10,000.", false);
                return;
            }

            model.addBook(title, author, quantity);

            view.clearBookForm();
            view.showBookFormMessage(
                    "Book added successfully!", true);

            refreshDashboard();

        } catch (NumberFormatException e) {
            view.showBookFormMessage(
                    "Please enter a valid quantity.", false);

        } catch (IllegalArgumentException e) {
            view.showBookFormMessage(e.getMessage(), false);
        } catch (SQLException e) {
            view.showBookFormMessage(
                    "Database error: " + e.getMessage(), false);
        }
    }

    // ---------- REGISTER MEMBER ----------

    public void registerMember() {
        try {
            String[] details = view.getMemberDetails();
            String name = details[0].trim();
            String email = details[1].trim();
            String phone = details[2].trim();

            if (name.isEmpty()) {
                view.showMemberFormMessage(
                        "Member name cannot be empty.", false);
                return;
            }

            if (name.length() > 100) {
                view.showMemberFormMessage(
                        "Member name cannot exceed 100 characters.", false);
                return;
            }

            if (!name.matches("(?U)[\\p{L}\\p{M}][\\p{L}\\p{M} .'-]{0,99}")) {
                view.showMemberFormMessage(
                        "Enter a valid name. Numbers and special symbols are not allowed.", false);
                return;
            }

            if (email.length() > 254) {
                view.showMemberFormMessage(
                        "Email address cannot exceed 254 characters.", false);
                return;
            }

            if (!email.isEmpty() &&
                    !email.matches(
                            "^[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?)+$")) {

                view.showMemberFormMessage(
                        "Enter a valid email address.", false);
                return;
            }

            if (!phone.isEmpty() && !phone.matches("[6-9][0-9]{9}")) {
                view.showMemberFormMessage(
                        "Enter a valid 10-digit Indian mobile number.", false);
                return;
            }

            model.registerMember(
                    name,
                    email.isEmpty() ? null : email,
                    phone.isEmpty() ? null : phone);

            view.clearMemberForm();
            view.showMemberFormMessage(
                    "Member registered successfully!", true);
            refreshDashboard();

        } catch (IllegalArgumentException e) {
            view.showMemberFormMessage(
                    e.getMessage(), false);

        } catch (SQLException e) {
            view.showMemberFormMessage(
                    "Database error: " + e.getMessage(), false);
        }
    }

    private void refreshBooksAndLoans() {
        try (Connection conn = model.connect();
                ResultSet books = model.getAllBooks(conn)) {
            view.updateBooksTable(books);
        } catch (SQLException e) {
            System.err.println("Could not refresh books: " + e.getMessage());
        }

        try (Connection conn = model.connect();
                ResultSet loans = model.getAllLoans(conn)) {
            view.updateLoansTable(loans);
        } catch (SQLException e) {
            System.err.println("Could not refresh loans: " + e.getMessage());
        }
    }

    // ---------- LOAN RECORDS ----------

    public void showLoans() {

        try (Connection conn = model.connect();
                ResultSet rs = model.getAllLoans(conn)) {

            view.showLoans(rs);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error loading loans: " + e.getMessage());
        }
    }
}
