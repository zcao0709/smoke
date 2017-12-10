package com.zhongyun.smoke.dao.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caozhennan on 2017/12/10.
 */
@Component
public class ReportRepository {
    @Autowired
//    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate jdbcTemplate;

    public Map<String, String> findByProjectId(long projectId) {
        String sql = "SELECT status, COUNT(ID) as count FROM op_task where project_id = :pid GROUP BY status";
        Map<String, Long> params = new HashMap<>();
        params.put("pid", projectId);
        List<KV> entries = jdbcTemplate.query(sql, params, (rs, row) -> new KV(rs.getString("status"), rs.getString("count")));
        Map<String, String> ret = new HashMap<>();
        entries.forEach(v -> ret.put(v.key, v.val));
        return ret;
    }

    private static class KV {
        private String key;
        private String val;

        public KV(String key, String val) {
            this.key = key;
            this.val = val;
        }
    }
}
