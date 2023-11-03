package com.javabootcamp.gym.data;

import com.javabootcamp.gym.data.model.IModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Component
public class InMemoryDataSource implements IDataSource {
    private final Map<Class<? extends IModel>, Set<?>> data;

    public InMemoryDataSource() {
        data = new HashMap<>();
    }

    protected InMemoryDataSource(Map<Class<? extends IModel>, Set<?>> data) {
        this.data = data;
    }

    @NotNull
    @Override
    public <T extends IModel> T create(@NotNull T entity, @NotNull Class<T> type) {
        var set = getSet(type);

        entity.setId(set.size() + 1);
        set.add(entity);

        return entity;
    }

    @Override
    public <T extends IModel> T getById(int id, Class<T> type) {
        var set = getSet(type);

        var r = set.stream().filter(e -> e.getId() == id).findFirst();

        return r.orElse(null);
    }

    @Override
    public <T extends IModel> boolean update(T entity, Class<T> type) {

        var old = getById(entity.getId(), type);
        if (old == null)
            return false;

        var set = getSet(type);

        set.remove(old);
        set.add(entity);
        return true;
    }

    @Override
    public <T extends IModel> boolean delete(T entity, Class<T> type) {
        return getSet(type).remove(entity);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    private <T extends IModel> Set<T> getSet(@NotNull Class<T> setKey) {
        return (Set<T>) data.computeIfAbsent(setKey, k -> new HashSet<>());
    }

    @Override
    public @NotNull <T extends IModel> Stream<T> search(Predicate<T> searchPredicate, Class<T> type) {
        var set = getSet(type);

        return set.stream().filter(searchPredicate);
    }
}
