package com.ddasum.core.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public abstract class BaseController {

    @RestController
    @RequestMapping("/json")
    public static class JsonService {
        @PostMapping
        public Map<String, Object> jsonMap(@RequestBody Map<String, Object> data) {
            return data; // 데이터 받을 때 HashMap으로 받아지도록
        }
    }
}
