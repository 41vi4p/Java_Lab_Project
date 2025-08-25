import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CourseRegistrationSystemEnhanced extends Frame implements ActionListener {
    private CourseManager courseManager;
    private Student currentStudent;
    
    private TextField studentIdField, nameField, emailField, semesterField, maxCreditsField;
    private TextField teacherUsernameField, teacherPasswordField;
    private Button loginButton, registerStudentButton, teacherLoginButton, refreshButton, saveButton, logoutButton;
    private java.awt.List coreList, pecList, honoursElectiveList, openElectiveList, mdmList, registeredList;
    private Button addCoreButton, addPECButton, addHonoursButton, addOpenButton, addMDMButton, dropButton;
    private Label creditsLabel, statusLabel;
    private TextArea courseDetailsArea;
    
    private Panel mainPanel;
    private CardLayout cardLayout;
    private static final String LOGIN_PANEL = "login";
    private static final String REGISTRATION_PANEL = "registration";
    
    // Red-Orange Professional Color Scheme (Consistent with main system)
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

    public CourseRegistrationSystemEnhanced() {
        courseManager = CourseManager.getInstance();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setTitle("ECS Course Registration System - Your College");
        setSize(1400, 900);
        setResizable(true);
        setLocationRelativeTo(null);
        setBackground(BACKGROUND_COLOR);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                courseManager.saveData("course_data.txt");
                System.exit(0);
            }
        });
    }
    
    private void initializeComponents() {
        cardLayout = new CardLayout();
        mainPanel = new Panel(cardLayout);
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Create styled text fields
        studentIdField = createStyledTextField(15);
        nameField = createStyledTextField(20);
        emailField = createStyledTextField(25);
        semesterField = createStyledTextField(5);
        maxCreditsField = createStyledTextField(5);
        maxCreditsField.setText("18");
        
        // Create styled buttons with colors
        loginButton = createStyledButton("Student Login", PRIMARY_COLOR, Color.WHITE);
        registerStudentButton = createStyledButton("Register New Student", ACCENT_COLOR, Color.WHITE);
        teacherLoginButton = createStyledButton("Teacher Login", PRIMARY_DARK, Color.WHITE);
        refreshButton = createStyledButton("Refresh Courses", SECONDARY_COLOR, TEXT_COLOR);
        saveButton = createStyledButton("Save Data", ACCENT_COLOR, Color.WHITE);
        logoutButton = createStyledButton("Logout", WARNING_COLOR, Color.WHITE);
        
        teacherUsernameField = createStyledTextField(15);
        teacherPasswordField = createStyledTextField(15);
        teacherPasswordField.setEchoChar('*');
        
        // Create styled lists
        coreList = createStyledList();
        pecList = createStyledList();
        honoursElectiveList = createStyledList();
        openElectiveList = createStyledList();
        mdmList = createStyledList();
        registeredList = createStyledList();
        
        // Create styled action buttons
        addCoreButton = createStyledButton("Add Core", PRIMARY_COLOR, Color.WHITE);
        addPECButton = createStyledButton("Add PEC", PRIMARY_COLOR, Color.WHITE);
        addHonoursButton = createStyledButton("Add Honours", PRIMARY_COLOR, Color.WHITE);
        addOpenButton = createStyledButton("Add Open Elective", PRIMARY_COLOR, Color.WHITE);
        addMDMButton = createStyledButton("Add MDM", PRIMARY_COLOR, Color.WHITE);
        dropButton = createStyledButton("Drop Course", DANGER_COLOR, Color.WHITE);
        
        // Create styled labels
        creditsLabel = createStyledLabel("Credits: 0/18");
        creditsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        creditsLabel.setForeground(PRIMARY_COLOR);
        
        statusLabel = createStyledLabel("Status: Please login or register");
        statusLabel.setForeground(TEXT_COLOR);
        
        // Create styled text area
        courseDetailsArea = new TextArea(6, 40);
        courseDetailsArea.setEditable(false);
        courseDetailsArea.setFont(new Font("Arial", Font.PLAIN, 12));
        courseDetailsArea.setBackground(SECONDARY_COLOR);
        courseDetailsArea.setForeground(TEXT_COLOR);
        
        refreshCourseLists();
    }
    
    // Helper methods to create styled components
    private TextField createStyledTextField(int columns) {
        TextField field = new TextField(columns);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_COLOR);
        return field;
    }
    
    private Button createStyledButton(String text, Color bgColor, Color textColor) {
        Button button = new Button(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(textColor);
        return button;
    }
    
    private java.awt.List createStyledList() {
        java.awt.List list = new java.awt.List(6, false);
        list.setFont(new Font("Arial", Font.PLAIN, 12));
        list.setBackground(Color.WHITE);
        list.setForeground(TEXT_COLOR);
        return list;
    }
    
    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setForeground(TEXT_COLOR);
        return label;
    }
    
    private Panel createStyledPanel() {
        Panel panel = new Panel();
        panel.setBackground(CARD_COLOR);
        return panel;
    }
    
    private void setupLayout() {
        Panel loginPanel = createLoginPanel();
        Panel registrationPanel = createRegistrationPanel();
        
        mainPanel.add(loginPanel, LOGIN_PANEL);
        mainPanel.add(registrationPanel, REGISTRATION_PANEL);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Create modern status bar
        Panel statusPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(SECONDARY_COLOR);
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);
        
        cardLayout.show(mainPanel, LOGIN_PANEL);
    }
    
    private Panel createLoginPanel() {
        Panel panel = new Panel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        // Create modern title section
        Panel titlePanel = new Panel(new FlowLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        
        Label titleLabel = new Label("ECS Course Registration System", Label.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_COLOR);
        titlePanel.add(titleLabel);
        panel.add(titlePanel, BorderLayout.NORTH);
        
        Panel mainFormPanel = new Panel(new GridLayout(1, 2, 30, 0));
        mainFormPanel.setBackground(BACKGROUND_COLOR);
        
        // Student Login Panel
        Panel studentPanel = createStyledPanel();
        studentPanel.setLayout(new BorderLayout());
        
        Label studentTitleLabel = new Label("Student Login", Label.CENTER);
        studentTitleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        studentTitleLabel.setForeground(PRIMARY_COLOR);
        studentPanel.add(studentTitleLabel, BorderLayout.NORTH);
        
        Panel studentFormPanel = new Panel(new GridBagLayout());
        studentFormPanel.setBackground(CARD_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        Label sidLabel = createStyledLabel("Student ID:");
        sidLabel.setFont(new Font("Arial", Font.BOLD, 12));
        studentFormPanel.add(sidLabel, gbc);
        gbc.gridx = 1;
        studentFormPanel.add(studentIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        Label nameLabel = createStyledLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        studentFormPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        studentFormPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        Label emailLabel = createStyledLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 12));
        studentFormPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        studentFormPanel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        Label semLabel = createStyledLabel("Semester:");
        semLabel.setFont(new Font("Arial", Font.BOLD, 12));
        studentFormPanel.add(semLabel, gbc);
        gbc.gridx = 1;
        studentFormPanel.add(semesterField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        Label maxCreditsLabel = createStyledLabel("Max Credits:");
        maxCreditsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        studentFormPanel.add(maxCreditsLabel, gbc);
        gbc.gridx = 1;
        studentFormPanel.add(maxCreditsField, gbc);
        
        Panel studentButtonPanel = new Panel(new FlowLayout());
        studentButtonPanel.setBackground(CARD_COLOR);
        studentButtonPanel.add(loginButton);
        studentButtonPanel.add(registerStudentButton);
        
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        studentFormPanel.add(studentButtonPanel, gbc);
        
        studentPanel.add(studentFormPanel, BorderLayout.CENTER);
        
        // Teacher Login Panel
        Panel teacherPanel = createStyledPanel();
        teacherPanel.setLayout(new BorderLayout());
        
        Label teacherTitleLabel = new Label("Teacher Login", Label.CENTER);
        teacherTitleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        teacherTitleLabel.setForeground(PRIMARY_COLOR);
        teacherPanel.add(teacherTitleLabel, BorderLayout.NORTH);
        
        Panel teacherFormPanel = new Panel(new GridBagLayout());
        teacherFormPanel.setBackground(CARD_COLOR);
        GridBagConstraints tgbc = new GridBagConstraints();
        tgbc.insets = new Insets(8, 8, 8, 8);
        
        tgbc.gridx = 0; tgbc.gridy = 0; tgbc.anchor = GridBagConstraints.WEST;
        Label usernameLabel = createStyledLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        teacherFormPanel.add(usernameLabel, tgbc);
        tgbc.gridx = 1;
        teacherFormPanel.add(teacherUsernameField, tgbc);
        
        tgbc.gridx = 0; tgbc.gridy = 1;
        Label passwordLabel = createStyledLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        teacherFormPanel.add(passwordLabel, tgbc);
        tgbc.gridx = 1;
        teacherFormPanel.add(teacherPasswordField, tgbc);
        
        Panel teacherButtonPanel = new Panel(new FlowLayout());
        teacherButtonPanel.setBackground(CARD_COLOR);
        teacherButtonPanel.add(teacherLoginButton);
        
        tgbc.gridx = 0; tgbc.gridy = 2;
        tgbc.gridwidth = 2;
        teacherFormPanel.add(teacherButtonPanel, tgbc);
        
        // Add sample teacher credentials info
        TextArea credentialsInfo = new TextArea(6, 25);
        credentialsInfo.setText("Sample Teacher Accounts:\n\n" +
                              "Username: admin\n" +
                              "Password: admin123\n\n" );
        credentialsInfo.setEditable(false);
        credentialsInfo.setFont(new Font("Arial", Font.PLAIN, 11));
        credentialsInfo.setBackground(SECONDARY_COLOR);
        credentialsInfo.setForeground(TEXT_COLOR);
        
        tgbc.gridx = 0; tgbc.gridy = 3;
        tgbc.gridwidth = 2;
        teacherFormPanel.add(credentialsInfo, tgbc);
        
        teacherPanel.add(teacherFormPanel, BorderLayout.CENTER);
        
        mainFormPanel.add(studentPanel);
        mainFormPanel.add(teacherPanel);
        
        panel.add(mainFormPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private Panel createRegistrationPanel() {
        Panel panel = new Panel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        Panel headerPanel = new Panel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        
        Panel studentInfoPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        studentInfoPanel.setBackground(BACKGROUND_COLOR);
        Label studentInfoLabel = new Label();
        studentInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        studentInfoLabel.setForeground(TEXT_COLOR);
        studentInfoPanel.add(studentInfoLabel);
        headerPanel.add(studentInfoPanel, BorderLayout.WEST);
        
        Panel creditPanel = new Panel(new FlowLayout(FlowLayout.RIGHT));
        creditPanel.setBackground(BACKGROUND_COLOR);
        creditPanel.add(creditsLabel);
        headerPanel.add(creditPanel, BorderLayout.EAST);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        Panel centerPanel = new Panel(new BorderLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);
        
        // Left side with course categories in a 2x3 grid with enhanced styling
        Panel coursesPanel = new Panel(new GridLayout(2, 3, 10, 10));
        coursesPanel.setBackground(BACKGROUND_COLOR);
        
        Panel corePanel = createCoursePanel("Core Courses (PCC)", coreList, addCoreButton, PRIMARY_COLOR);
        Panel pecPanel = createCoursePanel("Program Electives (PEC)", pecList, addPECButton, PRIMARY_COLOR);
        Panel honoursPanel = createCoursePanel("Honours Courses", honoursElectiveList, addHonoursButton, PRIMARY_COLOR);
        Panel openPanel = createCoursePanel("Open Electives", openElectiveList, addOpenButton, PRIMARY_COLOR);
        Panel mdmPanel = createCoursePanel("MDM Courses", mdmList, addMDMButton, PRIMARY_COLOR);
        Panel registeredPanel = createCoursePanel("My Registered Courses", registeredList, dropButton, DANGER_COLOR);
        
        coursesPanel.add(corePanel);
        coursesPanel.add(pecPanel);
        coursesPanel.add(honoursPanel);
        coursesPanel.add(openPanel);
        coursesPanel.add(mdmPanel);
        coursesPanel.add(registeredPanel);
        
        centerPanel.add(coursesPanel, BorderLayout.CENTER);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        // Enhanced details panel
        Panel detailsPanel = new Panel(new BorderLayout());
        detailsPanel.setBackground(CARD_COLOR);
        
        Label detailsTitle = new Label("Course Details:", Label.LEFT);
        detailsTitle.setFont(new Font("Arial", Font.BOLD, 14));
        detailsTitle.setForeground(TEXT_COLOR);
        detailsPanel.add(detailsTitle, BorderLayout.NORTH);
        detailsPanel.add(courseDetailsArea, BorderLayout.CENTER);
        
        Panel buttonPanel = new Panel(new FlowLayout());
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.add(refreshButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(logoutButton);
        detailsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        panel.add(detailsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private Panel createCoursePanel(String title, java.awt.List list, Button button, Color buttonColor) {
        Panel panel = createStyledPanel();
        panel.setLayout(new BorderLayout());
        
        Label titleLabel = new Label(title, Label.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBackground(SECONDARY_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Add some padding around the list
        Panel listPanel = new Panel(new BorderLayout());
        listPanel.setBackground(CARD_COLOR);
        listPanel.add(list, BorderLayout.CENTER);
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
        
        coreList.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    showCourseDetails(coreList.getSelectedItem(), "Core");
                    clearOtherSelections(coreList);
                }
            }
        });
        
        pecList.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    showCourseDetails(pecList.getSelectedItem(), "PEC");
                    clearOtherSelections(pecList);
                }
            }
        });
        
        honoursElectiveList.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    showCourseDetails(honoursElectiveList.getSelectedItem(), "Honours");
                    clearOtherSelections(honoursElectiveList);
                }
            }
        });
        
        openElectiveList.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    showCourseDetails(openElectiveList.getSelectedItem(), "Open Elective");
                    clearOtherSelections(openElectiveList);
                }
            }
        });
        
        mdmList.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    showCourseDetails(mdmList.getSelectedItem(), "MDM");
                    clearOtherSelections(mdmList);
                }
            }
        });
        
        registeredList.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    showRegisteredCourseDetails(registeredList.getSelectedItem());
                    clearOtherSelections(registeredList);
                }
            }
        });
    }
    
    private void clearOtherSelections(java.awt.List selectedList) {
        java.awt.List[] allLists = {coreList, pecList, honoursElectiveList, openElectiveList, mdmList, registeredList};
        for (java.awt.List list : allLists) {
            if (list != selectedList && list.getSelectedIndex() >= 0) {
                list.deselect(list.getSelectedIndex());
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
            String password = teacherPasswordField.getText().trim();
            
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
    
    private void addSelectedCourse(java.awt.List courseList, String expectedType) {
        String selectedItem = courseList.getSelectedItem();
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
        String selectedItem = registeredList.getSelectedItem();
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
        coreList.removeAll();
        pecList.removeAll();
        honoursElectiveList.removeAll();
        openElectiveList.removeAll();
        mdmList.removeAll();
        
        for (Course course : courseManager.getCoursesByType("Core")) {
            coreList.add(course.toString());
        }
        
        for (Course course : courseManager.getCoursesByType("PEC")) {
            pecList.add(course.toString());
        }
        
        for (Course course : courseManager.getCoursesByType("Honours")) {
            honoursElectiveList.add(course.toString());
        }
        
        for (Course course : courseManager.getCoursesByType("Open Elective")) {
            openElectiveList.add(course.toString());
        }
        
        for (Course course : courseManager.getCoursesByType("MDM")) {
            mdmList.add(course.toString());
        }
    }
    
    private void updateStudentInfo() {
        if (currentStudent != null) {
            Component[] components = mainPanel.getComponents();
            for (Component comp : components) {
                if (comp instanceof Panel) {
                    updateStudentInfoInPanel((Panel) comp);
                }
            }
        }
    }
    
    private void updateStudentInfoInPanel(Panel panel) {
        Component[] components = panel.getComponents();
        for (Component comp : components) {
            if (comp instanceof Panel) {
                updateStudentInfoInPanel((Panel) comp);
            } else if (comp instanceof Label) {
                Label label = (Label) comp;
                if (label.getFont() != null && label.getFont().isBold() && 
                    label.getFont().getSize() == 14) {
                    label.setText("Student: " + currentStudent.getName() + 
                                 " (" + currentStudent.getStudentId() + ") - Semester " + 
                                 currentStudent.getSemester());
                }
            }
        }
    }
    
    private void updateRegisteredCourses() {
        registeredList.removeAll();
        if (currentStudent != null) {
            for (Course course : currentStudent.getRegisteredCourses()) {
                registeredList.add(course.toString());
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
        registeredList.removeAll();
        creditsLabel.setText("Credits: 0/18");
    }
    
    public static void main(String[] args) {
        new CourseRegistrationSystemEnhanced().setVisible(true);
    }
}