package com.hcmute.management.model.payload.request.Student;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Data
@NoArgsConstructor
public class AddNewStudentRequest {
    @NotEmpty(message = " student id can not be empty")
    private String studentId;
    @NotEmpty(message = "full name can not be empty")
    private String fullName;
    @NotEmpty(message = "gender can not be empty")
    private String gender;
    @NotEmpty(message = "address can not be empty")
    private String address;
    @NotNull(message = "school year can not be empty")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime schoolYear;
    private String major;
    @NotEmpty(message =  "program can not be empty")
    private String educationProgram;
    @NotNull(message = "class name can not be empty")
    private String  className;
    @NotNull(message = "Email can not be empty")
    private String email;
    @NotNull(message = "school year can not be empty")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime birthDay;
}
