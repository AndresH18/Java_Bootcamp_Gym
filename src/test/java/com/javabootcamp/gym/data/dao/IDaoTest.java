package com.javabootcamp.gym.data.dao;

import com.javabootcamp.gym.data.model.IModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IDaoTest {
    @Test
    @SuppressWarnings("unchecked")
    void count_returnsZero() {
        // arrange
        IDao<Model> mock = (IDao<Model>) mock(IDao.class);
        when(mock.count(any())).thenCallRealMethod();

        // act
        var count = mock.count(ignored -> true);

        // assert
        assertEquals(0, count);
    }

    @Test
    @SuppressWarnings("unchecked")
    void count() {
        // arrange
        IDao<Model> mock = (IDao<Model>) mock(IDao.class);
        when(mock.count(any()))
                .thenReturn(10L);

        // act
        var count = mock.count(p -> true);

        // assert
        assertEquals(10L, count);
    }

    private static class Model implements IModel {
        private int id;

        public Model(int id) {
            this.id = id;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public void setId(int id) {
            this.id = id;
        }
    }
}