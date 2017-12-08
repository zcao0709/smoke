package com.zhongyun.smoke.service;

import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.dao.mysql.OpTaskRepository;
import com.zhongyun.smoke.dao.mysql.SensorRepository;
import com.zhongyun.smoke.dao.mysql.UserRepository;
import com.zhongyun.smoke.model.OpTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Created by caozhennan on 2017/11/30.
 */
@Service
public class OpTaskService {
    @Autowired
    public OpTaskRepository opTaskRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public SensorRepository sensorRepository;

    public OpTask add(OpTask opTask) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        opTask.setStatus(Util.OPTASK_UNSOLVED);
        opTask.setMtime(ts);
        opTask.setCtime(ts);
        return opTaskRepository.save(opTask);
    }

    public void delete(long id) {
        opTaskRepository.delete(id);
    }

    public OpTask update(OpTask opTask) {
        opTask.setMtime(new Timestamp(System.currentTimeMillis()));
        return opTaskRepository.save(opTask);
    }

    public OpTask find(long id) {
        OpTask ot = opTaskRepository.findOne(id);
        complete(ot);
        return ot;
    }

    public Page<OpTask> findAll(Pageable pageable) {
        Page<OpTask> ots = opTaskRepository.findAll(pageable);
        ots.getContent().forEach(v -> complete(v));

        return ots;
    }

    public Page<OpTask> findUnsolved(Pageable pageable) {
        Page<OpTask> ots = opTaskRepository.findByStatus(Util.OPTASK_UNSOLVED, pageable);
        ots.getContent().forEach(v -> complete(v));

        return ots;
    }

    private void complete(OpTask ot) {
        ot.setPoster(userRepository.findOne(ot.getPostUser()));
        ot.setOp(userRepository.findOne(ot.getOpUser()));
        ot.setSensor(sensorRepository.findByEui(ot.getEui()));
    }
}
