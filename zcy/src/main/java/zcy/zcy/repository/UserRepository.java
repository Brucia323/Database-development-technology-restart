package zcy.zcy.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import zcy.zcy.models.User;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class UserRepository {
    @Resource
    JdbcTemplate jdbcTemplate;
    
    public void batchInsert(List<User> users) {
        jdbcTemplate.batchUpdate("insert into user(name) value (?)", users, 1000, (ps, argument) -> ps.setString(1, argument.getName()));
    }
    
    public List<User> query() {
        return jdbcTemplate.query("select id from user", (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            return user;
        });
    }
}
