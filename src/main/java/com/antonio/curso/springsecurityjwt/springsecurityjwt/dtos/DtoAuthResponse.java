package com.antonio.curso.springsecurityjwt.springsecurityjwt.dtos;

import lombok.Data;

//Esta clase devuelve la información con el token y el tipo que tenga este
@Data
public class DtoAuthResponse {
    private String accessToken;
    private String tokenType = "Bearer ";

    public DtoAuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
