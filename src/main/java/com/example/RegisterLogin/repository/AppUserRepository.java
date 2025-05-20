package com.example.RegisterLogin.repository;

import com.example.RegisterLogin.domain.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    @Query("select appUser from AppUser appUser where (:id is null or appUser.id=:id) " +
            "and (:searchKey is null or appUser.mobile=:searchKey or appUser.email=:searchKey)")
    Page<AppUser> findAllUser(Pageable pageable, String searchKey, Long id);

    Optional<AppUser> findByEmail(String email);
}
