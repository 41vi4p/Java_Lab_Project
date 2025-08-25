public class Teacher {
    private int teacherId;
    private String username;
    private String password;
    private String name;
    private String email;
    private String department;
    private String designation;
    
    public Teacher() {}
    
    public Teacher(int teacherId, String username, String name, String email, 
                   String department, String designation) {
        this.teacherId = teacherId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.department = department;
        this.designation = designation;
    }
    
    public Teacher(String username, String password, String name, String email, 
                   String department, String designation) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.department = department;
        this.designation = designation;
    }
    
    // Getters and Setters
    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    
    @Override
    public String toString() {
        return name + " (" + designation + ", " + department + ")";
    }
}