package prlorence.antifraud.api.v1.suspiciousip.services.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IpAddressException extends ResponseStatusException {
    public IpAddressException(HttpStatus status) {
        super(status);
    }

    public IpAddressException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public IpAddressException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
