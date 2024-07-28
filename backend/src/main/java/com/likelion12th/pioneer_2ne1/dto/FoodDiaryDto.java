package com.likelion12th.pioneer_2ne1.dto;

import com.likelion12th.pioneer_2ne1.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class FoodDiaryDto {
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private String eatingType;
    private String menuName;
    private String photoUrl;
    private String eatingWith;
    private String eatingWhere;
    private String feeling;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getEatingType() {
        return eatingType;
    }

    public void setEatingType(String eatingType) {
        this.eatingType = eatingType;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getEatingWith() {
        return eatingWith;
    }

    public void setEatingWith(String eatingWith) {
        this.eatingWith = eatingWith;
    }

    public String getEatingWhere() {
        return eatingWhere;
    }

    public void setEatingWhere(String eatingWhere) {
        this.eatingWhere = eatingWhere;
    }

    public String getFeeling() {
        return feeling;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }
}

