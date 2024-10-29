package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository extends InMemoryBaseRepository<User> implements UserRepository {
    @Override
    public User save(User obj) {
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
        //  ToDo:   А как будет удалена еда для этого пользователя?
        return repository.remove(id) != null;
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return repository.values()
                .stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return repository.values()
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findAny()
                .orElse(null)
                ;
    }
}
