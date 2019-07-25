package com.clbee.pagebuilder.payload;

import javax.validation.constraints.*;

public class SignUpRequest {
    @Size(min = 2, max = 20)
    private String lastname;

    @Size(min = 1, max = 20)
    private String firstname;
    
    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    public String getLastname() {
        return lastname;
    }
    
    public String getFirstname() {
        return firstname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
