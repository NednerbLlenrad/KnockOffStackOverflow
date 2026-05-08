package learn.slack_overflow.data;

import learn.slack_overflow.models.SlackQuestion;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SlackQuestionRepository {

    List<SlackQuestion> findAll();

    List<SlackQuestion> findByUserId(int slackUserId);

    SlackQuestion findById(int questionId);

    SlackQuestion add(SlackQuestion question);

    boolean update(SlackQuestion question);

    @Transactional
    boolean deleteById(int questionId);
}
