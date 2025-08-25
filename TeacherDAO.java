import java.sql.*;
import java.util.*;

public class TeacherDAO {
    private DatabaseConfig dbConfig;
    
    public TeacherDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }
    
    public Teacher authenticateTeacher(String username, String password) {
        String sql = "SELECT * FROM teachers WHERE teacher_username = ? AND teacher_password = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractTeacherFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating teacher: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT * FROM teachers ORDER BY teacher_name";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Teacher teacher = extractTeacherFromResultSet(rs);
                teachers.add(teacher);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching teachers: " + e.getMessage());
            e.printStackTrace();
        }
        
        return teachers;
    }
    
    public Teacher getTeacherById(int teacherId) {
        String sql = "SELECT * FROM teachers WHERE teacher_id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, teacherId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractTeacherFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching teacher by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean addTeacher(Teacher teacher) {
        String sql = "INSERT INTO teachers (teacher_username, teacher_password, teacher_name, " +
                    "teacher_email, department, designation) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, teacher.getUsername());
            stmt.setString(2, teacher.getPassword());
            stmt.setString(3, teacher.getName());
            stmt.setString(4, teacher.getEmail());
            stmt.setString(5, teacher.getDepartment());
            stmt.setString(6, teacher.getDesignation());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding teacher: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateTeacher(Teacher teacher) {
        String sql = "UPDATE teachers SET teacher_name = ?, teacher_email = ?, " +
                    "department = ?, designation = ? WHERE teacher_id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, teacher.getName());
            stmt.setString(2, teacher.getEmail());
            stmt.setString(3, teacher.getDepartment());
            stmt.setString(4, teacher.getDesignation());
            stmt.setInt(5, teacher.getTeacherId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating teacher: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private Teacher extractTeacherFromResultSet(ResultSet rs) throws SQLException {
        int teacherId = rs.getInt("teacher_id");
        String username = rs.getString("teacher_username");
        String name = rs.getString("teacher_name");
        String email = rs.getString("teacher_email");
        String department = rs.getString("department");
        String designation = rs.getString("designation");
        
        return new Teacher(teacherId, username, name, email, department, designation);
    }
}