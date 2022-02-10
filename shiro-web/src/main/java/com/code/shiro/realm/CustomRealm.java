package com.code.shiro.realm;

import com.code.dao.UserDao;
import com.code.vo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author fxs
 * @Date 2021/12/30 14:59
 * @Description:
 */
public class CustomRealm extends AuthorizingRealm {

    @Resource
    private UserDao userDao;

    /**
     * 授权
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        // 从数据库或者缓存获取角色数据和权限数据
        Set<String> roles = getRolesByUsername(username);
        Set<String> permissions = getPermissionsByUsername(username);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setStringPermissions(permissions);
        authorizationInfo.setRoles(roles);

        return authorizationInfo;
    }

    /**
     * 从数据库或者缓存获取权限信息
     * @param username
     * @return
     */
    private Set<String> getPermissionsByUsername(String username) {
        Set<String> sets = new HashSet<>();
        sets.add("deleteUser");
        sets.add("addUser");
        return sets;
    }

    /**
     * 从数据库或缓存获取角色信息
     * @param username
     * @return
     */
    private Set<String> getRolesByUsername(String username) {
        System.out.println("从数据库中获取角色信息...");
        List<String> list = userDao.queryRolesByUsername(username);
        Set<String> sets = new HashSet<>(list);
        return sets;
    }

    /**
     * 认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 1.从主体传过来的认证信息中，获得用户名
        String username = (String) token.getPrincipal();
        // 2.通过用户名从数据库获取凭证
        String password = getPasswordByUsername(username);
        if (password == null) {
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, password, "customRealm");

        //盐值
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("qwer!@#$"));

        return authenticationInfo;
    }

    /**
     * 从数据库查询凭证
     * @param username
     * @return
     */
    private String getPasswordByUsername(String username) {
        System.out.println("从数据库中获取用户信息...");
        User user = userDao.getUserByUsername(username);
        if (user != null) {
            return user.getPassword();
        }

        return null;
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123456","qwer!@#$");
        System.out.println(md5Hash);
    }
}
