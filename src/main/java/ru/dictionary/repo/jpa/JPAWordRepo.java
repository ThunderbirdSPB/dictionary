package ru.dictionary.repo.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.dictionary.entity.User;
import ru.dictionary.entity.Word;
import ru.dictionary.repo.WordRepo;
import ru.dictionary.util.ValidationUtil;
import ru.dictionary.util.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

@Profile("jpa")
@Repository
@Transactional(readOnly = true)
public class JPAWordRepo implements WordRepo {
    private static final Logger log = LoggerFactory.getLogger(JPAWordRepo.class);
    @PersistenceContext
    EntityManager em;

    @Transactional
    @Override
    public Word save(Word word, Integer userId) {
        word.setUser(em.getReference(User.class, userId));
        log.info("save word {} for user with id {}", word, userId);
        em.persist(word);
        return word;
    }

    @Transactional
    @Override
    public void delete(Integer wordId, Integer userId) {
        log.info("delete wordId {} for userId {}", wordId, userId);
        boolean deleted = em.createNamedQuery(Word.DELETE_BY_WORD_ID_AND_USER_ID)
                .setParameter("wordId", wordId)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
        ValidationUtil.checkNotFoundWithId(deleted, wordId);
    }

    @Transactional
    @Override
    public void delete(List<Integer> wordIds, Integer userId) {
        log.info("delete wordIds {} for userId {}", wordIds, userId);
        em.createNamedQuery(Word.DELETE_BY_WORD_IDS_AND_USER_ID)
                .setParameter("wordIds", wordIds)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Transactional
    @Override
    public Word update(Word word, Integer userId) {
        if (ValidationUtil.isNew(word))
            throw new NotFoundException("word doesn't exist");
        log.info("update word {} for userId {}", word, userId);
        if (Objects.equals(word.getUser().getId(), userId))
            return em.merge(word);
        else
            throw new NotFoundException("Not found word with id=" + word.getId());
    }

    @Override
    public Word getById(Integer wordId, Integer userId) {
        log.info("get word by id {} for userId {}", wordId, userId);
        Word word = em.find(Word.class, wordId);
        if (word != null && Objects.equals(word.getUser().getId(), userId))
            return word;
        else
            throw new NotFoundException("Not found word with id=" + wordId);
    }

    @Override
    public List<Word> getAll(Integer userId) {
        log.info("get all words by userId {}", userId);
        return em.createNamedQuery(Word.GET_ALL_BY_USER_ID, Word.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Word> getUnlearnedWords(Integer userId, Integer trainingId) {
        log.info("get unlearned words by userId {} and trainingId{}", userId, trainingId);
        return em.createNamedQuery(Word.GET_UNLEARNED_WORDS, Word.class)
                .setParameter("userId", userId)
                .setParameter("trainingId", trainingId)
                .getResultList();
    }
}



