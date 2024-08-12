package lk.ijse.student_mng_system.dao.impl;

import jakarta.json.bind.JsonbBuilder;
import lk.ijse.student_mng_system.dao.StudentData;
import lk.ijse.student_mng_system.dto.StudentDTO;

import java.sql.Connection;
import java.sql.SQLException;

public  /*non-sealed*/ class StudentDataProcess implements StudentData { //final krnnath pluwan
    public String SAVE_STUDENT = "INSERT INTO Student (id, name, city, email, level) VALUES (?, ?, ?, ?, ?)";
    static String GET_STUDENT = "SELECT id, name, city, email, level FROM Student WHERE id = ?";
    static String UPDATE_STUDENT = "UPDATE Student set name=?,city=?,email=?,level=? WHERE id=?";
    static String DELETE_STUDENT = "DELETE FROM Student WHERE id=?";

    @Override
    public StudentDTO getStudent(String studentID, Connection connection) {
        var studentDTO = new StudentDTO();
        try {
            var ps = connection.prepareStatement(GET_STUDENT);
            ps.setString(1, studentID);
            var resultSet = ps.executeQuery();
            while (resultSet.next()) {
                studentDTO.setId(resultSet.getString("id"));
                studentDTO.setName(resultSet.getString("name"));
                studentDTO.setCity(resultSet.getString("city"));
                studentDTO.setEmail(resultSet.getString("email"));
                studentDTO.setLevel(resultSet.getString("level"));
            }
        } catch (SQLException e) {
        }
        return studentDTO;
    }



    @Override
    public boolean saveStudent(StudentDTO studentDTO,Connection connection) {
        try {
            var ps = connection.prepareStatement(SAVE_STUDENT);
            ps.setString(1, studentDTO.getId());
            ps.setString(2, studentDTO.getName());
            ps.setString(3, studentDTO.getCity());
            ps.setString(4, studentDTO.getEmail());
            ps.setString(5, studentDTO.getLevel());
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public boolean deleteStudent(String studentID,Connection connection) {
        try {
            var ps = connection.prepareStatement(DELETE_STUDENT);
            ps.setString(1, studentID);
            return ps.executeUpdate() != 0;
        }catch (SQLException e){
            throw new RuntimeException();
        }

    }

    @Override
    public boolean updateStudent(String studentID, StudentDTO studentDTO,Connection connection) {
        try {
            var ps = connection.prepareStatement(UPDATE_STUDENT);
            ps.setString(1, studentDTO.getName());
            ps.setString(2, studentDTO.getCity());
            ps.setString(3, studentDTO.getEmail());
            ps.setString(4, studentDTO.getLevel());
            ps.setString(5, studentID);
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
