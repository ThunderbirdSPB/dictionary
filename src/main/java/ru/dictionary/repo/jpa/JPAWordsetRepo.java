package ru.dictionary.repo.jpa;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.dictionary.entity.User;
import ru.dictionary.entity.Word;
import ru.dictionary.entity.Wordset;
import ru.dictionary.repo.WordsetRepo;
import ru.dictionary.util.ValidationUtil;
import ru.dictionary.util.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Transactional(readOnly = true)
@Repository
@Profile("jpa")
public class JPAWordsetRepo implements WordsetRepo {
    private static final Logger log = LoggerFactory.getLogger(JPAWordsetRepo.class);
    @PersistenceContext
    EntityManager em;

    @Override
    @Transactional
    public Wordset save(Wordset wordset, Integer userId) {
        wordset.setUser(em.getReference(User.class, userId));
        log.info("save wordset {} for userId{}", wordset, userId);
        em.persist(wordset);

        return wordset;
    }

    @Override
    @Transactional
    public void delete(Integer wordsetId, Integer userId) {
        log.info("delete wordsetId {} for userId{}", wordsetId, userId);
        boolean deleted = em.createNamedQuery(Wordset.DELETE_BY_WS_ID_AND_USER_ID)
                .setParameter("wordsetId", wordsetId)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }

    @Override
    @Transactional
    public void delete(List<Integer> wordsetIds, Integer userId) {
        log.info("delete wordsetIds {} for userId{}", wordsetIds, userId);
        em.createNamedQuery(Wordset.DELETE_BY_WS_IDS_AND_USER_ID)
                .setParameter("wordsetIds", wordsetIds)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    @Transactional
    public Wordset rename(Integer wordsetId, Integer userId, String name) {
        log.info("rename wordsetId {} for userId{} new name{}", wordsetId, userId, name);
        Wordset ws = getById(wordsetId, userId);
        ws.setName(name);
        return em.merge(ws);
    }

    @Override
    public Wordset getById(Integer wordsetId, Integer userId) {
        log.info("get wordset by id {} for userId{}", wordsetId, userId);
        Wordset ws = em.find(Wordset.class, wordsetId);
        if (isUserOwningWordset(ws, userId))
            return ws;
        else
            throw new NotFoundException("Not found wordset with id=" + wordsetId);
    }

    @Override
    public List<Wordset> getAll(Integer userId) {
        log.info("get all wordsets by userId{}", userId);
        return em.createNamedQuery(Wordset.GET_ALL_BY_USER_ID, Wordset.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Word> getWords(Integer wordsetId, Integer userId) {
        log.info("get words from wordset by wordsetId{} and userId{}", wordsetId, userId);
        return em.createNamedQuery(Wordset.GET_WORDS, Wordset.class)
                .setParameter("wordsetId", wordsetId)
                .setParameter("userId", userId)
                .getSingleResult().getWords();
    }

    @Override
    @Transactional
    public void deleteWord(Integer wordId, Integer wordsetId, Integer userId) {
        log.info("delete word from wordset by wordsetId{}, userId{} and wordId={}", wordsetId, userId, wordId);
        Wordset ws = em.getReference(Wordset.class, wordsetId);
        boolean deleted = false;
        if (isUserOwningWordset(ws, userId))
            deleted = em.createNamedQuery(Wordset.DELETE_WORD_BY_WORDSET_ID_AND_WORD_ID)
                    .setParameter("wordId", wordId)
                    .setParameter("wordsetId", wordsetId)
                    .executeUpdate() != 0;
        ValidationUtil.checkNotFoundWithId(deleted, wordId);
    }

    @Override
    @Transactional
    public void deleteWords(List<Integer> wordIds, Integer wordsetId, Integer userId) {
        log.info("delete words from wordset by wordsetId{}, userId{} and wordIds={}", wordsetId, userId, wordIds);
        Wordset ws = em.getReference(Wordset.class, wordsetId);

        if (isUserOwningWordset(ws, userId))
            em.createNamedQuery(Wordset.DELETE_WORDS_BY_WORDSET_ID_AND_WORD_IDS)
                    .setParameter("wordIds", wordIds)
                    .setParameter("wordsetId", wordsetId)
                    .executeUpdate();
        else
            throw new NotFoundException("wordset with id=" + wordsetId);
    }

    @Override
    @Transactional
    public void addWord(Integer wordId, Integer wordsetId, Integer userId) {
        log.info("add word to wordset by wordsetId{}, userId{} and wordId={}", wordsetId, userId, wordId);
        Wordset ws = em.getReference(Wordset.class, wordsetId);

        if (isUserOwningWordset(ws, userId)) {
            em.createNamedQuery(Wordset.ADD_WORD_TO_WS)
                    .setParameter("wordId", wordId)
                    .setParameter("wordsetId", wordsetId)
                    .executeUpdate();
        }
        else
            throw new NotFoundException("Not found wordset with id=" + wordsetId);
    }

    @Override
    @Transactional
    public void addWords(List<Integer> wordIds, Integer wordsetId, Integer userId) {
        log.info("add words to wordset by wordsetId{}, userId{} and wordIds={}", wordsetId, userId, wordIds);
        Wordset ws = em.getReference(Wordset.class, wordsetId);

        if (!isUserOwningWordset(ws, userId))
            throw new NotFoundException("Not found wordset with id=" + wordsetId);

        Session hibernateSession = em.unwrap(Session.class);
        String sql = Wordset.INSERT_WORDS;
        hibernateSession.doWork(connection -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                int i = 1;
                for(Integer wordId : wordIds) {
                    preparedStatement.setInt(1, wordId);
                    preparedStatement.setInt(2, wordsetId);
                    preparedStatement.addBatch();
                    //Batch size: 20
                    if (i % 20 == 0)
                        preparedStatement.executeBatch();
                    i++;
                }
                preparedStatement.executeBatch();
            }
        });
    }

    private boolean isUserOwningWordset(Wordset ws, Integer userId){
        if (ws == null || userId == null)
            throw new NotFoundException("");
        return Objects.equals(ws.getUser().getId(), em.getReference(User.class, userId).getId());
    }
}
