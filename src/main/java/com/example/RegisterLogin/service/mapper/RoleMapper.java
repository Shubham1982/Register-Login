package com.example.RegisterLogin.service.mapper;

import com.example.RegisterLogin.domain.Role;
import com.example.RegisterLogin.service.dto.RoleDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface RoleMapper extends EntityMapper<RoleDTO, Role>{

}
