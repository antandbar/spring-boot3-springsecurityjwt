package com.antonio.curso.springsecurityjwt.springsecurityjwt.models;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ManyToAny;
import org.hibernate.engine.internal.Cascade;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles", 
    joinColumns = @JoinColumn(
        name = "user_id", referencedColumnName = "id_user"),
        inverseJoinColumns = @JoinColumn(name = "Role_id", referencedColumnName = "id_role"))
    private List<Role> roles = new ArrayList<>();

}
