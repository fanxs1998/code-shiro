package com.code.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * @author fxs
 * @Date 2021/12/29 16:36
 * @Description:
 */
public class IniRealmTest {

    @Test
    public void testIniRealm() {

        IniRealm iniRealm = new IniRealm("classpath:user.ini");

        //1.构建SecurityManage环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);

        //2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        //收集用户身份和证明
        UsernamePasswordToken token = new UsernamePasswordToken("fxs", "123456");
        //提交身份和证明
        subject.login(token);

        //打印认证结果
        System.out.println("isAuthenticated:" + subject.isAuthenticated());

        //校验角色
        subject.checkRoles("admin");

        //校验权限
        subject.checkPermission("addUser");

    }
}
