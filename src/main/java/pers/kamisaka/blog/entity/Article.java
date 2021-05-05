package pers.kamisaka.blog.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import sun.rmi.runtime.Log;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
public class Article implements Serializable {

    @ApiModelProperty(value = "文章id")
    Long aid;
    @ApiModelProperty(value = "文章标题")
    String title;
    @ApiModelProperty(value = "作者")
    String author;
    @ApiModelProperty(value = "创建时间",hidden = true)
    Timestamp createDate;
    @ApiModelProperty(value = "更新时间",hidden = true)
    Timestamp updateDate;
    //表示所在目录的id
    @ApiModelProperty(value = "目录id")
    Long cid;
    @ApiModelProperty(value = "文章描述")
    String description;
    @ApiModelProperty(value = "文章内容")
    String content;
    @ApiModelProperty(value = "点击量")
    Integer view;

    @Override
    public String toString() {
        return "Article{" +
                "aid=" + aid +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", cid=" + cid +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", view=" + view +
                '}';
    }

    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Article(Long aid, String title, String author, Timestamp createDate, Timestamp updateDate, Long cid, String description, String content, Integer view) {
        this.aid = aid;
        this.title = title;
        this.author = author;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.cid = cid;
        this.description = description;
        this.content = content;
        this.view = view;
    }

    public Article() {
    }
}
