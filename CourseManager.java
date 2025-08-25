import java.util.*;
import java.io.*;

public class CourseManager {
    private CourseDAO courseDAO;
    private StudentDAO studentDAO;
    private static CourseManager instance;
    
    private CourseManager() {
        try {
            courseDAO = new CourseDAO();
            studentDAO = new StudentDAO();
            System.out.println("CourseManager initialized with database connection");
        } catch (Exception e) {
            System.err.println("Failed to initialize CourseManager with database: " + e.getMessage());
            System.err.println("Falling back to in-memory data...");
            // Fallback to in-memory if database connection fails
            courseDAO = null;
            studentDAO = null;
        }
    }
    
    public static CourseManager getInstance() {
        if (instance == null) {
            instance = new CourseManager();
        }
        return instance;
    }
    
    private void initializeSampleCourses() {
        // SEMESTER-V CORE COURSES (PCC)
        allCourses.add(new Course("25PCC13EC11", "Control Systems", 3, "Core", 
                                 "Dr. Kumar", 60, null));
        allCourses.add(new Course("25PCC13EC12", "Computer Networks", 3, "Core", 
                                 "Dr. Sharma", 60, null));
        allCourses.add(new Course("25PCC13EC13", "Artificial Intelligence", 3, "Core", 
                                 "Dr. Patel", 60, null));
        allCourses.add(new Course("25PCC13EC14", "Analysis of Algorithms", 1, "Core", 
                                 "Dr. Singh", 60, null));
        allCourses.add(new Course("25PCC13EC15", "Data Warehousing and Mining", 2, "Core", 
                                 "Dr. Gupta", 60, null));
        
        // SEMESTER-VI CORE COURSES (PCC)
        allCourses.add(new Course("25PCC13EC16", "VLSI Design", 3, "Core", 
                                 "Dr. Mehta", 60, null));
        allCourses.add(new Course("25PCC13EC17", "Analog and Digital Communication", 3, "Core", 
                                 "Dr. Verma", 60, null));
        allCourses.add(new Course("25PCC13EC18", "Machine Learning", 1, "Core", 
                                 "Dr. Joshi", 60, null));
        allCourses.add(new Course("25PCC13EC19", "C++ for VLSI", 1, "Core", 
                                 "Dr. Agarwal", 60, null));
        allCourses.add(new Course("25PCC13EC20", "System Security", 1, "Core", 
                                 "Dr. Mishra", 60, null));
        
        // PROGRAM ELECTIVE COURSES (PEC)
        allCourses.add(new Course("25PEC13ECXX", "Program Elective Course", 3, "PEC", 
                                 "Dr. Reddy", 40, null));
        allCourses.add(new Course("25PEC13EC1X", "Program Elective Course", 3, "PEC", 
                                 "Dr. Nair", 40, null));
        allCourses.add(new Course("25PEC13ECXX-L", "Program Elective Lab", 1, "PEC", 
                                 "Dr. Iyer", 40, null));
        
        // HONOURS DEGREE COURSES - IoT Specialization
        allCourses.add(new Course("HIoTC501", "IoT Sensor Technologies", 4, "Honours", 
                                 "Dr. Krishnan", 30, null));
        allCourses.add(new Course("HIoTC601", "IoT System Design", 4, "Honours", 
                                 "Dr. Pillai", 30, new String[]{"HIoTC501"}));
        allCourses.add(new Course("HIoTC701", "Dynamic Paradigm in IoT", 4, "Honours", 
                                 "Dr. Menon", 30, new String[]{"HIoTC601"}));
        allCourses.add(new Course("HIoTSBL701", "Interfacing & Programming with IoT Lab", 4, "Honours", 
                                 "Dr. Nambiar", 20, new String[]{"HIoTC701"}));
        allCourses.add(new Course("HIoTC801", "Industrial IoT", 4, "Honours", 
                                 "Dr. Raghavan", 30, new String[]{"HIoTSBL701"}));
        
        // HONOURS DEGREE COURSES - AI/ML Specialization
        allCourses.add(new Course("HAIMLC501", "Mathematics for AI & ML", 4, "Honours", 
                                 "Dr. Subramanian", 30, null));
        allCourses.add(new Course("HAIMLC601", "Game Theory using AI & ML", 4, "Honours", 
                                 "Dr. Venkatesh", 30, new String[]{"HAIMLC501"}));
        allCourses.add(new Course("HAIMLC701", "AI & ML in Healthcare", 4, "Honours", 
                                 "Dr. Balasubramanian", 30, new String[]{"HAIMLC601"}));
        allCourses.add(new Course("HAIMLSBL701", "AI & ML in Healthcare: Lab", 4, "Honours", 
                                 "Dr. Srinivasan", 20, new String[]{"HAIMLC701"}));
        allCourses.add(new Course("HAIMLC801", "Text, Web and Social Media Analytics", 4, "Honours", 
                                 "Dr. Raman", 30, new String[]{"HAIMLSBL701"}));
        
        // HONOURS DEGREE COURSES - Data Science Specialization
        allCourses.add(new Course("HDSCC501", "Mathematics for Data Science", 4, "Honours", 
                                 "Dr. Natarajan", 30, null));
        allCourses.add(new Course("HDSCC601", "Statistical Learning for Data Science", 4, "Honours", 
                                 "Dr. Sundaram", 30, new String[]{"HDSCC501"}));
        allCourses.add(new Course("HDSCC701", "Data Science for Health and Social Care", 4, "Honours", 
                                 "Dr. Parthasarathy", 30, new String[]{"HDSCC601"}));
        allCourses.add(new Course("HDSSBL701", "Data Science for Health and Social Care Lab", 4, "Honours", 
                                 "Dr. Gopalakrishnan", 20, new String[]{"HDSCC701"}));
        allCourses.add(new Course("HDSCC801", "Text, Web and Social Media Analytics", 4, "Honours", 
                                 "Dr. Jayaraman", 30, new String[]{"HDSSBL701"}));
        
        // HONOURS DEGREE COURSES - Blockchain Specialization
        allCourses.add(new Course("HBCCC501", "Bit coin and Crypto currency", 4, "Honours", 
                                 "Dr. Kannan", 30, null));
        allCourses.add(new Course("HBCCC601", "Blockchain Platform", 4, "Honours", 
                                 "Dr. Murugan", 30, new String[]{"HBCCC501"}));
        allCourses.add(new Course("HBCCC701", "Blockchain Development", 4, "Honours", 
                                 "Dr. Selvam", 30, new String[]{"HBCCC601"}));
        allCourses.add(new Course("HBCSBL701", "Private Blockchain Setup Lab", 4, "Honours", 
                                 "Dr. Rajesh", 20, new String[]{"HBCCC701"}));
        allCourses.add(new Course("HBCCC801", "DeFi (Decentralized Finance)", 4, "Honours", 
                                 "Dr. Lakshmi", 30, new String[]{"HBCSBL701"}));
        
        // HONOURS DEGREE COURSES - Cyber Security Specialization  
        allCourses.add(new Course("HCSCC501", "Ethical Hacking", 4, "Honours", 
                                 "Dr. Pradeep", 30, null));
        allCourses.add(new Course("HCSCC601", "Digital Forensic", 4, "Honours", 
                                 "Dr. Suresh", 30, new String[]{"HCSCC501"}));
        allCourses.add(new Course("HCSCC701", "Security Information Management", 4, "Honours", 
                                 "Dr. Ramesh", 30, new String[]{"HCSCC601"}));
        allCourses.add(new Course("HCSSBL601", "Vulnerability Assessment Penetration Testing Lab", 4, "Honours", 
                                 "Dr. Dinesh", 20, new String[]{"HCSCC701"}));
        allCourses.add(new Course("HCSCC801", "Application Security", 4, "Honours", 
                                 "Dr. Mahesh", 30, new String[]{"HCSSBL601"}));
        
        // PROGRAM ELECTIVE COURSES - Track A
        allCourses.add(new Course("PE-TA-BIL", "Automation, Biomedical Instrumentation Laboratory", 3, "PEC", 
                                 "Dr. Vijay", 25, null));
        allCourses.add(new Course("PE-TA-MC", "Mobile Communication", 3, "PEC", 
                                 "Dr. Ajay", 30, null));
        allCourses.add(new Course("PE-TA-DSP", "Digital Signal Processing", 3, "PEC", 
                                 "Dr. Sanjay", 30, null));
        allCourses.add(new Course("PE-TA-AVLSI", "Analog VLSI Design", 3, "PEC", 
                                 "Dr. Rajeev", 30, null));
        allCourses.add(new Course("PE-TA-IOT", "IoT Laboratory", 1, "PEC", 
                                 "Dr. Deepak", 20, null));
        allCourses.add(new Course("PE-TA-IPL", "Image Processing Laboratory", 1, "PEC", 
                                 "Dr. Anand", 20, null));
        
        // PROGRAM ELECTIVE COURSES - Track B
        allCourses.add(new Course("PE-TB-CRYPTO", "Cryptography", 3, "PEC", 
                                 "Dr. Mohan", 30, null));
        allCourses.add(new Course("PE-TB-AJP", "Advanced Java Programming", 3, "PEC", 
                                 "Dr. Rohan", 30, null));
        allCourses.add(new Course("PE-TB-NLP", "Natural Language Processing", 3, "PEC", 
                                 "Dr. Sohan", 30, null));
        allCourses.add(new Course("PE-TB-BDA", "Big Data Analytics", 3, "PEC", 
                                 "Dr. Gihan", 30, null));
        allCourses.add(new Course("PE-TB-AA", "Advanced Algorithms", 3, "PEC", 
                                 "Dr. Kiran", 30, null));
        allCourses.add(new Course("PE-TB-DLL", "Deep Learning Laboratory", 1, "PEC", 
                                 "Dr. Sharan", 20, null));
        allCourses.add(new Course("PE-TB-STQA", "Software Testing & Quality Assurance Laboratory", 1, "PEC", 
                                 "Dr. Charan", 20, null));
        
        // OPEN ELECTIVES
        allCourses.add(new Course("OE-DBMS", "Database Management System", 2, "Open Elective", 
                                 "Dr. Prasad", 50, null));
        allCourses.add(new Course("OE-SEWA", "Software Engineering for Web Applications", 2, "Open Elective", 
                                 "Dr. Arun", 50, null));
        allCourses.add(new Course("OE-OS", "Operating Systems", 2, "Open Elective", 
                                 "Dr. Varun", 50, null));
        allCourses.add(new Course("OE-CC", "Cloud Computing", 2, "Open Elective", 
                                 "Dr. Tarun", 50, null));
        
        // MULTI-DISCIPLINARY MINORS (MDM)
        allCourses.add(new Course("MDM-LFE", "Law for Engineers", 2, "MDM", 
                                 "Dr. Advocate Sharma", 40, null));
        allCourses.add(new Course("MDM-ETL", "Emerging Technology and Law", 2, "MDM", 
                                 "Dr. Legal Expert", 40, null));
        allCourses.add(new Course("MDM-HWP", "Health, Wellness and Psychology", 2, "MDM", 
                                 "Dr. Psychologist", 40, null));
        allCourses.add(new Course("MDM-ESI", "Emotional and Spiritual Intelligence", 2, "MDM", 
                                 "Dr. Counselor", 40, null));
        allCourses.add(new Course("MDM-PRCC", "Public Relations and Corporate Communication", 2, "MDM", 
                                 "Dr. Communication", 40, null));
        allCourses.add(new Course("MDM-POM", "Principles of Management", 2, "MDM", 
                                 "Dr. MBA Faculty", 40, null));
        allCourses.add(new Course("MDM-SWAYAM", "From SWAYAM", 2, "MDM", 
                                 "SWAYAM Platform", 100, null));
    }
    
