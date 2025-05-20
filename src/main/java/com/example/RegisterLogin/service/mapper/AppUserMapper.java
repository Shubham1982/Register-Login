package com.example.RegisterLogin.service.mapper;

import com.example.RegisterLogin.domain.AppUser;
import com.example.RegisterLogin.service.dto.AppUserDTO;

import org.mapstruct.Mapper;
@Mapper(componentModel = "spring", uses = {})
public interface AppUserMapper extends EntityMapper<AppUserDTO, AppUser>{

}
