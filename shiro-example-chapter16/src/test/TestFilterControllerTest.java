

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
        "classpath:spring-config.xml",
        "classpath:spring-mvc.xml"
})
//
public class TestFilterControllerTest {

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
     *      1.将SysUserFilter添加到上下文中, sysUserFilter 里面获取当前用户添加到request attribute里面
     *      2.添加CurrentUserMethodArgumentResolver 添加到上下文，将request attribute 里面的值取出来
     * 测试自定义CurrentUser注解是否讲参数正确注入到method 参数中
     * @throws Exception
     */
    @Test
    public void test_args() throws Exception {

        this.mockMvc.perform(get("/say"))
                .andDo(print())
                .andExpect(content().string("success"));

    }

}
