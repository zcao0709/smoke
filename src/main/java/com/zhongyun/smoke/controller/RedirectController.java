package com.zhongyun.smoke.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by caozhennan on 2017/12/17.
 */
@Controller
@RequestMapping("/")
public class RedirectController {

    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public String redirect() {
        return "redirect:/html/index.html";
    }
}
