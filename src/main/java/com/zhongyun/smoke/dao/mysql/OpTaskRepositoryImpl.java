package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.model.OpTask;
import com.zhongyun.smoke.model.Project;
import com.zhongyun.smoke.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by caozhennan on 2017/12/10.
 */
public class OpTaskRepositoryImpl implements OpTaskOther {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private static final String SELECT_OPTASK =
            "SELECT o.id, o.eui, o.post_user, o.post_time, o.op_user, o.op_time, o.cause, o.handler, o.worker, o.status, o.project_id, o.mtime, o.ctime, " +
                    "u1.id AS u1id, u1.name AS u1name, u1.fullname AS u1fname, u1.type AS u1type, u1.mtime AS u1mtime, u1.ctime AS u1ctime, " +
                    "u2.id AS u2id, u2.name AS u2name, u2.fullname AS u2fname, u2.type AS u2type, u2.mtime AS u2mtime, u2.ctime AS u2ctime, " +
                    "p.id As pid, p.name AS pname, p.province, p.city, p.district, p.address, p.room_count, p.phone, p.mtime AS pmtime, p.ctime AS pctime " +
            "FROM op_task o LEFT JOIN project p ON o.project_id = p.id LEFT JOIN user u2 ON o.op_user = u2.id INNER JOIN user u1 ON o.post_user = u1.id " +
            "WHERE ";

    @Override
    public OpTask findById(long id) {
        System.out.println("id: " + id);
        String sql = SELECT_OPTASK + "o.id = :id";
        Map<String, Long> args = new HashMap<>();
        args.put("id", id);

        return jdbcTemplate.queryForObject(sql, args, (rs, row) -> {
            User u1 = new User(rs.getLong("u1id"), rs.getString("u1name"), rs.getString("u1fname"), rs.getString("u1type"),
                               rs.getTimestamp("u1mtime"), rs.getTimestamp("u1ctime"));
            User u2 = new User(rs.getLong("u2id"), rs.getString("u2name"), rs.getString("u2fname"), rs.getString("u2type"),
                               rs.getTimestamp("u2mtime"), rs.getTimestamp("u2ctime"));
            Project p = new Project(rs.getLong("pid"), rs.getString("pname"), rs.getString("province"), rs.getString("city"), rs.getString("district"),
                                    rs.getString("address"), rs.getInt("room_count"), rs.getString("phone"), rs.getTimestamp("pmtime"), rs.getTimestamp("pctime"));

            OpTask o = new OpTask(rs.getLong("id"), rs.getLong("eui"), rs.getLong("post_user"), rs.getTimestamp("post_time"), rs.getLong("op_user"),
                              rs.getTimestamp("op_time"), rs.getString("cause"), rs.getString("handler"), rs.getString("worker"), rs.getString("status"),
                              rs.getLong("project_id"), rs.getTimestamp("mtime"), rs.getTimestamp("ctime"));
            o.setPoster(u1);
            o.setOp(u2);
            o.setProject(p);
            return o;
        });
    }
}
