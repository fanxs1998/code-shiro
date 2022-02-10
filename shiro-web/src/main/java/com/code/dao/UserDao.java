package com.code.dao;

import com.code.vo.User;

import java.util.List;

/**
 * @author fxs
 * @Date 2021/12/30 15:34
 * @Description:
 */
public interface UserDao {

    /**
     * 通过用户名查询用户信息
     *
     * @param username
     * @return
     */
    User getUserByUsername(String username);

    /**
     * 通过用户名查询角色
     *
     * @param username
     * @return
     */
    List<String> queryRolesByUsername(String username);
}
