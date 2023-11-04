package com.javabootcamp.gym.data.dao;

import com.javabootcamp.gym.data.IDataSource;
import com.javabootcamp.gym.data.model.Training;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class TrainingDaoTest {

    @Test
    void create() {
        // arrange
        var date = new Date();
        Training expected = new Training(1, 2, 3, "Weights", date, 10);

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.create(any(), eq(Training.class)))
                .thenReturn(new Training(1, 1, 2, 3, "Weights", date, 10));

        TrainingDao dao = new TrainingDao(dataSource);

        // act
        var r = dao.create(expected);

        // assert
        assertNotNull(r);
        assertEquals(1, r.getId());
        assertEquals(expected.getTraineeId(), r.getTraineeId());
        assertEquals(expected.getTrainerId(), r.getTrainerId());
        assertEquals(expected.getTrainingTypeId(), r.getTrainingTypeId());
        assertEquals(expected.getDate(), r.getDate());
        assertEquals(expected.getName(), r.getName());
        assertEquals(expected.getDuration(), r.getDuration());
    }

    @Test
    void getById_returnsNotNull() {
        // arrange
        Training t1 = new Training(1, 2, 3, "Weights", new Date(), 10);

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.getById(1, Training.class))
                .thenReturn(t1);

        TrainingDao dao = new TrainingDao(dataSource);

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
        when(dataSource.getById(anyInt(), eq(Training.class)))
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
        Training t = new Training(1, 2, 3, "Weights", new Date(), 10);

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.update(any(), eq(Training.class)))
                .thenReturn(true);

        TrainingDao dao = new TrainingDao(dataSource);

        // act
        var r = dao.update(t);

        // assert
        assertTrue(r);
    }

    @Test
    void update_returnsFalse() {
        // arrange
        Training t = new Training(1, 2, 3, "Weights", new Date(), 10);

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.update(any(), eq(Training.class)))
                .thenReturn(false);

        TrainingDao dao = new TrainingDao(dataSource);

        // act
        var r = dao.update(t);

        // assert
        assertFalse(r);
    }

    @Test
    void delete_returnsTrue() {
        Training t = new Training(1, 2, 3, "Weights", new Date(), 10);

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.delete(any(), eq(Training.class)))
                .thenReturn(true);

        TrainingDao dao = new TrainingDao(dataSource);

        // act
        var r = dao.delete(t);

        // assert
        assertTrue(r);
    }

    @Test
    void delete_returnsFalse() {
        Training t = new Training(1, 2, 3, "Weights", new Date(), 10);

        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.delete(any(), eq(Training.class)))
                .thenReturn(false);

        TrainingDao dao = new TrainingDao(dataSource);

        // act
        var r = dao.delete(t);

        // assert
        assertFalse(r);
    }

    @Test
    void exists_returnFalse() {
        // arrange
        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.getById(anyInt(), eq(Training.class)))
                .thenReturn(null);

        TrainingDao dao = new TrainingDao(dataSource);

        // act
        var r = dao.exists(2);

        // assert
        assertFalse(r);
    }

    @Test
    void exists_returnTrue() {
        // arrange
        IDataSource dataSource = mock(IDataSource.class);
        when(dataSource.getById(anyInt(), eq(Training.class)))
                .thenReturn(new Training(1, 2, 3, "Weights", new Date(), 10));

        TrainingDao dao = new TrainingDao(dataSource);

        // act
        var r = dao.exists(1);

        // assert
        assertTrue(r);
    }
}

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TrainingDaoSpringTest {

    private final TrainingDao dao;

    @Autowired
    public TrainingDaoSpringTest(TrainingDao dao) {
        this.dao = dao;
    }

    @Test
    void create() {
        // arrange
        var t = new Training(1, 2, 3, "Weights", new Date(), 10);

        // act
        var r = dao.create(t);

        // assert
        assertEquals(1001, r.getId());
    }

    @Test
    void getById() throws ParseException {
        // act
        var formatter = new SimpleDateFormat("M/dd/yyy");
        var t = dao.getById(1);

        // assert
        assertNotNull(t);
        assertEquals(1, t.getId());
        assertEquals(389, t.getTraineeId());
        assertEquals(760, t.getTrainerId());
        assertEquals(916, t.getTrainingTypeId());
        assertEquals("Cuscutaceae", t.getName());
        assertEquals(formatter.parse("3/14/2023"), t.getDate());
        assertEquals(5, t.getDuration());
    }
}