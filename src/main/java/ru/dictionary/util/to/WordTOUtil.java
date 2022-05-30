package ru.dictionary.util.to;

import ru.dictionary.entity.Word;
import ru.dictionary.entity.to.WordTO;

import java.util.List;
import java.util.stream.Collectors;

public class WordTOUtil {
    public static Word createNewWordFromTO(WordTO to){
        return new Word(null, to.getWord(), to.getTranslation());
    }

    public static Word updateWordFromTO(WordTO to){
        return new Word(to.getId(), to.getWord(), to.getTranslation());
    }

    public static WordTO getToFromWord(Word word){
        return new WordTO(word.getId(), word.getWord(), word.getTranslation());
    }

    public static List<WordTO> getTOsFromWords(List<Word> words){
        return words.stream().map(WordTOUtil::getToFromWord).collect(Collectors.toList());
    }
}
