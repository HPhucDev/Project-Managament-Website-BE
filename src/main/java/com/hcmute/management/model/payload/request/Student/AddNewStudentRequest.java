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
    private String mssv;
    @NotEmpty(message = "fullname can not be empty")
    private String fullname;
    @NotEmpty(message = "gender can not be empty")
    private String sex;
    @NotEmpty(message = "address can not be empty")
    private String address;
    @NotNull(message = "school year can not be empty")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date schoolyear;
    @NotEmpty(message = "major can not be empty")
    private String major;
    @NotEmpty(message =  "program can not be empty")
    private String educationprogram;
    @NotNull(message = "Mã lớp học can not be empty")
    private String  classid;
    @NotNull(message = "Email can not be empty")
    private String email;
    @NotNull(message = "Ngày tháng năm không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime birthday;
}
