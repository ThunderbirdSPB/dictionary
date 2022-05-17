package ru.dictionary.repo.datajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.dictionary.entity.Word;
import ru.dictionary.repo.WordRepo;
import ru.dictionary.util.ValidationUtil;
import ru.dictionary.util.exception.NotFoundException;

import java.util.List;
import java.util.Objects;

@Repository
@Profile("datajpa")
public class DataJpaWordRepo implements WordRepo {
    private static final Logger log = LoggerFactory.getLogger(DataJpaWordRepo.class);
    private final CrudWordRepo wordRepo;
    private final CrudUserRepo userRepo;

    public DataJpaWordRepo(CrudWordRepo wordRepo, CrudUserRepo userRepo) {
        this.wordRepo = wordRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Word save(Word word, Integer userId) {
        ValidationUtil.validate(word);
        word.setUser(userRepo.getById(userId));
        log.info("save word {}", word);
        return wordRepo.save(word);
    }

    @Override
    public void delete(Integer wordId, Integer userId) {
        log.info("delete word with wordId={} and userId={}", wordId, userId);
        wordRepo.deleteByWordIdAndUserId(wordId, userId);
    }

    @Override
    public void delete(List<Integer> wordIds, Integer userId) {
        log.info("delete words with wordIds={} and userId={}", wordIds, userId);
        wordRepo.deleteByWordIdsAndUserId(wordIds, userId);
    }

    @Override
    public Word update(Word word, Integer userId) {
        if (ValidationUtil.isNew(word))
            throw new NotFoundException("word doesn't exist");
        log.info("update word {} with userId={}", word, userId);
        if (Objects.equals(word.getUser().getId(), userId))
            return wordRepo.save(word);
        else
            throw new NotFoundException("Not found word with id=" + word.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Word getById(Integer wordId, Integer userId) {
        log.info("get word by wordId={} and userId={}", wordId, userId);
        Word word = wordRepo.getById(wordId);
        if (Objects.equals(word.getUser().getId(), userId))
            return word;
        else
            throw new NotFoundException("Not found word with id=" + wordId);
    }

    @Override
    public List<Word> getAll(Integer userId) {
        log.info("get all words by userId={}", userId);
        return wordRepo.getAllByUserId(userId);
    }

    @Override
    public List<Word> getUnlearnedWords(Integer userId, Integer trainingId) {
        log.info("get unlearned words by userId={} and trainingId={}", userId, trainingId);
        return wordRepo.getUnlearnedWords(userId, trainingId);
    }
}