package pers.kamisaka.blog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import pers.kamisaka.blog.entity.Message;

import java.util.List;

@Repository
@Mapper
public interface MessageMapper {
    List<Message> getMessages(Long index,int pageSize);
    int addMessage(Message message);
    int deleteMessage(Long mid);
    Long countMessage();
}
