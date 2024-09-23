package com.antonio.curso.springsecurityjwt.springsecurityjwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// indica al contenedor de spring que es una clase de seguridad al momento de arrancar la aplicación
@Configuration
// Indicamos que se activa la seguridad web en nuestra aplicación y además está
// será una clase que contendrá toda la configuración referente a la seguridad
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    // Este bean va a encargarse de verificar la información de los ususarios que se
    // loguaran en nuestra api
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Con este bean nos encargaremos de encriptar todas las contraseñas
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Este bean incorpora el filtro de seguridad de json web token que creamos en
    // nuestra clase anterior
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    // Se crea bean el cual va a establecer una cadena de filtros en nuestra
    // aplicación
    // y es aquí donde determinaremos los permisos segúnn los roles de usuarios para
    // acceder a nuestra aplicación
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .exceptionHandling() // Permitimos el manejo de Excepciones
            .authenticationEntryPoint(authenticationEntryPoint) // Nos establece un punto de entrada personalizado
                                                                    // de autenticación para el manejo de
                                                                    // autenticaciones no autorizadas
            .and()
            .sessionManagement() // Persiste la gesión de sessiones
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests() // Toda petición HTTP debe ser autorizada
            .requestMatchers("/api/auth/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .httpBasic();
       
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
        
    }
}
