package com.example.RegisterLogin.resource;

import com.example.RegisterLogin.domain.Role;
import com.example.RegisterLogin.service.RoleService;
import com.example.RegisterLogin.service.dto.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleResource {

    @Autowired
    private RoleService roleService;

    @PostMapping("/create-new-role")
    public RoleDTO createRole(@RequestBody RoleDTO roleDTO){
        return roleService.createNewRole(roleDTO);
    }
}
