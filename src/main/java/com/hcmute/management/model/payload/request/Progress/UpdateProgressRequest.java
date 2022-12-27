package com.hcmute.management.model.payload.request.Progress;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Data
@NoArgsConstructor
public class UpdateProgressRequest {
    @NotEmpty(message = "Mô tả không được để trống")
    private String description;

    // @NotEmpty(message = "")
    private String status;
    @NotNull(message = "Ngày tháng năm không được để trống")
    @Past(message = "Ngày tháng năm không được vượt quá hôm nay")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createDate;

    @NotNull(message = "Ngày tháng năm không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime modiferDate;

    @NotNull(message = "Ngày tháng năm không được để trống")
    @Past(message = "Ngày tháng năm không được vượt quá hôm nay")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime timeSubmit;

    @NotEmpty(message = "Id không được để trống")
    private String subjectId;

    @NotEmpty(message = "Id không được để trống")
    private String studentId;
}
