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

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    @PostMapping("/")
    public ResponseEntity<String> upload(@RequestParam(value = "project", defaultValue = "0") long projectId) throws IOException, FileUploadException {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        if (projectId <= 0) {
            return Resp.bad("invalid project");
        }
        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload();
        String filename = "p" + projectId;

        // Parse the request
        FileItemIterator iter = upload.getItemIterator(request);
        while (iter.hasNext()) {
            FileItemStream item = iter.next();

            InputStream stream = item.openStream();
            if (!item.isFormField()) {
                // Process the input stream
                OutputStream out = new FileOutputStream(config.getFileLocation() + filename);
                IOUtils.copy(stream, out);
                stream.close();
                out.close();
            }
        }
        projectService.updateGraph(projectId, filename);
        return Resp.ok("upload success");
    }
}
