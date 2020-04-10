package com.me.cloud.chat.dao;

import com.me.cloud.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User findUserById(Integer userId) {
        String sql = "SELECT * FROM tb_user WHERE user_id = " + userId;

        return jdbcTemplate.query(sql, new UserRowMapper()).get(0);
    }

    @Override
    public User findUserByUsernameAndPassword(String username, String password) {
        String sql = "SELECT user_name as userName, user_password as userPassword, user_balance as userBalance FROM tb_user WHERE user_name = " + username + " and user_password = " + password;

        return jdbcTemplate.queryForObject(sql, User.class);
    }

    private class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {

            Integer userId = resultSet.getInt("user_id");
            String userName = resultSet.getString("user_name");
            String userPassword = resultSet.getString("user_password");
            Double userBalance = resultSet.getDouble("user_balance");

            User user = new User();
            user.setUserId(userId);
            user.setUserName(userName);
            user.setUserPassword(userPassword);
            user.setUserBalance(userBalance);

            return user;
        }

    }

}
