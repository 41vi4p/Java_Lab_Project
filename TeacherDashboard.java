import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class TeacherDashboard extends JFrame implements ActionListener {
    private Teacher currentTeacher;
    private CourseManager courseManager;
    private TeacherDAO teacherDAO;

    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Components for different views
    private JList<String> studentsList, coursesList, enrollmentsList;
    private JTextArea studentDetailsArea, courseDetailsArea;
    private JButton viewStudentsButton, manageCourseButton, logoutButton;
    private JButton editCourseButton, addCourseButton, deleteCourseButton;
    private JButton refreshButton;

    // Course editing components
    private JTextField courseIdField, courseNameField, creditsField, instructorField, maxStudentsField;
    private JComboBox<String> courseTypeChoice;
    private JTextArea prerequisitesArea, descriptionArea;
    private JButton saveCourseButton, cancelEditButton;

    private JLabel statusLabel;

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

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

        refreshData();
    }

    private void initializeComponents() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BACKGROUND_COLOR);

        studentsList = createStyledList();
        coursesList = createStyledList();
        enrollmentsList = createStyledList();

        studentDetailsArea = createStyledTextArea(10, 40);
        studentDetailsArea.setEditable(false);

        courseDetailsArea = createStyledTextArea(8, 40);
        courseDetailsArea.setEditable(false);

        viewStudentsButton = createStyledButton("👥 View All Students", PRIMARY_COLOR, Color.WHITE);
        manageCourseButton = createStyledButton("📚 Manage Courses", PRIMARY_COLOR, Color.WHITE);
        logoutButton = createStyledButton("🚪 Logout", DANGER_COLOR, Color.WHITE);
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

        courseTypeChoice = new JComboBox<>();
        courseTypeChoice.addItem("Core");
        courseTypeChoice.addItem("PEC");
        courseTypeChoice.addItem("Honours");
        courseTypeChoice.addItem("Open Elective");
        courseTypeChoice.addItem("MDM");
        courseTypeChoice.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        courseTypeChoice.setBackground(Color.WHITE);
        courseTypeChoice.setForeground(TEXT_COLOR);

        prerequisitesArea = createStyledTextArea(4, 25);
        descriptionArea = createStyledTextArea(6, 25);

        saveCourseButton = createStyledButton("💾 Save Course", ACCENT_COLOR, Color.WHITE);
        cancelEditButton = createStyledButton("❌ Cancel", WARNING_COLOR, Color.WHITE);

        statusLabel = createStyledLabel("Status: Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setForeground(TEXT_COLOR);
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
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        // Add mouse listeners for hover effects
        button.addMouseListener(new MouseAdapter() {
            Color originalColor = bgColor;

            public void mouseEntered(MouseEvent e) {
                int r = Math.max(0, originalColor.getRed() - 20);
                int g = Math.max(0, originalColor.getGreen() - 20);
                int b = Math.max(0, originalColor.getBlue() - 20);
                button.setBackground(new Color(r, g, b));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor);
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

    private JTextArea createStyledTextArea(int rows, int columns) {
        JTextArea area = new JTextArea(rows, columns);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        area.setBackground(Color.WHITE);
        area.setForeground(TEXT_COLOR);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return area;
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
        JPanel mainViewPanel = createMainViewPanel();
        JPanel editViewPanel = createEditViewPanel();

        mainPanel.add(mainViewPanel, MAIN_VIEW);
        mainPanel.add(editViewPanel, COURSE_EDIT_VIEW);

        add(mainPanel, BorderLayout.CENTER);

        // Status bar
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(SECONDARY_COLOR);
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);

        cardLayout.show(mainPanel, MAIN_VIEW);
    }

    private JPanel createMainViewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        // Header with title and main action buttons
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("📚 Teacher Dashboard", JLabel.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(refreshButton);
        buttonPanel.add(logoutButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Main content with tabs simulation
        JPanel contentPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Students Panel
        JPanel studentsPanel = createStyledPanel();
        studentsPanel.setLayout(new BorderLayout());
        studentsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 2),
            "👥 Students Overview",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            PRIMARY_COLOR
        ));

        JScrollPane studentsScrollPane = new JScrollPane(studentsList);
        studentsScrollPane.setPreferredSize(new Dimension(300, 250));
        studentsPanel.add(studentsScrollPane, BorderLayout.CENTER);

        JPanel studentsButtonPanel = new JPanel(new FlowLayout());
        studentsButtonPanel.setBackground(CARD_COLOR);
        studentsButtonPanel.add(viewStudentsButton);
        studentsPanel.add(studentsButtonPanel, BorderLayout.SOUTH);

        contentPanel.add(studentsPanel);

        // Courses Panel
        JPanel coursesPanel = createStyledPanel();
        coursesPanel.setLayout(new BorderLayout());
        coursesPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 2),
            "📚 Course Management",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            PRIMARY_COLOR
        ));

        JScrollPane coursesScrollPane = new JScrollPane(coursesList);
        coursesScrollPane.setPreferredSize(new Dimension(300, 250));
        coursesPanel.add(coursesScrollPane, BorderLayout.CENTER);

        JPanel coursesButtonPanel = new JPanel(new FlowLayout());
        coursesButtonPanel.setBackground(CARD_COLOR);
        coursesButtonPanel.add(addCourseButton);
        coursesButtonPanel.add(editCourseButton);
        coursesButtonPanel.add(deleteCourseButton);
        coursesPanel.add(coursesButtonPanel, BorderLayout.SOUTH);

        contentPanel.add(coursesPanel);

        // Details Panel
        JPanel detailsPanel = createStyledPanel();
        detailsPanel.setLayout(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 2),
            "📋 Details View",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            PRIMARY_COLOR
        ));

        JTabbedPane detailsTabs = new JTabbedPane();
        detailsTabs.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JScrollPane studentDetailsScrollPane = new JScrollPane(studentDetailsArea);
        detailsTabs.addTab("👤 Student Details", studentDetailsScrollPane);

        JScrollPane courseDetailsScrollPane = new JScrollPane(courseDetailsArea);
        detailsTabs.addTab("📖 Course Details", courseDetailsScrollPane);

        JScrollPane enrollmentsScrollPane = new JScrollPane(enrollmentsList);
        detailsTabs.addTab("👥 Enrollments", enrollmentsScrollPane);

        detailsPanel.add(detailsTabs, BorderLayout.CENTER);

        contentPanel.add(detailsPanel);

        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createEditViewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("✏️ Course Editor", JLabel.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = createStyledPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Course ID
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createStyledLabel("Course ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(courseIdField, gbc);

        // Course Name
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createStyledLabel("Course Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(courseNameField, gbc);

        // Credits
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createStyledLabel("Credits:"), gbc);
        gbc.gridx = 1;
        formPanel.add(creditsField, gbc);

        // Instructor
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createStyledLabel("Instructor:"), gbc);
        gbc.gridx = 1;
        formPanel.add(instructorField, gbc);

        // Max Students
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(createStyledLabel("Max Students:"), gbc);
        gbc.gridx = 1;
        formPanel.add(maxStudentsField, gbc);

        // Course Type
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(createStyledLabel("Course Type:"), gbc);
        gbc.gridx = 1;
        formPanel.add(courseTypeChoice, gbc);

        // Prerequisites
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(createStyledLabel("Prerequisites:"), gbc);
        gbc.gridx = 1;
        JScrollPane prereqScrollPane = new JScrollPane(prerequisitesArea);
        prereqScrollPane.setPreferredSize(new Dimension(300, 80));
        formPanel.add(prereqScrollPane, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(createStyledLabel("Description:"), gbc);
        gbc.gridx = 1;
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setPreferredSize(new Dimension(300, 120));
        formPanel.add(descScrollPane, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.add(saveCourseButton);
        buttonPanel.add(cancelEditButton);
        formPanel.add(buttonPanel, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

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

        // Add selection listeners for lists
        studentsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = studentsList.getSelectedValue();
                if (selected != null) {
                    showStudentDetails(selected);
                }
            }
        });

        coursesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = coursesList.getSelectedValue();
                if (selected != null) {
                    showCourseDetails(selected);
                    showEnrollments(selected);
                }
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "👥 View All Students":
                refreshStudentsList();
                break;
            case "📚 Manage Courses":
                refreshCoursesList();
                break;
            case "🚪 Logout":
                logout();
                break;
            case "✏️ Edit Course":
                editSelectedCourse();
                break;
            case "➕ Add Course":
                addNewCourse();
                break;
            case "❌ Delete Course":
                deleteSelectedCourse();
                break;
            case "🔄 Refresh":
                refreshData();
                break;
            case "💾 Save Course":
                saveCourse();
                break;
            case "❌ Cancel":
                cancelEdit();
                break;
        }
    }

    private void refreshData() {
        refreshStudentsList();
        refreshCoursesList();
        statusLabel.setText("Status: Data refreshed");
        statusLabel.setForeground(ACCENT_COLOR);
    }

    private void refreshStudentsList() {
        DefaultListModel<String> model = (DefaultListModel<String>) studentsList.getModel();
        model.clear();

        try {
            List<Student> students = courseManager.getAllStudents();
            for (Student student : students) {
                model.addElement(student.getStudentId() + " - " + student.getName());
            }
            statusLabel.setText("Status: Students list updated (" + students.size() + " students)");
            statusLabel.setForeground(ACCENT_COLOR);
        } catch (Exception ex) {
            statusLabel.setText("Status: Error loading students - " + ex.getMessage());
            statusLabel.setForeground(DANGER_COLOR);
        }
    }

    private void refreshCoursesList() {
        DefaultListModel<String> model = (DefaultListModel<String>) coursesList.getModel();
        model.clear();

        try {
            List<Course> courses = courseManager.getAllCourses();
            for (Course course : courses) {
                model.addElement(course.getCourseId() + " - " + course.getCourseName());
            }
            statusLabel.setText("Status: Courses list updated (" + courses.size() + " courses)");
            statusLabel.setForeground(ACCENT_COLOR);
        } catch (Exception ex) {
            statusLabel.setText("Status: Error loading courses - " + ex.getMessage());
            statusLabel.setForeground(DANGER_COLOR);
        }
    }

    private void showStudentDetails(String selectedItem) {
        String studentId = selectedItem.split(" - ")[0];
        try {
            Student student = courseManager.getStudentById(studentId);
            if (student != null) {
                StringBuilder details = new StringBuilder();
                details.append("Student ID: ").append(student.getStudentId()).append("\\n");
                details.append("Name: ").append(student.getName()).append("\\n");
                details.append("Email: ").append(student.getEmail()).append("\\n");
                details.append("Semester: ").append(student.getSemester()).append("\\n");
                details.append("Max Credits: ").append(student.getMaxCredits()).append("\\n");
                details.append("Current Credits: ").append(student.getCurrentCredits()).append("\\n\\n");
                details.append("Registered Courses:\\n");
                for (Course course : student.getRegisteredCourses()) {
                    details.append("- ").append(course.getCourseId()).append(": ").append(course.getCourseName()).append("\\n");
                }
                studentDetailsArea.setText(details.toString());
            }
        } catch (Exception ex) {
            studentDetailsArea.setText("Error loading student details: " + ex.getMessage());
        }
    }

    private void showCourseDetails(String selectedItem) {
        String courseId = selectedItem.split(" - ")[0];
        try {
            Course course = courseManager.getCourseById(courseId);
            if (course != null) {
                courseDetailsArea.setText(course.getDetailedInfo());
            }
        } catch (Exception ex) {
            courseDetailsArea.setText("Error loading course details: " + ex.getMessage());
        }
    }

    private void showEnrollments(String selectedItem) {
        String courseId = selectedItem.split(" - ")[0];
        DefaultListModel<String> model = (DefaultListModel<String>) enrollmentsList.getModel();
        model.clear();

        try {
            List<Student> enrolledStudents = courseManager.getStudentsInCourse(courseId);
            for (Student student : enrolledStudents) {
                model.addElement(student.getStudentId() + " - " + student.getName());
            }
        } catch (Exception ex) {
            model.addElement("Error loading enrollments: " + ex.getMessage());
        }
    }

    private void editSelectedCourse() {
        String selected = coursesList.getSelectedValue();
        if (selected == null) {
            statusLabel.setText("Status: Please select a course to edit");
            statusLabel.setForeground(WARNING_COLOR);
            return;
        }

        String courseId = selected.split(" - ")[0];
        try {
            Course course = courseManager.getCourseById(courseId);
            if (course != null) {
                populateEditForm(course);
                cardLayout.show(mainPanel, COURSE_EDIT_VIEW);
            }
        } catch (Exception ex) {
            statusLabel.setText("Status: Error loading course for editing - " + ex.getMessage());
            statusLabel.setForeground(DANGER_COLOR);
        }
    }

    private void addNewCourse() {
        clearEditForm();
        cardLayout.show(mainPanel, COURSE_EDIT_VIEW);
    }

    private void deleteSelectedCourse() {
        String selected = coursesList.getSelectedValue();
        if (selected == null) {
            statusLabel.setText("Status: Please select a course to delete");
            statusLabel.setForeground(WARNING_COLOR);
            return;
        }

        String courseId = selected.split(" - ")[0];
        int confirmation = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete course " + courseId + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                boolean deleted = courseManager.deleteCourse(courseId);
                if (deleted) {
                    statusLabel.setText("Status: Course " + courseId + " deleted successfully");
                    statusLabel.setForeground(ACCENT_COLOR);
                    refreshCoursesList();
                } else {
                    statusLabel.setText("Status: Failed to delete course " + courseId);
                    statusLabel.setForeground(DANGER_COLOR);
                }
            } catch (Exception ex) {
                statusLabel.setText("Status: Error deleting course - " + ex.getMessage());
                statusLabel.setForeground(DANGER_COLOR);
            }
        }
    }

    private void populateEditForm(Course course) {
        courseIdField.setText(course.getCourseId());
        courseNameField.setText(course.getCourseName());
        creditsField.setText(String.valueOf(course.getCredits()));
        instructorField.setText(course.getInstructor());
        maxStudentsField.setText(String.valueOf(course.getMaxStudents()));
        courseTypeChoice.setSelectedItem(course.getType());
        prerequisitesArea.setText(String.join(", ", course.getPrerequisites()));
        descriptionArea.setText(""); // No description field in Course class
    }

    private void clearEditForm() {
        courseIdField.setText("");
        courseNameField.setText("");
        creditsField.setText("");
        instructorField.setText("");
        maxStudentsField.setText("");
        courseTypeChoice.setSelectedIndex(0);
        prerequisitesArea.setText("");
        descriptionArea.setText("");
    }

    private void saveCourse() {
        try {
            String courseId = courseIdField.getText().trim();
            String courseName = courseNameField.getText().trim();
            int credits = Integer.parseInt(creditsField.getText().trim());
            String instructor = instructorField.getText().trim();
            int maxStudents = Integer.parseInt(maxStudentsField.getText().trim());
            String courseType = (String) courseTypeChoice.getSelectedItem();
            String prerequisites = prerequisitesArea.getText().trim();
            String description = descriptionArea.getText().trim();

            if (courseId.isEmpty() || courseName.isEmpty() || instructor.isEmpty()) {
                statusLabel.setText("Status: Please fill all required fields");
                statusLabel.setForeground(WARNING_COLOR);
                return;
            }

            String[] prereqArray = null;
            if (!prerequisites.isEmpty()) {
                prereqArray = prerequisites.split(",\\s*");
                for (int i = 0; i < prereqArray.length; i++) {
                    prereqArray[i] = prereqArray[i].trim();
                }
            }

            Course course = new Course(courseId, courseName, credits, courseType, instructor, maxStudents, prereqArray);
            // Note: Description is not saved as Course class doesn't have this field

            boolean saved = courseManager.addCourse(course);
            if (saved) {
                statusLabel.setText("Status: Course saved successfully");
                statusLabel.setForeground(ACCENT_COLOR);
                cardLayout.show(mainPanel, MAIN_VIEW);
                refreshCoursesList();
            } else {
                statusLabel.setText("Status: Failed to save course");
                statusLabel.setForeground(DANGER_COLOR);
            }

        } catch (NumberFormatException ex) {
            statusLabel.setText("Status: Please enter valid numbers for credits and max students");
            statusLabel.setForeground(DANGER_COLOR);
        } catch (Exception ex) {
            statusLabel.setText("Status: Error saving course - " + ex.getMessage());
            statusLabel.setForeground(DANGER_COLOR);
        }
    }

    private void cancelEdit() {
        cardLayout.show(mainPanel, MAIN_VIEW);
    }

    private void logout() {
        int confirmation = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirmation == JOptionPane.YES_OPTION) {
            this.dispose();
            new CourseRegistrationSystem().setVisible(true);
        }
    }
}