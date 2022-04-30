package ru.dictionary.repo;

import ru.dictionary.entity.User;

import java.util.List;

public interface UserRepo {
    User save(User user);
    void delete(Integer id);
    User get(Integer id);
    List<User> getAll();
    User update(User user);
}
