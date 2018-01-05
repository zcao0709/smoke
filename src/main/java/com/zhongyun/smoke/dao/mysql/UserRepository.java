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
public interface UserRepository extends JpaRepository<User, Long>, UserOther {
    @Modifying
    @Query(value = "UPDATE user SET name = ?1, fullname = ?2, type = ?3, mtime = NOW() WHERE id = ?4", nativeQuery = true)
    void updateById(String name, String fullname, String type, long id);

    @Modifying
    @Query(value = "UPDATE user SET name = ?1, pass = ?2, fullname = ?3, type = ?4, mtime = NOW() WHERE id = ?5", nativeQuery = true)
    void updateByIdWithPass(String name, String pass, String fullname, String type, long id);
}
