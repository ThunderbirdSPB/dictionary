package ru.dictionary.repo.jpa;

import org.hibernate.Session;
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
public class JPAWordsetRepo implements WordsetRepo {
    @PersistenceContext
    EntityManager em;

    @Override
    @Transactional
    public Wordset save(Wordset wordset, Integer userId) {
        wordset.setUser(em.getReference(User.class, userId));
        em.persist(wordset);

        return wordset;
    }

    @Override
    @Transactional
    public void delete(Integer wordsetId, Integer userId) {
        boolean deleted = em.createNamedQuery(Wordset.DELETE_BY_ID)
                .setParameter("wordsetId", wordsetId)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
        ValidationUtil.checkNotFoundWithId(deleted, wordsetId);
    }

    @Override
    @Transactional
    public void delete(List<Integer> wordsetIds, Integer userId) {
        em.createNamedQuery(Wordset.DELETE_BY_IDS)
                .setParameter("wordsetIds", wordsetIds)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    @Transactional
    public Wordset rename(Integer wordsetId, Integer userId, String name) {
        Wordset ws = getById(wordsetId, userId);
        ws.setName(name);
        return em.merge(ws);
    }

    @Override
    public Wordset getById(Integer wordsetId, Integer userId) {
        Wordset ws = em.find(Wordset.class, wordsetId);
        if (isUserOwningWordset(ws, userId))
            return ws;
        else
            throw new NotFoundException("Not found wordset with id=" + wordsetId);
    }

    @Override
    public List<Wordset> getAll(Integer userId) {
        return em.createNamedQuery(Wordset.GET_ALL, Wordset.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Word> getWords(Integer wordsetId, Integer userId) {
        return em.createNamedQuery(Wordset.GET_WORDS, Wordset.class)
                .setParameter("wordsetId", wordsetId)
                .setParameter("userId", userId)
                .getSingleResult().getWords();
    }

    @Override
    @Transactional
    public void deleteWord(Integer wordId, Integer wordsetId, Integer userId) {
        Wordset ws = em.getReference(Wordset.class, wordsetId);
        boolean deleted = false;
        if (isUserOwningWordset(ws, userId))
            deleted = em.createNamedQuery(Word.DELETE_BY_ID)
                    .setParameter("wordId", wordId)
                    .setParameter("userId", userId)
                    .executeUpdate() != 0;
        ValidationUtil.checkNotFoundWithId(deleted, wordId);
    }

    @Override
    @Transactional
    public void deleteWords(List<Integer> wordIds, Integer wordsetId, Integer userId) {
        Wordset ws = em.getReference(Wordset.class, wordsetId);

        if (isUserOwningWordset(ws, userId))
            em.createNamedQuery(Word.DELETE_BY_IDS)
                    .setParameter("wordIds", wordIds)
                    .setParameter("userId", userId)
                    .executeUpdate();
    }

    @Override
    @Transactional
    public void addWord(Integer wordId, Integer wordsetId, Integer userId) {
        Wordset ws = em.getReference(Wordset.class, wordsetId);

        if (isUserOwningWordset(ws, userId)) {
            ws.getWords().add(em.getReference(Word.class, wordId));
            em.persist(ws);
        }
        else
            throw new NotFoundException("Not found wordset with id=" + wordsetId);
    }

    @Override
    @Transactional
    public void addWords(List<Integer> wordIds, Integer wordsetId, Integer userId) {
        Wordset ws = em.getReference(Wordset.class, wordsetId);

        if (!isUserOwningWordset(ws, userId))
            throw new NotFoundException("Not found wordset with id=" + wordsetId);

        Session hibernateSession = em.unwrap(Session.class);
        String sql = "insert into wordset_word (word_id, wordset_id) values (:word_id, :wordset_id)";
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
            } catch (SQLException e) {
                //TODO: logging
            }
        });
    }

    private boolean isUserOwningWordset(Wordset ws, Integer userId){
        if (ws == null || userId == null)
            throw new NotFoundException("");
        return Objects.equals(ws.getUser().getId(), em.getReference(User.class, userId).getId());
    }
}
