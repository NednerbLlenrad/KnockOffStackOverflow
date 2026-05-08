package learn.slack_overflow.data;

import learn.slack_overflow.data.mappers.SlackUserMapper;
import learn.slack_overflow.models.SlackUser;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SlackUserJdbcClientRepository implements SlackUserRepository {

    private final JdbcClient jdbcClient;

    public SlackUserJdbcClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<SlackUser> findAll() {
        String sql = """
                select slack_user_id,
                    username,
                    password_hash,
                    email,
                    chill_points,
                    created_at,
                    edited_at
                from slack_user
                order by slack_user_id;
                """;

        return jdbcClient.sql(sql)
                .query(new SlackUserMapper())
                .list();
    }

    @Override
    public SlackUser findById(int userId) {

        String sql = """
                select slack_user_id,
                    username,
                    password_hash,
                    email,
                    chill_points,
                    created_at,
                    edited_at
                from slack_user
                where slack_user_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(userId)
                .query(new SlackUserMapper())
                .optional()
                .orElse(null);
    }

    @Override
    public SlackUser add(SlackUser user) {
        String sql = """
                insert into slack_user (username, password_hash, email, chill_points)
                values (?, ?, ?, ?);
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcClient.sql(sql)
                .param(user.getUsername())
                .param(user.getPasswordHash())
                .param(user.getEmail())
                .param(user.getChillPoints())
                .update(keyHolder, "slack_user_id");

        if (rowsAffected <= 0) {
            return null;
        }

        user.setSlackUserId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public boolean update(SlackUser user) {
        String sql = """
                update slack_user set
                    username = ?,
                    password_hash = ?,
                    email = ?,
                    chill_points = ?,
                    edited_at = current_timestamp
                where slack_user_id = ?;
                """;

        return jdbcClient.sql(sql)
                .param(user.getUsername())
                .param(user.getPasswordHash())
                .param(user.getEmail())
                .param(user.getChillPoints())
                .param(user.getSlackUserId())
                .update() > 0;
    }

    @Override
    public boolean deleteById(int userId) {
        String sql = "delete from slack_user where slack_user_id = ?;";

        return jdbcClient.sql(sql)
                .param(userId)
                .update() > 0;
    }
}
