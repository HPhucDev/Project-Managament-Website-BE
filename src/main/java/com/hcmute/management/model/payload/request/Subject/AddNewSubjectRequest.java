package com.hcmute.management.model.payload.request.Subject;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Constraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Data
@NoArgsConstructor
public class AddNewSubjectRequest {
    final String EMPTY_MESSAGE ="cannot be empty";
    @NotEmpty(message = "Subject name "+EMPTY_MESSAGE)
    private String name;
    @NotEmpty(message = "target "+EMPTY_MESSAGE)
    private String target;
    @NotEmpty(message = "Requirement "+EMPTY_MESSAGE)
    private String requirement;
    @NotEmpty(message = "Product "+EMPTY_MESSAGE)
    private String product;
    @NotEmpty(message ="Description "+EMPTY_MESSAGE)
    private String description;
    @NotEmpty(message = "Lecturer id "+EMPTY_MESSAGE)
    private String lecturerId;
    @Max(value = 4,message = "Group cannot have more than 4 student")
    @Min(value = 0)
    private int groupCap;
    private int groupLectureCap;
    private boolean regFromOtherMajor;
    @NotEmpty(message = "Major " + EMPTY_MESSAGE)
    private String major;
    @NotEmpty(message = "Subject Type "+EMPTY_MESSAGE)
    private String subjectType;
    @NotEmpty(message = "Year "+EMPTY_MESSAGE)
    private String year;
}
