package ru.dictionary.entity.to;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class WordTO extends BaseTO{
    @NotBlank
    @Size(min = 2, max = 120)
    private String word;

    @NotBlank
    @Size(min = 2, max = 120)
    private String translation;

    public WordTO() {
    }

    public WordTO(Integer id, String word, String translation) {
        super(id);
        this.word = word;
        this.translation = translation;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    @Override
    public String toString() {
        return "WordTO{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", translation='" + translation + '\'' +
                '}';
    }
}
