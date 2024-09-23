package com.antonio.curso.springsecurityjwt.springsecurityjwt.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// La función de esta clase será validar la función del token y si esto es exitoso, establecerá la autenticación de un usuario en la solicitud o en el contexto de seguridad de nuestra aplicación
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    @Autowired
    private CustomUsersDetailsService customUsersDetailsService;

    @Autowired
    private JwtGenerator jwtGenerator;

    private String getRequestToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }

        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain)throws ServletException, IOException {
       
        String token = getRequestToken(request);
        if(StringUtils.hasText(token) && jwtGenerator.validateToken(token)) {
            String userName = jwtGenerator.getUsernameJwt(token);
            UserDetails userDetails = customUsersDetailsService.loadUserByUsername(userName);
            List<String> userRoles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
            if(userRoles.contains("USER") || userRoles.contains("ADMIN")) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }

}
