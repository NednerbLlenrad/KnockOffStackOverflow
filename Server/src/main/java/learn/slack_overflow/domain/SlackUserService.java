package learn.slack_overflow.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import learn.slack_overflow.data.SlackUserRepository;
import learn.slack_overflow.models.SlackUser;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SlackUserService {

    private final SlackUserRepository repository;
    private final Validator validator;

    public SlackUserService(SlackUserRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public List<SlackUser> findAll() {
        return repository.findAll();
    }

    public SlackUser findById(int userId) {
        return repository.findById(userId);
    }

    public Result<SlackUser> add(SlackUser user) {
        Result<SlackUser> result = validate(user);

        if (user != null && user.getSlackUserId() != 0) {
            result.addMessage("Slack user ID cannot be set for add.", ResultType.INVALID);
        }

        if (!result.isSuccess()) {
            return result;
        }

        try {
            result.setPayload(repository.add(user));
        } catch (DuplicateKeyException ex) {
            result.addMessage("Username or email is already in use.", ResultType.INVALID);
        }

        return result;
    }

    public Result<SlackUser> update(SlackUser user) {
        Result<SlackUser> result = validate(user);

        if (user != null && user.getSlackUserId() <= 0) {
            result.addMessage("Slack user ID is required for update.", ResultType.INVALID);
        }

        if (!result.isSuccess()) {
            return result;
        }

        try {
            if (!repository.update(user)) {
                String message = String.format("Slack user ID %s was not found.", user.getSlackUserId());
                result.addMessage(message, ResultType.NOT_FOUND);
            }
        } catch (DuplicateKeyException ex) {
            result.addMessage("Username or email is already in use.", ResultType.INVALID);
        }

        return result;
    }

    public boolean deleteById(int userId) {
        return repository.deleteById(userId);
    }

    private Result<SlackUser> validate(SlackUser user) {
        Result<SlackUser> result = new Result<>();

        if (user == null) {
            result.addMessage("Slack user cannot be null.", ResultType.INVALID);
            return result;
        }

        Set<ConstraintViolation<SlackUser>> violations = validator.validate(user);

        for (ConstraintViolation<SlackUser> violation : violations) {
            result.addMessage(violation.getMessage(), ResultType.INVALID);
        }

        return result;
    }
}
