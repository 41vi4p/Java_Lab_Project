import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class TeacherDashboard extends Frame implements ActionListener {
    private Teacher currentTeacher;
    private CourseManager courseManager;
    private TeacherDAO teacherDAO;
    
    private Panel mainPanel;
    private CardLayout cardLayout;
    
    // Components for different views
    private java.awt.List studentsList, coursesList, enrollmentsList;
    private TextArea studentDetailsArea, courseDetailsArea;
    private Button viewStudentsButton, manageCourseButton, logoutButton;
    private Button editCourseButton, addCourseButton, deleteCourseButton;
    private Button refreshButton;
    
    // Course editing components
    private TextField courseIdField, courseNameField, creditsField, instructorField, maxStudentsField;
    private Choice courseTypeChoice;
    private TextArea prerequisitesArea, descriptionArea;
    private Button saveCourseButton, cancelEditButton;
    
    private Label statusLabel;
    
    private static final String MAIN_VIEW = "main";
    private static final String COURSE_EDIT_VIEW = "edit";
    
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
    
    public TeacherDashboard(Teacher teacher) {
        this.currentTeacher = teacher;
        this.courseManager = CourseManager.getInstance();
        this.teacherDAO = new TeacherDAO();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setTitle("👩‍🏫 ECS Teacher Dashboard - " + teacher.getName());
        setSize(1400, 900);
        setResizable(true);
        setLocationRelativeTo(null);
        setBackground(BACKGROUND_COLOR);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        
        refreshData();
    }
    
    private void initializeComponents() {
        cardLayout = new CardLayout();
        mainPanel = new Panel(cardLayout);
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        studentsList = createStyledList(15);
        coursesList = createStyledList(15);
        enrollmentsList = createStyledList(10);
        
        studentDetailsArea = createStyledTextArea(10, 40);
        studentDetailsArea.setEditable(false);
        
        courseDetailsArea = createStyledTextArea(8, 40);
        courseDetailsArea.setEditable(false);
        
        viewStudentsButton = createStyledButton("👥 View All Students", PRIMARY_COLOR, Color.WHITE);
        manageCourseButton = createStyledButton("📚 Manage Courses", PRIMARY_COLOR, Color.WHITE);
        logoutButton = createStyledButton("💪 Logout", DANGER_COLOR, Color.WHITE);
        editCourseButton = createStyledButton("✏️ Edit Course", WARNING_COLOR, Color.WHITE);
        addCourseButton = createStyledButton("➕ Add Course", ACCENT_COLOR, Color.WHITE);
        deleteCourseButton = createStyledButton("❌ Delete Course", DANGER_COLOR, Color.WHITE);
        refreshButton = createStyledButton("🔄 Refresh", PRIMARY_DARK, Color.WHITE);
        
        // Course editing components with styling
        courseIdField = createStyledTextField(15);
        courseNameField = createStyledTextField(30);
        creditsField = createStyledTextField(5);
        instructorField = createStyledTextField(25);
        maxStudentsField = createStyledTextField(5);
        
        courseTypeChoice = new Choice();
        courseTypeChoice.add("Core");
        courseTypeChoice.add("PEC");
        courseTypeChoice.add("Honours");
        courseTypeChoice.add("Open Elective");
        courseTypeChoice.add("MDM");
        
        prerequisitesArea = createStyledTextArea(3, 30);
        descriptionArea = createStyledTextArea(4, 30);
        
        saveCourseButton = createStyledButton("✓ Save Course", ACCENT_COLOR, Color.WHITE);
        cancelEditButton = createStyledButton("❌ Cancel", WARNING_COLOR, Color.WHITE);
        
        statusLabel = createStyledLabel("Status: Ready");
        statusLabel.setBackground(BORDER_COLOR);
        statusLabel.setForeground(TEXT_COLOR);
    }
    
    private void setupLayout() {
        Panel mainView = createMainView();
        Panel editView = createCourseEditView();
        
        mainPanel.add(mainView, MAIN_VIEW);
        mainPanel.add(editView, COURSE_EDIT_VIEW);
        
        add(mainPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        
        cardLayout.show(mainPanel, MAIN_VIEW);
    }
    
    private Panel createMainView() {
        Panel panel = new Panel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        // Header panel with enhanced styling
        Panel headerPanel = new Panel(new FlowLayout());
        headerPanel.setBackground(HEADER_COLOR);
        Label titleLabel = new Label("👩‍🏫 Teacher Dashboard - " + currentTeacher.getName(), Label.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Main content panel
        Panel contentPanel = new Panel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        
        // Left side - Students and Enrollments with styled panels
        Panel leftPanel = createStyledSectionPanel();
        Label studentsLabel = new Label("👥 All Students", Label.CENTER);
        studentsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        studentsLabel.setForeground(PRIMARY_DARK);
        studentsLabel.setBackground(SECONDARY_COLOR);
        leftPanel.add(studentsLabel, BorderLayout.NORTH);
        leftPanel.add(studentsList, BorderLayout.CENTER);
        
        Panel leftButtonPanel = new Panel(new FlowLayout());
        leftButtonPanel.setBackground(CARD_COLOR);
        leftButtonPanel.add(viewStudentsButton);
        leftPanel.add(leftButtonPanel, BorderLayout.SOUTH);
        
        // Center - Course Management with styling
        Panel centerPanel = createStyledSectionPanel();
        Label coursesLabel = new Label("📚 All Courses", Label.CENTER);
        coursesLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        coursesLabel.setForeground(PRIMARY_DARK);
        coursesLabel.setBackground(SECONDARY_COLOR);
        centerPanel.add(coursesLabel, BorderLayout.NORTH);
        centerPanel.add(coursesList, BorderLayout.CENTER);
        
        Panel centerButtonPanel = new Panel(new GridLayout(2, 2, 5, 5));
        centerButtonPanel.setBackground(CARD_COLOR);
        centerButtonPanel.add(editCourseButton);
        centerButtonPanel.add(addCourseButton);
        centerButtonPanel.add(deleteCourseButton);
        centerButtonPanel.add(refreshButton);
        centerPanel.add(centerButtonPanel, BorderLayout.SOUTH);
        
        // Right side - Details and Enrollments with styling
        Panel rightPanel = createStyledSectionPanel();
        
        Panel detailsPanel = new Panel(new GridLayout(2, 1, 5, 5));
        detailsPanel.setBackground(CARD_COLOR);
        
        Panel studentDetailPanel = createStyledSectionPanel();
        Label studentDetailLabel = new Label("👤 Student Details", Label.CENTER);
        studentDetailLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        studentDetailLabel.setForeground(PRIMARY_DARK);
        studentDetailLabel.setBackground(SECONDARY_COLOR);
        studentDetailPanel.add(studentDetailLabel, BorderLayout.NORTH);
        studentDetailPanel.add(studentDetailsArea, BorderLayout.CENTER);
        detailsPanel.add(studentDetailPanel);
        
        Panel courseDetailPanel = createStyledSectionPanel();
        Label courseDetailLabel = new Label("📋 Course Details", Label.CENTER);
        courseDetailLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        courseDetailLabel.setForeground(PRIMARY_DARK);
        courseDetailLabel.setBackground(SECONDARY_COLOR);
        courseDetailPanel.add(courseDetailLabel, BorderLayout.NORTH);
        courseDetailPanel.add(courseDetailsArea, BorderLayout.CENTER);
        detailsPanel.add(courseDetailPanel);
        
        rightPanel.add(detailsPanel, BorderLayout.CENTER);
        
        Panel enrollmentPanel = createStyledSectionPanel();
        Label enrollmentLabel = new Label("📝 Course Enrollments", Label.CENTER);
        enrollmentLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        enrollmentLabel.setForeground(PRIMARY_DARK);
        enrollmentLabel.setBackground(SECONDARY_COLOR);
        enrollmentPanel.add(enrollmentLabel, BorderLayout.NORTH);
        enrollmentPanel.add(enrollmentsList, BorderLayout.CENTER);
        rightPanel.add(enrollmentPanel, BorderLayout.SOUTH);
        
        contentPanel.add(leftPanel, BorderLayout.WEST);
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(rightPanel, BorderLayout.EAST);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        // Bottom button panel with styling
        Panel bottomPanel = new Panel(new FlowLayout());
        bottomPanel.setBackground(BORDER_COLOR);
        bottomPanel.add(logoutButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private Panel createCourseEditView() {
        Panel panel = new Panel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        Panel headerPanel = new Panel(new FlowLayout());
        headerPanel.setBackground(HEADER_COLOR);
        Label titleLabel = new Label("📚 Course Management", Label.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        panel.add(headerPanel, BorderLayout.NORTH);
        
        Panel formPanel = new Panel(new GridBagLayout());
        formPanel.setBackground(CARD_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        Label courseIdLabel = createStyledLabel("Course ID:");
        courseIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(courseIdLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(courseIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        Label courseNameLabel = createStyledLabel("Course Name:");
        courseNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(courseNameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(courseNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        Label creditsLabel = createStyledLabel("Credits:");
        creditsLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(creditsLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(creditsField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        Label courseTypeLabel = createStyledLabel("Course Type:");
        courseTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(courseTypeLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(courseTypeChoice, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        Label instructorLabel = createStyledLabel("Instructor:");
        instructorLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(instructorLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(instructorField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        Label maxStudentsLabel = createStyledLabel("Max Students:");
        maxStudentsLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(maxStudentsLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(maxStudentsField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        Label prerequisitesLabel = createStyledLabel("Prerequisites:");
        prerequisitesLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(prerequisitesLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(prerequisitesArea, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        Label descriptionLabel = createStyledLabel("Description:");
        descriptionLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(descriptionLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(descriptionArea, gbc);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        Panel buttonPanel = new Panel(new FlowLayout());
        buttonPanel.setBackground(BORDER_COLOR);
        buttonPanel.add(saveCourseButton);
        buttonPanel.add(cancelEditButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        viewStudentsButton.addActionListener(this);
        manageCourseButton.addActionListener(this);
        logoutButton.addActionListener(this);
        editCourseButton.addActionListener(this);
        addCourseButton.addActionListener(this);
        deleteCourseButton.addActionListener(this);
        refreshButton.addActionListener(this);
        saveCourseButton.addActionListener(this);
        cancelEditButton.addActionListener(this);
        
        studentsList.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    showStudentDetails();
                }
            }
        });
        
        coursesList.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    showCourseDetails();
                    showCourseEnrollments();
                }
            }
        });
    }
    
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        switch (command) {
            case "View All Students":
                refreshStudentsList();
                break;
            case "Manage Courses":
                refreshCoursesList();
                break;
            case "Edit Selected Course":
                editSelectedCourse();
                break;
            case "Add New Course":
                addNewCourse();
                break;
            case "Delete Selected Course":
                deleteSelectedCourse();
                break;
            case "Refresh Data":
                refreshData();
                break;
            case "Save Course":
                saveCourse();
                break;
            case "Cancel":
                cardLayout.show(mainPanel, MAIN_VIEW);
                break;
            case "Logout":
                dispose();
                new CourseRegistrationSystem().setVisible(true);
                break;
        }
    }
    
    private void refreshData() {
        refreshStudentsList();
        refreshCoursesList();
        statusLabel.setText("Status: Data refreshed");
    }
    
    private void refreshStudentsList() {
        studentsList.removeAll();
        List<Student> students = courseManager.getAllStudents();
        for (Student student : students) {
            studentsList.add(student.toString());
        }
        statusLabel.setText("Status: " + students.size() + " students loaded");
    }
    
    private void refreshCoursesList() {
        coursesList.removeAll();
        List<Course> courses = courseManager.getAllCourses();
        for (Course course : courses) {
            coursesList.add(course.toString());
        }
        statusLabel.setText("Status: " + courses.size() + " courses loaded");
    }
    
    private void showStudentDetails() {
        String selectedItem = studentsList.getSelectedItem();
        if (selectedItem != null) {
            String studentId = selectedItem.split(" - ")[0];
            Student student = courseManager.getStudentById(studentId);
            if (student != null) {
                StringBuilder details = new StringBuilder();
                details.append("Student ID: ").append(student.getStudentId()).append("\n");
                details.append("Name: ").append(student.getName()).append("\n");
                details.append("Email: ").append(student.getEmail()).append("\n");
                details.append("Semester: ").append(student.getSemester()).append("\n");
                details.append("Max Credits: ").append(student.getMaxCredits()).append("\n");
                details.append("Current Credits: ").append(student.getCurrentCredits()).append("\n");
                details.append("\nRegistered Courses:\n");
                for (Course course : student.getRegisteredCourses()) {
                    details.append("- ").append(course.toString()).append("\n");
                }
                studentDetailsArea.setText(details.toString());
            }
        }
    }
    
    private void showCourseDetails() {
        String selectedItem = coursesList.getSelectedItem();
        if (selectedItem != null) {
            String courseId = selectedItem.split(" - ")[0];
            Course course = courseManager.getCourseById(courseId);
            if (course != null) {
                courseDetailsArea.setText(course.getDetailedInfo());
            }
        }
    }
    
    private void showCourseEnrollments() {
        enrollmentsList.removeAll();
        String selectedItem = coursesList.getSelectedItem();
        if (selectedItem != null) {
            String courseId = selectedItem.split(" - ")[0];
            List<Student> enrolledStudents = courseManager.getStudentsInCourse(courseId);
            for (Student student : enrolledStudents) {
                enrollmentsList.add(student.toString());
            }
            statusLabel.setText("Status: " + enrolledStudents.size() + " students enrolled in course");
        }
    }
    
    private void editSelectedCourse() {
        String selectedItem = coursesList.getSelectedItem();
        if (selectedItem == null) {
            statusLabel.setText("Status: Please select a course to edit");
            return;
        }
        
        String courseId = selectedItem.split(" - ")[0];
        Course course = courseManager.getCourseById(courseId);
        if (course != null) {
            // Populate form fields
            courseIdField.setText(course.getCourseId());
            courseIdField.setEditable(false); // Don't allow editing course ID
            courseNameField.setText(course.getCourseName());
            creditsField.setText(String.valueOf(course.getCredits()));
            courseTypeChoice.select(course.getType());
            instructorField.setText(course.getInstructor());
            maxStudentsField.setText(String.valueOf(course.getMaxStudents()));
            
            if (course.getPrerequisites() != null) {
                prerequisitesArea.setText(String.join(", ", course.getPrerequisites()));
            } else {
                prerequisitesArea.setText("");
            }
            descriptionArea.setText("");
            
            cardLayout.show(mainPanel, COURSE_EDIT_VIEW);
        }
    }
    
    private void addNewCourse() {
        // Clear form fields for new course
        courseIdField.setText("");
        courseIdField.setEditable(true);
        courseNameField.setText("");
        creditsField.setText("");
        courseTypeChoice.select(0);
        instructorField.setText("");
        maxStudentsField.setText("30");
        prerequisitesArea.setText("");
        descriptionArea.setText("");
        
        cardLayout.show(mainPanel, COURSE_EDIT_VIEW);
    }
    
    private void saveCourse() {
        try {
            String courseId = courseIdField.getText().trim();
            String courseName = courseNameField.getText().trim();
            int credits = Integer.parseInt(creditsField.getText().trim());
            String courseType = courseTypeChoice.getSelectedItem();
            String instructor = instructorField.getText().trim();
            int maxStudents = Integer.parseInt(maxStudentsField.getText().trim());
            
            if (courseId.isEmpty() || courseName.isEmpty() || instructor.isEmpty()) {
                statusLabel.setText("Status: Please fill all required fields");
                return;
            }
            
            String[] prerequisites = null;
            String prereqText = prerequisitesArea.getText().trim();
            if (!prereqText.isEmpty()) {
                prerequisites = prereqText.split(",");
                for (int i = 0; i < prerequisites.length; i++) {
                    prerequisites[i] = prerequisites[i].trim();
                }
            }
            
            Course course = new Course(courseId, courseName, credits, courseType, 
                                     instructor, maxStudents, prerequisites);
            
            boolean success;
            if (courseIdField.isEditable()) {
                // Adding new course
                success = courseManager.addCourse(course);
                statusLabel.setText(success ? "Status: Course added successfully" : "Status: Failed to add course");
            } else {
                // Updating existing course
                success = courseManager.updateCourse(course);
                statusLabel.setText(success ? "Status: Course updated successfully" : "Status: Failed to update course");
            }
            
            if (success) {
                cardLayout.show(mainPanel, MAIN_VIEW);
                refreshCoursesList();
            }
        } catch (NumberFormatException ex) {
            statusLabel.setText("Status: Please enter valid numbers for credits and max students");
        }
    }
    
    private void deleteSelectedCourse() {
        String selectedItem = coursesList.getSelectedItem();
        if (selectedItem == null) {
            statusLabel.setText("Status: Please select a course to delete");
            return;
        }
        
        String courseId = selectedItem.split(" - ")[0];
        
        // Simple confirmation dialog using AWT Dialog
        Dialog confirmDialog = new Dialog(this, "Confirm Deletion", true);
        confirmDialog.setLayout(new BorderLayout());
        confirmDialog.add(new Label("Are you sure you want to delete course: " + courseId + "?", Label.CENTER), BorderLayout.CENTER);
        
        Panel buttonPanel = new Panel(new FlowLayout());
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");
        
        yesButton.addActionListener(e -> {
            boolean success = courseManager.deleteCourse(courseId);
            statusLabel.setText(success ? "Status: Course deleted successfully" : "Status: Failed to delete course");
            if (success) {
                refreshCoursesList();
            }
            confirmDialog.dispose();
        });
        
        noButton.addActionListener(e -> confirmDialog.dispose());
        
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        confirmDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        confirmDialog.setSize(300, 120);
        confirmDialog.setLocationRelativeTo(this);
        confirmDialog.setVisible(true);
    }
    
    // Helper methods to create styled components
    private TextField createStyledTextField(int columns) {
        TextField field = new TextField(columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_COLOR);
        
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
    
    private Button createStyledButton(String text, Color bgColor, Color textColor) {
        Button button = new Button(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(textColor);
        
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
    
    private java.awt.List createStyledList(int rows) {
        java.awt.List list = new java.awt.List(rows, false);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        list.setBackground(Color.WHITE);
        list.setForeground(TEXT_COLOR);
        return list;
    }
    
    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(TEXT_COLOR);
        return label;
    }
    
    private TextArea createStyledTextArea(int rows, int cols) {
        TextArea area = new TextArea(rows, cols);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        area.setBackground(Color.WHITE);
        area.setForeground(TEXT_COLOR);
        return area;
    }
    
    private Panel createStyledSectionPanel() {
        Panel panel = new Panel(new BorderLayout());
        panel.setBackground(CARD_COLOR);
        return panel;
    }
}