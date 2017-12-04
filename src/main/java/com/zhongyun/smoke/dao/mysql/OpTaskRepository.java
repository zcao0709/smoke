package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.model.OpTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by caozhennan on 2017/11/30.
 */
public interface OpTaskRepository extends JpaRepository<OpTask, Long> {
//    @Query(value = "SELECT op_task.*, sensor.* FROM op_task LEFT JOIN sensor ON op_task.eui = sensor.id where op_task.id = ?1", nativeQuery = true)
//    OpTask findOne(Long id);
}
