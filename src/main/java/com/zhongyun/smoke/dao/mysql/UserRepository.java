package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by caozhennan on 2017/11/26.
 */
public interface UserRepository extends JpaRepository<User, Long> {
//    Page<User> findByNameLike(String partName, Pageable pageable);
    Page<User> findByNameLikeAndPhone1LikeAndPhone2LikeAndTypeLike(String name, String phone1, String phone2, String type, Pageable pageable);
}
