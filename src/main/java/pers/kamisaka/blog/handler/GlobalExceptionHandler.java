package pers.kamisaka.blog.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.kamisaka.blog.entity.AuthenticationException;
import pers.kamisaka.blog.entity.Result;
import pers.kamisaka.blog.entity.TokenResult;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    public Result AuthenticationExceptionHandler(AuthenticationException exception){
        Result result = new Result(exception.getStatus(),exception.getMsg());
        return result;
    }
}
