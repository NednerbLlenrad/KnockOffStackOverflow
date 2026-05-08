package learn.slack_overflow.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import learn.slack_overflow.data.SlackQuestionRepository;
import learn.slack_overflow.data.SlackUserRepository;
import learn.slack_overflow.models.SlackQuestion;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SlackQuestionService {

    private final SlackQuestionRepository repository;
    private final SlackUserRepository userRepository;
    private final Validator validator;

    public SlackQuestionService(SlackQuestionRepository repository, SlackUserRepository userRepository, Validator validator) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.validator = validator;
    }

    public List<SlackQuestion> findAll() {
        return repository.findAll();
    }

    public List<SlackQuestion> findByUserId(int slackUserId) {
        return repository.findByUserId(slackUserId);
    }

    public SlackQuestion findById(int questionId) {
        return repository.findById(questionId);
    }

    public Result<SlackQuestion> add(SlackQuestion question) {
        Result<SlackQuestion> result = validate(question);

        if (question != null && question.getSlackQuestionId() != 0) {
            result.addMessage("Slack question ID cannot be set for add.", ResultType.INVALID);
        }

        if (!result.isSuccess()) {
            return result;
        }

        try {
            result.setPayload(repository.add(question));
        } catch (DataIntegrityViolationException ex) {
            result.addMessage("Slack user ID does not exist.", ResultType.INVALID);
        }

        return result;
    }

    public Result<SlackQuestion> update(SlackQuestion question) {
        Result<SlackQuestion> result = validate(question);

        if (question != null && question.getSlackQuestionId() <= 0) {
            result.addMessage("Slack question ID is required for update.", ResultType.INVALID);
        }

        if (!result.isSuccess()) {
            return result;
        }

        try {
            if (!repository.update(question)) {
                String message = String.format("Slack question ID %s was not found.", question.getSlackQuestionId());
                result.addMessage(message, ResultType.NOT_FOUND);
            }
        } catch (DataIntegrityViolationException ex) {
            result.addMessage("Slack user ID does not exist.", ResultType.INVALID);
        }

        return result;
    }

    public boolean deleteById(int questionId) {
        return repository.deleteById(questionId);
    }

    private Result<SlackQuestion> validate(SlackQuestion question) {
        Result<SlackQuestion> result = new Result<>();

        if (question == null) {
            result.addMessage("Slack question cannot be null.", ResultType.INVALID);
            return result;
        }

        Set<ConstraintViolation<SlackQuestion>> violations = validator.validate(question);

        for (ConstraintViolation<SlackQuestion> violation : violations) {
            result.addMessage(violation.getMessage(), ResultType.INVALID);
        }

        if (question.getSlackUserId() <= 0) {
            result.addMessage("Slack user ID is required.", ResultType.INVALID);
        } else if (userRepository.findById(question.getSlackUserId()) == null) {
            result.addMessage("Slack user ID does not exist.", ResultType.INVALID);
        }

        return result;
    }
}
