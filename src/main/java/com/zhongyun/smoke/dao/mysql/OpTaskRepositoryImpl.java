package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.model.OpTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Created by caozhennan on 2017/12/10.
 */
public class OpTaskRepositoryImpl implements OpTaskOther {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private static final String SELECT_OPTASK = "SELECT o.id, o.eui, o.post_user, o.post_time, o.op_user, o.op_time, o.cause, o.handler, " +
            "o.worker, o.status, o.project_id, o.mtime, o.ctime, u1.id AS u1id, u1.name AS u1name, u1.phone1 AS u1p1, u1.phone2 AS u1p2, " +
            "u1.type AS u1type, u1.mtime AS u1mtime, u1.ctime AS u1ctime, u2.id AS u2id, u2.name AS u2name, u2.phone1 AS u2p1, " +
            "u2.phone2 AS u2p2, u2.type AS u2type, u2.mtime AS u2mtime, u2.ctime AS u2ctime, p.name AS pname " +
            "FROM op_task o LEFT JOIN project p ON o.project_id = p.id, user u1, user u2, project p " +
            "WHERE o.post_user = u1.id AND o.op_user = u2.id ";

    @Override
    public OpTask findById(long id) {
        return null;
    }
}
