package com.hcmute.management.model.payload.request.Subject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

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
    @ApiParam(hidden = true)
    final String EMPTY_MESSAGE ="cannot be empty";
    @ApiParam()
    private String name;
    @NotEmpty(message = "target"+EMPTY_MESSAGE)
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
    private boolean regFromOtherMajor;
    @NotEmpty(message = "Major " + EMPTY_MESSAGE)
    private String major;
    @NotEmpty(message = "Subject Type "+EMPTY_MESSAGE)
    private String subjectType;
    @NotEmpty(message = "Year "+EMPTY_MESSAGE)
    private String year;
}
