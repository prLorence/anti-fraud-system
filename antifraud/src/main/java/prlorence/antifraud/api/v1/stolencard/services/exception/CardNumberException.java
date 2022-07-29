package prlorence.antifraud.api.v1.stolencard.services.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CardNumberException extends ResponseStatusException {
    public CardNumberException(HttpStatus status) {
        super(status);
    }

    public CardNumberException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public CardNumberException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
