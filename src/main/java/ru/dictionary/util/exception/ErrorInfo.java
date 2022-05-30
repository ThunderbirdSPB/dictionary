package ru.dictionary.util.exception;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Для REST запросов в случае возникновения исключения будем возвращать информацию о нем в формате json.
 * Для этого создадим класс ErrorInfo - он будет представлять информацию об исключении в ответе клиенту.
 */
public class ErrorInfo {
    private final String url;
    private final ErrorType type;
    private final String typeMessage;
    private final String[] details;

    @JsonCreator
    public ErrorInfo(CharSequence url, ErrorType type, String typeMessage, String... details) {
        this.url = url.toString();
        this.type = type;
        this.typeMessage = typeMessage;
        this.details = details;
    }

    public String getUrl() {
        return url;
    }

    public ErrorType getType() {
        return type;
    }

    public String getTypeMessage() {
        return typeMessage;
    }

    public String[] getDetails() {
        return details;
    }
}