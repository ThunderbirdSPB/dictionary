package ru.dictionary.repo.datajpa;

import org.springframework.test.context.ActiveProfiles;
import ru.dictionary.repo.AbstractWordRepoTest;

import static ru.dictionary.ActiveDbProfileResolver.DATAJPA;

@ActiveProfiles(DATAJPA)
public class DataJpaWordRepoTest extends AbstractWordRepoTest {}
