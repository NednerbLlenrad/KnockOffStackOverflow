package learn.slack_overflow.data.mappers;

import learn.slack_overflow.models.SlackQuestion;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SlackQuestionMapper implements RowMapper<SlackQuestion> {

    @Override
    public SlackQuestion mapRow(ResultSet rs, int rowNum) throws SQLException {
        SlackQuestion question = new SlackQuestion();
        question.setSlackQuestionId(rs.getInt("slack_question_id"));
        question.setTitle(rs.getString("title"));
        question.setBody(rs.getString("body"));
        question.setSlackUserId(rs.getInt("slack_user_id"));
        question.setChillPoints(rs.getInt("chill_points"));
        question.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        Timestamp editedAt = rs.getTimestamp("edited_at");
        if (editedAt != null) {
            question.setEditedAt(editedAt.toLocalDateTime());
        }

        return question;
    }
}
