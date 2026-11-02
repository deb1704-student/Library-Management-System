import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibraryView {

    // ---------- THEME ----------

    private final Color SIDEBAR = new Color(25, 35, 55);
    private final Color BACKGROUND = new Color(245, 247, 250);
    private final Color ACCENT = new Color(79, 100, 220);
    private final Color TEXT = new Color(35, 45, 65);
    private final Color MUTED = new Color(120, 130, 145);
    private final Color BORDER = new Color(230, 233, 240);

    // ---------- MAIN COMPONENTS ----------

    private JFrame frame;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel headerTitle;

    // Navigation buttons
    private JButton booksButton, membersButton;
    private JButton issueButton, returnButton;
    private JButton searchButton, addBookButton;
    private JButton registerMemberButton, loansButton;
    private JButton dashboardButton;

    // Tables
    private JTable booksTable;
    private JTable membersTable;
    private JTable loansTable;

    // Dashboard statistics
    private JLabel booksCountLabel;
    private JLabel membersCountLabel;
    private JLabel issuedCountLabel;
    private JLabel overdueCountLabel;

    // Add Book form
    private JTextField bookTitleField;
    private JTextField bookAuthorField;
    private JTextField bookQuantityField;

    private JButton saveBookButton;
    private JLabel bookFormMessage;

    // Register Member form
    private JTextField memberNameField;
    private JTextField memberEmailField;
    private JTextField memberPhoneField;

    private JButton saveMemberButton;
    private JLabel memberFormMessage;

    // Issue Book form
    private JTextField issueBookIdField;
    private JTextField issueMemberIdField;
    private JTextField issueDateField;
    private JTextField dueDateField;
    private JButton submitIssueButton;
    private JLabel issueFormMessage;

    // Return Book form
    private JTextField returnLoanIdField;
    private JTextField returnDateField;
    private JTextField returnFineField;
    private JButton submitReturnButton;
    private JLabel returnFormMessage;

    // Search Books page
    private JTextField searchField;
    private JButton executeSearchButton;
    private JLabel searchMessage;
    private JTable searchResultsTable;


    // ---------- CONSTRUCTOR ----------

    public LibraryView() {
        createWindow();
    }


    // ---------- MAIN WINDOW ----------

    private void createWindow() {

        frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 760);
        frame.setMinimumSize(new Dimension(950, 600));
        frame.setLocationRelativeTo(null);

        frame.setLayout(new BorderLayout());

        frame.add(createSidebar(), BorderLayout.WEST);
        frame.add(createMainArea(), BorderLayout.CENTER);

        frame.setVisible(true);
    }


    // ---------- SIDEBAR ----------

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(SIDEBAR);
        sidebar.setPreferredSize(new Dimension(235, 0));

        // Logo
        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(
                BorderFactory.createEmptyBorder(30, 20, 20, 20)
        );

        JLabel logo = new JLabel("LIBRARY");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 24));

        JLabel subtitle = new JLabel("MANAGEMENT SYSTEM");
        subtitle.setForeground(new Color(160, 175, 200));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 10));

        top.add(logo);
        top.add(Box.createVerticalStrut(5));
        top.add(subtitle);
        top.add(Box.createVerticalStrut(35));

        // Navigation
        JPanel navigation = new JPanel();
        navigation.setOpaque(false);
        navigation.setLayout(
                new BoxLayout(navigation, BoxLayout.Y_AXIS)
        );

        navigation.setBorder(
                BorderFactory.createEmptyBorder(0, 12, 0, 12)
        );

        addSectionLabel(navigation, "WORKSPACE");

        dashboardButton = createNavButton("Dashboard");
        booksButton = createNavButton("Books");
        membersButton = createNavButton("Members");

        addNavButton(navigation, dashboardButton);
        addNavButton(navigation, booksButton);
        addNavButton(navigation, membersButton);

        navigation.add(Box.createVerticalStrut(25));

        addSectionLabel(navigation, "TRANSACTIONS");

        issueButton = createNavButton("Issue Book");
        returnButton = createNavButton("Return Book");
        loansButton = createNavButton("Loan Records");

        addNavButton(navigation, issueButton);
        addNavButton(navigation, returnButton);
        addNavButton(navigation, loansButton);

        navigation.add(Box.createVerticalStrut(25));

        addSectionLabel(navigation, "MANAGEMENT");

        addBookButton = createNavButton("Add Book");
        registerMemberButton = createNavButton("Register Member");
        searchButton = createNavButton("Search Books");

        addNavButton(navigation, addBookButton);
        addNavButton(navigation, registerMemberButton);
        addNavButton(navigation, searchButton);

        // Footer
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(
                BorderFactory.createEmptyBorder(15, 20, 20, 20)
        );

        JLabel footer = new JLabel("Library Management v1.1");
        footer.setForeground(new Color(145, 160, 185));
        footer.setFont(new Font("SansSerif", Font.PLAIN, 11));

        bottom.add(footer, BorderLayout.SOUTH);

        sidebar.add(top, BorderLayout.NORTH);
        sidebar.add(navigation, BorderLayout.CENTER);
        sidebar.add(bottom, BorderLayout.SOUTH);

        // Navigation listeners
        dashboardButton.addActionListener(e ->
                showPage("Dashboard"));

        booksButton.addActionListener(e ->
                showPage("Books"));

        membersButton.addActionListener(e ->
                showPage("Members"));

        loansButton.addActionListener(e ->
                showPage("Loan Records"));

        return sidebar;
    }


    private void addSectionLabel(JPanel panel, String text) {

        JLabel label = new JLabel(text);

        label.setForeground(new Color(130, 150, 180));
        label.setFont(new Font("SansSerif", Font.BOLD, 10));

        label.setBorder(
                BorderFactory.createEmptyBorder(5, 12, 10, 0)
        );

        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
    }


    private JButton createNavButton(String text) {

        JButton button = new JButton(text);

        button.setFont(new Font("SansSerif", Font.PLAIN, 13));
        button.setForeground(new Color(220, 228, 240));
        button.setBackground(SIDEBAR);
        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.setBorder(
                BorderFactory.createEmptyBorder(12, 14, 12, 10)
        );

        button.setFocusPainted(false);
        button.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        button.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 45)
        );

        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setOpaque(true);

        return button;
    }


    private void addNavButton(JPanel panel, JButton button) {

        panel.add(button);
        panel.add(Box.createVerticalStrut(4));
    }


    // ---------- MAIN CONTENT ----------

    private JPanel createMainArea() {

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BACKGROUND);

        main.add(createHeader(), BorderLayout.NORTH);

        cardLayout = new CardLayout();

        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BACKGROUND);

        contentPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 30, 30, 30)
        );

        contentPanel.add(createDashboard(), "Dashboard");

        contentPanel.add(
                createTablePage("Books", "Manage your book collection"),
                "Books"
        );

        contentPanel.add(
                createTablePage("Members", "Manage registered members"),
                "Members"
        );

        contentPanel.add(
                createTablePage(
                        "Loan Records",
                        "Track issued and returned books"
                ),
                "Loan Records"
        );

        // New Add Book page
        contentPanel.add(createAddBookPage(), "Add Book");
        contentPanel.add(
                createRegisterMemberPage(),
                "Register Member"
        );
        contentPanel.add(createIssueBookPage(), "Issue Book");
        contentPanel.add(createReturnBookPage(), "Return Book");
        contentPanel.add(createSearchBooksPage(), "Search Books");
        main.add(contentPanel, BorderLayout.CENTER);

        return main;
    }


    // ---------- HEADER ----------

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        header.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0, 0, 1, 0, BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                18, 30, 18, 30
                        )
                )
        );

        headerTitle = new JLabel("Library Dashboard");

        headerTitle.setFont(
                new Font("SansSerif", Font.BOLD, 19)
        );

        headerTitle.setForeground(TEXT);

        JLabel status = new JLabel("LIBRARY ADMINISTRATION");

        status.setFont(
                new Font("SansSerif", Font.PLAIN, 10)
        );

        status.setForeground(MUTED);

        header.add(headerTitle, BorderLayout.WEST);
        header.add(status, BorderLayout.EAST);

        return header;
    }


    // ---------- DASHBOARD ----------

    private JPanel createDashboard() {

        JPanel dashboard = new JPanel(new BorderLayout(0, 25));
        dashboard.setBackground(BACKGROUND);

        // Welcome section
        JPanel intro = new JPanel();
        intro.setOpaque(false);
        intro.setLayout(new BoxLayout(intro, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("Welcome to your library");
        heading.setFont(new Font("SansSerif", Font.BOLD, 25));
        heading.setForeground(TEXT);

        JLabel description = new JLabel(
                "Manage books, members and library transactions from one place."
        );

        description.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );

        description.setForeground(MUTED);

        intro.add(heading);
        intro.add(Box.createVerticalStrut(8));
        intro.add(description);

        // Statistics cards
        JPanel stats = new JPanel(new GridLayout(1, 4, 16, 0));
        stats.setOpaque(false);
        stats.setPreferredSize(new Dimension(0, 145));

        stats.add(createStatCard(
                "TOTAL BOOKS", "0", "Available book copies",
                "Books", 0
        ));

        stats.add(createStatCard(
                "MEMBERS", "0", "Registered members",
                "Members", 1
        ));

        stats.add(createStatCard(
                "BOOKS ISSUED", "0", "Currently on loan",
                "Loan Records", 2
        ));

        stats.add(createStatCard(
                "OVERDUE", "0", "Pending overdue returns",
                "Loan Records", 3
        ));

        // Quick Actions
        JPanel actions = new JPanel(new BorderLayout(0, 15));
        actions.setOpaque(false);

        JLabel actionTitle = new JLabel("Quick Actions");
        actionTitle.setFont(
                new Font("SansSerif", Font.BOLD, 19)
        );

        actionTitle.setForeground(TEXT);

        JPanel actionGrid = new JPanel(
                new GridLayout(2, 2, 18, 18)
        );

        actionGrid.setOpaque(false);

        actionGrid.add(createActionCard(
                "Add a new book",
                "Expand the library collection",
                addBookButton
        ));

        actionGrid.add(createActionCard(
                "Register a member",
                "Add a new library member",
                registerMemberButton
        ));

        actionGrid.add(createActionCard(
                "Issue a book",
                "Create a new loan transaction",
                issueButton
        ));

        actionGrid.add(createActionCard(
                "Return a book",
                "Process a book return",
                returnButton
        ));

        actions.add(actionTitle, BorderLayout.NORTH);
        actions.add(actionGrid, BorderLayout.CENTER);

        // Controlled sizing
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        stats.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 145)
        );

        actionGrid.setPreferredSize(
                new Dimension(0, 250)
        );

        actionGrid.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 250)
        );

        actions.setPreferredSize(
                new Dimension(0, 290)
        );

        actions.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 290)
        );

        center.add(stats);
        center.add(Box.createVerticalStrut(25));
        center.add(actions);

        dashboard.add(intro, BorderLayout.NORTH);
        dashboard.add(center, BorderLayout.CENTER);

        return dashboard;
    }


    // ---------- DASHBOARD STAT CARDS ----------

    private JPanel createStatCard(
            String label,
            String value,
            String detail,
            String targetPage,
            int index
    ) {

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        BorderFactory.createEmptyBorder(
                                18, 16, 18, 16
                        )
                )
        );

        JLabel category = new JLabel(label);
        category.setFont(
                new Font("SansSerif", Font.BOLD, 10)
        );

        category.setForeground(ACCENT);

        JLabel number = new JLabel(value);
        number.setFont(
                new Font("SansSerif", Font.BOLD, 27)
        );

        number.setForeground(TEXT);

        JLabel info = new JLabel(detail);
        info.setFont(
                new Font("SansSerif", Font.PLAIN, 11)
        );

        info.setForeground(MUTED);

        if (index == 0) booksCountLabel = number;
        if (index == 1) membersCountLabel = number;
        if (index == 2) issuedCountLabel = number;
        if (index == 3) overdueCountLabel = number;

        card.add(category);
        card.add(Box.createVerticalStrut(12));
        card.add(number);
        card.add(Box.createVerticalStrut(6));
        card.add(info);

        MouseAdapter listener = new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                navigateTo(targetPage);
            }

            public void mouseEntered(MouseEvent e) {
                card.setBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(ACCENT),
                                BorderFactory.createEmptyBorder(
                                        18, 16, 18, 16
                                )
                        )
                );
            }

            public void mouseExited(MouseEvent e) {
                card.setBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(BORDER),
                                BorderFactory.createEmptyBorder(
                                        18, 16, 18, 16
                                )
                        )
                );
            }
        };

        card.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        card.addMouseListener(listener);
        category.addMouseListener(listener);
        number.addMouseListener(listener);
        info.addMouseListener(listener);

        return card;
    }


    // Update statistics from SQLite
    public void updateDashboardStats(
            int books,
            int members,
            int issued,
            int overdue
    ) {

        booksCountLabel.setText(String.valueOf(books));
        membersCountLabel.setText(String.valueOf(members));
        issuedCountLabel.setText(String.valueOf(issued));
        overdueCountLabel.setText(String.valueOf(overdue));
    }


    // ---------- QUICK ACTION CARDS ----------

    private JPanel createActionCard(
            String title,
            String detail,
            JButton action
    ) {

        JPanel card = new JPanel(
                new BorderLayout(10, 10)
        );

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        BorderFactory.createEmptyBorder(
                                18, 20, 18, 20
                        )
                )
        );

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);

        textPanel.setLayout(
                new BoxLayout(textPanel, BoxLayout.Y_AXIS)
        );

        JLabel name = new JLabel(title);
        name.setFont(
                new Font("SansSerif", Font.BOLD, 15)
        );

        name.setForeground(TEXT);

        JLabel info = new JLabel(detail);
        info.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );

        info.setForeground(MUTED);

        textPanel.add(name);
        textPanel.add(Box.createVerticalStrut(8));
        textPanel.add(info);

        // Compact Open button
        JButton button = new JButton("Open");

        button.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        button.setForeground(ACCENT);
        button.setBackground(new Color(239, 242, 255));
        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        8, 16, 8, 16
                )
        );

        button.setPreferredSize(
                new Dimension(80, 34)
        );

        button.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        button.addActionListener(e -> action.doClick());

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 0, 0)
        );

        buttonPanel.setOpaque(false);
        buttonPanel.add(button);

        card.add(textPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }


    // ---------- ADD BOOK PAGE ----------


    private JPanel createAddBookPage() {

        JPanel page = createFormPageHeading(
                "Add New Book",
                "Add a new book to your library collection."
        );

        JPanel fields = createFormFieldsPanel();

        bookTitleField = new JTextField();
        bookAuthorField = new JTextField();
        bookQuantityField = new JTextField();

        fields.add(createFormField("Book Title", bookTitleField));
        fields.add(Box.createVerticalStrut(14));
        fields.add(createFormField("Author", bookAuthorField));
        fields.add(Box.createVerticalStrut(14));
        fields.add(createFormField("Quantity", bookQuantityField));

        bookFormMessage = createFormMessage();
        saveBookButton = createPrimaryButton("Save Book", 120);

        JPanel form = createFormCard(fields, bookFormMessage, saveBookButton);
        page.add(centerForm(form), BorderLayout.CENTER);

        return page;
    }


    // ---------- REGISTER MEMBER PAGE ----------

    private JPanel createRegisterMemberPage() {

        JPanel page = createFormPageHeading(
                "Register New Member",
                "Add a new member to your library."
        );

        JPanel fields = createFormFieldsPanel();

        memberNameField = new JTextField();
        memberEmailField = new JTextField();
        memberPhoneField = new JTextField();

        fields.add(createFormField("Full Name", memberNameField));
        fields.add(Box.createVerticalStrut(14));
        fields.add(createFormField("Email Address", memberEmailField));
        fields.add(Box.createVerticalStrut(14));
        fields.add(createFormField("Phone Number", memberPhoneField));

        memberFormMessage = createFormMessage();
        saveMemberButton = createPrimaryButton("Register Member", 150);

        JPanel form = createFormCard(fields, memberFormMessage, saveMemberButton);
        page.add(centerForm(form), BorderLayout.CENTER);

        return page;
    }


    private JPanel createIssueBookPage() {

        JPanel page = createFormPageHeading(
                "Issue a Book",
                "Create a loan transaction for a registered member."
        );

        JPanel fields = createFormFieldsPanel();

        issueBookIdField = new JTextField();
        issueMemberIdField = new JTextField();
        issueDateField = createReadOnlyField(java.time.LocalDate.now().toString());
        dueDateField = createReadOnlyField(
                java.time.LocalDate.now().plusDays(7).toString()
        );

        fields.add(createFormField("Book ID", issueBookIdField));
        fields.add(Box.createVerticalStrut(12));
        fields.add(createFormField("Member ID", issueMemberIdField));
        fields.add(Box.createVerticalStrut(12));
        fields.add(createFormField("Issue Date", issueDateField));
        fields.add(Box.createVerticalStrut(12));
        fields.add(createFormField("Due Date (7 days)", dueDateField));

        issueFormMessage = createFormMessage();
        submitIssueButton = createPrimaryButton("Issue Book", 125);

        JPanel form = createFormCard(fields, issueFormMessage, submitIssueButton);
        page.add(centerForm(form), BorderLayout.CENTER);

        return page;
    }


    // ---------- RETURN BOOK PAGE ----------

    private JPanel createReturnBookPage() {

        JPanel page = createFormPageHeading(
                "Return a Book",
                "Complete a return and calculate any overdue fine."
        );

        JPanel fields = createFormFieldsPanel();

        returnLoanIdField = new JTextField();
        returnDateField = createReadOnlyField(java.time.LocalDate.now().toString());
        returnFineField = createReadOnlyField("Calculated on return");

        fields.add(createFormField("Loan ID", returnLoanIdField));
        fields.add(Box.createVerticalStrut(14));
        fields.add(createFormField("Return Date", returnDateField));
        fields.add(Box.createVerticalStrut(14));
        fields.add(createFormField("Fine Amount", returnFineField));

        returnFormMessage = createFormMessage();
        submitReturnButton = createPrimaryButton("Return Book", 135);

        JPanel form = createFormCard(fields, returnFormMessage, submitReturnButton);
        page.add(centerForm(form), BorderLayout.CENTER);

        return page;
    }


    // Shared page heading for all form pages
    private JPanel createFormPageHeading(String titleText, String subtitleText) {
        JPanel page = new JPanel(new BorderLayout(0, 22));
        page.setBackground(BACKGROUND);

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("SansSerif", Font.BOLD, 25));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(subtitleText);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        heading.add(title);
        heading.add(Box.createVerticalStrut(7));
        heading.add(subtitle);

        page.add(heading, BorderLayout.NORTH);
        return page;
    }


    // Input fields are kept separate from the action area.
    private JPanel createFormFieldsPanel() {
        JPanel fields = new JPanel();
        fields.setOpaque(false);
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
        fields.setAlignmentX(Component.LEFT_ALIGNMENT);
        return fields;
    }


    // The button is pinned in the card's SOUTH region, outside the fields layout.
    // This prevents BoxLayout from shrinking or clipping the action button.
    private JPanel createFormCard(
            JPanel fields,
            JLabel message,
            JButton submitButton
    ) {
        JPanel card = new JPanel(new BorderLayout(0, 18));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(24, 28, 22, 28)
        ));

        // Keep the fields in the center.
        card.add(fields, BorderLayout.CENTER);

        // Dedicated footer: message above, button anchored bottom-right.
        JPanel footer = new JPanel(new BorderLayout(0, 10));
        footer.setOpaque(false);
        footer.add(message, BorderLayout.NORTH);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonRow.setOpaque(false);
        buttonRow.add(submitButton);
        footer.add(buttonRow, BorderLayout.SOUTH);

        card.add(footer, BorderLayout.SOUTH);

        // Compute the card's natural height after all children are attached.
        Dimension preferred = card.getPreferredSize();
        card.setPreferredSize(new Dimension(720, preferred.height));
        card.setMinimumSize(new Dimension(620, preferred.height));
        card.setMaximumSize(new Dimension(720, Integer.MAX_VALUE));

        return card;
    }


    private JPanel centerForm(JPanel form) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(15, 0, 15, 0);

        wrapper.add(form, gbc);
        return wrapper;
    }


    private JTextField createReadOnlyField(String value) {
        JTextField field = new JTextField(value);
        field.setEditable(false);
        field.setBackground(new Color(247, 248, 251));
        return field;
    }


    private JLabel createFormMessage() {
        JLabel message = new JLabel(" ");
        message.setFont(new Font("SansSerif", Font.PLAIN, 12));
        message.setAlignmentX(Component.LEFT_ALIGNMENT);
        return message;
    }


    private JButton createPrimaryButton(String text, int width) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(ACCENT);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(width, 38));
        button.setMinimumSize(new Dimension(width, 38));
        button.setMaximumSize(new Dimension(width, 38));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }


    // Reusable form field
    private JPanel createFormField(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setPreferredSize(new Dimension(620, 72));
        panel.setMinimumSize(new Dimension(120, 72));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(600, 40));
        field.setMinimumSize(new Dimension(100, 40));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
        panel.add(field);
        return panel;
    }


    // ---------- SEARCH BOOKS PAGE ----------

    private JPanel createSearchBooksPage() {

        JPanel page = createFormPageHeading(
                "Search Books",
                "Find books by title or author."
        );

        JPanel body = new JPanel(new BorderLayout(0, 16));
        body.setOpaque(false);

        JPanel searchCard = new JPanel(new BorderLayout(12, 10));
        searchCard.setBackground(Color.WHITE);
        searchCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)
        ));

        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Search by book title or author");
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(TEXT);

        searchField = new JTextField();
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        searchField.setPreferredSize(new Dimension(500, 40));
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        inputPanel.add(label);
        inputPanel.add(Box.createVerticalStrut(8));
        inputPanel.add(searchField);

        executeSearchButton = createPrimaryButton("Search", 100);
        searchCard.add(inputPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 22));
        actionPanel.setOpaque(false);
        actionPanel.add(executeSearchButton);
        searchCard.add(actionPanel, BorderLayout.EAST);

        searchField.addActionListener(e -> executeSearchButton.doClick());

        searchMessage = new JLabel("Enter a title or author to search.");
        searchMessage.setFont(new Font("SansSerif", Font.PLAIN, 12));
        searchMessage.setForeground(MUTED);

        searchResultsTable = new JTable();
        styleTable(searchResultsTable);

        JScrollPane resultsScroll = new JScrollPane(searchResultsTable);
        resultsScroll.setBorder(BorderFactory.createLineBorder(BORDER));
        resultsScroll.getViewport().setBackground(Color.WHITE);

        JPanel resultsPanel = new JPanel(new BorderLayout(0, 10));
        resultsPanel.setOpaque(false);
        resultsPanel.add(searchMessage, BorderLayout.NORTH);
        resultsPanel.add(resultsScroll, BorderLayout.CENTER);

        body.add(searchCard, BorderLayout.NORTH);
        body.add(resultsPanel, BorderLayout.CENTER);

        page.add(body, BorderLayout.CENTER);
        return page;
    }


    private void styleTable(JTable table) {
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(38);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(235, 238, 243));
        table.setSelectionBackground(new Color(231, 236, 255));
        table.setSelectionForeground(TEXT);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);

        table.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );
        table.getTableHeader().setBackground(new Color(244, 246, 250));
        table.getTableHeader().setForeground(TEXT);
        table.getTableHeader().setPreferredSize(new Dimension(0, 42));
        table.getTableHeader().setReorderingAllowed(false);
    }


    // ---------- NAVIGATION ----------

    private void navigateTo(String page) {

        switch (page) {

            case "Books":
                booksButton.doClick();
                break;

            case "Members":
                membersButton.doClick();
                break;

            case "Loan Records":
                loansButton.doClick();
                break;

            default:
                showPage(page);
        }
    }


    private void showPage(String name) {

        cardLayout.show(contentPanel, name);

        if (headerTitle != null) {

            if (name.equals("Add Book")) {
                headerTitle.setText("Book Management");
            } else if (name.equals("Register Member")) {
                headerTitle.setText("Member Management");
            } else if (name.equals("Issue Book")) {
                headerTitle.setText("Book Transactions");
            } else if (name.equals("Return Book")) {
                headerTitle.setText("Book Transactions");
            } else if (name.equals("Search Books")) {
                headerTitle.setText("Book Search");
            } else {
                headerTitle.setText(name);
            }
        }
    }


    public void showAddBookPage() {
        showPage("Add Book");
    }

    public void showRegisterMemberPage() {
        showPage("Register Member");
    }

    public void showIssueBookPage() {
        issueDateField.setText(java.time.LocalDate.now().toString());
        dueDateField.setText(java.time.LocalDate.now().plusDays(7).toString());
        showPage("Issue Book");
    }

    public void showReturnBookPage() {
        returnDateField.setText(java.time.LocalDate.now().toString());
        returnFineField.setText("Calculated on return");
        showPage("Return Book");
    }


    // ---------- TABLE PAGES ----------

    private JPanel createTablePage(
            String title,
            String subtitle
    ) {

        JPanel page = new JPanel(
                new BorderLayout(0, 20)
        );

        page.setBackground(BACKGROUND);

        // Heading
        JPanel heading = new JPanel();

        heading.setOpaque(false);

        heading.setLayout(
                new BoxLayout(heading, BoxLayout.Y_AXIS)
        );

        JLabel name = new JLabel(title);

        name.setFont(
                new Font("SansSerif", Font.BOLD, 24)
        );

        name.setForeground(TEXT);

        JLabel description = new JLabel(subtitle);

        description.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );

        description.setForeground(MUTED);

        heading.add(name);
        heading.add(Box.createVerticalStrut(7));
        heading.add(description);

        // Table
        JTable table = new JTable();

        table.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );

        table.setRowHeight(38);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(235, 238, 243));

        table.setSelectionBackground(
                new Color(231, 236, 255)
        );

        table.setSelectionForeground(TEXT);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);

        table.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        table.getTableHeader().setBackground(
                new Color(244, 246, 250)
        );

        table.getTableHeader().setForeground(TEXT);

        table.getTableHeader().setPreferredSize(
                new Dimension(0, 42)
        );

        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );

        scrollPane.getViewport().setBackground(Color.WHITE);

        // Store table reference
        if (title.equals("Books")) {
            booksTable = table;

        } else if (title.equals("Members")) {
            membersTable = table;

        } else {
            loansTable = table;
        }

        page.add(heading, BorderLayout.NORTH);
        page.add(scrollPane, BorderLayout.CENTER);

        return page;
    }


    // ---------- TABLE UPDATE ----------

    private void updateTable(
            JTable table,
            String[] columns,
            Object[][] data
    ) {

        DefaultTableModel model = new DefaultTableModel(
                data, columns
        ) {
            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        table.setModel(model);
    }


    public void updateBooksTable(ResultSet rs) throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        while (rs.next()) {
            rows.add(new Object[]{
                    rs.getInt("id"), rs.getString("title"),
                    rs.getString("author"), rs.getInt("quantity")
            });
        }
        updateTable(booksTable,
                new String[]{"ID", "Title", "Author", "Quantity"},
                rows.toArray(new Object[0][]));
    }

    public void updateLoansTable(ResultSet rs) throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        while (rs.next()) {
            rows.add(new Object[]{
                    rs.getInt("id"), rs.getString("title"),
                    rs.getString("name"), rs.getString("issue_date"),
                    rs.getString("due_date"), rs.getString("return_date"),
                    rs.getDouble("fine")
            });
        }
        updateTable(loansTable,
                new String[]{"Loan ID", "Book", "Member", "Issue Date",
                        "Due Date", "Return Date", "Fine"},
                rows.toArray(new Object[0][]));
    }


    // ---------- BOOKS ----------

    public void showBooks(ResultSet rs)
            throws SQLException {

        List<Object[]> rows = new ArrayList<>();

        while (rs.next()) {

            rows.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("quantity")
            });
        }

        updateTable(
                booksTable,
                new String[]{
                        "ID", "Title", "Author", "Quantity"
                },
                rows.toArray(new Object[0][])
        );

        showPage("Books");
    }


    // ---------- MEMBERS ----------

    public void showMembers(ResultSet rs)
            throws SQLException {

        List<Object[]> rows = new ArrayList<>();

        while (rs.next()) {

            rows.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone")
            });
        }

        updateTable(
                membersTable,
                new String[]{
                        "ID", "Name", "Email", "Phone"
                },
                rows.toArray(new Object[0][])
        );

        showPage("Members");
    }


    // ---------- LOANS ----------

    public void showLoans(ResultSet rs)
            throws SQLException {

        List<Object[]> rows = new ArrayList<>();

        while (rs.next()) {

            rows.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("name"),
                    rs.getString("issue_date"),
                    rs.getString("due_date"),
                    rs.getString("return_date"),
                    rs.getDouble("fine")
            });
        }

        updateTable(
                loansTable,
                new String[]{
                        "Loan ID",
                        "Book",
                        "Member",
                        "Issue Date",
                        "Due Date",
                        "Return Date",
                        "Fine"
                },
                rows.toArray(new Object[0][])
        );

        showPage("Loan Records");
    }


    // ---------- ADD BOOK FORM INPUTS ----------

    public String[] getBookDetails() {

        return new String[]{
                bookTitleField.getText(),
                bookAuthorField.getText(),
                bookQuantityField.getText()
        };
    }


    public void clearBookForm() {

        bookTitleField.setText("");
        bookAuthorField.setText("");
        bookQuantityField.setText("");
    }


    public void showBookFormMessage(
            String message,
            boolean success
    ) {

        bookFormMessage.setText(message);

        bookFormMessage.setForeground(
                success
                        ? new Color(22, 150, 90)
                        : Color.RED
        );
    }


    public JButton getSaveBookButton() {
        return saveBookButton;
    }


    // ---------- OTHER FORM INPUTS ----------

    public String getSearchKeyword() {
        return searchField.getText();
    }

    public JButton getSearchSubmitButton() {
        return executeSearchButton;
    }

    public void showSearchBooksPage() {
        showPage("Search Books");
        searchField.requestFocusInWindow();
    }

    public void showSearchMessage(String message, boolean success) {
        searchMessage.setText(message);
        searchMessage.setForeground(
                success ? new Color(22, 150, 90) : new Color(190, 55, 55)
        );
    }

    public void showSearchResults(ResultSet rs) throws SQLException {
        List<Object[]> rows = new ArrayList<>();

        while (rs.next()) {
            rows.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("quantity")
            });
        }

        updateTable(
                searchResultsTable,
                new String[]{"ID", "Title", "Author", "Available Copies"},
                rows.toArray(new Object[0][])
        );

        showSearchMessage(
                rows.size() + (rows.size() == 1 ? " book found." : " books found."),
                true
        );
        showPage("Search Books");
    }


    public String[] getMemberDetails() {
        return new String[]{
                memberNameField.getText(),
                memberEmailField.getText(),
                memberPhoneField.getText()
        };
    }

    public void clearMemberForm() {
        memberNameField.setText("");
        memberEmailField.setText("");
        memberPhoneField.setText("");
    }

    public void showMemberFormMessage(String message, boolean success) {
        memberFormMessage.setText(message);
        memberFormMessage.setForeground(
                success ? new Color(22, 150, 90) : Color.RED
        );
    }

    public JButton getSaveMemberButton() {
        return saveMemberButton;
    }


    // ---------- ISSUE BOOK FORM INPUTS ----------

    public int[] getIssueBookDetails() {
        return new int[]{
                Integer.parseInt(issueBookIdField.getText().trim()),
                Integer.parseInt(issueMemberIdField.getText().trim())
        };
    }

    public void clearIssueBookForm() {
        issueBookIdField.setText("");
        issueMemberIdField.setText("");
        issueDateField.setText(java.time.LocalDate.now().toString());
        dueDateField.setText(java.time.LocalDate.now().plusDays(7).toString());
    }

    public void showIssueFormMessage(String message, boolean success) {
        issueFormMessage.setText(message);
        issueFormMessage.setForeground(
                success ? new Color(22, 150, 90) : Color.RED
        );
    }

    public JButton getSubmitIssueButton() {
        return submitIssueButton;
    }

    // ---------- RETURN BOOK FORM INPUTS ----------

    public int getLoanId() {
        return Integer.parseInt(returnLoanIdField.getText().trim());
    }

    public void clearReturnBookForm() {
        returnLoanIdField.setText("");
        returnDateField.setText(java.time.LocalDate.now().toString());
        returnFineField.setText("Calculated on return");
    }

    public void showReturnFormMessage(String message, boolean success) {
        returnFormMessage.setText(message);
        returnFormMessage.setForeground(
                success ? new Color(22, 150, 90) : Color.RED
        );
    }

    public void setReturnFine(double fine) {
        returnFineField.setText(String.format("₹%.2f", fine));
    }

    public JButton getSubmitReturnButton() {
        return submitReturnButton;
    }


    // ---------- BUTTON GETTERS ----------

    public JButton getBooksButton() {
        return booksButton;
    }

    public JButton getMembersButton() {
        return membersButton;
    }

    public JButton getIssueButton() {
        return issueButton;
    }

    public JButton getReturnButton() {
        return returnButton;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public JButton getAddBookButton() {
        return addBookButton;
    }

    public JButton getRegisterMemberButton() {
        return registerMemberButton;
    }

    public JButton getLoansButton() {
        return loansButton;
    }
}
