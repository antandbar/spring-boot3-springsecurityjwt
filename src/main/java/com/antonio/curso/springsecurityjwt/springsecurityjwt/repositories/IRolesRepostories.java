package com.antonio.curso.springsecurityjwt.springsecurityjwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.antonio.curso.springsecurityjwt.springsecurityjwt.models.Role;
import java.util.Optional;

@Repository
public interface IRolesRepostories extends JpaRepository<Role, Long> {

    // Metodo para buscar un rol en bbdd
    Optional<Role> findByName(String name);

}
