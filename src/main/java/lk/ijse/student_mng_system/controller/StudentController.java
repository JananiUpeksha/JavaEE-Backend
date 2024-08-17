package lk.ijse.student_mng_system.controller;

import jakarta.json.JsonException;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.student_mng_system.dao.impl.StudentDataProcess;
import lk.ijse.student_mng_system.dto.StudentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet(urlPatterns = "/studentData",loadOnStartup = 2)
public class StudentController extends HttpServlet {
    static Logger logger = LoggerFactory.getLogger(StudentController.class);
    private Connection connection;

    /*public String SAVE_STUDENT = "INSERT INTO Student (id, name, city, email, level) VALUES (?, ?, ?, ?, ?)";
    static String GET_STUDENT = "SELECT id, name, city, email, level FROM Student WHERE id = ?";
    static String UPDATE_STUDENT = "UPDATE Student set name=?,city=?,email=?,level=? WHERE id=?";
    static String DELETE_STUDENT = "DELETE FROM Student WHERE id=?";*/

    @Override
    public void init() throws ServletException {
        logger.info("Initializing StudentController with call init method");
        try {
            /*var driverClass = getServletContext().getInitParameter("driver-class");
            var dbUrl = getServletContext().getInitParameter("dbURL");
            var dbUsername = getServletContext().getInitParameter("dbUserName");
            var dbPassword = getServletContext().getInitParameter("dbPassword");*/
            var ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/studentRej");

            /*Class.forName(driverClass);*/
            this.connection = /*DriverManager.getConnection(dbUrl, dbUsername, dbPassword)*/pool.getConnection();;

            System.out.println("Database connection established successfully");
        } catch (NamingException e) {
            e.printStackTrace();
            throw new ServletException("Database driver class not found", e);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Failed to establish database connection", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*var studentDTO = new StudentDTO();
        String studentId = req.getParameter("id");
        if (studentId == null || studentId.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing student ID");
            return;
        }
        studentDTO.setId(studentId);

        try (var writer = resp.getWriter()) {
            var ps = connection.prepareStatement(GET_STUDENT);
            ps.setString(1, studentDTO.getId());
            var resultSet = ps.executeQuery();
            while (resultSet.next()) {
                studentDTO.setId(resultSet.getString("id"));
                studentDTO.setName(resultSet.getString("name"));
                studentDTO.setCity(resultSet.getString("city"));
                studentDTO.setEmail(resultSet.getString("email"));
                studentDTO.setLevel(resultSet.getString("level"));
            }
            System.out.println("Student data retrieved: " + studentDTO);

            // Setting response content type to application/json
            resp.setContentType("application/json");

            // Serializing the studentDTO object to JSON and writing it to the response writer
            var jsonb = JsonbBuilder.create();
            jsonb.toJson(studentDTO, writer);

            // Console output to indicate JSON response is being sent
            System.out.println(jsonb.toJson(studentDTO));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
        var studentID = req.getParameter("id");
        var dataprocess = new StudentDataProcess();
        try (var writer = resp.getWriter())/*private data source ekak arn thiyenne*/{
            var student = dataprocess.getStudent(studentID,connection);
            System.out.println(student);
            resp.setContentType("application/json");
            var jsonb = JsonbBuilder.create();
            jsonb.toJson(student,writer);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getContentType().toLowerCase().contains("application/json")) {
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }

        /*String id = UUID.randomUUID().toString();
        Jsonb jsonb = JsonbBuilder.create();
        StudentDTO studentDTO = jsonb.fromJson(req.getReader(), StudentDTO.class);
        studentDTO.setId(id);
        System.out.println(studentDTO);

        try (var ps = connection.prepareStatement(SAVE_STUDENT)) {
            ps.setString(1, studentDTO.getId());
            ps.setString(2, studentDTO.getName());
            ps.setString(3, studentDTO.getCity());
            ps.setString(4, studentDTO.getEmail());
            ps.setString(5, studentDTO.getLevel());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save student data");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }*/
        String id = UUID.randomUUID().toString();
        try(var writer = resp.getWriter()) {
            Jsonb jsonb = JsonbBuilder.create();//json type eke object ekak crate kla
            StudentDTO studentDTO = jsonb.fromJson(req.getReader(),StudentDTO.class);//apita bind karanna one object type ekata meka set kranna one
            //1st para eken front end eken ena data read krnwa 2nd eken bind krnna one mona structure ekatada mona class ekatad kiyla denwa
            //e step deka iwra weddi frontend eken awa data bind una object ekak create wela thiyenne
            studentDTO.setId(id);
            var saveData = new StudentDataProcess();
            //writer.write(saveData.saveStudent(studentDTO,connection));
            if (saveData.saveStudent(studentDTO, connection)){
                writer.write("Student saved successfully");
                resp.setStatus(HttpServletResponse.SC_CREATED);
            }else {
                writer.write("Save student failed");
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }catch (JsonException e){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var stuID = req.getParameter("id");

        /*if (stuID == null || stuID.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Student ID is required.");
            return;
        }

        try {
            var ps = this.connection.prepareStatement(DELETE_STUDENT);
            ps.setString(1, stuID);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 0) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                resp.getWriter().write("Student not found.");
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("An error occurred while deleting the student.");
            throw new RuntimeException(e);
        }*/
        try (var writer = resp.getWriter()){
            var studentDataProcess = new StudentDataProcess();
            if(studentDataProcess.deleteStudent(stuID, connection)){
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("Delete Failed");
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getContentType().toLowerCase().contains("application/json")) {
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }
        /*try {
            var ps = this.connection.prepareStatement(UPDATE_STUDENT);
            var studentID = req.getParameter("id");
            Jsonb jsonb= JsonbBuilder.create();
            var updateStudent = jsonb.fromJson(req.getReader(),StudentDTO.class);
                    ps.setString(1,updateStudent.getName());
                    ps.setString(2,updateStudent.getCity());
                    ps.setString(3,updateStudent.getEmail());
                    ps.setString(4,updateStudent.getLevel());
                    ps.setString(5,studentID);
                    if (ps.executeUpdate()!=0){

                        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    }else {
                        resp.getWriter().write("Update Failed");
                    }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }*/
        String studentID = req.getParameter("id");
        if (studentID == null || studentID.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing student ID");
            return;
        }

        try (var writer = resp.getWriter()) {
            Jsonb jsonb = JsonbBuilder.create();
            StudentDTO updatedStudent = jsonb.fromJson(req.getReader(), StudentDTO.class);
            updatedStudent.setId(studentID);
            var dataProcess = new StudentDataProcess();
            if (dataProcess.updateStudent(studentID, updatedStudent, connection)) {
                writer.write("Student updated successfully");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                writer.write("Update failed");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (JsonException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }
}
