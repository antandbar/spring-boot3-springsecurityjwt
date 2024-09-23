package com.antonio.curso.springsecurityjwt.springsecurityjwt.controllers;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.antonio.curso.springsecurityjwt.springsecurityjwt.dtos.DtoAuthResponse;
import com.antonio.curso.springsecurityjwt.springsecurityjwt.dtos.DtoLogin;
import com.antonio.curso.springsecurityjwt.springsecurityjwt.dtos.DtoRecord;
import com.antonio.curso.springsecurityjwt.springsecurityjwt.models.Role;
import com.antonio.curso.springsecurityjwt.springsecurityjwt.models.User;
import com.antonio.curso.springsecurityjwt.springsecurityjwt.repositories.IRolesRepostories;
import com.antonio.curso.springsecurityjwt.springsecurityjwt.repositories.IUsersRespository;
import com.antonio.curso.springsecurityjwt.springsecurityjwt.security.JwtGenerator;

@RestController
@RequestMapping("/api/auth/")
public class RestControllerAuth {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IRolesRepostories rolesRepostories;
    @Autowired
    private IUsersRespository usersRespository;
    @Autowired
    private JwtGenerator jwtGenerator;

//Método para registar usuarios con rol USER
    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody DtoRecord dtoRecord) {
        if(usersRespository.existsByUsername(dtoRecord.getUsername())) {
            return new ResponseEntity<>("el usuario ya existe, intenta con otro", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setUsername(dtoRecord.getUsername()); 
        user.setPassword(passwordEncoder.encode(dtoRecord.getPassword()));
        Role role = rolesRepostories.findByName("USER").get();
        user.setRoles(Collections.singletonList(role));
        usersRespository.save(user);

        return new ResponseEntity<>("Registro de usuario exitoso", HttpStatus.OK);
    }

    //Método para registar usuarios con rol ADMIN
    @PostMapping("registerAdmin")
    public ResponseEntity<String> registerAdmin(@RequestBody DtoRecord dtoRecord) {
        if(usersRespository.existsByUsername(dtoRecord.getUsername())) {
            return new ResponseEntity<>("el admin ya existe, intenta con otro", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setPassword(passwordEncoder.encode(dtoRecord.getPassword()));
        Role role = rolesRepostories.findByName("ADMIN").get();
        user.setRoles(Collections.singletonList(role));
        usersRespository.save(user);

        return new ResponseEntity<>("Registro de admin exitoso", HttpStatus.OK);
    }

    //Método para loegear un usuario y obtener un token
    @PostMapping("login")
    public ResponseEntity<DtoAuthResponse> login(@RequestBody DtoLogin dtoLogin) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            dtoLogin.getUsername(), dtoLogin.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);

        return new ResponseEntity<>(new DtoAuthResponse(token), HttpStatus.OK);
    }
    

}
