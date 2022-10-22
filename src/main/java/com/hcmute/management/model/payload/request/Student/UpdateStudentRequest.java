package com.hcmute.management.model.payload.request.Student;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Data
@NoArgsConstructor
@Setter
@Getter
public class ChangeInfoStudentRequest {

    private String fullname;
    private Date birthday;
    private String sex;
    private String address;
    private Date schoolyear;
    private String major;
    private String educationprogram;
    private int classid;
}
