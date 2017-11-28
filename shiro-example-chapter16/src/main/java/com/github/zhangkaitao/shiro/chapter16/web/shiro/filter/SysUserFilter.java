package com.github.zhangkaitao.shiro.chapter16.web.shiro.filter;

import com.github.zhangkaitao.shiro.chapter16.Constants;
import com.github.zhangkaitao.shiro.chapter16.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-2-15
 * <p>Version: 1.0
 */
public class SysUserFilter extends PathMatchingFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private Environment env;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        String username = (String) SecurityUtils.getSubject().getPrincipal();

        if (env.containsProperty("context") && env.getProperty("context") == "test") {
            request.setAttribute(Constants.CURRENT_USER, "success");
        } else {
            request.setAttribute(Constants.CURRENT_USER, userService.findByUsername(username));
        }


        System.out.println("onPrehandler");
        return true;
    }
}
