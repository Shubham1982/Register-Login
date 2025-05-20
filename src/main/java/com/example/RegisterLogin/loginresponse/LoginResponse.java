package com.example.RegisterLogin.loginresponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String message;
    private Boolean status;

}
