package com.hcmute.management.model.payload.request.User;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Data
@NoArgsConstructor
public class AddUserInfoRequest {
    @NotEmpty(message = "Email cannot be Empty")
    String email;
    @NotEmpty(message = "Full Name cannot be Empty")
    String fullName;
    @NotEmpty(message = "gender cannot be empty")
    String gender;
}
