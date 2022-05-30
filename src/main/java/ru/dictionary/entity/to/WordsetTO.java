package ru.dictionary.entity.to;

import java.util.List;

public class WordsetTO extends BaseTO {
    private String name;

    public WordsetTO(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "WordsetTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
