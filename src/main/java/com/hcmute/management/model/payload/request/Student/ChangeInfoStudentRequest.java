package com.hcmute.management.model.payload.request.Student;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hcmute.management.common.MajorEnum;
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
    @NotEmpty(message = "Full name can not be empty")
    private String fullName;
    @NotEmpty(message = "Gender can not be empty")
    private String gender;
    @NotEmpty(message = "Address can not be empty")
    private String address;
    @NotNull(message = "school year can not be empty")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime schoolYear;
    private MajorEnum major;
    @NotEmpty(message =  "Education program can not be empty")
    private String educationProgram;
    @NotEmpty(message = "Class id can not be empty")
    private String classId;
    @NotEmpty(message = "Email can not be empty")
    private String email;
    @NotNull(message = "school year can not be empty")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime birthDay;
}
