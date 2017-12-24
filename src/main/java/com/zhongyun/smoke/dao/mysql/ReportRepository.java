package com.zhongyun.smoke.dao.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caozhennan on 2017/12/10.
 */
@Component
public class ReportRepository {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public Map<String, Map<String, String>> findInAllProject(long start, long end) {
        String sql =
                "SELECT n, s, COUNT(s) AS c FROM " +
                        "(SELECT project.name AS n, CONCAT(cause, '/', status) AS s FROM op_task INNER JOIN project ON project_id = project.id " +
                        "WHERE UNIX_TIMESTAMP(op_task.ctime) >= :start AND UNIX_TIMESTAMP(op_task.ctime) <= :end) AS t " +
                        "GROUP BY n, s";
        Map<String, Long> args = new HashMap<>();
        args.put("start", start/1000);
        args.put("end", end/1000);

        return resultMap(sql, args);
    }

    public Map<String, Map<String, String>> findByProjectId(long projectId, long start, long end) {
        String sql =
                "SELECT n, s, COUNT(s) AS c FROM " +
                        "(SELECT project.name AS n, CONCAT(cause, '/', status) AS s FROM op_task INNER JOIN project ON project_id = project.id " +
                        "WHERE project_id = :pid AND UNIX_TIMESTAMP(op_task.ctime) >= :start AND UNIX_TIMESTAMP(op_task.ctime) <= :end) AS t " +
                        "GROUP BY n, s";
        Map<String, Long> args = new HashMap<>();
        args.put("pid", projectId);
        args.put("start", start / 1000);
        args.put("end", end / 1000);

        return resultMap(sql, args);
    }

    public Map<String, Map<String, String>> findByLocation(String location, long start, long end) {
        String sql =
                "SELECT n, s, COUNT(s) AS c FROM " +
                        "(SELECT project." + location + " AS n, CONCAT(cause, '/', status) AS s FROM op_task INNER JOIN project ON project_id = project.id " +
                        "WHERE UNIX_TIMESTAMP(op_task.ctime) >= :start AND UNIX_TIMESTAMP(op_task.ctime) <= :end) AS t " +
                        "GROUP BY n, s";
        Map<String, Long> args = new HashMap<>();
        args.put("start", start / 1000);
        args.put("end", end / 1000);

        return resultMap(sql, args);
    }

    private Map<String, Map<String, String>> resultMap(String sql, Map<String, Long> args) {
        List<List<String>> entries = jdbcTemplate.query(sql, args, (rs, row) -> {
            List<String> l = new ArrayList<>(2);
            l.add(rs.getString("n"));
            l.add(rs.getString("s"));
            l.add(rs.getString("c"));
            return l;
        });
        Map<String, Map<String, String>> ret = new HashMap<>();
        entries.forEach(v -> {
            Map<String, String> im = ret.get(v.get(0));
            if (im == null) {
                Map<String, String> nim = new HashMap<>();
                nim.put(v.get(1), v.get(2));
                ret.put(v.get(0), nim);
            } else {
                im.put(v.get(1), v.get(2));
            }
        });
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
