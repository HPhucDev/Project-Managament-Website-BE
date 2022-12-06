package com.hcmute.management.model.payload.request.Progress;

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
    @NotNull(message = "Ngày tháng năm không được để trống")
    @Past(message = "Ngày tháng năm không được vượt quá hôm nay")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date createDate;


    @NotNull(message = "Ngày tháng năm không được để trống")
    @Past(message = "Ngày tháng năm không được vượt quá hôm nay")
    private Date timeSubmit;

    @NotEmpty(message = "Id không được để trống")
    private String subjectId;

    @NotEmpty(message = "Id không được để trống")
    private String studentId;
    @NotNull(message = "Week can not be null")
    private int week;


}
