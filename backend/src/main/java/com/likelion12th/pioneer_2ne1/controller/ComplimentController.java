package com.likelion12th.pioneer_2ne1.controller;

import com.likelion12th.pioneer_2ne1.dto.ComplimentReqDto;
import com.likelion12th.pioneer_2ne1.entity.Compliment;
import com.likelion12th.pioneer_2ne1.service.ComplimentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/compliments")
@Controller
@RequiredArgsConstructor
public class ComplimentController {
    private final ComplimentService complimentService;

    @PostMapping(value = "/create")
    public ResponseEntity<?> createCompliments(@Valid @RequestBody ComplimentReqDto complimentReqDto,
                                               @AuthenticationPrincipal UserDetails userDetails) {

        try {
            System.out.println("ComplimentController userDetails.getUsername(): " + userDetails.getUsername());
            complimentService.createNewCompliment(complimentReqDto, userDetails.getUsername());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("칭찬일기 생성 실패: " + e.getMessage());
        }

        return ResponseEntity.ok(complimentReqDto);
    }

}
