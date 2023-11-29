package nora.movlog.service.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    UNABLE_TO_SEND_EMAIL("1000", "Unable to send email"),
    NO_SUCH_ALGORITHM("2000", "No such algorithm");

    private final String code;
    private final String message;

    ExceptionCode(final String code, final String message) {
        this.code = code;
        this.message = message;
    }
}
