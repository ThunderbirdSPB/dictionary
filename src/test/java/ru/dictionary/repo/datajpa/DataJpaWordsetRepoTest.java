package ru.dictionary.repo.datajpa;

import org.springframework.test.context.ActiveProfiles;
import ru.dictionary.repo.AbstractWordsetRepoTest;
import static ru.dictionary.ActiveDbProfileResolver.DATAJPA;

@ActiveProfiles(DATAJPA)
public class DataJpaWordsetRepoTest extends AbstractWordsetRepoTest { }
