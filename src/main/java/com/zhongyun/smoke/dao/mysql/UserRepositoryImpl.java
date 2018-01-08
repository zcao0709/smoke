package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.common.Page;
import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.model.Project;
import com.zhongyun.smoke.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.*;

/**
 * Created by caozhennan on 2018/1/5.
 */
public class UserRepositoryImpl implements UserOther {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private static final String select = "SELECT u.id, u.name, u.fullname, u.type, u.mtime, u.ctime FROM user u ";

    private static final String selectOne = "SELECT u.id, u.name, u.fullname, u.type, u.mtime, u.ctime, " +
            "p.id AS pid, p.name AS pname, p.province, p.city, p.district, p.address, p.room_count, p.phone, " +
            "p.graph, p.mtime AS pmtime, p.ctime AS pctime " +
            "FROM user u LEFT JOIN user_project up ON u.id = up.user_id LEFT JOIN project p ON up.project_id = p.id ";

    private static final String count = "SELECT COUNT(u.id) AS c FROM user u ";

    private static final RowMapper<User> rowMapper = (rs, row) -> {
        User u = new User(rs.getLong("id"), rs.getString("name"), rs.getString("fullname"), rs.getString("type"),
                          rs.getTimestamp("mtime"), rs.getTimestamp("ctime"));
        u.setProjects(new ArrayList<>(0));
        return u;
    };

    private static final RowMapper<User> rowMapperOne = (rs, row) -> {
        User u = new User(rs.getLong("id"), rs.getString("name"), rs.getString("fullname"), rs.getString("type"),
                          rs.getTimestamp("mtime"), rs.getTimestamp("ctime"));
        u.setProjects(new LinkedList<>());

        long pid = rs.getLong("pid");
        if (pid == 0) {
            return u;
        } else {
            Project p = new Project(pid, rs.getString("pname"), rs.getString("province"), rs.getString("city"),
                                    rs.getString("district"), rs.getString("address"), rs.getInt("room_count"), rs.getString("phone"),
                                    rs.getString("graph"), rs.getTimestamp("pmtime"), rs.getTimestamp("pctime"));
            u.getProjects().add(p);
            return u;
        }
    };

    @Override
    public Page<User> findLike(String name, String fullname, String type, int page, int limit) {
        String where = "WHERE u.name LIKE :name AND u.fullname LIKE :fullname AND u.type LIKE :type ";
        Map<String, Object> args = new HashMap<>();
        args.put("name", name);
        args.put("fullname", fullname);
        args.put("type", type);

        List<User> us = jdbcTemplate.query(select + where + Util.order("u.name") + Util.page(page, limit), args, rowMapper);
        long total = jdbcTemplate.queryForObject(count + where, args, (rs, row) -> rs.getLong("c"));

        return new Page<>(us, total);
    }

    @Override
    public User findByName(String name) {
        String where = "WHERE u.name = :name ";
        Map<String, Object> args = new HashMap<>();
        args.put("name", name);

        return findOne(selectOne + where, args);
    }

    @Override
    public User findById(long id) {
        String where = "WHERE u.id = :id ";
        Map<String, Object> args = new HashMap<>();
        args.put("id", id);

        return findOne(selectOne + where, args);
    }

    private User findOne(String sql, Map<String, Object> args) {
        List<User> us = jdbcTemplate.query(sql, args, rowMapperOne);
        if (us == null || us.size() == 0) {
            return null;
        } else {
            User u = us.get(0);
            for (int i = 1; i < us.size(); i++) {
                u.getProjects().addAll(us.get(i).getProjects());
            }
            return u;
        }
    }
}
