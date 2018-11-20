package com.choujiang.choujiang;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @RequestMapping("/aa")
    public String aa(){
        return "aaaa";
    }
}
