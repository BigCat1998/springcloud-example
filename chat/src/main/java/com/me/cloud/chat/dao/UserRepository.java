package com.me.cloud.chat.dao;

import com.me.cloud.entity.User;

public interface UserRepository {

    /**
     * 根据 userId 查询用户信息
     *
     * @param userId
     * @return
     */
    User findUserById(Integer userId);

    /**
     * 查询用户
     *
     * @param username
     * @param password
     * @return
     */
    User findUserByUsernameAndPassword(String username, String password);

}
