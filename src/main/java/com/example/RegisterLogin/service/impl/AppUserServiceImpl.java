package com.example.RegisterLogin.service.impl;

import com.example.RegisterLogin.domain.AppUser;
import com.example.RegisterLogin.domain.Role;
import com.example.RegisterLogin.loginresponse.LoginResponse;
import com.example.RegisterLogin.repository.AppUserRepository;
import com.example.RegisterLogin.repository.RoleRepository;
import com.example.RegisterLogin.service.AppUserService;
import com.example.RegisterLogin.service.dto.AppUserDTO;
import com.example.RegisterLogin.service.dto.LoginDTO;
import com.example.RegisterLogin.service.dto.RoleDTO;
import com.example.RegisterLogin.service.mapper.AppUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public String addUser(AppUserDTO appUserDTO) {
        AppUser appUser = appUserMapper.toEntity(appUserDTO);
        appUser.setPassword(passwordEncoder.encode(appUserDTO.getPassword()));
        Set<Role> roles = new HashSet<>();
        for (RoleDTO roleDTO : appUserDTO.getAppRoles()) {
            Optional<Role> roleOptional = roleRepository.findByRoleName(roleDTO.getRoleName());
            Role role;
            if (roleOptional.isPresent()) {
                role = roleOptional.get();
            } else {
                role = new Role();
                role.setRoleName(roleDTO.getRoleName());
                role.setRoleDescription(roleDTO.getRoleDescription());
                roleRepository.save(role);
            }
            roles.add(role);
        }
        appUser.setAppRoles(roles);

        appUserRepository.save(appUser);
        return appUserDTO.getUserName();
    }

    @Override
    public LoginResponse login(LoginDTO loginDTO) {
        Optional<AppUser> appUser = appUserRepository.findByEmail(loginDTO.getEmail());
        if (appUser.isPresent()) {
            String pass = appUser.get().getPassword();
            LoginResponse passowordMatch = isPassowordMatch(loginDTO, pass);
            return passowordMatch;
        }

        return new LoginResponse("Email not exist", false);
    }

    //    @Override
//    public Page<AppUserDTO> getAllUser(Pageable pageable) {
//        Page<AppUser> users = appUserRepository.findAll(pageable);
//        Page<AppUserDTO> appUserDTOS = appUserMapper.toDto(users);
//        return users;
//    }
    @Override
    public Page<AppUserDTO> getAllUser(Pageable pageable, String searchKey, Long id) {
        Page<AppUser> users = appUserRepository.findAllUser(pageable, searchKey, id);
        return users.map(appUserMapper::toDto);
    }

    public LoginResponse isPassowordMatch(LoginDTO loginDTO, String pass) {
        boolean passWordMatch = passwordEncoder.matches(loginDTO.getPassword(), pass);
        if (!passWordMatch) {
            return new LoginResponse("Passoword incorrect", Boolean.FALSE);
        }
        return new LoginResponse("Login Successful", Boolean.TRUE);
    }
}
