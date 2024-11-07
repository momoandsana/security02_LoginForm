package com.web.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class indexController {

    @GetMapping("/")
    public String index() {
        log.info("/ 요청되었습니다...");
        return "index result";// 화면에 나오는 글씨
    }

    @GetMapping("/loginPage")
    public String loginPage() {
        log.info("loginPage 요청됨...");

        return "loginPage - LoginForm....";// 화면에 나오는 글씨
    }

    @GetMapping("/home")
    public String home() {
        log.info("/home 요청됨...");

        return "home -Authentication error";
    }
}

