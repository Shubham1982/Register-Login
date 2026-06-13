package com.example.RegisterLogin.service.mapper;

import com.example.RegisterLogin.domain.Role;
import com.example.RegisterLogin.service.dto.RoleDTO;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class RoleMapper implements EntityMapper<RoleDTO, Role> {

    @Override
    public Role toEntity(RoleDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Role role = new Role();
        role.setId( dto.getId() );
        role.setRoleName( dto.getRoleName() );
        role.setRoleDescription( dto.getRoleDescription() );
        return role;
    }

    @Override
    public RoleDTO toDto(Role entity) {
        if ( entity == null ) {
            return null;
        }

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId( entity.getId() );
        roleDTO.setRoleName( entity.getRoleName() );
        roleDTO.setRoleDescription( entity.getRoleDescription() );
        return roleDTO;
    }

    @Override
    public List<Role> toEntity(List<RoleDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Role> list = new ArrayList<Role>( dtoList.size() );
        for ( RoleDTO roleDTO : dtoList ) {
            list.add( toEntity( roleDTO ) );
        }

        return list;
    }

    @Override
    public List<RoleDTO> toDto(List<Role> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<RoleDTO> list = new ArrayList<RoleDTO>( entityList.size() );
        for ( Role role : entityList ) {
            list.add( toDto( role ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(Role entity, RoleDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getRoleName() != null ) {
            entity.setRoleName( dto.getRoleName() );
        }
        if ( dto.getRoleDescription() != null ) {
            entity.setRoleDescription( dto.getRoleDescription() );
        }
    }
}
