package com.likelion12th.pioneer_2ne1.controller;

import com.likelion12th.pioneer_2ne1.dto.LoginFormDto;
import com.likelion12th.pioneer_2ne1.dto.MemberFormDto;
import com.likelion12th.pioneer_2ne1.entity.Member;
import com.likelion12th.pioneer_2ne1.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

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

        if (!memberFormDto.isPasswordMatching()) {
            model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            System.out.println(e);
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/";
    }


    @GetMapping("/login")
    public String loginMember() {
        return "/member/memberLoginForm";
    }



    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "/member/memberLoginForm";
    }


    @GetMapping("/mypage")
    public String myPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberService.findByEmail(userDetails.getUsername());
        model.addAttribute("member", member);
        return "member/mypage";
    }

    @GetMapping("/update")
    public String editMemberForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberService.findByEmail(userDetails.getUsername());
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(member.getEmail());
        memberFormDto.setName(member.getName());

        model.addAttribute("memberFormDto", memberFormDto);
        return "member/updateMemberForm";
    }



    @PostMapping("/update")
    public String editMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            return "member/updateMemberForm";
        }

        if (!memberFormDto.isPasswordMatching()) {
            model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "member/updateMemberForm";
        }

        try {
            Member member = memberService.findByEmail(userDetails.getUsername());
            member.setName(memberFormDto.getName());
            if (!memberFormDto.getPassword().isEmpty()) {
                member.setPassword(passwordEncoder.encode(memberFormDto.getPassword()));
            }
            memberService.updateMember(member);

        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/updateMemberForm";
        }
        return "redirect:/members/mypage";
    }

    @GetMapping("/delete")
    public String deleteMember() {
        return "member/delete";
    }

    @PostMapping("/delete")
    public String deleteMember(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            memberService.deleteMember(userDetails.getUsername());

            // 로그아웃 처리
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
        }
        catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/delete";
        }

        return "redirect:/";
    }

}
