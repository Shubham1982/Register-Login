package com.example.RegisterLogin.service.mapper;

import com.example.RegisterLogin.domain.AppUser;
import com.example.RegisterLogin.service.dto.AppUserDTO;

import org.springframework.stereotype.Component;
import com.example.RegisterLogin.domain.Role;
import com.example.RegisterLogin.service.dto.RoleDTO;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class AppUserMapper implements EntityMapper<AppUserDTO, AppUser> {

    @Override
    public AppUser toEntity(AppUserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        AppUser appUser = new AppUser();

        appUser.setId( dto.getId() );
        appUser.setUserName( dto.getUserName() );
        appUser.setMobile( dto.getMobile() );
        appUser.setEmail( dto.getEmail() );
        appUser.setPassword( dto.getPassword() );
        appUser.setImage( dto.getImage() );
        appUser.setAppRoles( roleDTOSetToRoleSet( dto.getAppRoles() ) );

        return appUser;
    }

    @Override
    public AppUserDTO toDto(AppUser entity) {
        if ( entity == null ) {
            return null;
        }

        AppUserDTO appUserDTO = new AppUserDTO();

        appUserDTO.setId( entity.getId() );
        appUserDTO.setUserName( entity.getUserName() );
        appUserDTO.setMobile( entity.getMobile() );
        appUserDTO.setEmail( entity.getEmail() );
        appUserDTO.setPassword( entity.getPassword() );
        appUserDTO.setImage( entity.getImage() );
        appUserDTO.setAppRoles( roleSetToRoleDTOSet( entity.getAppRoles() ) );

        return appUserDTO;
    }

    @Override
    public List<AppUser> toEntity(List<AppUserDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<AppUser> list = new ArrayList<AppUser>( dtoList.size() );
        for ( AppUserDTO appUserDTO : dtoList ) {
            list.add( toEntity( appUserDTO ) );
        }

        return list;
    }

    @Override
    public List<AppUserDTO> toDto(List<AppUser> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<AppUserDTO> list = new ArrayList<AppUserDTO>( entityList.size() );
        for ( AppUser appUser : entityList ) {
            list.add( toDto( appUser ) );
        }

        return list;
    }

    protected Role roleDTOToRole(RoleDTO roleDTO) {
        if ( roleDTO == null ) {
            return null;
        }

        Role role = new Role();
        role.setId( roleDTO.getId() );
        role.setRoleName( roleDTO.getRoleName() );
        role.setRoleDescription( roleDTO.getRoleDescription() );
        return role;
    }

    protected Set<Role> roleDTOSetToRoleSet(Set<RoleDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<Role> set1 = new LinkedHashSet<Role>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( RoleDTO roleDTO : set ) {
            set1.add( roleDTOToRole( roleDTO ) );
        }

        return set1;
    }

    protected RoleDTO roleToRoleDTO(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId( role.getId() );
        roleDTO.setRoleName( role.getRoleName() );
        roleDTO.setRoleDescription( role.getRoleDescription() );
        return roleDTO;
    }

    protected Set<RoleDTO> roleSetToRoleDTOSet(Set<Role> set) {
        if ( set == null ) {
            return null;
        }

        Set<RoleDTO> set1 = new LinkedHashSet<RoleDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Role role : set ) {
            set1.add( roleToRoleDTO( role ) );
        }

        return set1;
    }

    @Override
    public void partialUpdate(AppUser entity, AppUserDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getUserName() != null ) {
            entity.setUserName( dto.getUserName() );
        }
        if ( dto.getMobile() != null ) {
            entity.setMobile( dto.getMobile() );
        }
        if ( dto.getEmail() != null ) {
            entity.setEmail( dto.getEmail() );
        }
        if ( dto.getPassword() != null ) {
            entity.setPassword( dto.getPassword() );
        }
        if ( dto.getImage() != null ) {
            entity.setImage( dto.getImage() );
        }
        if ( entity.getAppRoles() != null ) {
            Set<Role> set = roleDTOSetToRoleSet( dto.getAppRoles() );
            if ( set != null ) {
                entity.getAppRoles().clear();
                entity.getAppRoles().addAll( set );
            }
        }
        else {
            Set<Role> set = roleDTOSetToRoleSet( dto.getAppRoles() );
            if ( set != null ) {
                entity.setAppRoles( set );
            }
        }
    }
}
