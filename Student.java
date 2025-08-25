import java.util.*;

public class Student {
    private String studentId;
    private String name;
    private String email;
    private int semester;
    private int maxCredits;
    private List<Course> registeredCourses;
    
    public Student(String studentId, String name, String email, int semester, int maxCredits) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.semester = semester;
        this.maxCredits = maxCredits;
        this.registeredCourses = new ArrayList<>();
    }
    
    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public int getSemester() { return semester; }
    public int getMaxCredits() { return maxCredits; }
    public List<Course> getRegisteredCourses() { return registeredCourses; }
    
    public int getCurrentCredits() {
        return registeredCourses.stream().mapToInt(Course::getCredits).sum();
    }
    
    public int getRemainingCredits() {
        return maxCredits - getCurrentCredits();
    }
    
    public boolean canRegister(Course course) {
        if (getCurrentCredits() + course.getCredits() > maxCredits) {
            return false;
        }
        
        if (registeredCourses.contains(course)) {
            return false;
        }
        
        return course.isAvailable();
    }
    
    public boolean registerCourse(Course course) {
        if (canRegister(course)) {
            if (course.enroll()) {
                registeredCourses.add(course);
                return true;
            }
        }
        return false;
    }
    
    public boolean dropCourse(Course course) {
        if (registeredCourses.remove(course)) {
            course.unenroll();
            return true;
        }
        return false;
    }
    
    public int getCoreCredits() {
        return registeredCourses.stream()
                .filter(c -> "Core".equals(c.getType()))
                .mapToInt(Course::getCredits)
                .sum();
    }
    
    public int getElectiveCredits() {
        return registeredCourses.stream()
                .filter(c -> "PEC".equals(c.getType()) || "Honours".equals(c.getType()) || 
                           "Open Elective".equals(c.getType()) || "MDM".equals(c.getType()))
                .mapToInt(Course::getCredits)
                .sum();
    }
    
    public int getPECCredits() {
        return registeredCourses.stream()
                .filter(c -> "PEC".equals(c.getType()))
                .mapToInt(Course::getCredits)
                .sum();
    }
    
    public int getHonoursCredits() {
        return registeredCourses.stream()
                .filter(c -> "Honours".equals(c.getType()))
                .mapToInt(Course::getCredits)
                .sum();
    }
    
    public int getOpenElectiveCredits() {
        return registeredCourses.stream()
                .filter(c -> "Open Elective".equals(c.getType()))
                .mapToInt(Course::getCredits)
                .sum();
    }
    
    public int getMDMCredits() {
        return registeredCourses.stream()
                .filter(c -> "MDM".equals(c.getType()))
                .mapToInt(Course::getCredits)
                .sum();
    }
    
    @Override
    public String toString() {
        return studentId + " - " + name;
    }
}