package com.likelion12th.pioneer_2ne1.controller;

import com.likelion12th.pioneer_2ne1.dto.MainDto;
import com.likelion12th.pioneer_2ne1.service.MemberService;
import com.sun.tools.javac.Main;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainController {

    @Autowired
    private MemberService memberService;

    @GetMapping(value = "/main")
    public ResponseEntity<?> main(@AuthenticationPrincipal UserDetails userDetails) {
        try{
            MainDto mainDto = memberService.getMain(userDetails.getUsername());

            return ResponseEntity.ok(mainDto);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/api/hello")
    public String test() {
        return "Hello, world!";
    }
}
