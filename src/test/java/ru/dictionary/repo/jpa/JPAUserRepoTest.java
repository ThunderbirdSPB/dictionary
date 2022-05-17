package ru.dictionary.repo.jpa;


import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.dictionary.repo.AbstractUserRepoTest;

import javax.persistence.PersistenceException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.dictionary.ActiveDbProfileResolver.JPA;
import static ru.dictionary.testData.UserTestData.user;

@ActiveProfiles(JPA)
class JPAUserRepoTest extends AbstractUserRepoTest {
    @Test
    @Override
    protected void save_alreadyExistingEmail_exception(){
        assertThrows(PersistenceException.class, ()-> repo.save(user));
    }
}






