package pers.kamisaka.blog.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.kamisaka.blog.entity.Result;
import pers.kamisaka.blog.entity.Tag;
import pers.kamisaka.blog.service.TagService;

import java.util.List;

@RestController
@Api
public class TagController {
    @Autowired
    TagService tagService;

    @ApiOperation("获取全部标签信息")
    @GetMapping("/blog/tag")
    public List<Tag> getTags(){
        return tagService.getTags();
    }

    @ApiOperation("根据文章id获取标签信息")
    @ApiImplicitParams(
            @ApiImplicitParam(type = "path",name = "aid",value = "文章id",required = true,dataType = "Long")
    )
    @GetMapping("/blog/tag/aid/{aid}")
    public List<Tag> getTagsByAid(@PathVariable Long aid){
        return tagService.getTagsByAid(aid);
    }

    @ApiOperation("添加新标签信息")
    @PostMapping("/blog/admin/tag/add")
    public Result addTag(@RequestBody Tag tag){
        int flag = tagService.addTag(tag);
        if(flag == 1){
            return new Result("success","成功添加了新标签！aid="+tag.getTid());
        }
        else{
            return new Result("fail","标签添加失败！");
        }
    }

    @ApiOperation("为文章添加标签")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aid",value = "文章id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "tid",value = "标签id",required = true,dataType = "Long")
    })
    @PostMapping("/blog/admin/tag/addToArticle")
    public Result addTagToArticle(Long aid,Long tid){
        int flag = tagService.addTagToArticle(aid,tid);
        if(flag == 1){
            return new Result("success","成功添加了标签到文章！aid="+aid+"，tid="+tid);
        }
        else{
            return new Result("fail","标签添加失败！");
        }
    }

    @ApiOperation("更新标签信息")
    @PostMapping("/blog/admin/tag/update")
    public Result updateTag(@RequestBody Tag tag){
        int flag = tagService.updateTag(tag);
        if(flag == 1){
            return new Result("success","成功更新了标签！aid="+tag.getTid());
        }
        else{
            return new Result("fail","标签更新失败！");
        }
    }

    @ApiOperation("删除标签信息")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "tid",value = "标签id",required = true,dataType = "Long")
    )
    @PostMapping("/blog/admin/tag/delete")
    public Result deleteTag(Long tid){
        int flag = tagService.deleteTag(tid);
        if(flag == 1){
            return new Result("success","成功删除了标签！tid="+tid);
        }
        else{
            return new Result("fail","标签删除失败！");
        }
    }

    @ApiOperation("为文章删除标签")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aid",value = "文章id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "tid",value = "标签id",required = true,dataType = "Long")
    })
    @PostMapping("/blog/admin/tag/deleteToArticle")
    public Result deleteTagToArticle(Long aid,Long tid){
        if(!tagService.checkTA(aid, tid)){
            int flag = tagService.deleteTagToArticle(aid,tid);
            if(flag == 1){
                return new Result("success","成功将标签从文章中删除！aid="+aid+"，tid="+tid);
            }
            else{
                return new Result("fail","标签删除失败！");
            }
        }
        else {
            return new Result("fail","文章已经拥有该标签！");
        }
    }

    @ApiOperation("为文章修改标签")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aid",value = "文章id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "formerTid",value = "原来的标签id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "newTid",value = "想要修改成的标签id",required = true,dataType = "Long")
    })
    @PostMapping("/blog/admin/tag/updateToArticle")
    public Result updateTagToArticle(Long aid,Long formerTid,Long newTid){
        int flag = tagService.updateTA(aid, formerTid, newTid);
            if(flag == -1){
                return new Result("success","未发生标签修改");
            }
            else if(flag == 1){
                return new Result("success","成功将文章的标签进行更新！aid="+aid+"，tid="+newTid);
            }
            else{
                return new Result("fail","文章标签更新失败！");
            }

    }

    @ApiOperation("为文章删除所有标签")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "aid",value = "文章id",required = true,dataType = "Long")
    )
    @PostMapping("/blog/admin/tag/deleteAllTagsToArticle")
    public Result deleteAllTagsToArticle(Long aid){
        int flag = tagService.deleteAllTagsToArticle(aid);
        if(flag > 0){
            return new Result("success","成功删除文章的所有标签！aid="+aid);
        }
        else{
            return new Result("fail","删除标签失败！");
        }
    }
}
