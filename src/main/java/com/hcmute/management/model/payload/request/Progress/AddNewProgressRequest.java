package com.hcmute.management.model.payload.request.Progress;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

@Getter
@Setter
@Data
@NoArgsConstructor
public class AddNewProgressRequest {
    @NotEmpty(message = "Mô tả không được để trống")
    private String description;

    // @NotEmpty(message = "")
    private String status;

    @NotEmpty(message = "Id không được để trống")
    private String subjectId;

    @NotNull(message = "Week can not be null")
    private int week;


}
