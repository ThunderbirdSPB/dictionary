package ru.dictionary.repo.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.dictionary.repo.UserWordTrainingRepo;
import ru.dictionary.util.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class JDBCUserWordTrainingRepo implements UserWordTrainingRepo {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insert;

    public JDBCUserWordTrainingRepo(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
        insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("user_word_training");
    }

    @Override
    @Transactional
    public void moveWordToLearning(Integer userId, Integer wordId, Integer trainingId) {
        if (insert.execute(Map.<String, Integer>of("user_id", userId, "word_id", wordId, "training_id", trainingId)) == 0)
            throw new NotFoundException("Not found wordId=" + wordId + "or userId=" + userId);
    }

    @Override
    @Transactional
    public void moveWordsToLearning(Integer userId, List<Integer> wordIds, Integer trainingId) {
        List<MapSqlParameterSource> entries = new ArrayList<>();
        wordIds.forEach(
                wordId -> entries.add(
                        new MapSqlParameterSource(Map.of("user_id", userId, "word_id", wordId, "training_id", trainingId))
                )
        );
        int[] ints = insert.executeBatch(entries.toArray(new MapSqlParameterSource[entries.size()]));
    }

    @Override
    @Transactional
    public void moveWordToLearned(Integer userId, Integer wordId, Integer trainingId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("trainingId", trainingId)
                .addValue("wordId", wordId);
        if (namedParameterJdbcTemplate.update(
                "DELETE FROM user_word_training WHERE user_id=:userId AND word_id=:wordId AND training_id=:trainingId",
                mapSqlParameterSource) == 0)
            throw new NotFoundException("Not found wordId=" + wordId + "or userId=" + userId + "or trainingId=" + trainingId);
    }

    @Override
    @Transactional
    public void moveWordsToLearned(Integer userId, List<Integer> wordIds, Integer trainingId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("trainingId", trainingId)
                .addValue("wordIds", wordIds);

        namedParameterJdbcTemplate.update(
                "DELETE FROM user_word_training WHERE user_id=:userId AND training_id=:trainingId AND word_id IN (:wordIds)",
                mapSqlParameterSource
        );
    }

    @Override
    public int getNumOfUnlearnedWords(Integer userId, Integer trainingId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource
                .addValue("userId", userId)
                .addValue("trainingId", trainingId);
        return  Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(
                "SELECT count(word_id) FROM user_word_training WHERE user_id=:userId AND training_id=:trainingId", mapSqlParameterSource, Integer.class)).orElse(0);
    }
}
