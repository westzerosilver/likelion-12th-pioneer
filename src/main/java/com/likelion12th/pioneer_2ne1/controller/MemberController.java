package com.likelion12th.pioneer_2ne1.controller;

import com.likelion12th.pioneer_2ne1.dto.LoginFormDto;
import com.likelion12th.pioneer_2ne1.dto.MemberFormDto;
import com.likelion12th.pioneer_2ne1.entity.Member;
import com.likelion12th.pioneer_2ne1.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

//    // 회원 가입
//    @PostMapping("/new")
//    public ResponseEntity<?> registerMember(@RequestBody @Valid MemberFormDto memberFormDto, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
//        }
//        try {
//            Member member = Member.createMember(memberFormDto, passwordEncoder);
//            memberService.saveMember(member);
//        } catch (IllegalStateException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
//        }
//
//        return ResponseEntity.status(HttpStatus.CREATED).body("Member created successfully");
//    }
//
//    // 로그인 (예시로 로그인 후 JWT 토큰 반환하는 로직을 추가할 수 있음)
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody @Valid LoginFormDto loginFormDto, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
//        }
//
//        try {
//            UserDetails userDetails = memberService.loadUserByUsername(loginFormDto.getEmail());
//            if (!passwordEncoder.matches(loginFormDto.getPassword(), userDetails.getPassword())) {
//                throw new BadCredentialsException("Invalid password");
//            }
//
//            // 로그인 성공 시 JWT 토큰 생성 및 반환 (토큰 생성 로직은 추가 구현 필요)
//            String token = "dummy-jwt-token"; // 실제로는 JWT 생성 로직 필요
//            return ResponseEntity.ok().body(token);
//        } catch (UsernameNotFoundException | BadCredentialsException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
//        }
//    }
//

    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            return "member/memberForm";
        }
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            System.out.println("-------------------- error -------------------------");
            System.out.println(e);
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/";
    }

//    @PostMapping(value = "/new", consumes = "application/json", produces = "application/json")
//    public ResponseEntity<?> memberForm(@RequestBody @Valid MemberFormDto memberFormDto, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
//        }
//        try {
//            Member member = Member.createMember(memberFormDto, passwordEncoder);
//            memberService.saveMember(member);
//        } catch (IllegalStateException e) {
//            System.out.println("-------------------- error -------------------------");
//            System.out.println(e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//
//        return ResponseEntity.ok().body("Member created successfully");
//    }

    @GetMapping("/login")
    public String loginMember() {
        System.out.println("-------------login success ----------------");
        return "/member/memberLoginForm";
    }



    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        System.out.println("-------------login error ----------------");
        return "/member/memberLoginForm";
    }


    @GetMapping("/mypage")
    public String myPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            Member member = memberService.findByEmail(email);
            model.addAttribute("member", member);
        }
        return "member/mypage"; // mypage.html 템플릿을 반환
    }


}
