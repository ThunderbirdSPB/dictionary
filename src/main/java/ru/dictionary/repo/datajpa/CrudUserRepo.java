package ru.dictionary.repo.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.dictionary.entity.User;

@Transactional(readOnly = true)
public interface CrudUserRepo extends JpaRepository<User, Integer> {}
