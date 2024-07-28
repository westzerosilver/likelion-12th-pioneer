package com.likelion12th.pioneer_2ne1.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JwtResponse {
    private String token;

    public JwtResponse(String token) {
        this.token = token;
    }

}