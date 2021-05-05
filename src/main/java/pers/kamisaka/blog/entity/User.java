package pers.kamisaka.blog.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;

@ApiModel
public class User {
    @ApiModelProperty("用户id")
    String uid;
    @ApiModelProperty("用户名")
    String username;
    @ApiModelProperty("登陆密码")
    String password;
    @ApiModelProperty("注册邮箱")
    String email;
    @ApiModelProperty(value = "注册日期",hidden = true)
    Timestamp registerDate;
    @ApiModelProperty(value = "最后登陆",hidden = true)
    Timestamp lastLoginDate;
    @ApiModelProperty(value = "头像路径",hidden = true)
    String avatarPath;

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Timestamp registerDate) {
        this.registerDate = registerDate;
    }

    public Timestamp getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Timestamp lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String uid, String username, String password, String email, Timestamp registerDate, Timestamp lastLoginDate, String avatarPath) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.email = email;
        this.registerDate = registerDate;
        this.lastLoginDate = lastLoginDate;
        this.avatarPath = avatarPath;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", registerDate=" + registerDate +
                ", lastLoginDate=" + lastLoginDate +
                ", avatarPath='" + avatarPath + '\'' +
                '}';
    }
}
