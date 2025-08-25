import java.sql.*;
import java.util.*;

public class StudentDAO {
    private DatabaseConfig dbConfig;
    
    public StudentDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }
    
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY student_id";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Student student = extractStudentFromResultSet(rs);
                loadStudentEnrollments(student);
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching students: " + e.getMessage());
            e.printStackTrace();
        }
        
        return students;
    }
    
    public Student getStudentById(String studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Student student = extractStudentFromResultSet(rs);
                loadStudentEnrollments(student);
                return student;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching student by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (student_id, student_name, email, semester, max_credits) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getStudentId());
            stmt.setString(2, student.getName());
            stmt.setString(3, student.getEmail());
            stmt.setInt(4, student.getSemester());
            stmt.setInt(5, student.getMaxCredits());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean enrollStudentInCourse(String studentId, String courseId) {
        String sql = "INSERT INTO enrollments (student_id, course_id) VALUES (?, ?)";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, studentId);
            stmt.setString(2, courseId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error enrolling student in course: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean dropStudentFromCourse(String studentId, String courseId) {
        String sql = "DELETE FROM enrollments WHERE student_id = ? AND course_id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, studentId);
            stmt.setString(2, courseId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error dropping student from course: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Student> getStudentsEnrolledInCourse(String courseId) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.* FROM students s " +
                    "INNER JOIN enrollments e ON s.student_id = e.student_id " +
                    "WHERE e.course_id = ? ORDER BY s.student_name";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, courseId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Student student = extractStudentFromResultSet(rs);
                loadStudentEnrollments(student);
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching students enrolled in course: " + e.getMessage());
            e.printStackTrace();
        }
        
        return students;
    }
    
    private Student extractStudentFromResultSet(ResultSet rs) throws SQLException {
        String studentId = rs.getString("student_id");
        String name = rs.getString("student_name");
        String email = rs.getString("email");
        int semester = rs.getInt("semester");
        int maxCredits = rs.getInt("max_credits");
        
        return new Student(studentId, name, email, semester, maxCredits);
    }
    
    private void loadStudentEnrollments(Student student) {
        String sql = "SELECT c.* FROM courses c " +
                    "INNER JOIN enrollments e ON c.course_id = e.course_id " +
                    "WHERE e.student_id = ? ORDER BY c.course_id";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getStudentId());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String courseId = rs.getString("course_id");
                String courseName = rs.getString("course_name");
                int credits = rs.getInt("credits");
                String courseType = rs.getString("course_type");
                String instructor = rs.getString("instructor");
                int maxStudents = rs.getInt("max_students");
                
                Course course = new Course(courseId, courseName, credits, courseType, 
                                         instructor, maxStudents, null);
                student.getRegisteredCourses().add(course);
            }
        } catch (SQLException e) {
            System.err.println("Error loading student enrollments: " + e.getMessage());
            e.printStackTrace();
        }
    }
}