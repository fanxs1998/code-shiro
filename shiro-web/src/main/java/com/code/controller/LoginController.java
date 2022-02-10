package com.code.controller;

import com.code.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author fxs
 * @Date 2021/12/30 15:32
 * @Description:
 */
@Controller
public class LoginController {

    @ResponseBody
    @RequestMapping(value = "subLogin", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String subLogin(User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
            usernamePasswordToken.setRememberMe(user.getRememberMe());
            subject.login(usernamePasswordToken);
        } catch (AuthenticationException e) {
            return e.getMessage();
        }

        if (subject.hasRole("admin")) {
            return "登录成功,有admin权限";
        }
        return "登录成功,无admin权限";
    }

    //具备admim权限才可访问该链接
    //@RequiresPermissions("xxx")表示具备相应权限才可访问
    @RequiresRoles("admin")
    @RequestMapping(value = "/test_admin", method = RequestMethod.GET)
    @ResponseBody
    public String testAdmin() {
        return "testAdmin success!";
    }
    @RequiresRoles("admin1")
    @RequestMapping(value = "/test_admin1", method = RequestMethod.GET)
    @ResponseBody
    public String testAdmin1() {
        return "testAdmin1 success!";
    }

    // 测试内置过滤器
    @RequestMapping(value = "/test_role", method = RequestMethod.GET)
    @ResponseBody
    public String testRole() {
        return "testRole success!";
    }

    @RequestMapping(value = "/test_role1", method = RequestMethod.GET)
    @ResponseBody
    public String testRole1() {
        return "testRole1 success!";
    }

    @RequestMapping(value = "/test_perms", method = RequestMethod.GET)
    @ResponseBody
    public String testPerms() {
        return "testPerms success!";
    }

    @RequestMapping(value = "/test_perms1", method = RequestMethod.GET)
    @ResponseBody
    public String testPerms1() {
        return "testPerms1 success!";
    }

    /**
     * 测试自定义过滤器
     * @return
     */
    @RequestMapping(value = "/test_roles_or", method = RequestMethod.GET)
    @ResponseBody
    public String testRolesOr() {
        return "testRolesOr success!";
    }

}
