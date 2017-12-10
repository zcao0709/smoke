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

    public Map<String, String> findByProjectId(long projectId, long start, long end) {
        return reportRepository.findByProjectId(projectId, start, end);
    }
}
