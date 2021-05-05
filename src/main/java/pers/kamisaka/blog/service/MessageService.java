package pers.kamisaka.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import pers.kamisaka.blog.entity.Message;
import pers.kamisaka.blog.mapper.MessageMapper;

import java.sql.Timestamp;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageMapper messageMapper;
    private int pageSize = 5;
    public List<Message> getMessages(Long index){
        Long begin = (index-1)*pageSize;
        return messageMapper.getMessages(begin,pageSize);
    };
    public Long countMessage(){return messageMapper.countMessage();}
    public Long countMessagePage(){
        Long res = (messageMapper.countMessage()/pageSize)+1;
        return res;}
    public int addMessage(Message message){
        message.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return messageMapper.addMessage(message);
    };
    public int deleteMessage(Long mid){
        return messageMapper.deleteMessage(mid);
    };
}
