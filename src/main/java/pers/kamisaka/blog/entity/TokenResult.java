package pers.kamisaka.blog.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class TokenResult extends Result {
    @ApiModelProperty(value = "返回的token")
    String token;

    public TokenResult(String status, String msg, String token) {
        super(status, msg);
        this.token = token;
    }

    public TokenResult() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
