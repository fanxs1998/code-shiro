package com.code.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * @author fxs
 * @Date 2021/12/29 17:22
 * @Description:
 */
public class JdbcRealmTest {
    //创建数据源
    DruidDataSource dataSource = new DruidDataSource();

    //设置数据源
    {
        dataSource.setUrl("jdbc:mysql://localhost:3306/shiro_db");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
    }

    @Test
    public void testJdbcRealm() {
        //设置数据源
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);

        //设置查询权限开启
        jdbcRealm.setPermissionsLookupEnabled(true);

//        设置自定义表sql查询
//        jdbcRealm.setAuthenticationQuery("select password from users where username = ?");

        //1.构建SecurityManage环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        //2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();


        UsernamePasswordToken token = new UsernamePasswordToken("fxs", "123456");
        subject.login(token);

        //打印认证结果
        System.out.println("isAuthenticated:" + subject.isAuthenticated());

        subject.checkRole("admin");
        subject.checkRoles("admin", "user");
        subject.checkPermission("addUser");

    }
}
