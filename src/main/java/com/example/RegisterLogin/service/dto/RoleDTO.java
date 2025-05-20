package com.example.RegisterLogin.service.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class RoleDTO {
    private Long id;
    private String roleName;
    private String roleDescription;
}
