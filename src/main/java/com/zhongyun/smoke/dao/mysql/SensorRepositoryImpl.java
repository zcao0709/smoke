package com.zhongyun.smoke.dao.mysql;

import com.zhongyun.smoke.common.Util;
import com.zhongyun.smoke.model.Sensor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by caozhennan on 2017/12/8.
 */
public class SensorRepositoryImpl implements SensorOther {
    @PersistenceContext
    private EntityManager em;

    @Override
    public int updateLatiAndLongiByGatewayId(long gatewayId) {
        String update = "UPDATE sensor s1 INNER JOIN sensor s2 ON s1.gateway_id = s2.id SET s1.lati = s2.lati, s1.longi = s2.longi, s1.mtime = NOW() WHERE s2.id = ?1";
        return em.createNativeQuery(update).setParameter(1, gatewayId).executeUpdate();
    }

    public int deleteUselessGwrx() {
        String update = "DELETE FROM sensor WHERE id in (SELECT id FROM sensor WHERE type = ?1 AND id NOT IN (SELECT DISTINCT(gateway_id) FROM sensor))";
        return em.createNativeQuery(update).setParameter(1, Util.SENSOR_GWRX).executeUpdate();
    }
}
