package ru.job4j.concurrent.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class Cache {

    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    public boolean add(Base model) {
        return memory.putIfAbsent(model.getId(), model) == null;
    }

    public boolean update(Base model) {
        BiFunction<Integer, Base, Base> function = (id, storedModel) -> {
            if (storedModel.getVersion() != model.getVersion()) {
                throw new OptimisticException("Versions are not equals");
            }
            Base updatedModel = new Base(id, storedModel.getVersion() + 1);
            updatedModel.setName(model.getName());
            return updatedModel;
        };
        return memory.computeIfPresent(model.getId(), function) != null;
    }

    public void delete(Base model) {
        memory.remove(model.getId());
    }

    public int size() {
        return memory.size();
    }

    public Base get(Integer id) {
        return memory.get(id);
    }
}
