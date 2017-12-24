package com.zhongyun.smoke.service;

import com.zhongyun.smoke.dao.mysql.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by caozhennan on 2017/12/10.
 */
@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;

    public Map<String, Map<String, String>> findBy(long projectId, int district, int city, int province, long start, long end) {
        if (projectId >= 0) {
            return reportRepository.findByProjectId(projectId, start, end);
        } else if (district > 0) {
            return reportRepository.findByLocation("district", start, end);
        } else if (city > 0) {
            return reportRepository.findByLocation("city", start, end);
        } else if (province > 0) {
            return reportRepository.findByLocation("province", start, end);
        } else {
            return reportRepository.findInAllProject(start, end);
        }
    }
}
