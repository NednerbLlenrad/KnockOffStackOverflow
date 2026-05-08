package learn.slack_overflow.data.mappers;

import learn.slack_overflow.models.SlackUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SlackUserMapper implements RowMapper<SlackUser> {

    @Override
    public SlackUser mapRow(ResultSet rs, int i) throws SQLException {
        SlackUser user = new SlackUser();
        user.setSlackUserId(rs.getInt("slack_user_id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setEmail(rs.getString("email"));
        user.setChillPoints(rs.getInt("chill_points"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        if (rs.getTimestamp("edited_at") != null) {
            user.setEditedAt(rs.getTimestamp("edited_at").toLocalDateTime());
        }

        return user;
    }
}
