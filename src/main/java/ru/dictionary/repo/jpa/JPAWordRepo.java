package ru.dictionary.repo.jpa;

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

@Repository
@Transactional(readOnly = true)
public class JPAWordRepo implements WordRepo {
    @PersistenceContext
    EntityManager em;

    @Transactional
    @Override
    public Word save(Word word, Integer userId) {
        word.setUser(em.getReference(User.class, userId));
        em.persist(word);
        return word;
    }

    @Transactional
    @Override
    public void delete(Integer wordId, Integer userId) {
        boolean deleted = em.createNamedQuery(Word.DELETE_BY_ID)
                .setParameter("wordId", wordId)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
        ValidationUtil.checkNotFoundWithId(deleted, wordId);
    }

    @Transactional
    @Override
    public void delete(List<Integer> wordIds, Integer userId) {
        em.createNamedQuery(Word.DELETE_BY_ID)
                .setParameter("wordIds", wordIds)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Transactional
    @Override
    public Word update(Word word, Integer userId) {
        if (Objects.equals(word.getUser().getId(), userId))
            return em.merge(word);
        else
            throw new NotFoundException("Not found word with id=" + word.getId());
    }

    @Override
    public Word getById(Integer wordId, Integer userId) {
        Word word = em.find(Word.class, wordId);
        if (word != null && Objects.equals(word.getUser().getId(), userId))
            return word;
        else
            throw new NotFoundException("Not found word with id=" + wordId);
    }

    @Override
    public List<Word> getAll(Integer userId) {
        return em.createNamedQuery(Word.GET_ALL, Word.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Word> getUnlearnedWords(Integer userId, Integer trainingId) {
        return em.createNamedQuery(Word.GET_UNLEARNED_WORDS, Word.class)
                .setParameter("userId", userId)
                .setParameter("trainingId", trainingId)
                .getResultList();
    }
}



