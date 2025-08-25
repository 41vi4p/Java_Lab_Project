import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CourseRegistrationSystem extends Frame implements ActionListener {
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
    
    public CourseRegistrationSystem() {
        courseManager = CourseManager.getInstance();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setTitle("ECS Course Registration System - Your College");
        setSize(1200, 800);
        setResizable(true);
        
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
        
        studentIdField = new TextField(15);
        nameField = new TextField(20);
        emailField = new TextField(25);
        semesterField = new TextField(5);
        maxCreditsField = new TextField(5);
        maxCreditsField.setText("18");
        
        loginButton = new Button("Student Login");
        registerStudentButton = new Button("Register New Student");
        teacherLoginButton = new Button("Teacher Login");
        refreshButton = new Button("Refresh Courses");
        saveButton = new Button("Save Data");
        logoutButton = new Button("Logout");
        
        teacherUsernameField = new TextField(15);
        teacherPasswordField = new TextField(15);
        teacherPasswordField.setEchoChar('*');
        
        coreList = new java.awt.List(6, false);
        pecList = new java.awt.List(6, false);
        honoursElectiveList = new java.awt.List(6, false);
        openElectiveList = new java.awt.List(4, false);
        mdmList = new java.awt.List(4, false);
        registeredList = new java.awt.List(10, false);
        
        addCoreButton = new Button("Add Core");
        addPECButton = new Button("Add PEC");
        addHonoursButton = new Button("Add Honours");
        addOpenButton = new Button("Add Open Elective");
        addMDMButton = new Button("Add MDM");
        dropButton = new Button("Drop Course");
        
        creditsLabel = new Label("Credits: 0/18");
        statusLabel = new Label("Status: Please login or register");
        courseDetailsArea = new TextArea(6, 40);
        courseDetailsArea.setEditable(false);
        
        refreshCourseLists();
    }
    
    private void setupLayout() {
        Panel loginPanel = createLoginPanel();
        Panel registrationPanel = createRegistrationPanel();
        
        mainPanel.add(loginPanel, LOGIN_PANEL);
        mainPanel.add(registrationPanel, REGISTRATION_PANEL);
        
        add(mainPanel, BorderLayout.CENTER);
        
        Panel statusPanel = new Panel(new FlowLayout());
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);
        
        cardLayout.show(mainPanel, LOGIN_PANEL);
    }
    
    private Panel createLoginPanel() {
        Panel panel = new Panel(new BorderLayout());
        
        Panel titlePanel = new Panel(new FlowLayout());
        Label titleLabel = new Label("ECS Course Registration System", Label.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        panel.add(titlePanel, BorderLayout.NORTH);
        
        Panel mainFormPanel = new Panel(new GridLayout(1, 2, 20, 0));
        
        // Student Login Panel
        Panel studentPanel = new Panel(new BorderLayout());
        Label studentTitleLabel = new Label("Student Login", Label.CENTER);
        studentTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        studentPanel.add(studentTitleLabel, BorderLayout.NORTH);
        
        Panel studentFormPanel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        studentFormPanel.add(new Label("Student ID:"), gbc);
        gbc.gridx = 1;
        studentFormPanel.add(studentIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        studentFormPanel.add(new Label("Name:"), gbc);
        gbc.gridx = 1;
        studentFormPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        studentFormPanel.add(new Label("Email:"), gbc);
        gbc.gridx = 1;
        studentFormPanel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        studentFormPanel.add(new Label("Semester:"), gbc);
        gbc.gridx = 1;
        studentFormPanel.add(semesterField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        studentFormPanel.add(new Label("Max Credits:"), gbc);
        gbc.gridx = 1;
        studentFormPanel.add(maxCreditsField, gbc);
        
        Panel studentButtonPanel = new Panel(new FlowLayout());
        studentButtonPanel.add(loginButton);
        studentButtonPanel.add(registerStudentButton);
        
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        studentFormPanel.add(studentButtonPanel, gbc);
        
        studentPanel.add(studentFormPanel, BorderLayout.CENTER);
        
        // Teacher Login Panel
        Panel teacherPanel = new Panel(new BorderLayout());
        Label teacherTitleLabel = new Label("Teacher Login", Label.CENTER);
        teacherTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        teacherPanel.add(teacherTitleLabel, BorderLayout.NORTH);
        
        Panel teacherFormPanel = new Panel(new GridBagLayout());
        GridBagConstraints tgbc = new GridBagConstraints();
        tgbc.insets = new Insets(5, 5, 5, 5);
        
        tgbc.gridx = 0; tgbc.gridy = 0;
        teacherFormPanel.add(new Label("Username:"), tgbc);
        tgbc.gridx = 1;
        teacherFormPanel.add(teacherUsernameField, tgbc);
        
        tgbc.gridx = 0; tgbc.gridy = 1;
        teacherFormPanel.add(new Label("Password:"), tgbc);
        tgbc.gridx = 1;
        teacherFormPanel.add(teacherPasswordField, tgbc);
        
        Panel teacherButtonPanel = new Panel(new FlowLayout());
        teacherButtonPanel.add(teacherLoginButton);
        
        tgbc.gridx = 0; tgbc.gridy = 2;
        tgbc.gridwidth = 2;
        teacherFormPanel.add(teacherButtonPanel, tgbc);
        
        // Add some sample teacher credentials info
        TextArea credentialsInfo = new TextArea(6, 25);
        credentialsInfo.setText("Sample Teacher Accounts:\n\n" +
                              "Username: admin\n" +
                              "Password: admin123\n\n" );
        credentialsInfo.setEditable(false);
        credentialsInfo.setBackground(Color.LIGHT_GRAY);
        
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
        
        Panel headerPanel = new Panel(new BorderLayout());
        Panel studentInfoPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        Label studentInfoLabel = new Label();
        studentInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        studentInfoPanel.add(studentInfoLabel);
        headerPanel.add(studentInfoPanel, BorderLayout.WEST);
        
        Panel creditPanel = new Panel(new FlowLayout(FlowLayout.RIGHT));
        creditsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        creditPanel.add(creditsLabel);
        headerPanel.add(creditPanel, BorderLayout.EAST);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        Panel centerPanel = new Panel(new BorderLayout());
        
        // Left side with course categories in a 2x3 grid
        Panel coursesPanel = new Panel(new GridLayout(2, 3, 5, 5));
        
        Panel corePanel = new Panel(new BorderLayout());
        corePanel.add(new Label("Core Courses (PCC)", Label.CENTER), BorderLayout.NORTH);
        corePanel.add(coreList, BorderLayout.CENTER);
        corePanel.add(addCoreButton, BorderLayout.SOUTH);
        
        Panel pecPanel = new Panel(new BorderLayout());
        pecPanel.add(new Label("Program Electives (PEC)", Label.CENTER), BorderLayout.NORTH);
        pecPanel.add(pecList, BorderLayout.CENTER);
        pecPanel.add(addPECButton, BorderLayout.SOUTH);
        
        Panel honoursPanel = new Panel(new BorderLayout());
        honoursPanel.add(new Label("Honours Courses", Label.CENTER), BorderLayout.NORTH);
        honoursPanel.add(honoursElectiveList, BorderLayout.CENTER);
        honoursPanel.add(addHonoursButton, BorderLayout.SOUTH);
        
        Panel openPanel = new Panel(new BorderLayout());
        openPanel.add(new Label("Open Electives", Label.CENTER), BorderLayout.NORTH);
        openPanel.add(openElectiveList, BorderLayout.CENTER);
        openPanel.add(addOpenButton, BorderLayout.SOUTH);
        
        Panel mdmPanel = new Panel(new BorderLayout());
        mdmPanel.add(new Label("MDM Courses", Label.CENTER), BorderLayout.NORTH);
        mdmPanel.add(mdmList, BorderLayout.CENTER);
        mdmPanel.add(addMDMButton, BorderLayout.SOUTH);
        
        Panel registeredPanel = new Panel(new BorderLayout());
        registeredPanel.add(new Label("My Registered Courses", Label.CENTER), BorderLayout.NORTH);
        registeredPanel.add(registeredList, BorderLayout.CENTER);
        registeredPanel.add(dropButton, BorderLayout.SOUTH);
        
        coursesPanel.add(corePanel);
        coursesPanel.add(pecPanel);
        coursesPanel.add(honoursPanel);
        coursesPanel.add(openPanel);
        coursesPanel.add(mdmPanel);
        coursesPanel.add(registeredPanel);
        
        centerPanel.add(coursesPanel, BorderLayout.CENTER);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        Panel detailsPanel = new Panel(new BorderLayout());
        detailsPanel.add(new Label("Course Details:", Label.LEFT), BorderLayout.NORTH);
        detailsPanel.add(courseDetailsArea, BorderLayout.CENTER);
        
        Panel buttonPanel = new Panel(new FlowLayout());
        buttonPanel.add(refreshButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(logoutButton);
        detailsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        Panel bottomPanel = new Panel(new BorderLayout());
        bottomPanel.add(detailsPanel, BorderLayout.CENTER);
        
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
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
                }
            } else {
                statusLabel.setText("Status: Welcome back " + currentStudent.getName() + "!");
            }
            
            cardLayout.show(mainPanel, REGISTRATION_PANEL);
            updateStudentInfo();
            updateRegisteredCourses();
            statusLabel.setText("Status: Welcome " + currentStudent.getName() + "!");
            
        } catch (NumberFormatException ex) {
            statusLabel.setText("Status: Please enter valid numbers for semester and credits");
        }
    }
    
    private void handleTeacherLogin() {
        try {
            String username = teacherUsernameField.getText().trim();
            String password = teacherPasswordField.getText().trim();
            
            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Status: Please enter username and password");
                return;
            }
            
            TeacherDAO teacherDAO = new TeacherDAO();
            Teacher teacher = teacherDAO.authenticateTeacher(username, password);
            
            if (teacher != null) {
                statusLabel.setText("Status: Teacher login successful");
                this.dispose();
                new TeacherDashboard(teacher).setVisible(true);
            } else {
                statusLabel.setText("Status: Invalid username or password");
                teacherPasswordField.setText("");
            }
            
        } catch (Exception ex) {
            statusLabel.setText("Status: Database connection error - " + ex.getMessage());
            System.err.println("Teacher login error: " + ex.getMessage());
        }
    }
    
    private void addSelectedCourse(java.awt.List courseList, String expectedType) {
        String selectedItem = courseList.getSelectedItem();
        if (selectedItem == null) {
            statusLabel.setText("Status: Please select a course to add");
            return;
        }
        
        String courseId = selectedItem.split(" - ")[0];
        Course course = courseManager.getCourseById(courseId);
        
        if (course == null) {
            statusLabel.setText("Status: Course not found");
            return;
        }
        
        if (currentStudent.canRegister(course)) {
            boolean enrolled = courseManager.enrollStudentInCourse(currentStudent.getStudentId(), course.getCourseId());
            if (enrolled) {
                currentStudent.registerCourse(course);
                updateRegisteredCourses();
                updateCreditsLabel();
                statusLabel.setText("Status: Successfully registered for " + course.getCourseName());
            } else {
                statusLabel.setText("Status: Database enrollment failed");
            }
        } else {
            if (!course.isAvailable()) {
                statusLabel.setText("Status: Course is full");
            } else if (currentStudent.getCurrentCredits() + course.getCredits() > currentStudent.getMaxCredits()) {
                statusLabel.setText("Status: Would exceed credit limit");
            } else {
                statusLabel.setText("Status: Already registered for this course");
            }
        }
    }
    
    private void dropSelectedCourse() {
        String selectedItem = registeredList.getSelectedItem();
        if (selectedItem == null) {
            statusLabel.setText("Status: Please select a registered course to drop");
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
            } else {
                statusLabel.setText("Status: Failed to drop course from database");
            }
        } else {
            statusLabel.setText("Status: Course not found");
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
        new CourseRegistrationSystem().setVisible(true);
    }
}