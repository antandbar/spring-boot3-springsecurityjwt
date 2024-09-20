package com.antonio.curso.springsecurityjwt.springsecurityjwt.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.antonio.curso.springsecurityjwt.springsecurityjwt.models.Role;
import com.antonio.curso.springsecurityjwt.springsecurityjwt.models.User;
import com.antonio.curso.springsecurityjwt.springsecurityjwt.repositories.IUsersRespository;

@Service
public class CustomUsersDetailsService implements UserDetailsService{

    @Autowired
    private IUsersRespository usersRespo;

    //Metodo para traer una lista de autoridades por medio de una lista de roles
    public Collection<GrantedAuthority> mapToAuthorities(List<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    //Metodo para traer usuario con todos sus datos por medio de su username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersRespo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapToAuthorities(user.getRoles()));
    }
    
}
