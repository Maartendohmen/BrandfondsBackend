package nl.brandfonds.Brandfonds.abstraction;

import java.util.List;
import java.util.Optional;

public interface IBaseService<T> {
    List<T> getAll();

    Optional<T> getOne(Integer id);

    void save(T objectToSave);

    void delete(T objectToDelete);
}
