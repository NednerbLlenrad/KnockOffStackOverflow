package learn.slack_overflow.data;

import learn.slack_overflow.data.mappers.SlackQuestionMapper;
import learn.slack_overflow.models.SlackQuestion;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SlackQuestionJdbcClientRepository implements SlackQuestionRepository {

    private final JdbcClient jdbcClient;

    public SlackQuestionJdbcClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<SlackQuestion> findAll() {
        String sql = """
                select slack_question_id,
                    title,
                    body,
                    slack_user_id,
                    chill_points,
                    created_at,
                    edited_at
                from slack_question
                order by slack_question_id;
                """;

        return jdbcClient.sql(sql)
                .query(new SlackQuestionMapper())
                .list();
    }

    @Override
    public List<SlackQuestion> findByUserId(int slackUserId) {
        String sql = """
                select slack_question_id,
                    title,
                    body,
                    slack_user_id,
                    chill_points,
                    created_at,
                    edited_at
                from slack_question
                where slack_user_id = ?
                order by slack_question_id;
                """;

        return jdbcClient.sql(sql)
                .param(slackUserId)
                .query(new SlackQuestionMapper())
                .list();
    }

    @Override
    public SlackQuestion findById(int questionId) {
        String sql = """
                select slack_question_id,
                    title,
                    body,
                    slack_user_id,
                    chill_points,
                    created_at,
                    edited_at
                from slack_question
                where slack_question_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(questionId)
                .query(new SlackQuestionMapper())
                .optional()
                .orElse(null);
    }

    @Override
    public SlackQuestion add(SlackQuestion question) {
        String sql = """
                insert into slack_question (title, body, slack_user_id, chill_points)
                values (?, ?, ?, ?);
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcClient.sql(sql)
                .param(question.getTitle())
                .param(question.getBody())
                .param(question.getSlackUserId())
                .param(question.getChillPoints())
                .update(keyHolder, "slack_question_id");

        if (rowsAffected <= 0) {
            return null;
        }

        question.setSlackQuestionId(keyHolder.getKey().intValue());
        return question;
    }

    @Override
    public boolean update(SlackQuestion question) {
        String sql = """
                update slack_question set
                    title = ?,
                    body = ?,
                    slack_user_id = ?,
                    chill_points = ?,
                    edited_at = current_timestamp
                where slack_question_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(question.getTitle())
                .param(question.getBody())
                .param(question.getSlackUserId())
                .param(question.getChillPoints())
                .param(question.getSlackQuestionId())
                .update() > 0;
    }

    @Override
    public boolean deleteById(int questionId) {
        String sql = "delete from slack_question where slack_question_id = ?;";

        return jdbcClient.sql(sql)
                .param(questionId)
                .update() > 0;
    }
}
