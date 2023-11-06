package com.javabootcamp.gym.data;

import com.javabootcamp.gym.data.model.IModel;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class IDataSourceTest {

    @Test
    void search_returnsEmptyStream() {
        // arrange
        var mock = mock(IDataSource.class);
        when(mock.search(any(), eq(IModel.class)))
                .thenCallRealMethod();

        // act
        var stream = mock.search(p -> true, IModel.class);

        // assert
        assertNotNull(stream);
        assertEquals(0, stream.count());
    }

    @Test
    void search_returnsStream() {
        // arrange
        Model[] collection = new Model[]{new Model(1), new Model(2), new Model(3)};
        var mock = mock(IDataSource.class);
        when(mock.search(any(), eq(IModel.class)))
                .thenReturn(Stream.of(collection));

        // act
        var stream = mock.search(p -> true, IModel.class);

        // assert
        assertNotNull(stream);
        assertEquals(3, stream.count());
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