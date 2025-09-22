import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CourseRegistrationSystem extends JFrame implements ActionListener {
    private CourseManager courseManager;
    private Student currentStudent;

    private JTextField studentIdField, nameField, emailField, semesterField, maxCreditsField;
    private JTextField teacherUsernameField;
    private JPasswordField teacherPasswordField;
    private JButton loginButton, registerStudentButton, teacherLoginButton, refreshButton, saveButton, logoutButton;
    private JList<String> coreList, pecList, honoursElectiveList, openElectiveList, mdmList, registeredList;
    private JButton addCoreButton, addPECButton, addHonoursButton, addOpenButton, addMDMButton, dropButton;
    private JLabel creditsLabel, statusLabel;
    private JTextArea courseDetailsArea;

    private JPanel mainPanel;
    private CardLayout cardLayout;
    private static final String LOGIN_PANEL = "login";
    private static final String REGISTRATION_PANEL = "registration";

    // Red-Orange Professional Color Scheme
    private static final Color PRIMARY_COLOR = new Color(220, 38, 127);     // Deep Pink-Red
    private static final Color PRIMARY_DARK = new Color(190, 18, 60);       // Dark Red
    private static final Color SECONDARY_COLOR = new Color(254, 242, 242);   // Soft Pink Background
    private static final Color ACCENT_COLOR = new Color(34, 197, 94);       // Fresh Green (contrast)
    private static final Color WARNING_COLOR = new Color(251, 146, 60);     // Warm Orange
    private static final Color DANGER_COLOR = new Color(239, 68, 68);       // Bright Red
    private static final Color TEXT_COLOR = new Color(69, 10, 10);          // Dark Red-Brown
    private static final Color BACKGROUND_COLOR = new Color(255, 247, 237);  // Warm Cream-Orange
    private static final Color CARD_COLOR = new Color(254, 251, 249);       // Soft White-Pink
    private static final Color BORDER_COLOR = new Color(254, 215, 170);     // Soft Orange Border
    private static final Color HEADER_COLOR = new Color(194, 65, 12);       // Red-Orange Header
    private static final Color INPUT_FOCUS_COLOR = new Color(254, 240, 138); // Light Orange Focus
    private static final Color SUCCESS_BG = new Color(240, 253, 244);       // Light Green Background
    private static final Color WARNING_BG = new Color(255, 237, 213);       // Light Orange Background
    private static final Color ERROR_BG = new Color(254, 226, 226);         // Light Red Background

    public CourseRegistrationSystem() {
        courseManager = CourseManager.getInstance();
        initializeComponents();
        setupLayout();
        setupEventHandlers();

        setTitle("ECS Course Registration System - Your College");
        setSize(1400, 900);
        setResizable(true);
        setLocationRelativeTo(null);
        setBackground(BACKGROUND_COLOR);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                try {
                    courseManager.saveData("course_data.txt");
                } catch (Exception e) {
                    System.err.println("Error saving data: " + e.getMessage());
                }
                System.exit(0);
            }
        });
    }

    private void initializeComponents() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Create styled text fields
        studentIdField = createStyledTextField(15);
        nameField = createStyledTextField(20);
        emailField = createStyledTextField(25);
        semesterField = createStyledTextField(5);
        maxCreditsField = createStyledTextField(5);
        maxCreditsField.setText("18");

        // Create styled buttons with red-orange theme colors
        loginButton = createStyledButton("Student Login", PRIMARY_COLOR, Color.WHITE);
        registerStudentButton = createStyledButton("Register New Student", ACCENT_COLOR, Color.WHITE);
        teacherLoginButton = createStyledButton("Teacher Login", PRIMARY_DARK, Color.WHITE);
        refreshButton = createStyledButton("Refresh Courses", new Color(251, 146, 60), Color.WHITE);
        saveButton = createStyledButton("Save Data", ACCENT_COLOR, Color.WHITE);
        logoutButton = createStyledButton("Logout", new Color(239, 68, 68), Color.WHITE);

        teacherUsernameField = createStyledTextField(15);
        teacherPasswordField = new JPasswordField(15);
        teacherPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        teacherPasswordField.setBackground(Color.WHITE);
        teacherPasswordField.setForeground(TEXT_COLOR);
        teacherPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        // Create styled lists
        coreList = createStyledList();
        pecList = createStyledList();
        honoursElectiveList = createStyledList();
        openElectiveList = createStyledList();
        mdmList = createStyledList();
        registeredList = createStyledList();

        // Create styled action buttons with red-orange theme
        addCoreButton = createStyledButton("Add Core", new Color(220, 38, 127), Color.WHITE);
        addPECButton = createStyledButton("Add PEC", new Color(220, 38, 127), Color.WHITE);
        addHonoursButton = createStyledButton("Add Honours", new Color(220, 38, 127), Color.WHITE);
        addOpenButton = createStyledButton("Add Open Elective", new Color(220, 38, 127), Color.WHITE);
        addMDMButton = createStyledButton("Add MDM", new Color(220, 38, 127), Color.WHITE);
        dropButton = createStyledButton("Drop Course", new Color(239, 68, 68), Color.WHITE);

        // Create styled labels
        creditsLabel = createStyledLabel("Credits: 0/18");
        creditsLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        creditsLabel.setForeground(PRIMARY_DARK);

        statusLabel = createStyledLabel("Status: Please login or register");
        statusLabel.setForeground(TEXT_COLOR);

        // Create styled text area
        courseDetailsArea = new JTextArea(6, 40);
        courseDetailsArea.setEditable(false);
        courseDetailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        courseDetailsArea.setBackground(new Color(255, 245, 238));
        courseDetailsArea.setForeground(TEXT_COLOR);
        courseDetailsArea.setLineWrap(true);
        courseDetailsArea.setWrapStyleWord(true);

        refreshCourseLists();
    }

    // Helper methods to create styled components
    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        // Add focus listener for better visual feedback
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBackground(INPUT_FOCUS_COLOR);
            }
            public void focusLost(FocusEvent e) {
                field.setBackground(Color.WHITE);
            }
        });

        return field;
    }

    private JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        // Add mouse listeners for hover effects (visual feedback)
        button.addMouseListener(new MouseAdapter() {
            Color originalColor = bgColor;

            public void mouseEntered(MouseEvent e) {
                // Create darker shade for hover
                int r = Math.max(0, originalColor.getRed() - 20);
                int g = Math.max(0, originalColor.getGreen() - 20);
                int b = Math.max(0, originalColor.getBlue() - 20);
                button.setBackground(new Color(r, g, b));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor);
            }

            public void mousePressed(MouseEvent e) {
                // Even darker when pressed
                int r = Math.max(0, originalColor.getRed() - 40);
                int g = Math.max(0, originalColor.getGreen() - 40);
                int b = Math.max(0, originalColor.getBlue() - 40);
                button.setBackground(new Color(r, g, b));
            }

            public void mouseReleased(MouseEvent e) {
                // Back to hover color
                int r = Math.max(0, originalColor.getRed() - 20);
                int g = Math.max(0, originalColor.getGreen() - 20);
                int b = Math.max(0, originalColor.getBlue() - 20);
                button.setBackground(new Color(r, g, b));
            }
        });

        return button;
    }

    private JList<String> createStyledList() {
        JList<String> list = new JList<>(new DefaultListModel<>());
        list.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        list.setBackground(Color.WHITE);
        list.setForeground(TEXT_COLOR);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        return list;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_COLOR);
        return panel;
    }

    private void setupLayout() {
        JPanel loginPanel = createLoginPanel();
        JPanel registrationPanel = createRegistrationPanel();

        mainPanel.add(loginPanel, LOGIN_PANEL);
        mainPanel.add(registrationPanel, REGISTRATION_PANEL);

        add(mainPanel, BorderLayout.CENTER);

        // Create modern status bar with red-orange gradient effect
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(new Color(254, 215, 170));

        // Add padding to status label
        JPanel statusContainer = new JPanel(new FlowLayout());
        statusContainer.setBackground(new Color(254, 215, 170));
        statusContainer.add(statusLabel);
        statusPanel.add(statusContainer);

        add(statusPanel, BorderLayout.SOUTH);

        cardLayout.show(mainPanel, LOGIN_PANEL);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        // Create modern title section
        JPanel titlePanel = new JPanel(new FlowLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("🎓 ECS Course Registration System", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(PRIMARY_COLOR);
        titlePanel.add(titleLabel);
        panel.add(titlePanel, BorderLayout.NORTH);

        JPanel mainFormPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        mainFormPanel.setBackground(BACKGROUND_COLOR);

        // Student Login Panel
        JPanel studentPanel = createStyledPanel();
        studentPanel.setLayout(new BorderLayout());

        JLabel studentTitleLabel = new JLabel("👨‍🎓 Student Login", JLabel.CENTER);
        studentTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        studentTitleLabel.setForeground(PRIMARY_COLOR);
        studentPanel.add(studentTitleLabel, BorderLayout.NORTH);

        JPanel studentFormPanel = new JPanel(new GridBagLayout());
        studentFormPanel.setBackground(CARD_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        JLabel sidLabel = createStyledLabel("Student ID:");
        sidLabel.setFont(new Font("Arial", Font.BOLD, 12));
        studentFormPanel.add(sidLabel, gbc);
        gbc.gridx = 1;
        studentFormPanel.add(studentIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel nameLabel = createStyledLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        studentFormPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        studentFormPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel emailLabel = createStyledLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 12));
        studentFormPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        studentFormPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel semLabel = createStyledLabel("Semester:");
        semLabel.setFont(new Font("Arial", Font.BOLD, 12));
        studentFormPanel.add(semLabel, gbc);
        gbc.gridx = 1;
        studentFormPanel.add(semesterField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        JLabel maxCreditsLabel = createStyledLabel("Max Credits:");
        maxCreditsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        studentFormPanel.add(maxCreditsLabel, gbc);
        gbc.gridx = 1;
        studentFormPanel.add(maxCreditsField, gbc);

        JPanel studentButtonPanel = new JPanel(new FlowLayout());
        studentButtonPanel.setBackground(CARD_COLOR);
        studentButtonPanel.add(loginButton);
        studentButtonPanel.add(registerStudentButton);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        studentFormPanel.add(studentButtonPanel, gbc);

        studentPanel.add(studentFormPanel, BorderLayout.CENTER);

        // Teacher Login Panel
        JPanel teacherPanel = createStyledPanel();
        teacherPanel.setLayout(new BorderLayout());

        JLabel teacherTitleLabel = new JLabel("👩‍🏫 Teacher Login", JLabel.CENTER);
        teacherTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        teacherTitleLabel.setForeground(PRIMARY_COLOR);
        teacherPanel.add(teacherTitleLabel, BorderLayout.NORTH);

        JPanel teacherFormPanel = new JPanel(new GridBagLayout());
        teacherFormPanel.setBackground(CARD_COLOR);
        GridBagConstraints tgbc = new GridBagConstraints();
        tgbc.insets = new Insets(8, 8, 8, 8);

        tgbc.gridx = 0; tgbc.gridy = 0; tgbc.anchor = GridBagConstraints.WEST;
        JLabel usernameLabel = createStyledLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        teacherFormPanel.add(usernameLabel, tgbc);
        tgbc.gridx = 1;
        teacherFormPanel.add(teacherUsernameField, tgbc);

        tgbc.gridx = 0; tgbc.gridy = 1;
        JLabel passwordLabel = createStyledLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        teacherFormPanel.add(passwordLabel, tgbc);
        tgbc.gridx = 1;
        teacherFormPanel.add(teacherPasswordField, tgbc);

        JPanel teacherButtonPanel = new JPanel(new FlowLayout());
        teacherButtonPanel.setBackground(CARD_COLOR);
        teacherButtonPanel.add(teacherLoginButton);

        tgbc.gridx = 0; tgbc.gridy = 2;
        tgbc.gridwidth = 2;
        teacherFormPanel.add(teacherButtonPanel, tgbc);

        // Add sample teacher credentials info
        JTextArea credentialsInfo = new JTextArea(6, 25);
        credentialsInfo.setText("Sample Teacher Accounts:\\n\\n" +
                              "Username: admin\\n" +
                              "Password: admin123\\n\\n" );
        credentialsInfo.setEditable(false);
        credentialsInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        credentialsInfo.setBackground(new Color(255, 245, 238));
        credentialsInfo.setForeground(new Color(154, 52, 18));
        credentialsInfo.setLineWrap(true);
        credentialsInfo.setWrapStyleWord(true);

        tgbc.gridx = 0; tgbc.gridy = 3;
        tgbc.gridwidth = 2;
        teacherFormPanel.add(credentialsInfo, tgbc);

        teacherPanel.add(teacherFormPanel, BorderLayout.CENTER);

        mainFormPanel.add(studentPanel);
        mainFormPanel.add(teacherPanel);

        panel.add(mainFormPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRegistrationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);

        JPanel studentInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        studentInfoPanel.setBackground(BACKGROUND_COLOR);
        JLabel studentInfoLabel = new JLabel();
        studentInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        studentInfoLabel.setForeground(PRIMARY_DARK);
        studentInfoPanel.add(studentInfoLabel);
        headerPanel.add(studentInfoPanel, BorderLayout.WEST);

        JPanel creditPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        creditPanel.setBackground(BACKGROUND_COLOR);
        creditPanel.add(creditsLabel);
        headerPanel.add(creditPanel, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);

        // Left side with course categories in a 2x3 grid with enhanced styling
        JPanel coursesPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        coursesPanel.setBackground(BACKGROUND_COLOR);

        JPanel corePanel = createCoursePanel("Core Courses (PCC)", coreList, addCoreButton, PRIMARY_COLOR);
        JPanel pecPanel = createCoursePanel("Program Electives (PEC)", pecList, addPECButton, PRIMARY_COLOR);
        JPanel honoursPanel = createCoursePanel("Honours Courses", honoursElectiveList, addHonoursButton, PRIMARY_COLOR);
        JPanel openPanel = createCoursePanel("Open Electives", openElectiveList, addOpenButton, PRIMARY_COLOR);
        JPanel mdmPanel = createCoursePanel("MDM Courses", mdmList, addMDMButton, PRIMARY_COLOR);
        JPanel registeredPanel = createCoursePanel("My Registered Courses", registeredList, dropButton, DANGER_COLOR);

        coursesPanel.add(corePanel);
        coursesPanel.add(pecPanel);
        coursesPanel.add(honoursPanel);
        coursesPanel.add(openPanel);
        coursesPanel.add(mdmPanel);
        coursesPanel.add(registeredPanel);

        centerPanel.add(coursesPanel, BorderLayout.CENTER);
        panel.add(centerPanel, BorderLayout.CENTER);

        // Enhanced details panel
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(CARD_COLOR);

        JLabel detailsTitle = new JLabel("📋 Course Details:", JLabel.LEFT);
        detailsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        detailsTitle.setForeground(PRIMARY_DARK);
        detailsPanel.add(detailsTitle, BorderLayout.NORTH);

        // Wrap text area in scroll pane
        JScrollPane scrollPane = new JScrollPane(courseDetailsArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        detailsPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.add(refreshButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(logoutButton);
        detailsPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(detailsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCoursePanel(String title, JList<String> list, JButton button, Color buttonColor) {
        JPanel panel = createStyledPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBackground(HEADER_COLOR);
        titleLabel.setOpaque(true);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Add list with scroll pane
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(200, 120));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(CARD_COLOR);
        listPanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(listPanel, BorderLayout.CENTER);

        // Style the button
        button.setBackground(buttonColor);
        button.setForeground(Color.WHITE);
        panel.add(button, BorderLayout.SOUTH);

        return panel;
    }

    private void setupEventHandlers() {
        loginButton.addActionListener(this);
        registerStudentButton.addActionListener(this);
        teacherLoginButton.addActionListener(this);
        addCoreButton.addActionListener(this);
        addPECButton.addActionListener(this);
        addHonoursButton.addActionListener(this);
        addOpenButton.addActionListener(this);
        addMDMButton.addActionListener(this);
        dropButton.addActionListener(this);
        refreshButton.addActionListener(this);
        saveButton.addActionListener(this);
        logoutButton.addActionListener(this);

        coreList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = coreList.getSelectedValue();
                if (selected != null) {
                    showCourseDetails(selected, "Core");
                    clearOtherSelections(coreList);
                }
            }
        });

        pecList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = pecList.getSelectedValue();
                if (selected != null) {
                    showCourseDetails(selected, "PEC");
                    clearOtherSelections(pecList);
                }
            }
        });

        honoursElectiveList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = honoursElectiveList.getSelectedValue();
                if (selected != null) {
                    showCourseDetails(selected, "Honours");
                    clearOtherSelections(honoursElectiveList);
                }
            }
        });

        openElectiveList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = openElectiveList.getSelectedValue();
                if (selected != null) {
                    showCourseDetails(selected, "Open Elective");
                    clearOtherSelections(openElectiveList);
                }
            }
        });

        mdmList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = mdmList.getSelectedValue();
                if (selected != null) {
                    showCourseDetails(selected, "MDM");
                    clearOtherSelections(mdmList);
                }
            }
        });

        registeredList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = registeredList.getSelectedValue();
                if (selected != null) {
                    showRegisteredCourseDetails(selected);
                    clearOtherSelections(registeredList);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void clearOtherSelections(JList<String> selectedList) {
        JList<String>[] allLists = new JList[]{coreList, pecList, honoursElectiveList, openElectiveList, mdmList, registeredList};
        for (JList<String> list : allLists) {
            if (list != selectedList && list.getSelectedIndex() >= 0) {
                list.clearSelection();
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Student Login":
            case "Register New Student":
                handleStudentLogin();
                break;
            case "Teacher Login":
                handleTeacherLogin();
                break;
            case "Add Core":
                addSelectedCourse(coreList, "Core");
                break;
            case "Add PEC":
                addSelectedCourse(pecList, "PEC");
                break;
            case "Add Honours":
                addSelectedCourse(honoursElectiveList, "Honours");
                break;
            case "Add Open Elective":
                addSelectedCourse(openElectiveList, "Open Elective");
                break;
            case "Add MDM":
                addSelectedCourse(mdmList, "MDM");
                break;
            case "Drop Course":
                dropSelectedCourse();
                break;
            case "Refresh Courses":
                refreshCourseLists();
                break;
            case "Save Data":
                courseManager.saveData("course_data.txt");
                statusLabel.setText("Status: Data saved successfully");
                statusLabel.setForeground(ACCENT_COLOR);
                break;
            case "Logout":
                logout();
                break;
        }
    }

    private void handleStudentLogin() {
        try {
            String id = studentIdField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            int semester = Integer.parseInt(semesterField.getText().trim());
            int maxCredits = Integer.parseInt(maxCreditsField.getText().trim());

            if (id.isEmpty() || name.isEmpty() || email.isEmpty()) {
                statusLabel.setText("Status: Please fill all required fields");
                statusLabel.setForeground(WARNING_COLOR);
                return;
            }

            // Try to get existing student from database first
            currentStudent = courseManager.getStudentById(id);

            if (currentStudent == null) {
                // Create new student and add to database
                currentStudent = new Student(id, name, email, semester, maxCredits);
                boolean registered = courseManager.registerStudent(currentStudent);
                if (!registered) {
                    statusLabel.setText("Status: Failed to register student in database (continuing with session)");
                    statusLabel.setForeground(WARNING_COLOR);
                }
            } else {
                statusLabel.setText("Status: Welcome back " + currentStudent.getName() + "!");
                statusLabel.setForeground(ACCENT_COLOR);
            }

            cardLayout.show(mainPanel, REGISTRATION_PANEL);
            updateStudentInfo();
            updateRegisteredCourses();
            statusLabel.setText("Status: Welcome " + currentStudent.getName() + "!");
            statusLabel.setForeground(ACCENT_COLOR);

        } catch (NumberFormatException ex) {
            statusLabel.setText("Status: Please enter valid numbers for semester and credits");
            statusLabel.setForeground(DANGER_COLOR);
        }
    }

    private void handleTeacherLogin() {
        try {
            String username = teacherUsernameField.getText().trim();
            String password = new String(teacherPasswordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Status: Please enter username and password");
                statusLabel.setForeground(WARNING_COLOR);
                return;
            }

            TeacherDAO teacherDAO = new TeacherDAO();
            Teacher teacher = teacherDAO.authenticateTeacher(username, password);

            if (teacher != null) {
                statusLabel.setText("Status: Teacher login successful");
                statusLabel.setForeground(ACCENT_COLOR);
                this.dispose();
                new TeacherDashboard(teacher).setVisible(true);
            } else {
                statusLabel.setText("Status: Invalid username or password");
                statusLabel.setForeground(DANGER_COLOR);
                teacherPasswordField.setText("");
            }

        } catch (Exception ex) {
            statusLabel.setText("Status: Database connection error - " + ex.getMessage());
            statusLabel.setForeground(DANGER_COLOR);
            System.err.println("Teacher login error: " + ex.getMessage());
        }
    }

    private void addSelectedCourse(JList<String> courseList, String expectedType) {
        String selectedItem = courseList.getSelectedValue();
        if (selectedItem == null) {
            statusLabel.setText("Status: Please select a course to add");
            statusLabel.setForeground(WARNING_COLOR);
            return;
        }

        String courseId = selectedItem.split(" - ")[0];
        Course course = courseManager.getCourseById(courseId);

        if (course == null) {
            statusLabel.setText("Status: Course not found");
            statusLabel.setForeground(DANGER_COLOR);
            return;
        }

        if (currentStudent.canRegister(course)) {
            boolean enrolled = courseManager.enrollStudentInCourse(currentStudent.getStudentId(), course.getCourseId());
            if (enrolled) {
                currentStudent.registerCourse(course);
                updateRegisteredCourses();
                updateCreditsLabel();
                statusLabel.setText("Status: Successfully registered for " + course.getCourseName());
                statusLabel.setForeground(ACCENT_COLOR);
            } else {
                statusLabel.setText("Status: Database enrollment failed");
                statusLabel.setForeground(DANGER_COLOR);
            }
        } else {
            if (!course.isAvailable()) {
                statusLabel.setText("Status: Course is full");
                statusLabel.setForeground(WARNING_COLOR);
            } else if (currentStudent.getCurrentCredits() + course.getCredits() > currentStudent.getMaxCredits()) {
                statusLabel.setText("Status: Would exceed credit limit");
                statusLabel.setForeground(WARNING_COLOR);
            } else {
                statusLabel.setText("Status: Already registered for this course");
                statusLabel.setForeground(WARNING_COLOR);
            }
        }
    }

    private void dropSelectedCourse() {
        String selectedItem = registeredList.getSelectedValue();
        if (selectedItem == null) {
            statusLabel.setText("Status: Please select a registered course to drop");
            statusLabel.setForeground(WARNING_COLOR);
            return;
        }

        String courseId = selectedItem.split(" - ")[0];
        Course course = courseManager.getCourseById(courseId);

        if (course != null) {
            boolean dropped = courseManager.dropStudentFromCourse(currentStudent.getStudentId(), course.getCourseId());
            if (dropped) {
                currentStudent.dropCourse(course);
                updateRegisteredCourses();
                updateCreditsLabel();
                statusLabel.setText("Status: Successfully dropped " + course.getCourseName());
                statusLabel.setForeground(ACCENT_COLOR);
            } else {
                statusLabel.setText("Status: Failed to drop course from database");
                statusLabel.setForeground(DANGER_COLOR);
            }
        } else {
            statusLabel.setText("Status: Course not found");
            statusLabel.setForeground(DANGER_COLOR);
        }
    }

    private void showCourseDetails(String selectedItem, String type) {
        if (selectedItem != null) {
            String courseId = selectedItem.split(" - ")[0];
            Course course = courseManager.getCourseById(courseId);
            if (course != null) {
                courseDetailsArea.setText(course.getDetailedInfo());
            }
        }
    }

    private void showRegisteredCourseDetails(String selectedItem) {
        if (selectedItem != null) {
            showCourseDetails(selectedItem, "Registered");
        }
    }

    private void refreshCourseLists() {
        DefaultListModel<String> coreModel = (DefaultListModel<String>) coreList.getModel();
        DefaultListModel<String> pecModel = (DefaultListModel<String>) pecList.getModel();
        DefaultListModel<String> honoursModel = (DefaultListModel<String>) honoursElectiveList.getModel();
        DefaultListModel<String> openModel = (DefaultListModel<String>) openElectiveList.getModel();
        DefaultListModel<String> mdmModel = (DefaultListModel<String>) mdmList.getModel();

        coreModel.clear();
        pecModel.clear();
        honoursModel.clear();
        openModel.clear();
        mdmModel.clear();

        for (Course course : courseManager.getCoursesByType("Core")) {
            coreModel.addElement(course.toString());
        }

        for (Course course : courseManager.getCoursesByType("PEC")) {
            pecModel.addElement(course.toString());
        }

        for (Course course : courseManager.getCoursesByType("Honours")) {
            honoursModel.addElement(course.toString());
        }

        for (Course course : courseManager.getCoursesByType("Open Elective")) {
            openModel.addElement(course.toString());
        }

        for (Course course : courseManager.getCoursesByType("MDM")) {
            mdmModel.addElement(course.toString());
        }
    }

    private void updateStudentInfo() {
        if (currentStudent != null) {
            Component[] components = mainPanel.getComponents();
            for (Component comp : components) {
                if (comp instanceof JPanel) {
                    updateStudentInfoInPanel((JPanel) comp);
                }
            }
        }
    }

    private void updateStudentInfoInPanel(JPanel panel) {
        Component[] components = panel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                updateStudentInfoInPanel((JPanel) comp);
            } else if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getFont() != null && label.getFont().isBold() &&
                    label.getFont().getSize() == 16) {
                    label.setText("Student: " + currentStudent.getName() +
                                 " (" + currentStudent.getStudentId() + ") - Semester " +
                                 currentStudent.getSemester());
                }
            }
        }
    }

    private void updateRegisteredCourses() {
        DefaultListModel<String> registeredModel = (DefaultListModel<String>) registeredList.getModel();
        registeredModel.clear();
        if (currentStudent != null) {
            for (Course course : currentStudent.getRegisteredCourses()) {
                registeredModel.addElement(course.toString());
            }
        }
    }

    private void updateCreditsLabel() {
        if (currentStudent != null) {
            int current = currentStudent.getCurrentCredits();
            int max = currentStudent.getMaxCredits();
            creditsLabel.setText("Credits: " + current + "/" + max +
                               " (Core:" + currentStudent.getCoreCredits() +
                               " PEC:" + currentStudent.getPECCredits() +
                               " Hons:" + currentStudent.getHonoursCredits() +
                               " OE:" + currentStudent.getOpenElectiveCredits() +
                               " MDM:" + currentStudent.getMDMCredits() + ")");
        }
    }

    private void logout() {
        currentStudent = null;
        cardLayout.show(mainPanel, LOGIN_PANEL);
        clearFields();
        statusLabel.setText("Status: Please login or register");
        statusLabel.setForeground(TEXT_COLOR);
    }

    private void clearFields() {
        studentIdField.setText("");
        nameField.setText("");
        emailField.setText("");
        semesterField.setText("");
        maxCreditsField.setText("18");
        courseDetailsArea.setText("");
        DefaultListModel<String> registeredModel = (DefaultListModel<String>) registeredList.getModel();
        registeredModel.clear();
        creditsLabel.setText("Credits: 0/18");
    }

    public static void main(String[] args) {
        new CourseRegistrationSystem().setVisible(true);
    }
}