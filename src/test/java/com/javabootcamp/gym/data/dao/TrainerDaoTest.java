package com.javabootcamp.gym.data.dao;

import com.javabootcamp.gym.data.IDataSource;
import com.javabootcamp.gym.data.model.Trainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrainerDaoTest {

    @Test
    void create() {
        // arrange
        Trainer expected = new Trainer(12, 2);

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.create(any(), eq(Trainer.class)))
                .thenReturn(new Trainer(1, 12, 2));

        TrainerDao dao = new TrainerDao(dataSource);

        // act
        var r = dao.create(expected);

        // assert
        assertNotNull(r);
        assertEquals(1, r.getId());
        assertEquals(expected.getUserId(), r.getUserId());
        assertEquals(expected.getSpecializationId(), r.getSpecializationId());
    }

    @Test
    void getById_returnsNotNull() {
        // arrange
        Trainer t1 = new Trainer(1, 2);

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.getById(1, Trainer.class))
                .thenReturn(t1);

        TrainerDao dao = new TrainerDao(dataSource);

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
        when(dataSource.getById(anyInt(), eq(Trainer.class)))
                .thenReturn(null);

        TrainerDao dao = new TrainerDao(dataSource);

        // act
        var r = dao.getById(2);

        // assert
        assertNull(r);
    }

    @Test
    void update_returnsTrue() {
        // arrange
        Trainer t = new Trainer(1, 1, 2);

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.update(any(), eq(Trainer.class)))
                .thenReturn(true);

        TrainerDao dao = new TrainerDao(dataSource);

        // act
        var r = dao.update(t);

        // assert
        assertTrue(r);
    }

    @Test
    void update_returnsFalse() {
        // arrange
        Trainer t = new Trainer(1, 1, 2);

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.update(any(), eq(Trainer.class)))
                .thenReturn(false);

        TrainerDao dao = new TrainerDao(dataSource);

        // act
        var r = dao.update(t);

        // assert
        assertFalse(r);
    }

    @Test
    void delete_returnsTrue() {
        Trainer t = new Trainer(1, 1, 1);

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.delete(any(), eq(Trainer.class)))
                .thenReturn(true);

        TrainerDao dao = new TrainerDao(dataSource);

        // act
        var r = dao.delete(t);

        // assert
        assertTrue(r);
    }

    @Test
    void delete_returnsFalse() {
        Trainer t = new Trainer(1, 1, 1);

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.delete(any(), eq(Trainer.class)))
                .thenReturn(false);

        TrainerDao dao = new TrainerDao(dataSource);

        // act
        var r = dao.delete(t);

        // assert
        assertFalse(r);
    }

    @Test
    void exists_returnFalse() {
        // arrange
        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.getById(anyInt(), eq(Trainer.class)))
                .thenReturn(null);

        TrainerDao dao = new TrainerDao(dataSource);

        // act
        var r = dao.exists(2);

        // assert
        assertFalse(r);
    }

    @Test
    void exists_returnTrue() {
        // arrange
        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.getById(anyInt(), eq(Trainer.class)))
                .thenReturn(new Trainer(1, 1, 1));

        TrainerDao dao = new TrainerDao(dataSource);

        // act
        var r = dao.exists(1);

        // assert
        assertTrue(r);
    }
}

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TrainerDaoSpringTest {

    private final TrainerDao dao;

    @Autowired
    public TrainerDaoSpringTest(TrainerDao dao) {
        this.dao = dao;
    }

    @Test
    void create() {
        // arrange
        var t = new Trainer(1, 1);

        // act
        var r = dao.create(t);

        // assert
        assertEquals(970, r.getId());
    }

    @Test
    void getById() {
        // act
        var t = dao.getById(1);

        // assert
        assertNotNull(t);
        assertEquals(1, t.getId());
        assertEquals(809, t.getUserId());
        assertEquals(846, t.getSpecializationId());
    }
}