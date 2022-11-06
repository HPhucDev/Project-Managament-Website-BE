package com.hcmute.management.model.payload.request.Student;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@Setter
@Getter
public class AddNewStudentRequest {
    @NotEmpty(message = "can not be empty")
    private String mssv;
    @NotEmpty(message = "can not be empty")
    private String fullname;
    @NotNull(message = "can not be empty")
    private Date birthday;
    @NotEmpty(message = "can not be empty")
    private String sex;
    @NotEmpty(message = "can not be empty")
    private String address;
    @NotEmpty(message = "can not be empty")
    @DateTimeFormat(pattern = "yyyy")
    private Date schoolyear;
    @NotEmpty(message = "can not be empty")
    private String major;
    @NotEmpty(message =  "can not be empty")
    private String educationprogram;
    @NotNull(message = "Mã lớp học can not be empty")
    private int  classid;
    @NotNull(message = "Phone number can not be empty")
    private String phone;
}
