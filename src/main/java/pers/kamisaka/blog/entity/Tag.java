package pers.kamisaka.blog.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;

@ApiModel
public class Tag {
    @ApiModelProperty("标签id")
    Long tid;
    @ApiModelProperty("标签名")
    String tagName;
    @ApiModelProperty(value = "创建日期",hidden = true)
    Timestamp createDate;

    @Override
    public String toString() {
        return "Tag{" +
                "tid=" + tid +
                ", tagName='" + tagName + '\'' +
                ", createDate=" + createDate +
                '}';
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Tag(Long tid, String tagName, Timestamp createDate) {
        this.tid = tid;
        this.tagName = tagName;
        this.createDate = createDate;
    }

    public Tag() {
    }
}
