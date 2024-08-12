package lk.ijse.student_mng_system.dao;

import lk.ijse.student_mng_system.dto.StudentDTO;

import java.sql.Connection;

public /*sealed*/ interface StudentData /*permits StudentDataProcess*/ {
    StudentDTO getStudent(String studentID, Connection connection);
    boolean saveStudent(StudentDTO studentDTO,Connection connection);
    boolean deleteStudent(String studentID,Connection connection);
    boolean updateStudent(String studentID, StudentDTO studentDTO,Connection connection);
}
