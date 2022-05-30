package ru.dictionary.web;

import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.dictionary.util.exception.ErrorInfo;
import ru.dictionary.util.exception.ErrorType;
import ru.dictionary.util.exception.NotFoundException;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;

import static ru.dictionary.util.exception.ErrorType.*;
import static ru.dictionary.util.exception.ExceptionUtil.*;

@RestControllerAdvice(annotations = RestController.class)
public class ExceptionInfoHandler {
    private static final Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);
    private final MessageSourceAccessor messageSourceAccessor;

    public ExceptionInfoHandler(MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }

    @ExceptionHandler({NotFoundException.class, NoResultException.class})
    public ResponseEntity<ErrorInfo> handleNotFoundException(HttpServletRequest req, Exception e){
        return logAndGetErrorInfo(req, e, true, DATA_NOT_FOUND);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, PSQLException.class})
    public ResponseEntity<ErrorInfo> handleDataIntegrityException(HttpServletRequest req, Exception e){
        return logAndGetErrorInfo(req, e, true, DATA_ERROR);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorInfo> handleBindValidationError(HttpServletRequest req, BindException e) {
        String[] details = e.getBindingResult().getFieldErrors().stream()
                .map(messageSourceAccessor::getMessage)
                .toArray(String[]::new);

        return logAndGetErrorInfo(req, e, true, VALIDATION_ERROR, details);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorInfo> handleIllegalRequestDataError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR);
    }

    //    https://stackoverflow.com/questions/538870/should-private-helper-methods-be-static-if-they-can-be-static
    private ResponseEntity<ErrorInfo> logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logStackTrace, ErrorType errorType, String... details) {
        Throwable rootCause = logAndGetRootCause(log, req, e, logStackTrace, errorType);
        return ResponseEntity.status(errorType.getStatus())
                .body(new ErrorInfo(req.getRequestURL(), errorType,
                        messageSourceAccessor.getMessage(errorType.getErrorCode()),
                        details.length != 0 ? details : new String[]{getMessage(rootCause)})
                );
    }
}
