package ru.dictionary.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.dictionary.entity.to.WordTO;
import ru.dictionary.repo.UserWordTrainingRepo;
import ru.dictionary.repo.WordRepo;
import ru.dictionary.util.to.WordTOUtil;

import java.util.List;

import static ru.dictionary.util.SecurityUtil.authUserId;

@RequestMapping(TrainingRestController.REST_URL)
@RestController
public class TrainingRestController {
    private static final Logger log = LoggerFactory.getLogger(TrainingRestController.class);
    static final String REST_URL = "/trainings";

    private final UserWordTrainingRepo trainingRepo;
    private final WordRepo wordRepo;

    public TrainingRestController(UserWordTrainingRepo trainingRepo, WordRepo wordRepo) {
        this.trainingRepo = trainingRepo;
        this.wordRepo = wordRepo;
    }

    @GetMapping(path = "/{id}/words", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WordTO> getUnlearnedWords(@PathVariable Integer id){
        log.info("get unlearned words for trainingId={}", id);
        return WordTOUtil.getTOsFromWords(wordRepo.getUnlearnedWords(authUserId(), id));
    }

    @GetMapping("/{id}/words/num")
    public Integer getNumOfUnlearnedWords(@PathVariable Integer id){
        log.info("get num of unlearned words for trainingId={}", id);
        return trainingRepo.getNumOfUnlearnedWords(authUserId(), id);
    }

    @PostMapping(path = "/{id}/words", params = "toLearning")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void moveWordsToLearning(@PathVariable Integer id, @RequestParam List<Integer> wordIds){
        log.info("move wordIds={} to learning for trainingId={}", wordIds, id);
        trainingRepo.moveWordsToLearning(authUserId(), wordIds, id);
    }

    @PostMapping(path = "/{id}/words", params = "toLearned")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void moveWordsToLearned(@PathVariable Integer id, @RequestParam List<Integer> wordIds){
        log.info("move wordIds={} to learned for trainingId={}", wordIds, id);
        trainingRepo.moveWordsToLearned(authUserId(), wordIds, id);
    }
}
