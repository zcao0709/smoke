package com.zhongyun.smoke.service;

import com.zhongyun.smoke.dao.mysql.GatewayRepository;
import com.zhongyun.smoke.model.Gateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by caozhennan on 2017/11/30.
 */
@Service
public class GatewayService {
    @Autowired
    public GatewayRepository repository;

    public Gateway add(Gateway gateway) {
        gateway.setMtime(new Timestamp(System.currentTimeMillis()));
        gateway.setCtime(new Timestamp(System.currentTimeMillis()));
        return repository.save(gateway);
    }

    public void delete(long id) {
        repository.delete(id);
    }

    public Gateway update(Gateway gateway) {
        gateway.setMtime(new Timestamp(System.currentTimeMillis()));
        return repository.save(gateway);
    }

    public Gateway find(long id) {
        return repository.findOne(id);
    }

    public List<Gateway> findAll() {
        return repository.findAll();
    }
}
