package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.common.Page;
import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.model.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * Created by caozhennan on 2017/12/8.
 */
public class SensorRepositoryImpl implements SensorOther {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private static final String select = "SELECT s.id, s.eui, s.eui16, s.model, s.type, s.location, s.lati, s.longi, s.install_time, " +
            "s.guarantee, s.status, s.project_id, s.gateway_id, s.phone, s.mtime, s.ctime, COUNT(o.id) as c " +
            "FROM sensor s LEFT JOIN op_task o ON s.eui = o.eui ";

    private static final String count = "SELECT COUNT(s.id) AS c FROM sensor s ";

    private static final RowMapper<Sensor> rowMapper = (rs, row) ->
            new Sensor(rs.getLong("id"), rs.getLong("eui"), rs.getString("eui16"), rs.getString("model"), rs.getString("type"), rs.getString("location"),
                       rs.getString("lati"), rs.getString("longi"), rs.getTimestamp("install_time"), rs.getString("guarantee"), rs.getString("status"),
                       rs.getLong("project_id"), rs.getLong("gateway_id"), rs.getString("phone"), rs.getTimestamp("mtime"), rs.getTimestamp("ctime"),
                       rs.getLong("c"));

    @Override
    public int updateLatiAndLongiByGatewayId(long gatewayId) {
        String update = "UPDATE sensor s1 INNER JOIN sensor s2 ON s1.gateway_id = s2.id SET s1.lati = s2.lati, s1.longi = s2.longi, " +
                "s1.mtime = NOW() WHERE s2.id = :id";
        Map<String, Long> args = new HashMap<>();
        args.put("id", gatewayId);

        return jdbcTemplate.update(update, args);
    }

    @Override
    public Sensor findById(long id) {
        String where = "WHERE id = :id";
        Map<String, Long> args = new HashMap<>();
        args.put("id", id);

        return jdbcTemplate.queryForObject(select + where, args, rowMapper);
    }

    @Override
    public Page<Sensor> findLike(String eui, String model, String type, String location, String guarantee, String status, String phone,
                                 Date installTimeStart, Date installTimeEnd, int page, int limit) {
        String where = "WHERE s.eui16 LIKE :eui AND s.model LIKE :model AND s.type LIKE :type AND s.location like :location AND s.guarantee LIKE :guarantee " +
                "AND s.status LIKE :status AND s.phone LIKE :phone AND s.install_time BETWEEN :installTimeStart AND :installTimeEnd ";
        Map<String, Object> args = args(eui, model, type, location, guarantee, status, phone, installTimeStart, installTimeEnd);
        return query(where, args, page, limit);
    }

    @Override
    public Page<Sensor> findLike(long projectId, String eui, String model, String type, String location, String guarantee, String status, String phone,
                                 Date installTimeStart, Date installTimeEnd, int page, int limit) {
        String where = "WHERE s.project_id = :projectId AND s.eui16 LIKE :eui AND s.model LIKE :model AND s.type LIKE :type AND s.location like :location " +
                "AND s.guarantee LIKE :guarantee AND s.status LIKE :status AND s.phone LIKE :phone " +
                "AND s.install_time BETWEEN :installTimeStart AND :installTimeEnd ";
        Map<String, Object> args = args(eui, model, type, location, guarantee, status, phone, installTimeStart, installTimeEnd);
        args.put("projectId", projectId);
        return query(where, args, page, limit);
    }

    @Override
    public List<Sensor> findByProjectIdAndTypeAndStatusIsIn(long projectId, String type, Set<String> status) {
        String where = "WHERE s.project_id = :projectId AND s.type = :type AND s.status IN (:status) ";
        Map<String, Object> args = new HashMap<>();
        args.put("projectId", projectId);
        args.put("type", type);
        args.put("status", status);
        return jdbcTemplate.query(select + where + "GROUP BY s.id " + Util.order("s.eui16"), args, rowMapper);
    }

    private Page<Sensor> query(String where, Map<String, Object> args, int page, int limit) {
        String sql = select + where + "GROUP BY s.id " + Util.order("s.eui16") + Util.page(page, limit);

        List<Sensor> ss = jdbcTemplate.query(sql, args, rowMapper);
        if (ss == null) {
            return new Page<>(new ArrayList<>(0), 0);
        }
        long total = jdbcTemplate.queryForObject(count + where, args, (rs, row) -> rs.getLong("c"));
        return new Page<>(ss, total);
    }

    private Map<String, Object> args(String eui, String model, String type, String location, String guarantee, String status, String phone,
                                     Date installTimeStart, Date installTimeEnd) {
        Map<String, Object> args = new HashMap<>();
        args.put("eui", eui);
        args.put("model", model);
        args.put("type", type);
        args.put("location", location);
        args.put("guarantee", guarantee);
        args.put("status", status);
        args.put("phone", phone);
        args.put("installTimeStart", installTimeStart);
        args.put("installTimeEnd", installTimeEnd);

        return args;
    }
}
