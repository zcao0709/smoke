package com.zhongyun.smoke.service;

import static com.zhongyun.smoke.common.Util.*;

import com.zhongyun.smoke.common.Page;
import com.zhongyun.smoke.dao.mysql.OpTaskRepository;
import com.zhongyun.smoke.dao.mysql.ProjectRepository;
import com.zhongyun.smoke.dao.mysql.SensorRepository;
import com.zhongyun.smoke.dao.mysql.UserRepository;
import com.zhongyun.smoke.model.OpTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

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
            throw new IllegalArgumentException(ERR_INVALID_ID);
        }
        if (opTask.getStatus().equals(OPTASK_UNSOLVED)) {
            opTaskRepository.updateById(opTask.getOpUser(), opTask.getHandler(), opTask.getWorker(), opTask.getStatus(), opTask.getId());
        } else {
            opTaskRepository.updateByIdWithOpTime(opTask.getOpUser(), opTask.getHandler(), opTask.getWorker(), opTask.getStatus(), opTask.getId());
        }
        return opTask;
    }

    public OpTask find(long id) {
        OpTask ot = opTaskRepository.findById(id);
        return ot;
    }

    public Page<OpTask> findUnsolved(int page, int limit) {
        Page<OpTask> ots = opTaskRepository.findByCauseInAndStatus(OpTaskAlarmCause, OPTASK_UNSOLVED, page, limit);
//        ots.getContent().forEach(v -> complete(v));

        return ots;
    }

//    public Page<OpTask> findLike(long projectId, String eui, String cause, String handler, String worker, String status,
//                                 Date ctimeStart, Date ctimeEnd, Pageable pageable) {
//        if (projectId < 0) {
//            return opTaskRepository.findByEui16LikeAndCauseLikeAndHandlerLikeAndWorkerLikeAndStatusLikeAndCtimeBetween
//                    (like(eui), like(cause), like(handler), like(worker), like(status), ctimeStart, ctimeEnd, pageable);
//        } else {
//            return opTaskRepository.findByProjectIdAndEui16LikeAndCauseLikeAndHandlerLikeAndWorkerLikeAndStatusLikeAndCtimeBetween
//                    (projectId, like(eui), like(cause), like(handler), like(worker), like(status), ctimeStart, ctimeEnd, pageable);
//        }
//
//    }

    public Page<OpTask> findLike(long projectId, String eui, String cause, String handler, String worker, String status,
                                 Date ctimeStart, Date ctimeEnd, int page, int limit) {
        if (projectId < 0) {
            return opTaskRepository.findLike
                    (postLike(eui), like(cause), like(handler), like(worker), like(status), ctimeStart, ctimeEnd, page, limit);
        } else {
            return opTaskRepository.findLike
                    (projectId, postLike(eui), like(cause), like(handler), like(worker), like(status), ctimeStart, ctimeEnd, page, limit);
        }
    }

    private void complete(OpTask ot) {
//        ot.setEui16(String.format("%X", ot.getEui()));
//        ot.setPoster(userRepository.findOne(ot.getPostUser()));
//        ot.setOp(userRepository.findOne(ot.getOpUser()));
//        ot.setSensor(sensorRepository.findByEui(ot.getEui()));
//        ot.setProject(projectRepository.findOne(ot.getProjectId()));
        ot.setExpired(System.currentTimeMillis() > OPTASK_EXPIRED + ot.getCtime().getTime());
    }
}
