package ru.dictionary.repo.datajpa;

import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import ru.dictionary.repo.AbstractUserRepoTest;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static ru.dictionary.ActiveDbProfileResolver.DATAJPA;
import static ru.dictionary.testData.UserTestData.NOT_EXISTING_USER_ID;

@ActiveProfiles(DATAJPA)
public class DataJpaUserRepoTest extends AbstractUserRepoTest {}
