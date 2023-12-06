package com.javabootcamp.gym.data.dto;

import com.javabootcamp.gym.data.model.TrainingType;

import java.util.List;

public record TrainingTypeDto(int id, String name) {

    public static TrainingTypeDto convert(TrainingType trainingType) {
        return new TrainingTypeDto(trainingType.getId(), trainingType.getName());
    }

    public static List<TrainingTypeDto> convert(List<TrainingType> trainingTypeList) {
        return trainingTypeList.stream().map(TrainingTypeDto::convert).toList();
    }
}
