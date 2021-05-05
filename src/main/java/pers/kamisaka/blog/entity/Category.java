package pers.kamisaka.blog.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;

@ApiModel
public class Category {
    @ApiModelProperty(value = "目录id")
    Long cid;
    @ApiModelProperty(value = "目录名")
    String categoryName;
    @ApiModelProperty(value = "创建时间",hidden = true)
    Timestamp createDate;

    @Override
    public String toString() {
        return "Category{" +
                "cid=" + cid +
                ", categoryName='" + categoryName + '\'' +
                ", createDate=" + createDate +
                '}';
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public Category(Long cid, String categoryName, Timestamp createDate) {
        this.cid = cid;
        this.categoryName = categoryName;
        this.createDate = createDate;
    }

    public Category() {
    }
}
