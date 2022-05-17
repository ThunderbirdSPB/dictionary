package ru.dictionary.repo.jpa;



import org.springframework.test.context.ActiveProfiles;
import ru.dictionary.repo.AbstractWordRepoTest;

import static ru.dictionary.ActiveDbProfileResolver.JPA;

@ActiveProfiles(JPA)
class JPAWordRepoTest extends AbstractWordRepoTest {}