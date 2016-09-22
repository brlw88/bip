package me.brlw.bip.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by ww on 19.09.16.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NumRetriesExceededException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NumRetriesExceededException() {}

    public NumRetriesExceededException(String message) {
        super(message);
    }

    public NumRetriesExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
