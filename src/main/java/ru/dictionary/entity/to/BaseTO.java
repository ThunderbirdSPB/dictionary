package ru.dictionary.entity.to;

import com.fasterxml.jackson.annotation.JsonInclude;

public abstract class BaseTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected Integer id;

    public BaseTO() {}

    public BaseTO(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}