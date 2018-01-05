package com.zhongyun.smoke.controller;

import com.zhongyun.smoke.ApplicationConfig;
import com.zhongyun.smoke.model.Resp;
import com.zhongyun.smoke.service.ProjectService;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by caozhennan on 2018/1/2.
 */
@RestController
@RequestMapping("/api/file/v1")
public class FileUploadController {
//    @Autowired
//    private StorageService storageService;

    @Autowired
    private ApplicationConfig config;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private HttpServletRequest request;

    private static final Logger logger = LoggerFactory.getLogger("FileUploadController");

    @PostMapping("")
    public ResponseEntity<String> upload(
            @RequestParam(value = "project", defaultValue = "0") long projectId,
            @RequestPart("file") MultipartFile file
    ) throws IOException {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        if (projectId <= 0) {
            return Resp.bad("invalid project");
        }
        String filename = "p" + projectId;
        OutputStream out = new FileOutputStream(config.getFileLocation() + "/" + filename);
        IOUtils.copy(file.getInputStream(), out);

        projectService.updateGraph(projectId, filename);
        return Resp.ok("upload success");
    }
}
