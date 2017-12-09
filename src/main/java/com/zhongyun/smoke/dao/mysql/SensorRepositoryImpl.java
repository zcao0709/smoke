package com.zhongyun.smoke.dao.mysql;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by caozhennan on 2017/12/8.
 */
public class SensorRepositoryImpl implements SensorOther {
    @PersistenceContext
    private EntityManager em;

    @Override
    public int updateLatiAndLongiByGatewayId(long gatewayId) {
        String update = "UPDATE sensor s1 INNER JOIN sensor s2 ON s1.gateway_id = s2.id SET s1.lati = s2.lati, s1.longi = s2.longi, mtime = NOW() WHERE s2.id = ?1";
        return em.createNativeQuery(update).setParameter(1, gatewayId).executeUpdate();
    }
}
