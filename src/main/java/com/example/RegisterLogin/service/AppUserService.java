package com.example.RegisterLogin.service;
import com.example.RegisterLogin.loginresponse.LoginResponse;
import com.example.RegisterLogin.service.dto.AppUserDTO;
import com.example.RegisterLogin.service.dto.LoginDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AppUserService {
    String addUser(AppUserDTO appUserDTO);

    LoginResponse login(LoginDTO loginDTO);

    Page<AppUserDTO> getAllUser(Pageable pageable, String searchKey, Long id);
}
