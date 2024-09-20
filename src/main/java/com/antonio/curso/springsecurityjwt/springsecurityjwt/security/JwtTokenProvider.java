package com.antonio.curso.springsecurityjwt.springsecurityjwt.security;

import java.util.Date;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

    //Método para crear un token por medio de la autenticación
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentTime = new Date();
        Date tokenExpiration = new Date(currentTime.getTime() + ConstantSecurity.JWT_EXPIRATION_TOKEN);

        //Se genera el token
        String token = Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(tokenExpiration)
            .signWith(SignatureAlgorithm.HS512, ConstantSecurity.JRT_SIGNATURE)
            .compact();
        
        return token;
    }

    //Método para extraer un username desde un token
    public String getUsernameJwt(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(ConstantSecurity.JRT_SIGNATURE)
            .parseClaimsJws(token)
            .getBody();
        
        return claims.getSubject();
    }

    //Metodo para validar token
    public Boolean validateToken(String token) {
        try{
            Jwts.parser().setSigningKey(ConstantSecurity.JRT_SIGNATURE).parseClaimsJws(token);
            return true;
        }catch(Exception e){
            throw new AuthenticationCredentialsNotFoundException("jwt has experienced or is not correct");
        }
    }

}
