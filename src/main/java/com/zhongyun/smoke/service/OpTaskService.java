package com.zhongyun.smoke.service;

import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.dao.mysql.OpTaskRepository;
import com.zhongyun.smoke.dao.mysql.ProjectRepository;
import com.zhongyun.smoke.dao.mysql.SensorRepository;
import com.zhongyun.smoke.dao.mysql.UserRepository;
import com.zhongyun.smoke.model.OpTask;
import com.zhongyun.smoke.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    public ProjectRepository projectRepository;

    public OpTask add(OpTask opTask) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
//        opTask.setStatus(Util.OPTASK_UNSOLVED);
        opTask.setMtime(ts);
        opTask.setCtime(ts);
        return opTaskRepository.save(opTask);
    }

    public void delete(long id) {
        opTaskRepository.delete(id);
    }

    @Transactional
    public OpTask update(OpTask opTask) {
        if (opTask.getId() == 0) {
            throw new IllegalArgumentException("no op task id");
        }
        opTaskRepository.updateById(opTask.getOpUser(), opTask.getHandler(), opTask.getWorker(), opTask.getStatus(), opTask.getId());
        return opTask;
    }

    public OpTask find(long id) {
        OpTask ot = opTaskRepository.findById(id);
        if (ot != null) {
            complete(ot);
        }
        return ot;
    }

    public Page<OpTask> findBaseByEui(long eui, Pageable pageable) {
        return opTaskRepository.findByEui(eui, pageable);
    }

    public Page<OpTask> findAll(Pageable pageable) {
        Page<OpTask> ots = opTaskRepository.findByCauseIn(Util.OpTaskCause, pageable);
        ots.getContent().forEach(v -> complete(v));

        return ots;
    }

    public Page<OpTask> findUnsolved(Pageable pageable) {
        Page<OpTask> ots = opTaskRepository.findByCauseInAndStatus(Util.OpTaskCause, Util.OPTASK_UNSOLVED, pageable);
        ots.getContent().forEach(v -> complete(v));

        return ots;
    }

    private void complete(OpTask ot) {
        ot.setPoster(userRepository.findOne(ot.getPostUser()));
        ot.setOp(userRepository.findOne(ot.getOpUser()));
        ot.setSensor(sensorRepository.findByEui(ot.getEui()));
        Project p = projectRepository.findOne(ot.getProjectId());
        if (p == null) {
            ot.setProject(null);
        } else {
            ot.setProject(p);
        }
        ot.setExpired(System.currentTimeMillis() > Util.OPTASK_EXPIRED + ot.getCtime().getTime());
    }
}
