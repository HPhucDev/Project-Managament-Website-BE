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

@Data
@NoArgsConstructor
@Setter
@Getter
public class ChangeInfoStudentRequest {
    @NotEmpty(message = "Họ và tên không được để trống")
    private String fullname;
    @NotEmpty(message = "Giới tính không được để trống")
    private String gender;
    @NotEmpty(message = "Địa chỉ không được để trống")
    private String address;
    @NotNull(message = "school year can not be empty")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date schoolyear;
    @NotEmpty(message = "Chuyên ngành không được để trống")
    private String major;
    @NotEmpty(message =  "Chương trình đào tạo không được để trống")
    private String educationprogram;
    @NotEmpty(message = "Mã lớp học không được để trống")
    private String classid;
    @NotEmpty(message = "Email không được để trống")
    private String email;
    @NotNull(message = "Ngày tháng năm không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime birthday;
}
