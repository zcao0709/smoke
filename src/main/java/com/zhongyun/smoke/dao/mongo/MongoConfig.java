package com.zhongyun.smoke.dao.mongo;

import com.mongodb.Mongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.annotation.PostConstruct;

/**
 * Created by caozhennan on 2017/11/25.
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.zhongyun.smoke.dao.mongo")
public class MongoConfig extends AbstractMongoConfiguration {
    @Autowired
    private Mongo mongo;

    @Autowired
    private MappingMongoConverter converter;

    @Override
    public String getDatabaseName() {
        return "zhongyun";
    }

    @Override
    public Mongo mongo() throws Exception {
        return mongo;
    }

    @PostConstruct
    private void init() {
        converter.setMapKeyDotReplacement("_");
    }
}
