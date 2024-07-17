package com.likelion12th.pioneer_2ne1.entity;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable { // 직렬화 기능을 가진 세션 DTO

    // 인증된 사용자 정보만 필요 => name, email, picture 필드만 선언
    private String name;
    private String email;

    public SessionUser(Member user) {
        this.name = user.getName();
        this.email = user.getEmail();
    }
}