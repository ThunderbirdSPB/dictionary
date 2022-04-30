package ru.dictionary.repo.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dictionary.entity.Wordset;
import ru.dictionary.repo.AbstractRepoTest;
import ru.dictionary.repo.WordsetRepo;
import ru.dictionary.util.exception.NotFoundException;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static ru.dictionary.testData.WordsetTestData.*;
import static ru.dictionary.testData.WordTestData.*;
import static ru.dictionary.util.SecurityUtil.authUserId;


class JPAWordsetRepoTest extends AbstractRepoTest {
    @Autowired
    private WordsetRepo repo;

    @Test
    void save() {
        Wordset saved = repo.save(NEW_WS, authUserId());
        WORDSET_MATCHER.assertMatch(saved, repo.getById(saved.getId(), authUserId()));
    }

    @Test
    void delete() {
        WORDSET_MATCHER.assertMatch(WS, repo.getById(WS_ID, authUserId()));
        repo.delete(WS_ID, authUserId());
        assertThrows(NotFoundException.class, ()-> repo.getById(WS_ID, authUserId()));
    }

    @Test
    void update() {
        WORDSET_MATCHER.assertMatch(UPDATED_WS, repo.rename(WS_ID, authUserId(), NEW_NAME));
    }

    @Test
    void getById() {
        WORDSET_MATCHER.assertMatch(WS, repo.getById(WS_ID, authUserId()));
    }

    @Test
    void getAll() {
        WORDSET_MATCHER.assertMatch(ALL_WS, repo.getAll(authUserId()));
    }

    @Test
    void getWords() {
        WORD_MATCHER.assertMatch(ALL_WORDS_FOR_USER, repo.getWords(WS_ID, authUserId()));
    }

    @Test
    void deleteWord() {
        repo.deleteWord(APPLE_ID, WS_ID, authUserId());
    }

    @Test
    void deleteNotExistingWord() {
        assertThrows(NotFoundException.class, ()-> repo.deleteWord(Integer.MAX_VALUE, WS_ID, authUserId()));
    }

    @Test
    void deleteWords() {
        repo.deleteWords(WORD_IDS, WS_ID, authUserId());
    }

    @Test
    void deleteNotExistingWordset() {
        assertThrows(EntityNotFoundException.class, () -> repo.deleteWords(WORD_IDS, Integer.MAX_VALUE, authUserId()));
    }

    @Test
    void addWord() {
        repo.addWord(NOT_ADDED_WORD_IDS.get(0), WS_ID, authUserId());
    }

    @Test
    void addWordToNotExistingWS() {
        assertThrows(EntityNotFoundException.class, ()-> repo.addWord(NOT_ADDED_WORD_IDS.get(0), Integer.MAX_VALUE, authUserId()));
    }

    @Test
    void addWords() {
        repo.addWords(NOT_ADDED_WORD_IDS, WS_ID, authUserId());
    }
}