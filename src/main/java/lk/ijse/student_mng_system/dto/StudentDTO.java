package lk.ijse.student_mng_system.dto;

import lombok.*;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class StudentDTO implements Serializable {
    private String id;
    private String name;
    private String city;
    private String email;
    private String level;
}
