package pers.kamisaka.blog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import pers.kamisaka.blog.entity.User;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {
    List<User> getUsers();
    User getUserByUsername(String username);
    User getUserByUid(String uid);
    int countUsers();
    int addUser(User user);
    int deleteUser(String uid);
    int updateUser(User user);
}
