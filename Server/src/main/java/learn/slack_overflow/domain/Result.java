package learn.slack_overflow.domain;

import java.util.ArrayList;
import java.util.List;

public class Result<T> {

    private final ArrayList<String> messages = new ArrayList<>();
    private T payload;
    private ResultType type = ResultType.SUCCESS;

    public boolean isSuccess() {
        return type == ResultType.SUCCESS;
    }

    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }

    public void addMessage(String message, ResultType type) {
        messages.add(message);
        this.type = type;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public ResultType getType() {
        return type;
    }
}
