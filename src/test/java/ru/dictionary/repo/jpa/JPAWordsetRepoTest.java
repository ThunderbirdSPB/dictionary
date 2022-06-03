package ru.dictionary.repo.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.dictionary.repo.AbstractWordsetRepoTest;
import javax.persistence.NoResultException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.dictionary.ActiveDbProfileResolver.JPA;
import static ru.dictionary.testData.UserTestData.USER_ID;
import static ru.dictionary.testData.WordsetTestData.*;

@ActiveProfiles(JPA)
class JPAWordsetRepoTest extends AbstractWordsetRepoTest {

    @Test
    @Override
    protected void getById_byNotExistingId_exception() {
        assertThrows(NoResultException.class, ()-> repo.getById(NOT_EXISTING_WS_ID, USER_ID));
    }

    @Test
    @Override
    protected void getWords_byNotExistingWsId_exception() {
        assertThrows(NoResultException.class, () -> repo.getWords(NOT_EXISTING_WS_ID, USER_ID));
    }

    @Test
    @Override
    protected void addWord_addNotAddedWordToNotExistingWordset_exception() {
        assertThrows(NoResultException.class, () -> repo.addWord(NOT_ADDED_WORD_IDS.get(0), NOT_EXISTING_WS_ID, USER_ID));
    }

    @Test
    @Override
    protected void deleteWords_byNotExistingWordset_exception() {
        assertThrows(NoResultException.class, () -> repo.deleteWords(ALL_USERS_WORD_IDS, NOT_EXISTING_WS_ID, USER_ID));
    }

    @Test
    @Override
    protected void delete_existingWs_success() {
        WORDSET_MATCHER.assertMatch(WS, repo.getById(WS_ID, USER_ID));
        repo.delete(WS_ID, USER_ID);
        assertThrows(NoResultException.class, ()-> repo.getById(WS_ID, USER_ID));
    }

    @Test
    @Override
    protected void update_notExistingWS_exception() {
        assertThrows(NoResultException.class, () -> repo.rename(NOT_EXISTING_WS_ID, USER_ID, NEW_NAME));
    }
}