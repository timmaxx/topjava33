package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.AbstractBaseEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryBaseRepository<T extends AbstractBaseEntity> {
    protected static final Logger log = LoggerFactory.getLogger(InMemoryBaseRepository.class);

    protected final Map<Integer, T> repository = new ConcurrentHashMap<>();
    protected final AtomicInteger counter = new AtomicInteger(0);
}
