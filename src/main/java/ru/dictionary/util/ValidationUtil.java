package ru.dictionary.util;
import org.springframework.dao.DataIntegrityViolationException;
import ru.dictionary.entity.AbstractBaseEntity;
import ru.dictionary.util.exception.NotFoundException;

import javax.validation.*;
import java.util.Optional;
import java.util.Set;

public class ValidationUtil {
    private static final Validator validator;
    private static final String NOT_FOUND_ENTITY_WITH = "Not found entity with ";
    private static final String ENTITY_DOES_NOT_EXIST = "entity doesn't exist";
    private static final String ENTITY_ALREADY_EXISTS = "entity already exists";
    private static final String ID = "id=";

    static {
        //  From Javadoc: implementations are thread-safe and instances are typically cached and reused.
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        //  From Javadoc: implementations of this interface must be thread-safe
        validator = factory.getValidator();
    }
    private ValidationUtil() {}

    public static <T extends AbstractBaseEntity> void validate(T bean) {
        // https://alexkosarev.name/2018/07/30/bean-validation-api/
        Set<ConstraintViolation<T>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public static <T extends AbstractBaseEntity> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static <T extends AbstractBaseEntity> T checkNotFoundWithId(Optional<T> object, int id) {
        return object.orElseThrow(() -> new NotFoundException(NOT_FOUND_ENTITY_WITH + ID + id));
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, ID + id);
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException(NOT_FOUND_ENTITY_WITH + msg);
        }
    }

    public static <T extends AbstractBaseEntity> boolean isNew(T bean){
        return bean.getId() == null;
    }

    public static <T extends AbstractBaseEntity> void checkIsNotNew(T bean){
        if (isNew(bean))
            throw new NotFoundException(ENTITY_DOES_NOT_EXIST);
    }

    public static <T extends AbstractBaseEntity> void checkIsNew(T bean){
        if (!isNew(bean))
            throw new DataIntegrityViolationException(ENTITY_ALREADY_EXISTS);
    }
}
