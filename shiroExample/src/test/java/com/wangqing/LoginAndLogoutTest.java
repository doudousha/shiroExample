package com.wangqing;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;


public class LoginAndLogoutTest {


    private Subject subject;

    @Before
    public void init() {
        // 初始化securityManager工厂方法
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        // 获取securityManager
        SecurityUtils.setSecurityManager(factory.getInstance());
        // subject 验证登录
        subject = SecurityUtils.getSubject();
    }

    /**
     * 说明: 正确的用户名密码，尝试登录操作，无异常抛出
     */
    @Test
    public void test_login_not_error() {
        final String userName = "wang";
        final String pwd = "123";

        UsernamePasswordToken token = new UsernamePasswordToken(userName, pwd);
        subject.login(token);
    }

    /**
     * 说明: 密码错误时，抛出异常
     * 前置条件: 密码错误
     * 期望: 抛出IncorrectCredentialsException 登录异常
     */
    @Test(expected = IncorrectCredentialsException.class)
    public void test_login_throw_exception() {
        final String userName = "wang";
        final String pwd = "xxx";

        UsernamePasswordToken token = new UsernamePasswordToken(userName, pwd);
        subject.login(token);
    }

    /**
     * 说明: 正确的用户名密码登录后，用户已经登录
     */

    @Test
    public void test_isAuthenticated_return_true(){
        final String userName = "wang";
        final String pwd = "123";

        UsernamePasswordToken token = new UsernamePasswordToken(userName, pwd);
        subject.login(token);

        assertTrue(subject.isAuthenticated());
    }


    /**
     * 说明：错误的用户名密码登录后，用户状态为'未登录'
     */
    @Test
    public void test_isAuthenticated_return_false(){
        final String userName = "wang";
        final String pwd = "xxx";

        UsernamePasswordToken token = new UsernamePasswordToken(userName, pwd);

        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        assertFalse(subject.isAuthenticated());
    }
}

