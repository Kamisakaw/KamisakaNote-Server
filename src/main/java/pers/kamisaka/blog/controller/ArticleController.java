package pers.kamisaka.blog.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.kamisaka.blog.entity.Article;
import pers.kamisaka.blog.entity.Result;
import pers.kamisaka.blog.ratelimit.RateLimit;
import pers.kamisaka.blog.service.ArticleService;
import pers.kamisaka.blog.service.TagService;

import java.util.List;

@RestController
@Api
public class ArticleController {
    @Autowired
    ArticleService articleService;
    @Autowired
    TagService tagService;

    @ApiOperation(value = "分页获取文章信息")
    @GetMapping("/blog/article/{index}")
    @ApiImplicitParams(
            @ApiImplicitParam(type = "path",name = "index",value = "页码",required = true,dataType = "Long")
    )
    public List<Article> getArticles(@PathVariable Long index) {
        return articleService.getArticles(index);
    }

    @ApiOperation(value = "获取文章页数")
    @GetMapping("blog/article/count")
    public Long countArticlePages(){
        return articleService.countArticlePage();
    }

    @ApiOperation(value = "根据文章id获取文章信息")
    @ApiImplicitParams(
            @ApiImplicitParam(type = "path",name = "aid",value = "文章id",required = true,dataType = "Long")
    )
    @GetMapping("/blog/article/aid/{aid}")
    @RateLimit(key = "getArticleById",time = 10,count = 10)
    public Article getArticleById(@PathVariable Long aid)
    {
        return articleService.getArticleById(aid);
    }

    @ApiOperation(value = "根据文章目录获取文章信息")
    @ApiImplicitParams(
            @ApiImplicitParam(type = "path",name = "cid",value = "目录id",required = true,dataType = "Long")
    )
    @GetMapping("/blog/article/cid/{cid}")
    public List<Article> getArticlesByCategory(@PathVariable Long cid) {
        return articleService.getArticlesByCategory(cid);
    }

    @ApiOperation(value = "根据文章标签获取文章信息")
    @ApiImplicitParams(
            @ApiImplicitParam(type = "path",name = "tid",value = "标签id",required = true,dataType = "Long")
    )
    @GetMapping("/blog/article/tid/{tid}")
    public List<Article> getArticlesByTags(@PathVariable Long tid){
        return articleService.getArticlesByTid(tid);
    }

    @ApiOperation(value = "获取若干篇最新文章")
    @ApiImplicitParams(
            @ApiImplicitParam(type = "path",name = "limit",value = "获取文章数",required = true,dataType = "int")
    )
    @GetMapping("/blog/article/new/{size}")
    public List<Article>getArticlesByDate(@PathVariable Integer size){
        return articleService.getArticlesByDate(size);
    }

    @ApiOperation(value = "增加新文章")
    @PostMapping("/blog/admin/article/add")
    public Result addArticle(@RequestBody Article article) {
        int flag = articleService.addArticle(article);
        if (flag == 1) {
            return new Result("success", "成功添加了新文章!aid=" + article.getAid());
        } else {
            return new Result("fail", "文章添加失败！");
        }
    }

    @ApiOperation(value = "删除文章")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "aid",value = "要删除的文章id",required = true,dataType = "Long")
    )
    @PostMapping("/blog/admin/article/delete")
    public Result deleteArticle(Long aid) {
        int flag1 = articleService.deleteArticle(aid);
        if (flag1 == 1) {
            return new Result("success", "成功删除了文章!aid=" + aid);
        } else {
            return new Result("fail", "文章删除失败！");
        }
    }

    @ApiOperation(value = "更新文章")
    @PostMapping("/blog/admin/article/article/update")
    public Result updateArticle(@RequestBody Article article) {
        int flag = articleService.updateArticle(article);
        if (flag == 1) {
            return new Result("success", "成功更新了文章!aid=" + article.getAid());
        } else {
            return new Result("fail", "文章更新失败！");
        }
    }
}
