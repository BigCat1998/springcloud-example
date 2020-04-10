package com.me.cloud.chat.dao;

import com.me.cloud.entity.Message;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class MessageRepositoryImpl implements MessageRepository {

    private final JdbcTemplate jdbcTemplate;

    public MessageRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Message save(Message message) {
        String sql = "insert into tb_message(create_date, is_receive, message_data, receive_id, send_id) values(?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            // 预处理 注意参数 PreparedStatement.RETURN_GENERATED_KEYS
            PreparedStatement ps = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setDate(1, new java.sql.Date(message.getCreateDate().getTime()));
            ps.setInt(2, message.getIsReceive());
            ps.setString(3, message.getMessageData());
            ps.setInt(4, message.getReceiveId());
            ps.setInt(5, message.getSendId());
            return ps;
        }, keyHolder);

        String qSql = "select * from tb_message where id = " + keyHolder.getKey().intValue();
        return  jdbcTemplate.query(qSql, new MessageRowMapper()).get(0);
    }

    @Transactional
    @Override
    public int updateByReceiveId(Integer receiveId) {

        String sql = "update tb_message set is_receive = 0 where receive_id = ?";
        int res = jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setInt(1, receiveId);
        });

        return res;
    }

    @Override
    public List<Message> findByMessageList(Integer sendId, Integer receiveId, Integer isReceive, String createDate, Integer page, Integer total) {
        String sql = "select * from tb_message where send_id = " + sendId + " and receive_id = " + receiveId + " and is_receive = " + isReceive + " and create_date < '" + createDate + "' order by id desc limit " + page + ", " + total;
        return jdbcTemplate.query(sql, new MessageRowMapper());
    }

    private class MessageRowMapper implements RowMapper<Message> {

        @Override
        public Message mapRow(ResultSet resultSet, int i) throws SQLException {

            Integer id = resultSet.getInt("id");
            Integer isReceive = resultSet.getInt("is_receive");
            Integer receiveId = resultSet.getInt("receive_id");
            Integer sendId = resultSet.getInt("send_id");
            String messageData = resultSet.getString("message_data");
            Date createDate = resultSet.getDate("create_date");

            Message message = new Message();
            message.setId(id);
            message.setCreateDate(createDate);
            message.setIsReceive(isReceive);
            message.setReceiveId(receiveId);
            message.setMessageData(messageData);
            message.setSendId(sendId);

            return message;
        }

    }

}
