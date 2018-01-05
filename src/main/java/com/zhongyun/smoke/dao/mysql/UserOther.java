package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.common.Page;
import com.zhongyun.smoke.model.User;

/**
 * Created by caozhennan on 2018/1/5.
 */
public interface UserOther {
    Page<User> findLike(String name, String fullname, String type, int page, int limit);
    User findByName(String name);
    User findById(long id);
}
