public class Course {
    private String courseId;
    private String courseName;
    private int credits;
    private String type;
    private String instructor;
    private int maxStudents;
    private int enrolledStudents;
    private String[] prerequisites;
    
    public Course(String courseId, String courseName, int credits, String type, 
                  String instructor, int maxStudents, String[] prerequisites) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.type = type;
        this.instructor = instructor;
        this.maxStudents = maxStudents;
        this.enrolledStudents = 0;
        this.prerequisites = prerequisites != null ? prerequisites : new String[0];
    }
    
    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public int getCredits() { return credits; }
    public String getType() { return type; }
    public String getInstructor() { return instructor; }
    public int getMaxStudents() { return maxStudents; }
    public int getEnrolledStudents() { return enrolledStudents; }
    public String[] getPrerequisites() { return prerequisites; }
    
    public boolean isAvailable() {
        return enrolledStudents < maxStudents;
    }
    
    public boolean enroll() {
        if (isAvailable()) {
            enrolledStudents++;
            return true;
        }
        return false;
    }
    
    public void unenroll() {
        if (enrolledStudents > 0) {
            enrolledStudents--;
        }
    }
    
    @Override
    public String toString() {
        return courseId + " - " + courseName + " (" + credits + " credits)";
    }
    
    public String getDetailedInfo() {
        return String.format("%s - %s\nInstructor: %s\nCredits: %d\nType: %s\nCapacity: %d/%d",
                courseId, courseName, instructor, credits, type, enrolledStudents, maxStudents);
    }
}