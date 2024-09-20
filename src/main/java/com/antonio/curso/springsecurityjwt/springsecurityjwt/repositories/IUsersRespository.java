package com.antonio.curso.springsecurityjwt.springsecurityjwt.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.antonio.curso.springsecurityjwt.springsecurityjwt.models.User;

@Repository
public interface IUsersRespository extends JpaRepository<User, Long> {

    // Metodo para buscar un suario mediante su nombre
    Optional<User> findByUsername(String username);

    // Metodo para verificar si un usuario exite en bbdd
    Boolean existsByUsername(String username);

}
