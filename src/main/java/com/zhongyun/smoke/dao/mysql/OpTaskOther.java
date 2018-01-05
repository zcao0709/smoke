package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.common.Page;
import com.zhongyun.smoke.model.OpTask;

import java.util.Date;
import java.util.Set;

/**
 * Created by caozhennan on 2017/12/10.
 */
public interface OpTaskOther {
    OpTask findById(long id);
    Page<OpTask> findByCauseInAndStatus(Set<String> cause, String status, int page, int limit);
    Page<OpTask> findLike(String eui, String cause, String handler, String worker, String status, Date ctimeStart, Date ctimeEnd, int page, int limit);
    Page<OpTask> findLike(long projectId, String eui, String cause, String handler, String worker, String status, Date ctimeStart, Date ctimeEnd, int page, int limit);
}
