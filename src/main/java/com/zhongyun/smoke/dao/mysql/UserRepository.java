package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


/**
 * Created by caozhennan on 2017/11/26.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByNameLikeAndPhone1LikeAndPhone2LikeAndTypeLike(String name, String phone1, String phone2, String type, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE user SET name = ?1, phone1 = ?2, phone2 = ?3, type = ?4, mtime = NOW() WHERE id = ?5", nativeQuery = true)
    void updateById(String name, String phone1, String phone2, String type, long id);
}
