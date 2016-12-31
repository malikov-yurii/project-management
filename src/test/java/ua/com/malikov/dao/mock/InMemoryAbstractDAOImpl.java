package ua.com.malikov.dao.mock;

import ua.com.malikov.model.NamedEntity;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public abstract class InMemoryAbstractDAOImpl<T extends NamedEntity> {
    private Map<Integer, T> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

//    @PostConstruct
//    public void postConstruct() {
//        LOG.info("+++ PostConstruct");
//    }
//
//    @PreDestroy
//    public void preDestroy() {
//        LOG.info("+++ PreDestroy");
//    }

    public T save(T t) {
        if (t.isNew()) {
            t.setId(counter.incrementAndGet());
        }
        repository.put(t.getId(), t);
        return t;
    }

    public void saveAll(List<T> list) {
        list.forEach(t -> {
            save(t);
            repository.put(t.getId(), t);
        });
    }

    public T load(int id) {
        return repository.get(id);
    }

    public List<T> findAll() {
        return repository.values().stream()
                .sorted(Comparator.comparing(T::getId))
                .collect(Collectors.toList());
    }

    public void deleteById(int id) {
        repository.remove(id);
    }

    public void deleteAll() {
        repository.clear();
    }

    public T load(String name) {
        return repository.values().stream()
                .filter(t -> name.equals(t.getName()))
                .findFirst()
                .orElse(null);
    }
}
