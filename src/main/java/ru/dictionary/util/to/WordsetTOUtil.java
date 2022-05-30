package ru.dictionary.util.to;

import ru.dictionary.entity.Wordset;
import ru.dictionary.entity.to.WordsetTO;

import java.util.List;
import java.util.stream.Collectors;

public class WordsetTOUtil {
    public static WordsetTO getTOFromWs(Wordset ws){
        return new WordsetTO(ws.getId(), ws.getName());
    }

    public static List<WordsetTO> getTOsFromWs(List<Wordset> ws){
        return ws.stream().map(WordsetTOUtil::getTOFromWs).collect(Collectors.toList());
    }
}
