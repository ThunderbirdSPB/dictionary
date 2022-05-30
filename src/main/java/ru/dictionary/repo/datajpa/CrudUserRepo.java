package ru.dictionary.repo.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.dictionary.entity.User;

@Transactional(readOnly = true)
public interface CrudUserRepo extends JpaRepository<User, Integer> {
    @Modifying
    @Transactional
    int deleteByUserId(@Param("id") Integer id);
}
