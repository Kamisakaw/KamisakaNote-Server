package pers.kamisaka.blog.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;

@ApiModel
public class Message {
    @ApiModelProperty(value = "留言id")
    Long mid;
    @ApiModelProperty(value = "留言内容")
    String content;
    @ApiModelProperty(value = "留言人")
    String author;
    @ApiModelProperty(value = "留言日期")
    Timestamp createTime;

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Message(Long mid, String content, String author, Timestamp createTime) {
        this.mid = mid;
        this.content = content;
        this.author = author;
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "mid=" + mid +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", createTime=" + createTime +
                '}';
    }

    public Message() {
    }
}