    public List<Course> getAllCourses() {
        if (courseDAO != null) {
            return courseDAO.getAllCourses();
        } else {
            // Fallback to in-memory data
            if (allCourses == null) {
                allCourses = new ArrayList<>();
                initializeSampleCourses();
            }
            return new ArrayList<>(allCourses);
        }
    }
    
    public List<Course> getCoursesByType(String type) {
        if (courseDAO != null) {
            return courseDAO.getCoursesByType(type);
        } else {
            // Fallback to in-memory data
            if (allCourses == null) {
                allCourses = new ArrayList<>();
                initializeSampleCourses();
            }
            return allCourses.stream()
                    .filter(c -> c.getType().equals(type))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
    }
    
    public Course getCourseById(String courseId) {
        if (courseDAO != null) {
            return courseDAO.getCourseById(courseId);
        } else {
            // Fallback to in-memory data
            if (allCourses == null) {
                allCourses = new ArrayList<>();
                initializeSampleCourses();
            }
            return allCourses.stream()
                    .filter(c -> c.getCourseId().equals(courseId))
                    .findFirst()
                    .orElse(null);
        }
    }
    
    // Student management methods
    public boolean registerStudent(Student student) {
        if (studentDAO != null) {
            return studentDAO.addStudent(student);
        }
        return false;
    }
    
    public Student getStudentById(String studentId) {
        if (studentDAO != null) {
            return studentDAO.getStudentById(studentId);
        }
        return null;
    }
    
    public boolean enrollStudentInCourse(String studentId, String courseId) {
        if (studentDAO != null) {
            return studentDAO.enrollStudentInCourse(studentId, courseId);
        }
        return false;
    }
    
    public boolean dropStudentFromCourse(String studentId, String courseId) {
        if (studentDAO != null) {
            return studentDAO.dropStudentFromCourse(studentId, courseId);
        }
        return false;
    }
    
    public List<Student> getAllStudents() {
        if (studentDAO != null) {
            return studentDAO.getAllStudents();
        }
        return new ArrayList<>();
    }
    
    public List<Student> getStudentsInCourse(String courseId) {
        if (studentDAO != null) {
            return studentDAO.getStudentsEnrolledInCourse(courseId);
        }
        return new ArrayList<>();
    }
    
    // Course management methods for teachers
    public boolean updateCourse(Course course) {
        if (courseDAO != null) {
            return courseDAO.updateCourse(course);
        }
        return false;
    }
    
    public boolean addCourse(Course course) {
        if (courseDAO != null) {
            return courseDAO.addCourse(course);
        }
        return false;
    }
    
    public boolean deleteCourse(String courseId) {
        if (courseDAO != null) {
            return courseDAO.deleteCourse(courseId);
        }
        return false;
    }
    
    private List<Course> allCourses;
    
    public void saveData(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Course course : allCourses) {
                writer.println(course.getCourseId() + "," + course.getCourseName() + "," +
                              course.getCredits() + "," + course.getType() + "," +
                              course.getInstructor() + "," + course.getMaxStudents() + "," +
                              course.getEnrolledStudents());
            }
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }
    
    public void loadData(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            allCourses.clear();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    Course course = new Course(parts[0], parts[1], Integer.parseInt(parts[2]),
                                             parts[3], parts[4], Integer.parseInt(parts[5]), null);
                    allCourses.add(course);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
            initializeSampleCourses();
        }
    }
}