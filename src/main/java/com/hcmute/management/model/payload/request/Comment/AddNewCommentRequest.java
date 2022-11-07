package com.hcmute.management.model.payload.request.Comment;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddNewCommentRequest {
    @NotEmpty(message = "Nội dung không được để trống")
    private String message;
    @NotNull(message="ProgressId ")
    private  int progressid;
}
