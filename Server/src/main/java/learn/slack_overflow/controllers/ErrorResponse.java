package learn.slack_overflow.controllers;

import learn.slack_overflow.domain.Result;
import learn.slack_overflow.domain.ResultType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ErrorResponse {

    private final List<String> messages;

    public ErrorResponse(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }

    public static ResponseEntity<Object> build(Result<?> result) {
        HttpStatus status = switch (result.getType()) {
            case INVALID -> HttpStatus.BAD_REQUEST;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return new ResponseEntity<>(new ErrorResponse(result.getMessages()), status);
    }
}
