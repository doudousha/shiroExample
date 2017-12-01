

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
        "classpath:spring-config.xml",
        "classpath:spring-mvc.xml"
})
@TestPropertySource(properties = {"test.holder=true"})
public class ShiroFilterControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;


    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter((Filter) wac.getBean("shiroFilter")).build();

    }

    /**
     * 具体逻辑：
     * 1.将SysUserFilter添加到上下文中, sysUserFilter 里面获取当前用户添加到request attribute里面
     * 2.添加CurrentUserMethodArgumentResolver 添加到上下文，将request attribute 里面的值取出来
     * 3. shiro url '/say' 配置添加sysUserFilter ,mockmvc 添加shiroFilter ,
     * 测试自定义CurrentUser注解是否讲参数正确注入到method 参数中
     *
     * @throws Exception
     */
    @Test
    public void test_shiro_view_content_eq_success() throws Exception {
        this.mockMvc.perform(get("/shiro/view"))
                .andDo(print())
                .andExpect(content().string("success"));
    }


    /**
     * 前置条件:
     * 1) 在spring-confog-shiro 中 已经配置'
     *
     * @throws Exception
     */
    @Test
    public void test_shiro_add_user() throws Exception {
        this.mockMvc.perform(get("/shiro/add"))
                .andDo(print())
                .andExpect(status().is(302));
    }


    @Test
    public void test_shiro_add_user_2() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/login").param("username", "admin").param("password", "123456"))
                .andDo(print())
                .andReturn();

        Cookie cookie = doGetCookie(mvcResult);
        this.mockMvc.perform(get("/shiro/add").cookie(cookie))
                .andDo(print())
                .andExpect(status().is(200));

    }

    private Cookie doGetCookie(MvcResult mvcResult) {
        String cookieStr = mvcResult.getResponse().getHeader("Set-Cookie").split(";")[0];

        System.out.println("cookieStr: " + cookieStr.split(";")[0]);
        String[] cookieArr = cookieStr.split("=");
        return new Cookie(cookieArr[0],cookieArr[1]) ;
    }


}
