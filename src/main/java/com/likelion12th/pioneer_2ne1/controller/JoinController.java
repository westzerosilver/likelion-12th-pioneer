package com.likelion12th.pioneer_2ne1.controller;




import com.likelion12th.pioneer_2ne1.dto.JoinDTO;
import com.likelion12th.pioneer_2ne1.entity.Member;
import com.likelion12th.pioneer_2ne1.service.JoinService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class JoinController {

    private final JoinService joinService;

    public JoinController(@RequestBody JoinService joinService) {

        this.joinService = joinService;
    }


//    @PostMapping("/join")
//    public String joinProcess(@RequestBody JoinDTO joinDTO, BindingResult result) {
//        System.out.println("---------------------------------------------------");
//        System.out.println("Received JoinDTO: " + joinDTO);
//        System.out.println("---------------------------------------------------");
//        joinService.joinProcess(joinDTO);
//
//        return "ok";
//    }

    @PostMapping(value="/join")
    public ResponseEntity<?> joinProcess(@Valid @RequestBody JoinDTO joinDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }
        try {
//            Member member = Member.createMember(memberFormDto, passwordEncoder);
//            memberService.saveMember(member);
            joinService.joinProcess(joinDTO);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("멤버 생성 실패: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }
}