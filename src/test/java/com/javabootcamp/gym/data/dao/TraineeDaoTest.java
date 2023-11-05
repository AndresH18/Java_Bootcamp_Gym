package com.javabootcamp.gym.data.dao;

import com.javabootcamp.gym.data.IDataSource;
import com.javabootcamp.gym.data.model.Trainee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class TraineeDaoTest {

    @Test
    void create() {
        // arrange
        var date = LocalDate.now();

        Trainee expected = new Trainee(12, date, "Address");

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.create(any(), eq(Trainee.class)))
                .thenReturn(new Trainee(1, 12, date, "Address"));

        TraineeDao dao = new TraineeDao(dataSource);

        // act
        var r = dao.create(expected);

        // assert
        assertNotNull(r);
        assertEquals(1, r.getId());
        assertEquals(expected.getUserId(), r.getUserId());
        assertEquals(expected.getDateOfBirth(), r.getDateOfBirth());
        assertEquals(expected.getAddress(), r.getAddress());
    }

    @Test
    void getById_returnsNotNull() {
        // arrange
        Trainee t1 = new Trainee(1, 2, LocalDate.now(), "Address");

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.getById(anyInt(), eq(Trainee.class)))
                .thenReturn(t1);

        TraineeDao dao = new TraineeDao(dataSource);

        // act
        var r = dao.getById(1);

        // assert
        assertNotNull(r);
        assertSame(t1, r);
    }

    @Test
    void getById_returnsNull() {
        // arrange
        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.getById(anyInt(), eq(Trainee.class)))
                .thenReturn(null);

        TraineeDao dao = new TraineeDao(dataSource);

        // act
        var r = dao.getById(2);

        // assert
        assertNull(r);
    }

    @Test
    void update_returnsTrue() {
        // arrange
        var date = LocalDate.now();
        Trainee t1 = new Trainee(12, date, "Address");

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.update(any(), eq(Trainee.class)))
                .thenReturn(true);

        TraineeDao dao = new TraineeDao(dataSource);

        // act
        var r = dao.update(t1);

        // assert
        assertTrue(r);
    }

    @Test
    void update_returnsFalse() {
        // arrange
        var date = LocalDate.now();
        Trainee t1 = new Trainee(12, date, "Address");

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.update(any(), eq(Trainee.class)))
                .thenReturn(false);

        TraineeDao dao = new TraineeDao(dataSource);

        // act
        var r = dao.update(t1);

        // assert
        assertFalse(r);
    }

    @Test
    void delete_returnsTrue() {
        Trainee t1 = new Trainee(12, LocalDate.now(), "Address");

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.delete(any(), eq(Trainee.class)))
                .thenReturn(true);

        TraineeDao dao = new TraineeDao(dataSource);

        // act
        var r = dao.delete(t1);

        // assert
        assertTrue(r);
    }

    @Test
    void delete_returnsFalse() {
        Trainee t1 = new Trainee(12, LocalDate.now(), "Address");

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.delete(any(), eq(Trainee.class)))
                .thenReturn(false);

        TraineeDao dao = new TraineeDao(dataSource);

        // act
        var r = dao.delete(t1);

        // assert
        assertFalse(r);
    }

    @Test
    void exists_returnFalse() {
        // arrange
        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.getById(anyInt(), eq(Trainee.class)))
                .thenReturn(null);

        TraineeDao dao = new TraineeDao(dataSource);

        // act
        var r = dao.exists(2);

        // assert
        assertFalse(r);
    }

    @Test
    void exists_returnTrue() {
        // arrange
        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.getById(anyInt(), eq(Trainee.class)))
                .thenReturn(new Trainee(1, 1, LocalDate.now(), "Address"));

        TraineeDao dao = new TraineeDao(dataSource);

        // act
        var r = dao.exists(2);

        // assert
        assertTrue(r);
    }


}

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TraineeDaoSpringTest {

    private final TraineeDao dao;

    @Autowired
    public TraineeDaoSpringTest(TraineeDao dao) {
        this.dao = dao;
    }

    @Test
    void create() {
        // arrange
        var t = new Trainee(1, LocalDate.now(), "Somewhere 123");

        // act
        var r = dao.create(t);

        // assert
        assertEquals(1001, r.getId());
    }

    @Test
    void getById() {
        // act
        var t = dao.getById(1);

        // assert
        assertNotNull(t);
        assertEquals(1, t.getId());
        assertEquals("17213 Dottie Terrace", t.getAddress());
    }
}