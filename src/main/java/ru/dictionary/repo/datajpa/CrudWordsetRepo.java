package ru.dictionary.repo.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import ru.dictionary.entity.Wordset;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudWordsetRepo extends JpaRepository<Wordset, Integer> {
    @Transactional
    @Modifying
    int deleteByWsIdAndUserId(@Param("wordsetId") Integer wordsetId, @Param("userId") Integer userId) throws EntityNotFoundException;

    @Transactional
    @Modifying
    void deleteByWsIdsAndUserId(List<Integer> wordsetIds, Integer userId);

    List<Wordset> getAllByUserId( @Param("userId") Integer userId);

    Wordset getByWsIdAndUserId(@Param("wsId") Integer wsId, @Param("userId") Integer userId);

    Wordset getWords(@Param("wordsetId") Integer wordsetId, @Param("userId") Integer userId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, name = Wordset.DELETE_WORD_BY_WORDSET_ID_AND_WORD_ID)
    int deleteWord(@Param("wordId") Integer wordId, @Param("wordsetId") Integer wordsetId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, name = Wordset.DELETE_WORDS_BY_WORDSET_ID_AND_WORD_IDS)
    int deleteWords(@Param("wordIds") List<Integer> wordIds, @Param("wordsetId") Integer wordsetId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, name = Wordset.ADD_WORD_TO_WS)
    int addWord(@Param("wordId") Integer wordId, @Param("wordsetId") Integer wordsetId);
}
