package ru.dictionary.repo.jdbc;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.dictionary.repo.RandomRepo;

import java.util.List;

@Repository
public class JDBCRandomRepo implements RandomRepo {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JDBCRandomRepo(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<String> getRandomWords(List<String> exclude, int num) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("words", exclude)
                .addValue("lim", num);
        return namedParameterJdbcTemplate.queryForList(
                "SELECT word FROM random_words WHERE word NOT IN (:words) LIMIT :lim",
                mapSqlParameterSource, String.class);
    }

    @Override
    public List<String> getRandomTranslations(List<String> exclude, int num) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("translations", exclude)
                .addValue("lim", num);
        return namedParameterJdbcTemplate.queryForList(
                "SELECT translation FROM random_translations WHERE translation NOT IN (:translations) LIMIT :lim",
                mapSqlParameterSource, String.class);
    }
}
