package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.model.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by caozhennan on 2017/11/26.
 */
public interface UserProjectRepository extends JpaRepository<UserProject, Long> {
    void deleteByUserIdAndProjectId(long userId, long projectId);
}
