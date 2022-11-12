package com.hcmute.management.model.payload.request.Student;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Data
@NoArgsConstructor
public class AddNewStudentRequest {
    @NotEmpty(message = " student id can not be empty")
    private String mssv;
    @NotEmpty(message = "fullname can not be empty")
    private String fullname;
    @NotNull(message = "birthday can not be empty")
    private Date birthday;
    @NotEmpty(message = "gender can not be empty")
    private String sex;
    @NotEmpty(message = "address can not be empty")
    private String address;
    @NotEmpty(message = "school year can not be empty")
    @DateTimeFormat(pattern = "yyyy")
    private Date schoolyear;
    @NotEmpty(message = "major can not be empty")
    private String major;
    @NotEmpty(message =  "program can not be empty")
    private String educationprogram;
    @NotNull(message = "Mã lớp học can not be empty")
    private String  classid;
    @NotNull(message = "Phone number can not be empty")
    private String phone;
}
