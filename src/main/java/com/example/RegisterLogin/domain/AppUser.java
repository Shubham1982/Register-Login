package com.example.RegisterLogin.domain;

import lombok.Data;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "app_users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "image")
    private String image;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "app_user_app_role",
            joinColumns = @JoinColumn(name = "app_user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "app_role_id", referencedColumnName = "id"))
    private Set<Role> appRoles = new HashSet<>();
}
