package com.javabootcamp.gym.data;

import static com.javabootcamp.gym.data.IDataSource.MemoryModels.*;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class InMemoryDataSourceTest {

    @Test
    void create_canAdd_True() {
        // arrange
        var source = new HashMap<Class<? extends IModel>, Set<?>>();
        var dataSource = new InMemoryDataSource(source);
        var m1 = new ModelType1();
        var m2 = new ModelType1();

        // act
        dataSource.create(m1, ModelType1.class);
        dataSource.create(m2, ModelType1.class);

        var set = source.get(ModelType1.class);

        // assert
        assertEquals(2, set.size());
    }

    @Test
    void create_setsId() {
        // arrange
        var dataSource = new InMemoryDataSource();
        var m1 = new ModelType1();
        var m2 = new ModelType1();

        // act
        var r1 = dataSource.create(m1, ModelType1.class);
        var r2 = dataSource.create(m2, ModelType1.class);

        // assert
        assertEquals(1, r1.getId());
        assertEquals(2, r2.getId());
    }

    @Test
    void create_newItem_returnsNotNull() {

    }

    @Test
    void create_canAddMultipleType_True() {
        // arrange
        var source = new HashMap<Class<? extends IModel>, Set<?>>();
        var dataSource = new InMemoryDataSource(source);
        var m1 = new ModelType1();
        var m2 = new ModelType2();

        // act
        dataSource.create(m1, ModelType1.class);
        dataSource.create(m2, ModelType2.class);

        var keys = source.keySet();

        // assert
        assertTrue(keys.contains(ModelType1.class));
        assertTrue(keys.contains(ModelType2.class));
    }


    @Test
    void getById_returnsObject() {
        // arrange
        var dataSource = new InMemoryDataSource();

        dataSource.create(new ModelType1(), ModelType1.class);
        dataSource.create(new ModelType1(), ModelType1.class);

        // act
        var model1 = dataSource.getById(1, ModelType1.class);

        // assert
        assertNotNull(model1);
    }

    @Test
    void getById_idNotFound_returnsNull() {
        // arrange
        var dataSource = new InMemoryDataSource();

        dataSource.create(new ModelType1(), ModelType1.class);
        dataSource.create(new ModelType1(), ModelType1.class);

        // act
        var model1 = dataSource.getById(3, ModelType1.class);

        // assert
        assertNull(model1);
    }

    @Test
    void update_notTracked_withNoExistingDataForType_returnsFalse() {
        // arrange
        var dataSource = new InMemoryDataSource();

        // act
        var b = dataSource.update(new ModelType1(), ModelType1.class);

        // assert
        assertFalse(b);
    }

    @Test
    void update_notTracked_withExistingDataForType_returnsFalse() {
        // arrange
        var dataSource = new InMemoryDataSource();
        dataSource.create(new ModelType1(), ModelType1.class);

        // act
        var b = dataSource.update(new ModelType1(2), ModelType1.class);

        // assert
        assertFalse(b);
    }

    @Test
    void update_updatesItem() {
        // arrange
        var dataSource = new InMemoryDataSource();
        var m = dataSource.create(new ModelType3("lowercase"), ModelType3.class);
        assertEquals(1, m.getId());
        // act
        var b = dataSource.update(new ModelType3(1, "UPPERCASE"), ModelType3.class);
        var r = dataSource.getById(1, ModelType3.class);

        // assert
        assertTrue(b);
        assertNotNull(r);
        assertEquals("UPPERCASE", r.getName());
        assertNotEquals("lowercase", r.getName());
    }

    @Test
    void update_tracked_returnsTrue() {
        // arrange
        var dataSource = new InMemoryDataSource();
        dataSource.create(new ModelType3("lowercase"), ModelType3.class);

        // act
        var b = dataSource.update(new ModelType3(1, "UPPERCASE"), ModelType3.class);

        // assert
        assertTrue(b);
    }

    @Test
    void delete_notExisting_returnsFalse() {
        // arrange
        var dataSource = new InMemoryDataSource();

        dataSource.create(new ModelType1(), ModelType1.class);
        dataSource.create(new ModelType1(), ModelType1.class);
        dataSource.create(new ModelType1(), ModelType1.class);

        // act
        var b = dataSource.delete(new ModelType1(555), ModelType1.class);

        // assert
        assertFalse(b);
    }

    @Test
    void delete_Existing_returnsTrue() {
        // arrange
        var dataSource = new InMemoryDataSource();

        dataSource.create(new ModelType1(), ModelType1.class);
        dataSource.create(new ModelType1(), ModelType1.class);
        dataSource.create(new ModelType1(), ModelType1.class);

        // act
        var b = dataSource.delete(new ModelType1(2), ModelType1.class);

        // assert
        assertTrue(b);
    }

    @Test
    void search_returnsNotNull() {
        // arrange
        Map<Class<? extends IModel>, Set<?>> map = new HashMap<>();
        map.put(TrainingType.class, new HashSet<>());

        var dataSource = new InMemoryDataSource(map);

        // act
        var stream = dataSource.search(p -> true, TrainingType.class);

        // assert
        assertNotNull(stream);
    }

    @Test
    void search_countReturns() {
        // arrange
        Set<InMemoryDataSource.Entity<TrainingType>> set = new HashSet<>();
        set.add(new InMemoryDataSource.Entity<>(new TrainingType(1, "Weights")));
        set.add(new InMemoryDataSource.Entity<>(new TrainingType(2, "Running")));
        set.add(new InMemoryDataSource.Entity<>(new TrainingType(3, "Athleticism")));
        set.add(new InMemoryDataSource.Entity<>(new TrainingType(4, "Climbing")));

        Map<Class<? extends IModel>, Set<?>> map = new HashMap<>();
        map.put(TrainingType.class, set);

        var dataSource = new InMemoryDataSource(map);

        // act - if word starts with W or C
        var stream = dataSource.search(p -> p.getName().matches("\\b[WC]\\w+"), TrainingType.class);

        // assert
        assertNotNull(stream);
        var resultSet = stream.collect(Collectors.toSet());
        assertEquals(2, resultSet.size());
    }

    @Test
    void constructor_loader() {
        // arrange
        var loader = mock(InMemoryDataLoader.class);

        // act
        var dataSource = new InMemoryDataSource(loader);

        // assert
        assertNotNull(dataSource);


    }

    @Test
    void loadData() {
        // arrange
        var loader = mock(InMemoryDataLoader.class);
        when(loader.loadUsers()).thenReturn(new HashSet<>());
        when(loader.loadTrainingTypes()).thenReturn(new HashSet<>());
        when(loader.loadTrainees()).thenReturn(new HashSet<>());
        when(loader.loadTrainers()).thenReturn(new HashSet<>());
        when(loader.loadTrainings()).thenReturn(new HashSet<>());

        var dataSource = new InMemoryDataSource(loader);

        // act
        dataSource.loadData();

        // assert
        verify(loader).loadUsers();
        verify(loader).loadTrainingTypes();
        verify(loader).loadTrainees();
        verify(loader).loadTrainers();
        verify(loader).loadTrainings();
    }

    private static class ModelType1 implements IModel {
        private int id;

        ModelType1(int id) {
            this.id = id;
        }

        public ModelType1() {
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public void setId(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ModelType1 model = (ModelType1) o;

            return id == model.id;
        }

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public String toString() {
            return "ModelType1[" +
                    "id=" + id + ']';
        }

    }

    private static class ModelType2 implements IModel {
        private int id;

        public ModelType2() {
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public void setId(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ModelType2 model = (ModelType2) o;

            return id == model.id;
        }

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public String toString() {
            return "ModelType2[" +
                    "id=" + id + ']';
        }

    }

    private static class ModelType3 implements IModel {
        private final String name;
        private int id;

        ModelType3(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public ModelType3(String name) {
            this.name = name;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ModelType3 that = (ModelType3) o;

            return id == that.id;
        }

        @Override
        public int hashCode() {
            return id;
        }
    }
}