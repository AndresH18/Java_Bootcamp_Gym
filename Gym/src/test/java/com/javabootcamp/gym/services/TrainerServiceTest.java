//package com.javabootcamp.gym.services;
//
//import com.javabootcamp.gym.data.dao.TrainerDao;
//import com.javabootcamp.gym.data.dao.UserDao;
//import com.javabootcamp.gym.data.model.Trainer;
//import com.javabootcamp.gym.data.model.User;
//import org.junit.jupiter.api.Test;
//
//import java.util.concurrent.atomic.AtomicReference;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class TrainerServiceTest {
//
//    @Test
//    void create_invalidIds_returnsNull() {
//        // arrange
//        TrainerDao trainerDao = mock(TrainerDao.class);
//        UserDao userDao = mock(UserDao.class);
//
//        TrainerService service = new TrainerService(trainerDao, userDao);
//
//        // act
//        var r = service.create(-1, -1);
//
//        assertNull(r);
//        verifyNoInteractions(trainerDao);
//        verifyNoInteractions(userDao);
//    }
//
//    @Test
//    void create_notExists_returnsNull() {
//        // arrange
//        TrainerDao trainerDao = mock(TrainerDao.class);
//        UserDao userDao = mock(UserDao.class);
//        when(userDao.exists(anyInt())).thenReturn(false);
//
//        TrainerService service = new TrainerService(trainerDao, userDao);
//
//        // act
//        var r = service.create(1, 1);
//
//        // assert
//        assertNull(r);
//        verifyNoInteractions(trainerDao);
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
//        TrainerDao trainerDao = mock(TrainerDao.class);
//        when(trainerDao.create(any(Trainer.class)))
//                .thenReturn(new Trainer(2, 1));
//
//        TrainerService service = new TrainerService(trainerDao, userDao);
//
//        // act
//        var r = service.create(1, 1);
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
//        var trainerId = 10;
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
//        TrainerDao trainerDao = mock(TrainerDao.class);
//        when(trainerDao.create(any(Trainer.class)))
//                .thenAnswer(a -> {
//                    Trainer t = a.getArgument(0);
//                    t.setId(trainerId);
//                    return t;
//                });
//
//        TrainerService service = new TrainerService(trainerDao, userDao);
//
//        // act
//        var r = service.create("Andres", "Hoyos", 1);
//
//        // assert
//        assertNotNull(r);
//        assertEquals(trainerId, r.getId());
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
//        TrainerDao trainerDao = mock(TrainerDao.class);
//
//        TrainerService service = new TrainerService(trainerDao, userDao);
//
//        // act
//        var r = service.getById(-1);
//
//        // assert
//        assertNull(r);
//        verifyNoInteractions(userDao);
//        verifyNoInteractions(trainerDao);
//    }
//
//    @Test
//    void getById_validId_returnsNotNull() {
//        // arrange
//        UserDao userDao = mock(UserDao.class);
//        TrainerDao trainerDao = mock(TrainerDao.class);
//        when(trainerDao.getById(anyInt())).thenReturn(new Trainer(1, 1, 1));
//
//        TrainerService service = new TrainerService(trainerDao, userDao);
//
//        // act
//        var r = service.getById(1);
//
//        // assert
//        assertNotNull(r);
//        verify(trainerDao).getById(anyInt());
//        verifyNoInteractions(userDao);
//    }
//
//    @Test
//    void update_invalidId_returnsFalse() {
//        // arrange
//        UserDao userDao = mock(UserDao.class);
//        TrainerDao trainerDao = mock(TrainerDao.class);
//
//        TrainerService service = new TrainerService(trainerDao, userDao);
//
//        // act
//        var r = service.update(new Trainer(-1, -1, -1));
//
//        // assert
//        assertFalse(r);
//        verifyNoInteractions(userDao);
//        verifyNoInteractions(trainerDao);
//    }
//
//    @Test
//    void update_validId_returnsTrue() {
//        // arrange
//        UserDao userDao = mock(UserDao.class);
//        TrainerDao trainerDao = mock(TrainerDao.class);
//        when(trainerDao.update(any(Trainer.class)))
//                .thenReturn(true);
//
//        TrainerService service = new TrainerService(trainerDao, userDao);
//
//        // act
//        var r = service.update(new Trainer(1, 1, 1));
//
//        // assert
//        assertTrue(r);
//        verify(trainerDao).update(any());
//        verifyNoInteractions(userDao);
//    }
//}