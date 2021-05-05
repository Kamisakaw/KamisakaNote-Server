package pers.kamisaka.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import pers.kamisaka.blog.entity.User;
import pers.kamisaka.blog.mapper.UserMapper;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    public List<User> getUsers(){
        return userMapper.getUsers();
    };
    public User getUserByUsername(String username){
        return userMapper.getUserByUsername(username);
    };
    public User getUserByUid(String uid){return userMapper.getUserByUid(uid);}
    public int addUser(User user){
        user.setRegisterDate(new Timestamp(System.currentTimeMillis()));
        return userMapper.addUser(user);
    };
    public int deleteUser(String uid){
        return userMapper.deleteUser(uid);
    };
    public int updateUser(User user){
        return userMapper.updateUser(user);
    };
}
