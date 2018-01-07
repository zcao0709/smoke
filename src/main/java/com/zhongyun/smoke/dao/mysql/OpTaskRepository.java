package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.model.OpTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.Set;

/**
 * Created by caozhennan on 2017/11/30.
 */
public interface OpTaskRepository extends JpaRepository<OpTask, Long>, OpTaskOther {
    long countByEui(long eui);

    @Modifying
    @Query(value = "UPDATE op_task SET op_user = ?1, op_time = NOW(), handler = ?2, worker = ?3, status = ?4, mtime = NOW() WHERE id = ?5", nativeQuery = true)
    void updateByIdWithOpTime(long opUser, String handler, String worker, String status, long id);

    @Modifying
    @Query(value = "UPDATE op_task SET op_user = ?1, handler = ?2, worker = ?3, status = ?4, mtime = NOW() WHERE id = ?5", nativeQuery = true)
    void updateById(long opUser, String handler, String worker, String status, long id);
}
