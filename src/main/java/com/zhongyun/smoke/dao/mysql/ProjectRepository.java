package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by caozhennan on 2017/11/26.
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findByNameLikeAndProvinceLikeAndCityLikeAndDistrictLikeAndAddressLike(String name, String province, String city, String district, String address, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE project SET name = ?1, province = ?2, city = ?3, district = ?4, address = ?5, room_count = ?6, mtime = NOW() WHERE id = ?7", nativeQuery = true)
    void updateById(String name, String province, String city, String district, String address, int roomCount, long id);
}
