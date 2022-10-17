package com.hcmute.management.model.payload.request.Student;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Setter
@Getter
public class DeleteStudentRequest {
    private String[] studentId;
}
