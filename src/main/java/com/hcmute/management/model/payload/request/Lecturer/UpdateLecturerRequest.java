package com.hcmute.management.model.payload.request.Lecturer;

import com.hcmute.management.common.PositionLecturerEnum;
import com.hcmute.management.common.QualificationLecturerEnum;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Data
@NoArgsConstructor
public class UpdateLecturerRequest {
    @NotEmpty(message ="Bằng cấp không được để trống")
    private QualificationLecturerEnum qualification;
    @NotEmpty(message ="Chức vụ không được để trống")
    private PositionLecturerEnum position;
    @NotEmpty(message = "Họ và tên không được để trống")
    private String fullName;
    @NotNull(message = "Ngày tháng năm không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime birthday;
    @NotEmpty(message = "Giới tính không được để trống")
    private String gender;
    @NotEmpty(message = "Địa chỉ không được để trống")
    private String address;
    @Email
    private String email;
}
