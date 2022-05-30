package ru.dictionary.repo.jpa;



import org.springframework.test.context.ActiveProfiles;
import ru.dictionary.repo.AbstractUserRepoTest;
import static ru.dictionary.ActiveDbProfileResolver.JPA;

@ActiveProfiles(JPA)
class JPAUserRepoTest extends AbstractUserRepoTest {}