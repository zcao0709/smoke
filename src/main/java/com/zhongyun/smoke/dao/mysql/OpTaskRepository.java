package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.model.OpTask;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by caozhennan on 2017/11/30.
 */
public interface OpTaskRepository extends JpaRepository<OpTask, Long> {
}
