import java.awt.*;
import java.awt.event.*;
import java.util.*;
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
    
    public TeacherDashboard(Teacher teacher) {
        this.currentTeacher = teacher;
        this.courseManager = CourseManager.getInstance();
        this.teacherDAO = new TeacherDAO();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setTitle("ECS Teacher Dashboard - " + teacher.getName());
        setSize(1400, 900);
        setResizable(true);
        
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
        
        studentsList = new java.awt.List(15, false);
        coursesList = new java.awt.List(15, false);
        enrollmentsList = new java.awt.List(10, false);
        
        studentDetailsArea = new TextArea(10, 40);
        studentDetailsArea.setEditable(false);
        
        courseDetailsArea = new TextArea(8, 40);
        courseDetailsArea.setEditable(false);
        
        viewStudentsButton = new Button("View All Students");
        manageCourseButton = new Button("Manage Courses");
        logoutButton = new Button("Logout");
        editCourseButton = new Button("Edit Selected Course");
        addCourseButton = new Button("Add New Course");
        deleteCourseButton = new Button("Delete Selected Course");
        refreshButton = new Button("Refresh Data");
        
        // Course editing components
        courseIdField = new TextField(15);
        courseNameField = new TextField(30);
        creditsField = new TextField(5);
        instructorField = new TextField(25);
        maxStudentsField = new TextField(5);
        
        courseTypeChoice = new Choice();
        courseTypeChoice.add("Core");
        courseTypeChoice.add("PEC");
        courseTypeChoice.add("Honours");
        courseTypeChoice.add("Open Elective");
        courseTypeChoice.add("MDM");
        
        prerequisitesArea = new TextArea(3, 30);
        descriptionArea = new TextArea(4, 30);
        
        saveCourseButton = new Button("Save Course");
        cancelEditButton = new Button("Cancel");
        
        statusLabel = new Label("Status: Ready", Label.CENTER);
        statusLabel.setBackground(Color.LIGHT_GRAY);
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
        
        // Header panel
        Panel headerPanel = new Panel(new FlowLayout());
        Label titleLabel = new Label("Teacher Dashboard - " + currentTeacher.getName(), Label.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel);
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Main content panel
        Panel contentPanel = new Panel(new BorderLayout());
        
        // Left side - Students and Enrollments
        Panel leftPanel = new Panel(new BorderLayout());
        leftPanel.add(new Label("All Students", Label.CENTER), BorderLayout.NORTH);
        leftPanel.add(studentsList, BorderLayout.CENTER);
        
        Panel leftButtonPanel = new Panel(new FlowLayout());
        leftButtonPanel.add(viewStudentsButton);
        leftPanel.add(leftButtonPanel, BorderLayout.SOUTH);
        
        // Center - Course Management
        Panel centerPanel = new Panel(new BorderLayout());
        centerPanel.add(new Label("All Courses", Label.CENTER), BorderLayout.NORTH);
        centerPanel.add(coursesList, BorderLayout.CENTER);
        
        Panel centerButtonPanel = new Panel(new GridLayout(2, 2, 5, 5));
        centerButtonPanel.add(editCourseButton);
        centerButtonPanel.add(addCourseButton);
        centerButtonPanel.add(deleteCourseButton);
        centerButtonPanel.add(refreshButton);
        centerPanel.add(centerButtonPanel, BorderLayout.SOUTH);
        
        // Right side - Details and Enrollments
        Panel rightPanel = new Panel(new BorderLayout());
        
        Panel detailsPanel = new Panel(new GridLayout(2, 1, 5, 5));
        
        Panel studentDetailPanel = new Panel(new BorderLayout());
        studentDetailPanel.add(new Label("Student Details", Label.CENTER), BorderLayout.NORTH);
        studentDetailPanel.add(studentDetailsArea, BorderLayout.CENTER);
        detailsPanel.add(studentDetailPanel);
        
        Panel courseDetailPanel = new Panel(new BorderLayout());
        courseDetailPanel.add(new Label("Course Details", Label.CENTER), BorderLayout.NORTH);
        courseDetailPanel.add(courseDetailsArea, BorderLayout.CENTER);
        detailsPanel.add(courseDetailPanel);
        
        rightPanel.add(detailsPanel, BorderLayout.CENTER);
        
        Panel enrollmentPanel = new Panel(new BorderLayout());
        enrollmentPanel.add(new Label("Course Enrollments", Label.CENTER), BorderLayout.NORTH);
        enrollmentPanel.add(enrollmentsList, BorderLayout.CENTER);
        rightPanel.add(enrollmentPanel, BorderLayout.SOUTH);
        
        contentPanel.add(leftPanel, BorderLayout.WEST);
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(rightPanel, BorderLayout.EAST);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        // Bottom button panel
        Panel bottomPanel = new Panel(new FlowLayout());
        bottomPanel.add(logoutButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private Panel createCourseEditView() {
        Panel panel = new Panel(new BorderLayout());
        
        Panel headerPanel = new Panel(new FlowLayout());
        Label titleLabel = new Label("Course Management", Label.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel);
        panel.add(headerPanel, BorderLayout.NORTH);
        
        Panel formPanel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new Label("Course ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(courseIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new Label("Course Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(courseNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new Label("Credits:"), gbc);
        gbc.gridx = 1;
        formPanel.add(creditsField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new Label("Course Type:"), gbc);
        gbc.gridx = 1;
        formPanel.add(courseTypeChoice, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new Label("Instructor:"), gbc);
        gbc.gridx = 1;
        formPanel.add(instructorField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new Label("Max Students:"), gbc);
        gbc.gridx = 1;
        formPanel.add(maxStudentsField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new Label("Prerequisites:"), gbc);
        gbc.gridx = 1;
        formPanel.add(prerequisitesArea, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new Label("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(descriptionArea, gbc);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        Panel buttonPanel = new Panel(new FlowLayout());
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
}