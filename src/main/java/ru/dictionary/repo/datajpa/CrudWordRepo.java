package ru.dictionary.repo.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.dictionary.entity.Word;

import java.util.List;

@Transactional(readOnly = true)
public interface CrudWordRepo extends JpaRepository<Word, Integer> {
    @Modifying
    @Transactional
    int deleteByWordIdAndUserId(@Param("wordId") Integer wordId, @Param("userId") Integer userId);

    @Modifying
    @Transactional
    void deleteByWordIdsAndUserId(@Param("wordIds") List<Integer> wordIds, @Param("userId") Integer userId);

    List<Word> getAllByUserId(@Param("userId") Integer userId);

    List<Word> getUnlearnedWords(@Param("userId") Integer userId, @Param("trainingId") Integer trainingId);

    Word getByIdAndUserId(@Param("wordId") Integer wordId, @Param("userId") Integer userId);
}
