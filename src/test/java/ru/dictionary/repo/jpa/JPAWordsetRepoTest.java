package ru.dictionary.repo.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.dictionary.repo.AbstractWordsetRepoTest;

import static ru.dictionary.ActiveDbProfileResolver.JPA;

@ActiveProfiles(JPA)
class JPAWordsetRepoTest extends AbstractWordsetRepoTest { }