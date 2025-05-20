package com.example.RegisterLogin.service.impl;

import com.example.RegisterLogin.domain.Role;
import com.example.RegisterLogin.repository.RoleRepository;
import com.example.RegisterLogin.resource.RoleResource;
import com.example.RegisterLogin.service.RoleService;
import com.example.RegisterLogin.service.dto.RoleDTO;
import com.example.RegisterLogin.service.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public RoleDTO createNewRole(RoleDTO roleDTO) {

        Role role = roleMapper.toEntity(roleDTO);
        role = roleRepository.save(role);
        RoleDTO roleDTO1 = roleMapper.toDto(role);
        return roleDTO1;
    }
}
