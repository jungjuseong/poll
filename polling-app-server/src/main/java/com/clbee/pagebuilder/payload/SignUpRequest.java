package com.clbee.pagebuilder.payload;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class SignUpRequest {

    @Size(min = 1, max = 30)
    private String fullname;

    @NotBlank
    @Size(max = 30)
    @Email
    private String email;

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 5, max = 20)
    private String password;
}
