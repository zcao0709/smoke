package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.model.OpTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

/**
 * Created by caozhennan on 2017/11/30.
 */
public interface OpTaskRepository extends JpaRepository<OpTask, Long> {
    Page<OpTask> findByCauseIn(Set<String> cause, Pageable pageable);
    Page<OpTask> findByCauseInAndStatus(Set<String> cause, String status, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE op_task SET op_user = ?1, op_time = NOW(), handler = ?2, worker = ?3, status = ?4, mtime = NOW() WHERE id = ?5", nativeQuery = true)
    void updateById(long opUser, String handler, String worker, String status, long id);

    long countByEui(long eui);
    Page<OpTask> findByEui(long eui, Pageable pageable);
}
