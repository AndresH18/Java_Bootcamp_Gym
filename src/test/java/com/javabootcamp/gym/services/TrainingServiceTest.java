//package com.javabootcamp.gym.services;
//
//import com.javabootcamp.gym.data.dao.TrainingDao;
//import com.javabootcamp.gym.data.model.Training;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class TrainingServiceTest {
//    @Test
//    void create_validIds() {
//        // arrange
//        LocalDate date = LocalDate.now().minusYears(1);
//
//        TrainingDao mock = mock(TrainingDao.class);
//        when(mock.create(any(Training.class))).thenAnswer(a -> {
//            Training t = a.getArgument(0);
//            t.setId(1);
//            return t;
//        });
//
//        TrainingService service = new TrainingService(mock);
//
//        // act
//        var r = service.create(2, 3, 4, "Weights", 10, date);
//
//        // assert
//        assertNotNull(r);
//        assertEquals(1, r.getId());
//        assertEquals(2, r.getTraineeId());
//        assertEquals(3, r.getTrainerId());
//        assertEquals(4, r.getTrainingTypeId());
//        assertEquals("Weights", r.getName());
//        assertEquals(10, r.getDuration());
//        assertEquals(date, r.getDate());
//    }
//
//    @Test
//    void create_inValidIds() {
//        // arrange
//        LocalDate date = LocalDate.now().minusYears(1);
//
//        TrainingDao mock = mock(TrainingDao.class);
//        when(mock.create(any(Training.class))).thenAnswer(a -> {
//            Training t = a.getArgument(0);
//            t.setId(1);
//            return t;
//        });
//
//        TrainingService service = new TrainingService(mock);
//
//        // act
//        var r = service.create(-1, 0, 1, "Weights", 10, date);
//
//        // assert
//        assertNull(r);
//    }
//
//    @Test
//    void create_inValidDate() {
//        // arrange
//        LocalDate date = LocalDate.now().plusDays(1);
//
//        TrainingDao mock = mock(TrainingDao.class);
//        when(mock.create(any(Training.class))).thenAnswer(a -> {
//            Training t = a.getArgument(0);
//            t.setId(1);
//            return t;
//        });
//
//        TrainingService service = new TrainingService(mock);
//
//        // act
//        var r = service.create(2, 3, 4, "Weights", 10, date);
//
//        // assert
//        assertNull(r);
//    }
//
//
//    @Test
//    void getById() {
//        // arrange
//        TrainingDao mock = mock(TrainingDao.class);
//        when(mock.getById(anyInt())).thenReturn(new Training(1, 1, 1, 1, "Weights", LocalDate.now(), 1));
//
//        TrainingService service = new TrainingService(mock);
//
//        // act
//        var r = service.getById(1);
//
//        // assert
//        assertNotNull(r);
//        verify(mock).getById(anyInt());
//    }
//
//    @Test
//    void getById_invalidId_returnsNull() {
//        // arrange
//        TrainingDao mock = mock(TrainingDao.class);
//
//        TrainingService service = new TrainingService(mock);
//
//        // act
//        var r = service.getById(-1);
//
//        // assert
//        assertNull(r);
//        verifyNoInteractions(mock);
//    }
//}