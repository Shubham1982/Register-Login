package com.example.RegisterLogin.service.dto;

import com.example.RegisterLogin.domain.Role;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class AppUserDTO {
    private Long id;
    private String userName;
    private String mobile;
    private String email;
    private String password;
    private String image;
    private Set<RoleDTO> appRoles = new HashSet<>();

}
