package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.common.Page;
import com.zhongyun.smoke.model.Project;

/**
 * Created by caozhennan on 2018/1/6.
 */
public interface ProjectOther {
    Page<Project> findLike(String name, String province, String city, String district, String address, String phone, int page, int limit);
    Project findById(long id);
}
