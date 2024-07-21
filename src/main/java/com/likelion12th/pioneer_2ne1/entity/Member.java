package com.likelion12th.pioneer_2ne1.entity;

import com.likelion12th.pioneer_2ne1.constant.Role;
import com.likelion12th.pioneer_2ne1.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(value = {AutoCloseable.class})
@Table(name="member")
@Getter @Setter
@ToString
public class Member extends Base{
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;

    private String name;

//    @Enumerated(EnumType.STRING)
//    private Role role;
    private String role;

    // provider : google이 들어감
    private String provider;

    // providerId : 구굴 로그인 한 유저의 고유 ID가 들어감
    private String providerId;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Compliment> complimentEntries = new ArrayList<>();

    private int complimentCnt = 0;


//    @Builder
//    public Member(String name, String email, Role role) {
//        this.name = name;
//        this.email = email;
//        this.role = role;
//    }

//    @Builder
//    public Member(String name, String email, String role) {
//        this.name = name;
//        this.email = email;
//        this.role = role;
//    }
//
//    public Member() {
//        this.email = "";
//    }
//

//    public static Member createMember(MemberFormDto memberFormDto) {
//        Member member = new Member();
//        member.setEmail(memberFormDto.getEmail());
//        member.setPassword(memberFormDto.getPassword());
//        member.setRole("ROLE_USER");
//
//        return member;
//    }
//
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setEmail(memberFormDto.getEmail());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setRole("ROLE_ADMIN");
        member.setName(memberFormDto.getName());
        member.setPassword(password);

        return member;
    }

    public int addComplimentCnt() {
        return this.complimentCnt++ ;
    }

//    public String getRoleKey() {
//        return this.role.getKey();
//    }



}
