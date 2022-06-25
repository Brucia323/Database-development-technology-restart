package zcy.zcy.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import zcy.zcy.models.TK;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class TKRepository {
    @Resource
    JdbcTemplate jdbcTemplate;
    
    public void batchInsert(List<TK> tks) {
        jdbcTemplate.batchUpdate("insert into tk(title) value (?)", tks, 1000, (ps, argument) -> ps.setString(1, argument.getTitle()));
    }
    
    public List<TK> query() {
        return jdbcTemplate.query("select id from tk", (rs, rowNum) -> {
            TK tk = new TK();
            tk.setId(rs.getInt("id"));
            return tk;
        });
    }
}
