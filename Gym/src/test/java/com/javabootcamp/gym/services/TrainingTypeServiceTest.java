package com.javabootcamp.gym.services;

import com.javabootcamp.gym.data.model.TrainingType;
import com.javabootcamp.gym.data.repository.TrainingTypeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingTypeServiceTest {

    private static List<TrainingType> trainingTypeList;
    private static TrainingTypeRepository repository;
    private TrainingTypeService service;

    @BeforeAll
    public static void beforeAll() {
        trainingTypeList = List.of(
                new TrainingType(1, "1"),
                new TrainingType(2, "2"),
                new TrainingType(3, "3"),
                new TrainingType(4, "4"),
                new TrainingType(5, "5"),
                new TrainingType(6, "6"),
                new TrainingType(7, "7"),
                new TrainingType(8, "8"),
                new TrainingType(9, "9"),
                new TrainingType(10, "10"),
                new TrainingType(11, "11"),
                new TrainingType(12, "12"),
                new TrainingType(13, "13"),
                new TrainingType(14, "14"),
                new TrainingType(15, "15"),
                new TrainingType(16, "16"),
                new TrainingType(17, "17"),
                new TrainingType(18, "18"),
                new TrainingType(19, "19"),
                new TrainingType(20, "20"),
                new TrainingType(21, "21"),
                new TrainingType(22, "22"),
                new TrainingType(23, "23"),
                new TrainingType(24, "24"),
                new TrainingType(25, "25"),
                new TrainingType(26, "26"),
                new TrainingType(27, "27"),
                new TrainingType(28, "28"),
                new TrainingType(29, "29"),
                new TrainingType(30, "30")
        );
    }

    @BeforeEach
    public void beforeEach() {
        repository = mock(TrainingTypeRepository.class);
        when(repository.findAll(any(Pageable.class))).thenAnswer(findAllAnswer());

        service = new TrainingTypeService(repository);
    }

    @Test
    void getAll_validInputs_returnsList() {
        // arrange
        int page = 1;
        int size = 5;

        // act
        var r = service.getAll(page, size);

        // assert
        assertNotNull(r);
        assertTrue(r.isPresent());
        assertEquals(size, r.get().size());
        assertEquals(1, r.get().get(0).getId());
        assertEquals(5, r.get().get(4).getId());
    }

    @Test
    void getAll_invalidPage_returnsList() {
        // arrange
        int page = -1;
        int size = 5;

        // act
        var r = service.getAll(page, size);

        // assert
        assertNotNull(r);
        assertTrue(r.isPresent());
        assertEquals(size, r.get().size());
        assertEquals(1, r.get().get(0).getId());
        assertEquals(5, r.get().get(4).getId());
    }

    @Test
    void getAll_invalidSize_returnsList() {
        // arrange
        int page = 1;
        int size = -1;

        // act
        var r = service.getAll(page, size);

        // assert
        assertNotNull(r);
        assertTrue(r.isPresent());
        assertEquals(10, r.get().size());
        assertEquals(1, r.get().get(0).getId());
        assertEquals(10, r.get().get(9).getId());
    }

    @Test
    void getAll_exception_returnsOptionalEmpty() {
        // arrange
        when(repository.findAll(any(Pageable.class))).thenThrow(new RuntimeException());

        // act
        var r = service.getAll(1, 1);

        // assert
        assertNotNull(r);
        assertTrue(r.isEmpty());
    }

    private static Answer<Page<TrainingType>> findAllAnswer() {
        return a -> {
            Pageable pageable = a.getArgument(0);

            var trainings = trainingTypeList.stream().skip(pageable.getOffset()).limit(pageable.getPageSize()).toList();

            Page<TrainingType> page = new PageImpl<>(trainings);

            return page;
        };
    }
}