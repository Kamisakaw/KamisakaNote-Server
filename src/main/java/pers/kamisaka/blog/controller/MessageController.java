package pers.kamisaka.blog.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.kamisaka.blog.entity.Message;
import pers.kamisaka.blog.entity.Result;
import pers.kamisaka.blog.service.MessageService;

import java.util.List;

@RestController
public class MessageController {
    @Autowired
    MessageService messageService;

    @ApiOperation("获取留言信息")
    @GetMapping("/blog/message/{index}")
    public List<Message> getMessages(@PathVariable Long index){
        return messageService.getMessages(index);
    }

    @ApiOperation(value = "获取留言页数")
    @GetMapping("blog/message/count")
    public Long countArticlePages(){
        return messageService.countMessagePage();
    }

    @ApiOperation("添加新留言")
    @PostMapping("/blog/message/add")
    public Result addMessage(Message message){
        System.out.println(message);
        int flag = messageService.addMessage(message);
        if(flag == 1){
            return new Result("success","成功添加留言!");
        }
        else{
            return new Result("fail","添加留言失败！");
        }
    }

    @ApiOperation("删除留言")
    @PostMapping("/blog/admin/message/delete")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "mid",value = "要删除的评论id",required = true,dataType = "Long")
    )
    public Result deleteMessage(Long mid){
        int flag = messageService.deleteMessage(mid);
        if(flag == 1){
            return new Result("success","成功删除留言!");
        }
        else{
            return new Result("fail","删除留言失败！");
        }
    }
}
