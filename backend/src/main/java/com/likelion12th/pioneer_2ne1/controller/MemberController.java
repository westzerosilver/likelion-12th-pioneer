package com.likelion12th.pioneer_2ne1.controller;

import com.likelion12th.pioneer_2ne1.dto.*;
import com.likelion12th.pioneer_2ne1.entity.Member;
import com.likelion12th.pioneer_2ne1.jwt.JWTUtil;
import com.likelion12th.pioneer_2ne1.jwt.JwtResponse;
import com.likelion12th.pioneer_2ne1.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtil jwtUtil;


    @GetMapping(value = "/join")
    public ResponseEntity<?> getJoin() {
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }


    @PostMapping(value="/join")
    public ResponseEntity<?> joinProcess(@Valid @RequestBody MemberFormDto memberFormDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        if (!memberFormDto.getPassword().equals(memberFormDto.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match");
        }
        try {

            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("멤버 생성 실패: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwtToken = jwtUtil.createJwt(userDetails.getUsername(), userDetails.getAuthorities().toString(), null);
            return ResponseEntity.ok(new JwtResponse(jwtToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response,
                                         @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 로그아웃 처리
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return new ResponseEntity<>("You have been logged out successfully.", HttpStatus.OK);
    }




    @GetMapping("/mypage")
    public ResponseEntity<?> myPage(
            @AuthenticationPrincipal UserDetails userDetails) {

        try{
            Mypage mypage = memberService.getMypage(userDetails.getUsername());

            return ResponseEntity.ok(mypage);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/update")
    public ResponseEntity<?> updateMember(@AuthenticationPrincipal UserDetails userDetails) {

        Member member = memberService.findByEmail(userDetails.getUsername());
        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("member == null");
        }


        JoinDTO memberFormDto = new JoinDTO();
        memberFormDto.setEmail(member.getEmail());
        memberFormDto.setName(member.getName());

        return ResponseEntity.ok(memberFormDto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateMember(
            @Valid @RequestBody MemberFormDto memberFormDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails) {

        // 회원 조회
        Member member = memberService.findByEmail(userDetails.getUsername());
        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // 사용자 인증 확인 (선택 사항)
        if (!userDetails.getUsername().equals(member.getEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        if (!memberFormDto.getPassword().equals(memberFormDto.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match");
        }

        // 회원 정보 업데이트
        member.setName(memberFormDto.getName());
        if (memberFormDto.getPassword() != null && !memberFormDto.getPassword().isEmpty()) {
            // 비밀번호 암호화 후 저장
            String encodedPassword = passwordEncoder.encode(memberFormDto.getPassword());
            member.setPassword(encodedPassword);
        }

        memberService.updateMember(member); // 업데이트된 회원 정보를 저장


        return ResponseEntity.ok(member);
    }

    @PostMapping("/checkPassword")
    public ResponseEntity<?> checkPassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CheckPasswordDto checkPasswordDto)
    {
        try {
            memberService.checkPassword(userDetails.getUsername(), checkPasswordDto, passwordEncoder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok("");
    }

    @GetMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal UserDetails userDetails) {
        ProfileDto profileDto = new ProfileDto();
        try{
            Member member = memberService.findByEmail(userDetails.getUsername());

            profileDto.setName(member.getName());
            profileDto.setProfileImgPath(member.getProfileImgPath());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        return ResponseEntity.ok(profileDto);
    }

    @PatchMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@RequestPart(name = "profileDto", required = false) ProfileDto profileDto,
                                           @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        try {
            memberService.updateProfile(userDetails.getUsername(), profileDto, profileImage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok("update profile success");
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(
            HttpServletRequest request, HttpServletResponse response,
            @Valid @RequestBody PasswordDto passwordDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails) {

        // 회원 조회
        Member member = memberService.findByEmail(userDetails.getUsername());
        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        try {
            memberService.updatePassword(member, passwordDto, passwordEncoder);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            // 로그아웃 처리
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }

            return ResponseEntity.ok("password update success");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }



    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMember(@AuthenticationPrincipal UserDetails userDetails) {
        try {

            // 회원 조회
            Member member = memberService.findByEmail(userDetails.getUsername());
            if (member == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            if (!userDetails.getUsername().equals(member.getEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            memberService.deleteMember(userDetails.getUsername());


            return ResponseEntity.ok("delete ok");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("delete error");
        }
    }




}
