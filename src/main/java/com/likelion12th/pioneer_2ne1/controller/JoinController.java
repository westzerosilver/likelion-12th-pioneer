package com.likelion12th.pioneer_2ne1.controller;




import com.likelion12th.pioneer_2ne1.dto.JoinDTO;
import com.likelion12th.pioneer_2ne1.service.JoinService;
import org.springframework.stereotype.Controller;
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


    @PostMapping("/join")
    public String joinProcess(@RequestBody JoinDTO joinDTO) {
        System.out.println("---------------------------------------------------");
        System.out.println("Received JoinDTO: " + joinDTO);
        System.out.println("---------------------------------------------------");
        joinService.joinProcess(joinDTO);
        return "ok";
    }
}