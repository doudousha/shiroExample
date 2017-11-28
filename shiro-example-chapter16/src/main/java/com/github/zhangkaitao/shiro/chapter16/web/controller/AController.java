package com.github.zhangkaitao.shiro.chapter16.web.controller;


import com.github.zhangkaitao.shiro.chapter16.web.bind.annotation.CurrentUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AController {

    @RequestMapping("/say")
    @ResponseBody
    public String say(@CurrentUser String name) {
        System.out.println("say");
        return name ;
    }
}
