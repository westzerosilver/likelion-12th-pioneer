package com.likelion12th.pioneer_2ne1.service;

import com.likelion12th.pioneer_2ne1.dto.FoodDetailDto;
import com.likelion12th.pioneer_2ne1.entity.FoodComplete;
import com.likelion12th.pioneer_2ne1.entity.FoodDiary;
import com.likelion12th.pioneer_2ne1.repository.FoodCompleteRepository;
import com.likelion12th.pioneer_2ne1.repository.FoodDiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FoodDetailService {
    @Autowired
    private FoodDiaryRepository foodDiaryRepository;

    @Autowired
    private FoodCompleteRepository foodCompleteRepository;

    @Value("${uploadPath}")
    private String uploadPath;

    public FoodDetailDto getFoodDiaryDetailById(LocalDate date, Long id) {
        Optional<FoodDiary> foodDiary = foodDiaryRepository.findByIdAndDate(id, date);
        if (foodDiary.isPresent()) {
            Optional<FoodComplete> foodComplete = foodCompleteRepository.findById(id);
            if (foodComplete.isPresent()) {
                return convertToDetailDto(foodDiary.get(), foodComplete.get());
            }
        }
        return null;
    }

    public FoodDetailDto updateFoodDiary(LocalDate date, Long id, FoodDetailDto foodDetailDto, MultipartFile photoFile) throws IOException {
        Optional<FoodDiary> optionalFoodDiary = foodDiaryRepository.findByIdAndDate(id, date);
        Optional<FoodComplete> optionalFoodComplete = foodCompleteRepository.findById(id);

        if (optionalFoodDiary.isPresent() && optionalFoodComplete.isPresent()) {
            FoodDiary foodDiary = optionalFoodDiary.get();
            FoodComplete foodComplete = optionalFoodComplete.get();

            foodDiary.setDate(date);
            foodComplete.setStartEatingTime(parseTime(foodDetailDto.getStartEatingTime()));
            foodComplete.setEndEatingTime(parseTime(foodDetailDto.getEndEatingTime()));
            foodDiary.setEatingType(FoodDiary.EatingType.valueOf(foodDetailDto.getEatingType()));
            foodDiary.setMenuName(foodDetailDto.getMenuName());
            if (photoFile != null && !photoFile.isEmpty()) {
                UUID uuid = UUID.randomUUID();
                String fileName = uuid.toString() + "_" + photoFile.getOriginalFilename();
                File itemImgFile = new File(uploadPath, fileName);
                photoFile.transferTo(itemImgFile);
                foodDiary.setPhotoUrl(fileName);
                foodDiary.setPhotoUrlPath(uploadPath + "/" + fileName);
            }
            foodDiary.setEatingWith(FoodDiary.EatingWith.valueOf(foodDetailDto.getEatingWith()));
            foodDiary.setEatingWithOther(foodDetailDto.getEatingWithOther());
            foodDiary.setEatingWhere(FoodDiary.EatingWhere.valueOf(foodDetailDto.getEatingWhere()));
            foodDiary.setEatingWhereOther(foodDetailDto.getEatingWhereOther());
            foodDiary.setFeeling(FoodDiary.Feeling.valueOf(foodDetailDto.getFeeling()));

            foodComplete.setAfterfeeling(FoodComplete.Afterfeeling.valueOf(foodDetailDto.getAfterFeeling()));
            foodComplete.setSymptoms(parseSymptoms(foodDetailDto.getSymptoms()));
            foodComplete.setMemo(foodDetailDto.getMemo());

            FoodDiary updatedFoodDiary = foodDiaryRepository.save(foodDiary);
            foodCompleteRepository.save(foodComplete);

            return convertToDetailDto(updatedFoodDiary, foodComplete);
        }
        return null;
    }

    public boolean deleteFoodDiary(LocalDate date, Long id) {
        Optional<FoodDiary> optionalFoodDiary = foodDiaryRepository.findByIdAndDate(id, date);
        Optional<FoodComplete> optionalFoodComplete = foodCompleteRepository.findById(id);

        if (optionalFoodDiary.isPresent() && optionalFoodComplete.isPresent()) {
            foodDiaryRepository.deleteById(id);
            foodCompleteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private FoodDetailDto convertToDetailDto(FoodDiary foodDiary, FoodComplete foodComplete) {
        FoodDetailDto foodDetailDto = new FoodDetailDto();
        foodDetailDto.setId(foodDiary.getId());
        foodDetailDto.setDate(foodDiary.getDate());
        foodDetailDto.setEatingType(foodDiary.getEatingType().name());
        foodDetailDto.setMenuName(foodDiary.getMenuName());
        foodDetailDto.setPhotoUrl(foodDiary.getPhotoUrl());
        foodDetailDto.setEatingWith(foodDiary.getEatingWith().name());
        foodDetailDto.setEatingWithOther(foodDiary.getEatingWithOther());
        foodDetailDto.setEatingWhere(foodDiary.getEatingWhere().name());
        foodDetailDto.setEatingWhereOther(foodDiary.getEatingWhereOther());
        foodDetailDto.setFeeling(foodDiary.getFeeling().name());

        if (foodComplete.getStartEatingTime() != null) {
            foodDetailDto.setStartEatingTime(foodComplete.getStartEatingTime().toString());
        }
        if (foodComplete.getEndEatingTime() != null) {
            foodDetailDto.setEndEatingTime(foodComplete.getEndEatingTime().toString());
        }
        if (foodComplete.getAfterfeeling() != null) {
            foodDetailDto.setAfterFeeling(foodComplete.getAfterfeeling().name());
        }
        if (foodComplete.getSymptoms() != null) {
            foodDetailDto.setSymptoms(foodComplete.getSymptoms().stream()
                    .map(FoodComplete.Symptom::name)
                    .collect(Collectors.toSet()));
        }
        foodDetailDto.setMemo(foodComplete.getMemo());

        return foodDetailDto;
    }

    private LocalTime parseTime(String time) {
        return time != null ? LocalTime.parse(time) : null;
    }

    private Set<FoodComplete.Symptom> parseSymptoms(Set<String> symptoms) {
        if (symptoms != null && !symptoms.isEmpty()) {
            return symptoms.stream()
                    .map(String::trim)
                    .map(FoodComplete.Symptom::valueOf)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }
}
