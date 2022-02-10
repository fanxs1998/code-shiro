package com.code.test;

import com.code.shiro.realm.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * @author fxs
 * @Date 2021/12/30 10:33
 * @Description:
 */
public class CustomRealmTest {

    @Test
    public void testCustomRealm() {
        CustomRealm customRealm = new CustomRealm();

        // Shiro加密
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");//设置加密算法
        matcher.setHashIterations(1);//设置加密次数
        customRealm.setCredentialsMatcher(matcher);

        //1.构建SecurityManage环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);

        //2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        //收集用户身份和证明
        UsernamePasswordToken token = new UsernamePasswordToken("fxs", "123456");
        //提交身份和证明
        subject.login(token);

        //打印认证结果
        System.out.println("isAuthenticated:" + subject.isAuthenticated());

        //检查角色授权
        subject.checkRole("admin");
        subject.checkPermissions("addUser", "deleteUser");

    }
}
