-- ECS Course Registration System Database Schema

-- Create extension for UUID generation
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Teachers table
CREATE TABLE teachers (
    teacher_id SERIAL PRIMARY KEY,
    teacher_username VARCHAR(50) UNIQUE NOT NULL,
    teacher_password VARCHAR(255) NOT NULL,
    teacher_name VARCHAR(100) NOT NULL,
    teacher_email VARCHAR(100) UNIQUE NOT NULL,
    department VARCHAR(100),
    designation VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Courses table
CREATE TABLE courses (
    course_id VARCHAR(20) PRIMARY KEY,
    course_name VARCHAR(200) NOT NULL,
    credits INTEGER NOT NULL,
    course_type VARCHAR(50) NOT NULL,
    instructor VARCHAR(100),
    max_students INTEGER NOT NULL DEFAULT 60,
    enrolled_students INTEGER DEFAULT 0,
    semester INTEGER,
    prerequisites TEXT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Students table
CREATE TABLE students (
    student_id VARCHAR(20) PRIMARY KEY,
    student_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    semester INTEGER NOT NULL,
    max_credits INTEGER NOT NULL DEFAULT 20,
    password VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Course enrollments table
CREATE TABLE enrollments (
    enrollment_id SERIAL PRIMARY KEY,
    student_id VARCHAR(20) REFERENCES students(student_id) ON DELETE CASCADE,
    course_id VARCHAR(20) REFERENCES courses(course_id) ON DELETE CASCADE,
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'ENROLLED',
    UNIQUE(student_id, course_id)
);

-- Insert default teacher accounts
INSERT INTO teachers (teacher_username, teacher_password, teacher_name, teacher_email, department, designation) VALUES
('admin', 'admin123', 'System Administrator', 'admin@ecs.edu', 'Computer Science', 'Administrator'),
('dr.kumar', 'teacher123', 'Dr. Rajesh Kumar', 'rajesh.kumar@ecs.edu', 'Electronics', 'Professor'),
('dr.sharma', 'teacher123', 'Dr. Priya Sharma', 'priya.sharma@ecs.edu', 'Computer Science', 'Associate Professor'),
('dr.patel', 'teacher123', 'Dr. Amit Patel', 'amit.patel@ecs.edu', 'Computer Science', 'Assistant Professor');

-- Insert current courses data from running database
INSERT INTO courses (course_id, course_name, credits, course_type, instructor, max_students, semester, prerequisites, description) VALUES
('25PCC13EC11', 'Control Systems', 3, 'Core', 'Prof. Jayen Modi', 60, 0, NULL, NULL),
('25PCC13EC12', 'Computer Networks', 3, 'Core', 'Prof. Binsy Joseph', 60, 0, NULL, NULL),
('25PCC13EC13', 'Artificial Intelligence', 3, 'Core', 'Prof. Archana Lopes', 60, 0, NULL, NULL),
('25PCC13EC14', 'Analysis of Algorithms', 1, 'Core', 'Dr. Dipali K.', 60, 0, NULL, NULL),
('25PCC13EC15', 'Data Warehousing and Mining', 2, 'Core', 'Prof. Vaibhav Godbole', 60, 0, NULL, NULL),
('25PCC13EC16', 'VLSI Design', 3, 'Core', 'Dr. Deepak Bhoir', 60, 0, NULL, NULL),
('25PCC13EC17', 'Analog and Digital Communication', 3, 'Core', 'Dr. Sapna Prabhu', 60, 0, NULL, NULL),
('25PCC13EC18', 'Machine Learning', 1, 'Core', 'Dr. Dipali K.', 60, 0, '25PCC13EC13', NULL),
('25PCC13EC20', 'System Security', 1, 'Core', 'Prof. Unik Lokhande', 60, 0, NULL, NULL),
('HAIMLC701', 'AI & ML in Healthcare', 4, 'Honours', 'Dr. Swapnali M.', 30, 0, 'HAIMLC601', NULL),
('HCSCC501', 'Ethical Hacking', 4, 'Honours', 'Prof. Unik Lokhande', 30, 0, NULL, NULL),
('HCSCC601', 'Digital Forensic', 4, 'Honours', 'Prof. Unik Lokhande', 30, 0, 'HCSCC501', NULL),
('HCSCC701', 'Security Information Management', 4, 'Honours', 'Prof. Unik Lokhande', 30, 0, 'HCSCC601', NULL),
('HCSSBL601', 'Vulnerability Assessment Penetration Testing Lab', 4, 'Honours', 'Prof. Unik Lokhande', 20, 0, 'HCSCC701', NULL),
('HIoTC601', 'IoT System Design', 4, 'Honours', 'Prof. Flynn Jiu', 30, 0, 'HIoTC501', NULL),
('MDM-ESI', 'Emotional and Spiritual Intelligence', 2, 'MDM', 'Prof. Deepa Chitre', 40, 0, NULL, NULL),
('MDM-ETL', 'Emerging Technology and Law', 2, 'MDM', 'Dr. Surendra Singh Rathod', 40, 0, NULL, NULL),
('MDM-HWP', 'Health, Wellness and Psychology', 2, 'MDM', 'Prof. Kajal', 40, 0, NULL, NULL),
('MDM-LFE', 'Law for Engineers', 2, 'MDM', 'Dr. Surendra Singh Rathod', 40, 0, NULL, NULL),
('MDM-SWAYAM', 'From SWAYAM', 2, 'MDM', 'SWAYAM Platform', 100, NULL, NULL, 'Online courses from SWAYAM platform'),
('OE-CC', 'Cloud Computing', 2, 'Open Elective', 'Prof. Jayen Modi', 50, 0, NULL, NULL),
('OE-DBMS', 'Database Management System', 2, 'Open Elective', 'Dr. Dipali K.', 50, 0, NULL, NULL),
('OE-OS', 'Operating Systems', 2, 'Open Elective', 'Prof. Vaibhav Godbole', 50, 0, NULL, NULL),
('OE-SEWA', 'Software Engineering for Web Applications', 2, 'Open Elective', 'Prof. Deepa Chitre', 50, 0, NULL, NULL),
('PE-TA-BIL', 'Automation, Biomedical Instrumentation Laboratory', 3, 'PEC', 'Dr. Sapna Prabhu', 25, 0, NULL, NULL),
('PE-TA-DSP', 'Digital Signal Processing', 3, 'PEC', 'Dr. Sapna Prabhu', 30, 0, NULL, NULL),
('PE-TA-MC', 'Mobile Communication', 3, 'PEC', 'Prof. Jayen Modi', 30, 0, NULL, NULL),
('PE-TB-AJP', 'Advanced Java Programming', 3, 'PEC', 'Prof. Archana Lopes', 30, 0, NULL, NULL),
('PE-TB-BDA', 'Big Data Analytics', 3, 'PEC', 'Dr. Dipali K.', 30, 0, NULL, NULL),
('PE-TB-CRYPTO', 'Cryptography', 3, 'PEC', 'Prof. Prajakta B.', 30, 0, NULL, NULL),
('PE-TB-DLL', 'Deep Learning Laboratory', 1, 'PEC', 'Dr. Dipali K.', 20, 0, NULL, NULL),
('PE-TB-NLP', 'Natural Language Processing', 3, 'PEC', 'Dr. Dipali K.', 30, 0, NULL, NULL);

-- Create indexes for better performance
CREATE INDEX idx_enrollments_student_id ON enrollments(student_id);
CREATE INDEX idx_enrollments_course_id ON enrollments(course_id);
CREATE INDEX idx_courses_type ON courses(course_type);
CREATE INDEX idx_courses_semester ON courses(semester);
CREATE INDEX idx_students_semester ON students(semester);

-- Create a function to update enrolled_students count
CREATE OR REPLACE FUNCTION update_enrolled_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE courses SET enrolled_students = enrolled_students + 1 WHERE course_id = NEW.course_id;
        RETURN NEW;
    ELSIF TG_OP = 'DELETE' THEN
        UPDATE courses SET enrolled_students = enrolled_students - 1 WHERE course_id = OLD.course_id;
        RETURN OLD;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Create triggers to automatically update enrolled_students count
CREATE TRIGGER trigger_update_enrolled_count
    AFTER INSERT OR DELETE ON enrollments
    FOR EACH ROW
    EXECUTE FUNCTION update_enrolled_count();

-- Create a function to update the updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create trigger to update courses.updated_at
CREATE TRIGGER trigger_courses_updated_at
    BEFORE UPDATE ON courses
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Insert current students from running database
INSERT INTO students (student_id, student_name, email, semester, max_credits) VALUES
('10150', 'David', 'test@gmail.com', 5, 18),
('2021ECS001', 'Arjun Kumar', 'arjun.kumar@ecs.edu', 5, 20),
('2021ECS002', 'Priya Sharma', 'priya.sharma@ecs.edu', 6, 22),
('2021ECS003', 'Rahul Patel', 'rahul.patel@ecs.edu', 5, 20),
('2021ECS004', 'Sneha Gupta', 'sneha.gupta@ecs.edu', 6, 22),
('2021ECS005', 'Kiran Singh', 'kiran.singh@ecs.edu', 5, 18);

-- Insert current enrollments from running database
INSERT INTO enrollments (student_id, course_id) VALUES
('2021ECS001', '25PCC13EC11'),
('2021ECS001', '25PCC13EC12'),
('2021ECS002', '25PCC13EC16'),
('2021ECS002', '25PCC13EC17'),
('2021ECS003', '25PCC13EC13'),
('2021ECS003', 'PE-TB-CRYPTO'),
('2021ECS004', '25PCC13EC18'),
('2021ECS005', 'OE-DBMS'),
('10150', '25PCC13EC15');