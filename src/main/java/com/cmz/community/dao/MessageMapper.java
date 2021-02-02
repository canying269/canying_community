package com.cmz.community.dao;

import com.cmz.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MessageMapper {
    //查询当前用户的会话列表,针对每个会话只返回一条最新的私信
    List<Message> selectConversations(int userId, int offset, int limit);

    //查询当前用户 会话数量
    int selectConversationCount(int userId);

    //查询某个会话包含的私信列表
    List<Message> selectLetters(String conversationId, int offset, int limit);

    //查询某个会话包含的私信数量
    int selectLetterCount(String conversationId);

    //查询未读私信的数量 1.查所有未读数量 2.某一会话未读数量
    int selectLetterUnreadCount(int userId, String conversationId);

    //增加私信
    int insertMessage(Message message);

    //修改消息状态 设置已读或设置删除消息
    int updateStatus(List<Integer> ids, int status);


    //查询某个主题下最新的通知
    Message selectLatestNotice(int userId, String topic);

    //查询某个主题所包含的通知数量
    int selectNoticeCount(int userId,String topic);

    //查询未读的通知数量
    int selectNoticeUnread(int userId,String topic);

    //查询某个主题包含的通知列表
    List<Message> selectNotices(int userId, String topic, int offset, int limit);
}
