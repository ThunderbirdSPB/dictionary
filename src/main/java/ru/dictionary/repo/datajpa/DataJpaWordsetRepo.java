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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static ru.dictionary.util.ValidationUtil.checkNotFoundWithId;

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
        ValidationUtil.checkIsNew(wordset);
        ValidationUtil.validate(wordset);
        wordset.setUser(userRepo.getById(userId));
        log.info("save wordset {}", wordset);
        return wsRepo.save(wordset);
    }

    @Override
    public void delete(Integer wordsetId, Integer userId) {
        log.info("delete wordset with wordsetId={} and userId={}", wordsetId, userId);
        checkNotFoundWithId(wsRepo.deleteByWsIdAndUserId(wordsetId, userId) != 0, wordsetId);
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
        ValidationUtil.validate(ws);
        return wsRepo.save(ws);
    }

    @Override
    @Transactional(readOnly = true)
    public Wordset getById(Integer wordsetId, Integer userId) {
        log.info("get wordset by wordsetId={} and userId={}", wordsetId, userId);
        return checkNotFoundWithId(wsRepo.getByWsIdAndUserId(wordsetId, userId), wordsetId);
    }

    @Override
    public List<Wordset> getAll(Integer userId) {
        log.info("get all wordsets by userId={}", userId);
        return wsRepo.getAllByUserId(userId);
    }

    @Override
    public List<Word> getWords(Integer wordsetId, Integer userId) {
        log.info("get words from wordset with id={} and userId={}", wordsetId, userId);
        return checkNotFoundWithId(wsRepo.getWords(wordsetId, userId), wordsetId).getWords();
    }

    @Override
    @Transactional
    public void deleteWord(Integer wordId, Integer wordsetId, Integer userId) {
        log.info("delete word from wordset with id={} and userId={} and wordId={}", wordsetId, userId, wordId);
        getById(wordsetId, userId);
        ValidationUtil.checkNotFoundWithId(wsRepo.deleteWord(wordId, wordsetId) != 0, wordId);
    }

    @Override
    @Transactional
    public void deleteWords(List<Integer> wordIds, Integer wordsetId, Integer userId) {
        log.info("delete words from wordset with id={} and userId={} and wordIds={}", wordsetId, userId, wordIds);
        getById(wordsetId, userId);
        wsRepo.deleteWords(wordIds, wordsetId);
    }

    @Override
    @Transactional
    public void addWord(Integer wordId, Integer wordsetId, Integer userId) {
        log.info("add word to wordset with id={} and userId={} and wordId={}", wordsetId, userId, wordId);
        getById(wordsetId, userId);
        wsRepo.addWord(wordId, wordsetId);
    }

    @Override
    @Transactional
    public void addWords(List<Integer> wordIds, Integer wordsetId, Integer userId) {
        log.info("add words to wordset with id={} and userId={} and wordIds={}", wordsetId, userId, wordIds);
        getById(wordsetId, userId);

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
}
