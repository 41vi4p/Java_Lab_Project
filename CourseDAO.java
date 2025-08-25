import java.sql.*;
import java.util.*;

public class CourseDAO {
    private DatabaseConfig dbConfig;
    
    public CourseDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }
    
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY course_id";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Course course = extractCourseFromResultSet(rs);
                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching courses: " + e.getMessage());
            e.printStackTrace();
        }
        
        return courses;
    }
    
    public List<Course> getCoursesByType(String type) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses WHERE course_type = ? ORDER BY course_id";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Course course = extractCourseFromResultSet(rs);
                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching courses by type: " + e.getMessage());
            e.printStackTrace();
        }
        
        return courses;
    }
    
    public Course getCourseById(String courseId) {
        String sql = "SELECT * FROM courses WHERE course_id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, courseId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractCourseFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching course by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean updateCourse(Course course) {
        String sql = "UPDATE courses SET course_name = ?, credits = ?, course_type = ?, " +
                    "instructor = ?, max_students = ?, semester = ?, prerequisites = ?, " +
                    "description = ? WHERE course_id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, course.getCourseName());
            stmt.setInt(2, course.getCredits());
            stmt.setString(3, course.getType());
            stmt.setString(4, course.getInstructor());
            stmt.setInt(5, course.getMaxStudents());
            stmt.setInt(6, 0); // semester - can be added to Course class
            stmt.setString(7, String.join(",", course.getPrerequisites()));
            stmt.setString(8, ""); // description - can be added to Course class
            stmt.setString(9, course.getCourseId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating course: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean addCourse(Course course) {
        String sql = "INSERT INTO courses (course_id, course_name, credits, course_type, " +
                    "instructor, max_students, semester, prerequisites, description) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, course.getCourseId());
            stmt.setString(2, course.getCourseName());
            stmt.setInt(3, course.getCredits());
            stmt.setString(4, course.getType());
            stmt.setString(5, course.getInstructor());
            stmt.setInt(6, course.getMaxStudents());
            stmt.setInt(7, 0); // semester
            stmt.setString(8, String.join(",", course.getPrerequisites()));
            stmt.setString(9, ""); // description
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding course: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteCourse(String courseId) {
        String sql = "DELETE FROM courses WHERE course_id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, courseId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting course: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private Course extractCourseFromResultSet(ResultSet rs) throws SQLException {
        String courseId = rs.getString("course_id");
        String courseName = rs.getString("course_name");
        int credits = rs.getInt("credits");
        String courseType = rs.getString("course_type");
        String instructor = rs.getString("instructor");
        int maxStudents = rs.getInt("max_students");
        int enrolledStudents = rs.getInt("enrolled_students");
        String prerequisitesStr = rs.getString("prerequisites");
        
        String[] prerequisites = null;
        if (prerequisitesStr != null && !prerequisitesStr.trim().isEmpty()) {
            prerequisites = prerequisitesStr.split(",");
            for (int i = 0; i < prerequisites.length; i++) {
                prerequisites[i] = prerequisites[i].trim();
            }
        }
        
        Course course = new Course(courseId, courseName, credits, courseType, 
                                 instructor, maxStudents, prerequisites);
        
        // Set enrolled students count
        for (int i = 0; i < enrolledStudents; i++) {
            course.enroll();
        }
        
        return course;
    }
}