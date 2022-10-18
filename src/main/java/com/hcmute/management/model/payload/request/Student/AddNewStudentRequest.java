package com.hcmute.management.model.payload.request.Student;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@Setter
@Getter
public class AddNewStudentRequest {
    @NotEmpty(message = "Mã số sinh viên không được để trống")
    private String mssv;
    @NotEmpty(message = "Họ và tên không được để trống")
    private String fullname;
    @NotNull(message = "Ngày tháng năm không được để trống")
    private Date birthday;
    @NotEmpty(message = "Giới tính không được để trống")
    private String sex;
    @NotEmpty(message = "Địa chỉ không được để trống")
    private String address;
    @NotEmpty(message = "Năm học không được để trống")
    private Date schoolyear;
    @NotEmpty(message = "Chuyên ngành không được để trống")
    private String major;
    @NotEmpty(message =  "Chương trình đào tạo không được để trống")
    private String educationprogram;
    @NotNull(message = "Mã lớp học không được để trống")
    private int  classid;

}
