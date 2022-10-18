package com.hcmute.management.model.payload.request.Lecturer;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Data
@NoArgsConstructor
public class AddNewLecturerRequest {
    @NotEmpty(message ="Mã giảng viên không được để trống")
    private String id;
    @NotEmpty(message ="Bằng cấp không được để trống")
    private String qualification;
    @NotEmpty(message ="Chức vụ không được để trống")
    private String position;
    @NotEmpty(message = "Họ và tên không được để trống")
    private String fullName;
    @NotNull(message = "Ngày tháng năm không được để trống")
    private Date birthday;
    @NotEmpty(message = "Giới tính không được để trống")
    private String gender;
    @NotEmpty(message = "Địa chỉ không được để trống")
    private String address;
}
