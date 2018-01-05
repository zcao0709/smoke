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
 * Created by caozhennan on 2018/1/6.
 */
public class ProjectRepositoryImpl implements ProjectOther {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private static final String select = "SELECT p.id, p.name, p.province, p.city, p.district, p.address, p.room_count, p.phone, p.mtime, p.ctime, " +
            "COUNT(s.id) AS c FROM project p LEFT JOIN sensor s ON p.id = s.project_id AND s.type = :stype " ;

    private static final String selectById = "SELECT m.*, u.id AS uid, u.name AS uname, u.fullname, u.type, u.mtime AS umtime, u.ctime AS uctime " +
            "FROM (SELECT p.id, p.name, p.province, p.city, p.district, p.address, p.room_count, p.phone, p.mtime, p.ctime, " +
            "COUNT(s.id) AS c FROM project p LEFT JOIN sensor s ON p.id = s.project_id AND s.type = :stype WHERE p.id = :id GROUP BY p.id) m " +
            "LEFT JOIN user_project up ON m.id = up.project_id LEFT JOIN user u ON up.user_id = u.id ";

    private static final String count = "SELECT COUNT(p.id) AS c FROM project p ";

    private static final RowMapper<Project> rowMapper = (rs, row) -> {
        Project p = new Project(rs.getLong("id"), rs.getString("name"), rs.getString("province"), rs.getString("city"),
                                rs.getString("district"), rs.getString("address"), rs.getInt("room_count"), rs.getString("phone"),
                                rs.getTimestamp("mtime"), rs.getTimestamp("ctime"));
        p.setUsers(new ArrayList<>(0));
        p.setSensorCount(rs.getLong("c"));

        return p;
    };

    private static final RowMapper<Project> rowMapperOne = (rs, row) -> {
        Project p = new Project(rs.getLong("id"), rs.getString("name"), rs.getString("province"), rs.getString("city"),
                                rs.getString("district"), rs.getString("address"), rs.getInt("room_count"), rs.getString("phone"),
                                rs.getTimestamp("mtime"), rs.getTimestamp("ctime"));
        p.setUsers(new LinkedList<>());
        p.setSensorCount(rs.getLong("c"));

        long uid = rs.getLong("uid");
        if (uid == 0) {
            return p;
        } else {
            User u = new User(uid, rs.getString("uname"), rs.getString("fullname"), rs.getString("type"),
                              rs.getTimestamp("umtime"), rs.getTimestamp("uctime"));
            p.getUsers().add(u);
        }
        return p;
    };

    @Override
    public Page<Project> findLike(String name, String province, String city, String district, String address, String phone, int page, int limit) {
        String where = "WHERE p.name LIKE :name AND province LIKE :province AND city LIKE :city AND district LIKE :district AND address LIKE :address " +
                "AND p.phone LIKE :phone ";
        Map<String, Object> args = new HashMap<>();
        args.put("stype", Util.SENSOR_SMOKE);
        args.put("name", name);
        args.put("province", province);
        args.put("city", city);
        args.put("district", district);
        args.put("address", address);
        args.put("phone", phone);

        List<Project> ps = jdbcTemplate.query(select + where + "GROUP BY p.id " + Util.order("p.name") + Util.page(page, limit), args, rowMapper);
        long total = jdbcTemplate.queryForObject(count + where, args, (rs, row) -> rs.getLong("c"));
        return new Page<>(ps, total);
    }

    @Override
    public Project findById(long id) {
        Map<String, Object> args = new HashMap<>();
        args.put("stype", Util.SENSOR_SMOKE);
        args.put("id", id);

        List<Project> ps = jdbcTemplate.query(selectById, args, rowMapperOne);
        if (ps == null || ps.size() == 0) {
            return null;
        } else {
            Project p = ps.get(0);
            for (int i = 1; i < ps.size(); i++) {
                p.getUsers().addAll(ps.get(i).getUsers());
            }
            return p;
        }
    }
}
