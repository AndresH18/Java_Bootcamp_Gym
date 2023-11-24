//package com.javabootcamp.gym.services;
//
//import com.javabootcamp.gym.data.dao.TraineeDao;
//import com.javabootcamp.gym.data.dao.UserDao;
//import com.javabootcamp.gym.data.model.Trainee;
//import com.javabootcamp.gym.data.model.User;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.concurrent.atomic.AtomicReference;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.*;
//
//class TraineeServiceTest {
//    @Test
//    void create_invalidId_returnsNull() {
//        // arrange
//        TraineeDao traineeDao = mock(TraineeDao.class);
//        UserDao userDao = mock(UserDao.class);
//
//        TraineeService service = new TraineeService(traineeDao, userDao);
//
//        // act
//        var r = service.create(-1, LocalDate.now(), "Address");
//
//        assertNull(r);
//        verifyNoInteractions(traineeDao);
//        verifyNoInteractions(userDao);
//    }
//
//    @Test
//    void create_notExists_returnsNull() {
//        // arrange
//        TraineeDao traineeDao = mock(TraineeDao.class);
//        UserDao userDao = mock(UserDao.class);
//        when(userDao.exists(anyInt())).thenReturn(false);
//
//        TraineeService service = new TraineeService(traineeDao, userDao);
//
//        // act
//        var r = service.create(1, LocalDate.now().minusDays(1), "Address");
//
//        // assert
//        assertNull(r);
//        verifyNoInteractions(traineeDao);
//        verify(userDao, atMostOnce()).exists(anyInt());
//        verify(userDao, never()).create(any());
//    }
//
//    @Test
//    void create_exists_returnsNotNull() {
//        // arrange
//        UserDao userDao = mock(UserDao.class);
//        when(userDao.exists(anyInt())).thenReturn(true);
//
//        TraineeDao traineeDao = mock(TraineeDao.class);
//        when(traineeDao.create(any(Trainee.class)))
//                .thenReturn(new Trainee(1, 1, LocalDate.now(), "Address"));
//
//        TraineeService service = new TraineeService(traineeDao, userDao);
//
//        // act
//        var r = service.create(1, LocalDate.now().minusDays(1), "Address");
//
//        // assert
//        assertNotNull(r);
//        verify(userDao, atMostOnce()).exists(anyInt());
//        verify(userDao, atMostOnce()).create(any());
//    }
//
//    @Test
//    void create_newUser() {
//        // arrange
//        String firstName = "Andres";
//        String lastName = "Hoyos";
//        var count = 2L;
//        String userName = firstName.toLowerCase() + "." + lastName.toLowerCase() + count;
//        var userId = 50;
//        var traineeId = 10;
//        // This is needed to allow lambda to assign value to variable outside its scope
//        AtomicReference<User> u = new AtomicReference<>();
//
//        UserDao userDao = mock(UserDao.class);
//        when(userDao.count(any()))
//                .thenReturn(count);
//        when(userDao.create(any(User.class)))
//                .thenAnswer(a -> {
//                    User user = a.getArgument(0);
//                    user.setId(userId);
//                    user.setActive(true);
//
//                    u.set(user);
//
//                    return user;
//                });
//
//        TraineeDao traineeDao = mock(TraineeDao.class);
//        when(traineeDao.create(any(Trainee.class)))
//                .thenAnswer(a -> {
//                    Trainee t = a.getArgument(0);
//                    t.setId(traineeId);
//                    return t;
//                });
//
//        TraineeService service = new TraineeService(traineeDao, userDao);
//
//        // act
//        var r = service.create("Andres", "Hoyos", LocalDate.now().minusDays(1), "Address");
//
//        // assert
//        assertNotNull(r);
//        assertEquals(traineeId, r.getId());
//
//        assertEquals(firstName, u.get().getFirstName());
//        assertEquals(lastName, u.get().getLastName());
//        assertEquals(userName, u.get().getUsername());
//        assertEquals(10, u.get().getPassword().length());
//    }
//
//    @Test
//    void getById_invalidId_returnsNull() {
//        // arrange
//        UserDao userDao = mock(UserDao.class);
//        TraineeDao traineeDao = mock(TraineeDao.class);
//
//        TraineeService service = new TraineeService(traineeDao, userDao);
//
//        // act
//        var r = service.getById(-1);
//
//        // assert
//        assertNull(r);
//        verifyNoInteractions(userDao);
//        verifyNoInteractions(traineeDao);
//    }
//
//    @Test
//    void getById_validId_returnsNotNull() {
//        // arrange
//        UserDao userDao = mock(UserDao.class);
//        TraineeDao traineeDao = mock(TraineeDao.class);
//        when(traineeDao.getById(anyInt()))
//                .thenReturn(new Trainee(1, LocalDate.now().minusDays(1), "Address"));
//
//        TraineeService service = new TraineeService(traineeDao, userDao);
//
//        // act
//        var r = service.getById(1);
//
//        // assert
//        assertNotNull(r);
//        verify(traineeDao).getById(anyInt());
//        verifyNoInteractions(userDao);
//    }
//
//    @Test
//    void update_invalidId_returnsFalse() {
//        // arrange
//        UserDao userDao = mock(UserDao.class);
//        TraineeDao traineeDao = mock(TraineeDao.class);
//
//        TraineeService service = new TraineeService(traineeDao, userDao);
//
//        // act
//        var r = service.update(new Trainee(-1, LocalDate.now(), "Address"));
//
//        // assert
//        assertFalse(r);
//        verifyNoInteractions(userDao);
//        verifyNoInteractions(traineeDao);
//    }
//
//    @Test
//    void update_validId_returnsTrue() {
//        // arrange
//        UserDao userDao = mock(UserDao.class);
//        TraineeDao traineeDao = mock(TraineeDao.class);
//        when(traineeDao.update(any(Trainee.class)))
//                .thenReturn(true);
//
//        TraineeService service = new TraineeService(traineeDao, userDao);
//
//        // act
//        var r = service.update(new Trainee(1, 1, LocalDate.now(), "Address"));
//
//        // assert
//        assertTrue(r);
//        verify(traineeDao).update(any());
//        verifyNoInteractions(userDao);
//    }
//
//    @Test
//    void delete_notExists_returnsFalse() {
//        // arrange
//        UserDao userDao = mock(UserDao.class);
//
//        TraineeDao traineeDao = mock(TraineeDao.class);
//        when(traineeDao.getById(anyInt()))
//                .thenReturn(null);
//
//        TraineeService service = new TraineeService(traineeDao, userDao);
//
//        // act
//        var r = service.delete(2);
//
//        // assert
//        assertFalse(r);
//        verify(traineeDao).getById(anyInt());
//        verifyNoInteractions(userDao);
//    }
//
//    @Test
//    void delete_exists_returnsTrue() {
//        // arrange
//        Trainee t = new Trainee(1, 1, LocalDate.now(), "Address");
//
//        UserDao userDao = mock(UserDao.class);
//
//        TraineeDao traineeDao = mock(TraineeDao.class);
//        when(traineeDao.getById(anyInt()))
//                .thenReturn(t);
//        when(traineeDao.delete(any(Trainee.class)))
//                .thenReturn(true);
//
//        TraineeService service = new TraineeService(traineeDao, userDao);
//
//        // act
//        var r = service.delete(1);
//
//        // assert
//        assertTrue(r);
//        verify(traineeDao).getById(anyInt());
//        verifyNoInteractions(userDao);
//    }
//
//}