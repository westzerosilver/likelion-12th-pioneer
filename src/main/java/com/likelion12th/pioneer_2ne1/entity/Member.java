package com.likelion12th.pioneer_2ne1.entity;

import com.likelion12th.pioneer_2ne1.constant.Role;
import com.likelion12th.pioneer_2ne1.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@EntityListeners(value = {AutoCloseable.class})
@Table(name="member")
@Getter @Setter @ToString
public class Member extends Base{
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    // provider : google이 들어감
    private String provider;

    // providerId : 구굴 로그인 한 유저의 고유 ID가 들어감
    private String providerId;


    @Builder
    public Member(String name, String email, Role role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Member() {
        this.email = "";
    }


    public static Member createMember(MemberFormDto memberFormDto) {
        Member member = new Member();
        member.setEmail(memberFormDto.getEmail());
        member.setPassword(memberFormDto.getPassword());
        member.setRole(Role.USER);

        return member;
    }

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setEmail(memberFormDto.getEmail());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setRole(Role.USER);
        member.setName(memberFormDto.getName());
        member.setPassword(password);

        return member;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public Member update(String name) {
        this.name = name;

        return this;
    }


}
