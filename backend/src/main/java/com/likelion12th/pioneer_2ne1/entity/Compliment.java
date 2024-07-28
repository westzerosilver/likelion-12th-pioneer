package com.likelion12th.pioneer_2ne1.entity;


import com.likelion12th.pioneer_2ne1.dto.ComplimentReqDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
public class Compliment {
    @Id
    @Column(name = "compliment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "compliment1")
    private String compliment1;

    @Column(name = "compliment2")
    private String compliment2;

    @Column(name = "compliment3")
    private String compliment3;

    @Column(name = "compliment4")
    private String compliment4;

    @CreatedDate
    @Column(name = "createdDate")
    private LocalDate createdDate;



    public static Compliment createCompliment(Member member, ComplimentReqDto complimentReqDto) {
        Compliment compliment = new Compliment();

        member.addComplimentCnt();
        member.setComplementDate(LocalDate.now());
        compliment.setMember(member);

        compliment.setCompliment1(complimentReqDto.getCompliment1());
        compliment.setCompliment2(complimentReqDto.getCompliment2());
        compliment.setCompliment3(complimentReqDto.getCompliment3());
        compliment.setCompliment4(complimentReqDto.getCompliment4());

        return compliment;
    }
}
