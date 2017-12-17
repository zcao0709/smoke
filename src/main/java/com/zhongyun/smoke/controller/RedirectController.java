package com.zhongyun.smoke.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by caozhennan on 2017/12/17.
 */
@Controller
@RequestMapping("/api")
public class RedirectController {
    @Autowired
    private HttpServletRequest request;

    private static final Logger logger = LoggerFactory.getLogger("RedirectController");

    @RequestMapping(value = "/redirect", method = RequestMethod.POST)
    public String redirect() {

        logger.info(request.getRequestURL().append("?").append(request.getQueryString()).toString());

        return "redirect:/html/index.html";
    }
}
