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
public class ChangeInfoStudentRequest {
    @NotEmpty(message = "Họ và tên không được để trống")
    private String fullname;
    @NotEmpty(message = "Giới tính không được để trống")
    private String sex;
    @NotEmpty(message = "Địa chỉ không được để trống")
    private String address;
    @NotNull(message = "Năm học không được để trống")
    @DateTimeFormat(pattern = "yyyy")
    private Date schoolyear;
    @NotEmpty(message = "Chuyên ngành không được để trống")
    private String major;
    @NotEmpty(message =  "Chương trình đào tạo không được để trống")
    private String educationprogram;
    @NotEmpty(message = "Mã lớp học không được để trống")
    private String classid;
}
