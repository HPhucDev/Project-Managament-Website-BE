package com.hcmute.management.model.payload.request.Authenticate;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Setter
@Getter
public class AddNewUserRequest {

    @NotEmpty(message = "Thiếu password")
    @Size(min = 8, message = "Password phải từ 8 kí tự trở lên")
    private String password;
    @NotEmpty(message = "Thiếu userName")
    @Size(min = 9, message = "userName tối thiểu 9 kí tự")
    private String userName;
}
