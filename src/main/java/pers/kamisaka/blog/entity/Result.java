package pers.kamisaka.blog.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "请求响应类")
public class Result {
    @ApiModelProperty("请求是否成功")
    String status;
    @ApiModelProperty("响应信息")
    String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Result(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Result() {
    }
}
