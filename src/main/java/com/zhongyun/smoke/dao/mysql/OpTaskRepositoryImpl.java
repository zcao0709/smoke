package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.common.Page;
import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.model.OpTask;
import com.zhongyun.smoke.model.Project;
import com.zhongyun.smoke.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.*;

/**
 * Created by caozhennan on 2017/12/10.
 */
public class OpTaskRepositoryImpl implements OpTaskOther {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private static final String select =
            "SELECT o.id, o.eui, o.eui16, o.post_user, o.post_time, o.op_user, o.op_time, o.cause, o.handler, o.worker, o.status, o.project_id, o.mtime, o.ctime, " +
                    "u1.id AS u1id, u1.name AS u1name, u1.fullname AS u1fname, u1.type AS u1type, u1.mtime AS u1mtime, u1.ctime AS u1ctime, " +
                    "u2.id AS u2id, u2.name AS u2name, u2.fullname AS u2fname, u2.type AS u2type, u2.mtime AS u2mtime, u2.ctime AS u2ctime, " +
                    "p.id As pid, p.name AS pname, p.province, p.city, p.district, p.address, p.room_count, p.phone, p.graph, p.mtime AS pmtime, p.ctime AS pctime " +
            "FROM op_task o LEFT JOIN project p ON o.project_id = p.id LEFT JOIN user u2 ON o.op_user = u2.id INNER JOIN user u1 ON o.post_user = u1.id ";

    private static final String count = "SELECT COUNT(o.id) AS c FROM op_task o ";

    private static final RowMapper<OpTask> rowMapper = (rs, row) -> {
//            User u1 = new User(rs.getLong("u1id"), rs.getString("u1name"), rs.getString("u1fname"), rs.getString("u1type"),
//                               rs.getTimestamp("u1mtime"), rs.getTimestamp("u1ctime"));
        User u2 = new User(rs.getLong("u2id"), rs.getString("u2name"), rs.getString("u2fname"), rs.getString("u2type"),
                           rs.getTimestamp("u2mtime"), rs.getTimestamp("u2ctime"));
        Project p = new Project(rs.getLong("pid"), rs.getString("pname"), rs.getString("province"), rs.getString("city"), rs.getString("district"),
                                rs.getString("address"), rs.getInt("room_count"), rs.getString("phone"), rs.getString("graph"),
                                rs.getTimestamp("pmtime"), rs.getTimestamp("pctime"));

        OpTask o = new OpTask(rs.getLong("id"), rs.getLong("eui"), rs.getString("eui16"), rs.getLong("post_user"), rs.getTimestamp("post_time"),
                              rs.getLong("op_user"), rs.getTimestamp("op_time"), rs.getString("cause"), rs.getString("handler"),
                              rs.getString("worker"), rs.getString("status"), rs.getLong("project_id"), rs.getTimestamp("mtime"), rs.getTimestamp("ctime"));
//            o.setPoster(u1);
        o.setOp(u2);
        o.setProject(p);
        return o;
    };

    @Override
    public OpTask findById(long id) {
        System.out.println("id: " + id);
        String where = "WHERE o.id = :id";
        Map<String, Long> args = new HashMap<>();
        args.put("id", id);

        return jdbcTemplate.queryForObject(select + where, args, rowMapper);
    }

    @Override
    public Page<OpTask> findByCauseInAndStatus(Set<String> causes, String status, int page, int limit) {
        String where = "WHERE o.cause IN (:causes) AND status = :status ";
        Map<String, Object> args = new HashMap<>();
        args.put("causes", causes);
        args.put("status", status);

        return query(where, args, page, limit);
    }

    @Override
    public Page<OpTask> findLike(String eui, String cause, String handler, String worker, String status, Date ctimeStart, Date ctimeEnd, int page, int limit) {
        String where = "WHERE o.eui16 LIKE :eui AND o.cause LIKE :cause AND o.handler LIKE :handler AND o.worker LIKE :worker AND o.status LIKE :status AND " +
                "o.ctime BETWEEN :ctimeStart AND :ctimeEnd ";
        Map<String, Object> args = args(eui, cause, handler, worker, status, ctimeStart, ctimeEnd);
        return query(where, args, page, limit);
    }

    @Override
    public Page<OpTask> findLike(long projectId, String eui, String cause, String handler, String worker, String status, Date ctimeStart, Date ctimeEnd,
                                 int page, int limit) {
        String where = "WHERE o.project_id = :projectId AND o.eui16 LIKE :eui AND o.cause LIKE :cause AND o.handler LIKE :handler AND o.worker LIKE :worker " +
                "AND o.status LIKE :status AND o.ctime BETWEEN :ctimeStart AND :ctimeEnd ";
        Map<String, Object> args = args(eui, cause, handler, worker, status, ctimeStart, ctimeEnd);
        args.put("projectId", projectId);
        return query(where, args, page, limit);
    }

    private Page<OpTask> query(String where, Map<String, Object> args, int page, int limit) {
        String sql = select + where + Util.order("o.mtime") + Util.page(page, limit);

        List<OpTask> os = jdbcTemplate.query(sql, args, rowMapper);
        if (os == null) {
            return new Page<>(new ArrayList<>(0), 0);
        }
        long total = jdbcTemplate.queryForObject(count + where, args, (rs, row) -> rs.getLong("c"));
        return new Page<>(os, total);
    }

    private Map<String, Object> args(String eui, String cause, String handler, String worker, String status, Date ctimeStart, Date ctimeEnd) {
        Map<String, Object> args = new HashMap<>();
        args.put("eui", eui);
        args.put("cause", cause);
        args.put("handler", handler);
        args.put("worker", worker);
        args.put("status", status);
        args.put("ctimeStart", ctimeStart);
        args.put("ctimeEnd", ctimeEnd);

        return args;
    }
}
