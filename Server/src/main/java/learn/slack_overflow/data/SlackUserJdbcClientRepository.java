package learn.slack_overflow.data;

import learn.slack_overflow.data.mappers.SlackUserMapper;
import learn.slack_overflow.models.SlackUser;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SlackUserJdbcClientRepository implements SlackUserRepository{

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
            from slack_user;
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
        return null;
    }

    @Override
    public boolean update(SlackUser user) {
        return false;
    }

    @Override
    public boolean deletedById(int userId) {
        return false;
    }
}
