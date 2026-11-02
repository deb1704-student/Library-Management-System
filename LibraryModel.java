import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LibraryModel {

    private String url = "jdbc:sqlite:library.db";

    public Connection connect() throws SQLException {
        Connection conn = DriverManager.getConnection(url);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }
        return conn;
    }

    public void createTables() throws SQLException {
        try (Connection conn = connect();
                Statement stmt = conn.createStatement()) {

            String createBooks = "CREATE TABLE IF NOT EXISTS books (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL CHECK(length(trim(title)) BETWEEN 1 AND 200)," +
                    "author TEXT NOT NULL CHECK(length(trim(author)) BETWEEN 1 AND 100)," +
                    "quantity INTEGER NOT NULL CHECK(quantity BETWEEN 1 AND 10000)" +
                    ")";
            stmt.executeUpdate(createBooks);

            String createMembers = "CREATE TABLE IF NOT EXISTS members (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL CHECK(length(trim(name)) BETWEEN 1 AND 100)," +
                    "email TEXT CHECK(email IS NULL OR length(email) <= 254)," +
                    "phone TEXT CHECK(phone IS NULL OR phone GLOB '[6-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]')"
                    +
                    ")";
            stmt.executeUpdate(createMembers);

            String createLoans = "CREATE TABLE IF NOT EXISTS loans (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "book_id INTEGER NOT NULL," +
                    "member_id INTEGER NOT NULL," +
                    "issue_date TEXT NOT NULL," +
                    "due_date TEXT NOT NULL," +
                    "return_date TEXT," +
                    "fine REAL NOT NULL DEFAULT 0 CHECK(fine >= 0)," +
                    "CHECK(date(due_date) IS NOT NULL)," +
                    "CHECK(date(issue_date) IS NOT NULL)," +
                    "CHECK(date(due_date) >= date(issue_date))," +
                    "CHECK(return_date IS NULL OR date(return_date) IS NOT NULL)," +
                    "CHECK(return_date IS NULL OR date(return_date) >= date(issue_date))," +
                    "FOREIGN KEY(book_id) REFERENCES books(id)," +
                    "FOREIGN KEY(member_id) REFERENCES members(id)" +
                    ")";
            stmt.executeUpdate(createLoans);
        }
    }

    public int[] getDashboardStats() throws SQLException {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            int totalBooks = 0;
            int totalMembers = 0;
            int activeLoans = 0;
            int overdueLoans = 0;

            try (ResultSet rs = stmt.executeQuery(
                    "SELECT COALESCE(SUM(quantity), 0) FROM books")) {
                if (rs.next())
                    totalBooks = rs.getInt(1);
            }
            try (ResultSet rs = stmt.executeQuery(
                    "SELECT COUNT(*) FROM members")) {
                if (rs.next())
                    totalMembers = rs.getInt(1);
            }
            try (ResultSet rs = stmt.executeQuery(
                    "SELECT COUNT(*) FROM loans WHERE return_date IS NULL")) {
                if (rs.next())
                    activeLoans = rs.getInt(1);
            }
            try (ResultSet rs = stmt.executeQuery(
                    "SELECT COUNT(*) FROM loans WHERE return_date IS NULL " +
                            "AND date(due_date) < date('now', 'localtime')")) {
                if (rs.next())
                    overdueLoans = rs.getInt(1);
            }
            return new int[] { totalBooks, totalMembers, activeLoans, overdueLoans };
        }
    }

    public void addBook(String title, String author, int quantity) throws SQLException {
        if (title == null || title.trim().isEmpty() || title.trim().length() > 200) {
            throw new IllegalArgumentException("Invalid book title.");
        }

        if (author == null || author.trim().isEmpty() || author.trim().length() > 100) {
            throw new IllegalArgumentException("Invalid author name.");
        }

        if (quantity < 1 || quantity > 10000) {
            throw new IllegalArgumentException("Quantity must be between 1 and 10,000.");
        }

        title = title.trim();
        author = author.trim();

        String sql = "INSERT INTO books(title, author, quantity) VALUES (?, ?, ?)";

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setInt(3, quantity);

            pstmt.executeUpdate();
        }
    }

    public ResultSet getAllBooks(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = "SELECT id, title, author, quantity FROM books";
        return stmt.executeQuery(sql);
    }

    public void registerMember(String name, String email, String phone) throws SQLException {

        if (name == null || name.trim().isEmpty() || name.trim().length() > 100) {
            throw new IllegalArgumentException("Invalid member name.");
        }

        if (!name.trim().matches("(?U)[\\p{L}\\p{M}][\\p{L}\\p{M} .'-]{0,99}")) {
            throw new IllegalArgumentException("Enter a valid member name.");
        }

        if (email != null && !email.trim().isEmpty()) {
            email = email.trim();

            if (email.length() > 254 ||
                    !email.matches(
                            "^[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?)+$")) {
                throw new IllegalArgumentException("Enter a valid email address.");
            }
        } else {
            email = null;
        }

        if (phone != null && !phone.trim().isEmpty()) {
            phone = phone.trim();

            if (!phone.matches("[6-9][0-9]{9}")) {
                throw new IllegalArgumentException("Enter a valid 10-digit Indian mobile number.");
            }
        } else {
            phone = null;
        }

        name = name.trim();

        String sql = "INSERT INTO members(name, email, phone) VALUES(?, ?, ?)";

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);

            pstmt.executeUpdate();
        }
    }

    public ResultSet getAllMembers(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = "SELECT id, name, email, phone FROM members";
        return stmt.executeQuery(sql);
    }

    public void issueBook(int bookId, int memberId) throws SQLException {

        if (bookId <= 0) {
            throw new IllegalArgumentException("Invalid book ID.");
        }

        if (memberId <= 0) {
            throw new IllegalArgumentException("Invalid member ID.");
        }

        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(7);

        String insertLoan = "INSERT INTO loans(book_id, member_id, issue_date, due_date) " +
                "VALUES(?, ?, ?, ?)";

        String updateBook = "UPDATE books SET quantity = quantity - 1 " +
                "WHERE id = ? AND quantity > 0";

        try (Connection conn = connect();
                PreparedStatement loanStmt = conn.prepareStatement(insertLoan);
                PreparedStatement bookStmt = conn.prepareStatement(updateBook)) {

            try {
                conn.setAutoCommit(false);

                bookStmt.setInt(1, bookId);

                int rowsUpdated = bookStmt.executeUpdate();

                if (rowsUpdated == 0) {
                    throw new SQLException("Book is not available or does not exist.");
                }

                loanStmt.setInt(1, bookId);
                loanStmt.setInt(2, memberId);
                loanStmt.setString(3, issueDate.toString());
                loanStmt.setString(4, dueDate.toString());

                loanStmt.executeUpdate();

                conn.commit();

            } catch (SQLException | RuntimeException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public ResultSet getAllLoans(Connection conn) throws SQLException {

        Statement stmt = conn.createStatement();

        String sql = "SELECT loans.id, " +
                "books.title, " +
                "members.name, " +
                "loans.issue_date, " +
                "loans.due_date, " +
                "loans.return_date, " +
                "loans.fine " +
                "FROM loans " +
                "JOIN books ON loans.book_id = books.id " +
                "JOIN members ON loans.member_id = members.id";

        return stmt.executeQuery(sql);
    }

    public double returnBook(int loanId) throws SQLException {

        if (loanId <= 0) {
            throw new IllegalArgumentException("Invalid loan ID.");
        }

        LocalDate returnDate = LocalDate.now();

        String findLoan = "SELECT book_id, due_date " +
                "FROM loans " +
                "WHERE id = ? AND return_date IS NULL";

        String updateLoan = "UPDATE loans " +
                "SET return_date = ?, fine = ? " +
                "WHERE id = ? AND return_date IS NULL";

        String updateBook = "UPDATE books " +
                "SET quantity = quantity + 1 " +
                "WHERE id = ?";

        try (Connection conn = connect();
                PreparedStatement findStmt = conn.prepareStatement(findLoan);
                PreparedStatement loanStmt = conn.prepareStatement(updateLoan);
                PreparedStatement bookStmt = conn.prepareStatement(updateBook)) {

            try {
                conn.setAutoCommit(false);

                findStmt.setInt(1, loanId);

                int bookId;
                LocalDate dueDate;

                try (ResultSet rs = findStmt.executeQuery()) {

                    if (!rs.next()) {
                        throw new SQLException(
                                "Loan not found or book already returned.");
                    }

                    bookId = rs.getInt("book_id");
                    dueDate = LocalDate.parse(rs.getString("due_date"));
                }

                double fine = calculateFine(dueDate, returnDate);

                loanStmt.setString(1, returnDate.toString());
                loanStmt.setDouble(2, fine);
                loanStmt.setInt(3, loanId);

                int loanRows = loanStmt.executeUpdate();

                if (loanRows != 1) {
                    throw new SQLException("Unable to update loan record.");
                }

                bookStmt.setInt(1, bookId);

                int bookRows = bookStmt.executeUpdate();

                if (bookRows != 1) {
                    throw new SQLException("Unable to update book quantity.");
                }

                conn.commit();

                return fine;

            } catch (SQLException | RuntimeException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public double calculateFine(LocalDate dueDate, LocalDate returnDate) {

        if (dueDate == null || returnDate == null) {
            throw new IllegalArgumentException(
                    "Due date and return date cannot be null.");
        }

        long overdueDays = ChronoUnit.DAYS.between(
                dueDate,
                returnDate);

        if (overdueDays > 0) {
            return overdueDays * 5.0;
        }

        return 0.0;
    }

    public ResultSet searchBooks(Connection conn, String keyword)
            throws SQLException {

        if (conn == null) {
            throw new IllegalArgumentException(
                    "Database connection cannot be null.");
        }

        if (keyword == null) {
            keyword = "";
        }

        keyword = keyword.trim();

        String sql = "SELECT id, title, author, quantity " +
                "FROM books " +
                "WHERE title LIKE ? OR author LIKE ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        String searchPattern = "%" + keyword + "%";

        pstmt.setString(1, searchPattern);
        pstmt.setString(2, searchPattern);

        return pstmt.executeQuery();
    }

}
