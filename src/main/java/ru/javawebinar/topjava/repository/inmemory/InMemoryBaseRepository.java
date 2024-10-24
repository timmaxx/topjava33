package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.repository.BaseRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryBaseRepository<T extends AbstractBaseEntity> implements BaseRepository<T> {
    protected static final Logger log = LoggerFactory.getLogger(InMemoryBaseRepository.class);

    protected final Map<Integer, T> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public T save(T obj) {
        log.info("save {}", obj);
        if (obj.isNew()) {
            obj.setId(counter.incrementAndGet());
            repository.put(obj.getId(), obj);
            return obj;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(obj.getId(), (id, oldObj) -> obj);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public T get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }
}
