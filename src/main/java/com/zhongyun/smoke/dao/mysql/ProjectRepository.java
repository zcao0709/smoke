package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by caozhennan on 2017/11/26.
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findByNameLike(String partName, Pageable pageable);
}
