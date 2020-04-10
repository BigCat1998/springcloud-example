package com.me.cloud.user.dao;

public interface UserRepository {

    int updateUserByUserId(Integer userId, Double userBalance);

    Double selectUserBalance(Integer userId);
}
