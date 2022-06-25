package zcy.zcy.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import zcy.zcy.models.ProblemSolving;
import zcy.zcy.utils.ProblemSolvingTK;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class ProblemSolvingRepository {
    @Resource
    JdbcTemplate jdbcTemplate;
    
    public void batchInsert(List<ProblemSolving> problemSolvings) {
        jdbcTemplate.batchUpdate("insert into problem_solving(user_id, tk_id) VALUES (?,?)", problemSolvings, 1000000, (ps, argument) -> {
            ps.setInt(1, argument.getUserId());
            ps.setInt(2, argument.getTKId());
        });
    }
    
    public List<ProblemSolvingTK> query() {
        return jdbcTemplate.query("select ps.id id,t.title title from problem_solving ps,tk t where ps.tk_id=t.id", (rs, rowNum) -> {
            ProblemSolvingTK problemSolvingTK = new ProblemSolvingTK();
            problemSolvingTK.setId(rs.getInt("id"));
            problemSolvingTK.setTitle(rs.getString("title"));
            return problemSolvingTK;
        });
    }
}
