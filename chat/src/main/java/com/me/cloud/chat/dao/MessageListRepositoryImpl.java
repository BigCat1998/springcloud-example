package com.me.cloud.chat.dao;

import com.me.cloud.entity.MessageList;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MessageListRepositoryImpl implements MessageListRepository {

    private final JdbcTemplate jdbcTemplate;

    public MessageListRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MessageList save(MessageList messageList) {
        String sql = "insert into tb_message_list(receive_id, send_id) values(?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            // 预处理 注意参数 PreparedStatement.RETURN_GENERATED_KEYS
            PreparedStatement ps = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, messageList.getReceiveId());
            ps.setInt(2, messageList.getSendId());
            return ps;
        }, keyHolder);

        String qSql = "select * from tb_message_list where id = " + keyHolder.getKey().intValue();
        return  jdbcTemplate.query(qSql, new MessageListRowMapper()).get(0);
    }

    @Override
    public int findBySendIdAndReceiveId(Integer sendId, Integer receiveId) {
        String sql = "SELECT * FROM tb_message_list WHERE send_id = " + sendId +" and receive_id = " + receiveId;

        return jdbcTemplate.query(sql, new MessageListRowMapper()).size();
    }

    @Override
    public List<MessageList> findByReceiveId(Integer receiveId) {
        String sql = "select * from tb_message where receive_id = " + receiveId;
        return jdbcTemplate.query(sql, new MessageListRowMapper());
    }

    private class MessageListRowMapper implements RowMapper<MessageList> {

        @Override
        public MessageList mapRow(ResultSet resultSet, int i) throws SQLException {

            Integer id = resultSet.getInt("id");
            Integer receiveId = resultSet.getInt("receive_id");
            Integer sendId = resultSet.getInt("send_id");

            MessageList messageList = new MessageList();
            messageList.setId(id);
            messageList.setSendId(sendId);
            messageList.setReceiveId(receiveId);

            return messageList;
        }

    }
}
