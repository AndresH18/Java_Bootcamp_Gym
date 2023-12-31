package com.javabootcamp.gym.data.repository;

import com.javabootcamp.gym.data.model.Training;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Integer> {

    @Query("""
            select t from Training t
                join t.trainee tr
                join t.trainer trn
            where tr.user.username = :username
                and (:dateFrom is null or t.date >= :dateFrom)
                and (:dateTo is null or t.date <= :dateTo)
                and (:trainerUsername is null or trn.user.username = :trainerUsername)
                and (:trainingTypeName is null or t.trainingType = :trainingTypeName)
            """)
    List<Training> getTraineeTrainings(@NotNull @Param("username") String username,
                                       @Nullable LocalDate dateFrom,
                                       @Nullable LocalDate dateTo,
                                       @Nullable String trainingTypeName,
                                       @Nullable String trainerUsername);

    @Query("""
            select t from Training t
                join t.trainee tr
                join t.trainer trn
            where trn.user.username = :username
                and (:dateFrom is null or t.date >= :dateFrom)
                and (:dateTo is null or t.date <= :dateTo)
                and (:traineeUsername is null or trn.user.username = :traineeUsername)
                and (:trainingTypeName is null or t.trainingType = :trainingTypeName)
            """)
    List<Training> getTrainerTrainings(@NotNull @Param("username") String username,
                                       @Nullable LocalDate dateFrom,
                                       @Nullable LocalDate dateTo,
                                       @Nullable String trainingTypeName,
                                       @Nullable String traineeUsername);
}
