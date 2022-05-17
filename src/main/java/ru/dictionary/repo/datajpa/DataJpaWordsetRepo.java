package ru.dictionary.repo.datajpa;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.dictionary.entity.Word;
import ru.dictionary.entity.Wordset;
import ru.dictionary.repo.WordsetRepo;
import ru.dictionary.util.ValidationUtil;
import ru.dictionary.util.exception.NotFoundException;

import javax.persistence.EntityManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Repository
@Profile("datajpa")
public class DataJpaWordsetRepo implements WordsetRepo {
    private static final Logger log = LoggerFactory.getLogger(DataJpaWordsetRepo.class);
    private final CrudWordsetRepo wsRepo;
    private final CrudUserRepo userRepo;
    private final CrudWordRepo wordRepo;
    private final EntityManager em;

    public DataJpaWordsetRepo(CrudWordsetRepo wsRepo, CrudUserRepo userRepo, CrudWordRepo wordRepo, EntityManager em) {
        this.wsRepo = wsRepo;
        this.userRepo = userRepo;
        this.wordRepo = wordRepo;
        this.em = em;
    }

    @Override
    @Transactional
    public Wordset save(Wordset wordset, Integer userId) {
        ValidationUtil.validate(wordset);
        wordset.setUser(userRepo.getById(userId));
        log.info("save wordset {}", wordset);
        return wsRepo.save(wordset);
    }

    @Override
    public void delete(Integer wordsetId, Integer userId) {
        log.info("delete wordset with wordsetId={} and userId={}", wordsetId, userId);
        wsRepo.deleteByWsIdAndUserId(wordsetId, userId);
    }

    @Override
    public void delete(List<Integer> wordsetIds, Integer userId) {
        log.info("delete wordsets by wordsetIds={} and userId={}", wordsetIds, userId);
        wsRepo.deleteByWsIdsAndUserId(wordsetIds, userId);
    }

    @Override
    @Transactional
    public Wordset rename(Integer wordsetId, Integer userId, String name) {
        log.info("rename wordset with wordsetId={} and userId={}", wordsetId, userId);
        Wordset ws = getById(wordsetId, userId);
        ws.setName(name);

        return wsRepo.save(ws);
    }

    @Override
    @Transactional(readOnly = true)
    public Wordset getById(Integer wordsetId, Integer userId) {
        log.info("get wordset by wordsetId={} and userId={}", wordsetId, userId);
        Wordset ws = wsRepo.getById(wordsetId);
        if (isUserOwningWordset(ws, userId))
            return ws;
        else
            throw new NotFoundException("Not found wordset with id=" + wordsetId);
    }

    @Override
    public List<Wordset> getAll(Integer userId) {
        log.info("get all wordsets by userId={}", userId);
        return wsRepo.getAllByUserId(userId);
    }

    @Override
    public List<Word> getWords(Integer wordsetId, Integer userId) {
        log.info("get words from wordset with id={} and userId={}", wordsetId, userId);
        Wordset ws = wsRepo.getWords(wordsetId, userId);
        if (ws == null)
            throw new NotFoundException("not found wordset with id=" + wordsetId);
        return ws.getWords();
    }

    @Override
    @Transactional
    public void deleteWord(Integer wordId, Integer wordsetId, Integer userId) {
        log.info("delete word from wordset with id={} and userId={} and wordId={}", wordsetId, userId, wordId);
        Wordset ws = wsRepo.getById(wordsetId);
        if (isUserOwningWordset(ws, userId))
            if (wsRepo.deleteWord(wordId, wordsetId) == 0)
                throw new NotFoundException("Not found word with id=" + wordId);

    }

    @Override
    @Transactional
    public void deleteWords(List<Integer> wordIds, Integer wordsetId, Integer userId) {
        log.info("delete words from wordset with id={} and userId={} and wordIds={}", wordsetId, userId, wordIds);
        Wordset ws = wsRepo.getById(wordsetId);
        if (isUserOwningWordset(ws, userId))
            wsRepo.deleteWords(wordIds, wordsetId);
        else
            throw new NotFoundException("Not found word with ids=" + wordIds);
    }

    @Override
    @Transactional
    public void addWord(Integer wordId, Integer wordsetId, Integer userId) {
        Wordset ws = wsRepo.getById(wordsetId);
        log.info("add word to wordset with id={} and userId={} and wordId={}", wordsetId, userId, wordId);
        if (isUserOwningWordset(ws, userId))
            wsRepo.addWord(wordId, wordsetId);
        else
            throw new NotFoundException("Not found wordset with id=" + wordsetId);
    }

    @Override
    @Transactional
    public void addWords(List<Integer> wordIds, Integer wordsetId, Integer userId) {
        log.info("add words to wordset with id={} and userId={} and wordIds={}", wordsetId, userId, wordIds);
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
        return Objects.equals(ws.getUser().getId(), userRepo.getById(userId).getId());
    }
}
