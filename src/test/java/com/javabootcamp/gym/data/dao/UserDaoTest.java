package com.javabootcamp.gym.data.dao;

import com.javabootcamp.gym.data.IDataSource;
import com.javabootcamp.gym.data.model.User;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDaoTest {
    @Test
    void create() {
        // arrange
        User expected = new User("Andres", "Hoyos");

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.create(any(), eq(User.class)))
                .thenReturn(new User(1, "Andres", "Hoyos", "", "", true));

        UserDao dao = new UserDao(dataSource);

        // act
        var r = dao.create(expected);

        // assert
        assertNotNull(r);
        assertEquals(1, r.getId());
        assertEquals(expected.getFirstName(), r.getFirstName());
        assertEquals(expected.getLastName(), r.getLastName());
    }

    @Test
    void getById_returnsNotNull() {
        // arrange
        User u = new User(1, "Andres", "Hoyos", "", "", true);

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.getById(1, User.class))
                .thenReturn(u);

        UserDao dao = new UserDao(dataSource);

        // act
        var r = dao.getById(1);

        // assert
        assertNotNull(r);
        assertSame(u, r);
    }

    @Test
    void getById_returnsNull() {
        // arrange
        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.getById(anyInt(), eq(User.class)))
                .thenReturn(null);

        TrainingDao dao = new TrainingDao(dataSource);

        // act
        var r = dao.getById(2);

        // assert
        assertNull(r);
    }

    @Test
    void update_returnsTrue() {
        // arrange
        User u = new User(1, "Andres", "Hoyos", "", "", true);

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.update(any(), eq(User.class)))
                .thenReturn(true);

        UserDao dao = new UserDao(dataSource);

        // act
        var r = dao.update(u);

        // assert
        assertTrue(r);
    }

    @Test
    void update_returnsFalse() {
        // arrange
        User u = new User(1, "Andres", "Hoyos", "", "", true);

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.update(any(), eq(User.class)))
                .thenReturn(false);

        UserDao dao = new UserDao(dataSource);

        // act
        var r = dao.update(u);

        // assert
        assertFalse(r);
    }

    @Test
    void delete_returnsTrue() {
        User u = new User(1, "Andres", "Hoyos", "", "", true);

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.delete(any(), eq(User.class)))
                .thenReturn(true);

        UserDao dao = new UserDao(dataSource);

        // act
        var r = dao.delete(u);

        // assert
        assertTrue(r);
    }

    @Test
    void delete_returnsFalse() {
        User u = new User(1, "Andres", "Hoyos", "", "", true);

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.delete(any(), eq(User.class)))
                .thenReturn(false);

        UserDao dao = new UserDao(dataSource);

        // act
        var r = dao.delete(u);

        // assert
        assertFalse(r);
    }

    @Test
    void exists_returnFalse() {
        // arrange
        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.getById(anyInt(), eq(User.class)))
                .thenReturn(null);

        UserDao dao = new UserDao(dataSource);

        // act
        var r = dao.exists(2);

        // assert
        assertFalse(r);
    }

    @Test
    void exists_returnTrue() {
        // arrange
        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.getById(anyInt(), eq(User.class)))
                .thenReturn(new User(1, "Andres", "Hoyos", "", "", true));

        UserDao dao = new UserDao(dataSource);

        // act
        var r = dao.exists(1);

        // assert
        assertTrue(r);
    }

    @Test
    void count_returnsZero() {
        // arrange
        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.search(any(), eq(User.class)))
                .thenCallRealMethod();

        UserDao dao = new UserDao(dataSource);

        // act
        var count = dao.count(ignored -> true);

        // assert
        assertEquals(0, count);
    }

    @Test
    void count() {
        // arrange
        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.search(any(), eq(User.class)))
                .thenReturn(Stream.of(new User("Andres", "Hoyos")));

        UserDao dao = new UserDao(dataSource);

        // act
        var count = dao.count(p -> true);

        // assert
        assertEquals(1, count);
    }
}