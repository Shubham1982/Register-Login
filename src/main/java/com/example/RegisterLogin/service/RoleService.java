package com.example.RegisterLogin.service;

import com.example.RegisterLogin.domain.Role;
import com.example.RegisterLogin.service.dto.RoleDTO;

public interface RoleService {
    RoleDTO createNewRole(RoleDTO roleDTO);
}
