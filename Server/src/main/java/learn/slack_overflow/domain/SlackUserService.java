package learn.slack_overflow.domain;

import learn.slack_overflow.data.SlackUserRepository;
import learn.slack_overflow.models.SlackUser;
import org.springframework.stereotype.Service;

import jakarta.validation.Validator;

import java.util.List;

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

    public SlackUser findById(int userId){
        return repository.findById(userId);
    }
}
