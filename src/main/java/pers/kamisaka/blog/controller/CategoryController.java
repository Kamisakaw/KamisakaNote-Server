package pers.kamisaka.blog.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.kamisaka.blog.entity.Category;
import pers.kamisaka.blog.entity.Result;
import pers.kamisaka.blog.service.CategoryService;

import java.util.List;

@RestController
@Api
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @ApiOperation(value = "获取全部目录信息")
    @GetMapping("/blog/category")
    public List<Category> getCategories(){
        return categoryService.getCategories();
    }

    @ApiOperation(value = "根据目录id获取目录信息")
    @ApiImplicitParams(
            @ApiImplicitParam(type = "path",name = "cid",value = "目录id",required = true,dataType = "Long")
    )
    @GetMapping("/blog/category/{cid}")
    public Category getCategoryById(@PathVariable Long cid){
        return categoryService.getCategoryById(cid);
    }

    @ApiOperation(value = "增加新目录")
    @PostMapping("/blog/admin/category/add")
    public Result addCategory(@RequestBody Category category){
        int flag = categoryService.addCategory(category);
        if(flag == 1){
            return new Result("success","成功添加了新目录！cid="+category.getCid());
        }
        else{
            return new Result("fail","目录添加失败！");
        }
    }

    @ApiOperation(value = "删除目录")
    @ApiImplicitParams(
            @ApiImplicitParam(type = "path",name = "cid",value = "目录id",required = true,dataType = "Long")
    )
    @PostMapping("/blog/admin/category/delete")
    public Result deleteCategory(Long cid){
        System.out.println(cid);
        int flag = categoryService.deleteCategory(cid);
        if(flag == 1){
            return new Result("success","成功删除了目录！cid="+cid);
        }
        else{
            return new Result("fail","目录删除失败！");
        }
    }

    @ApiOperation("更新目录信息")
    @PostMapping("/blog/admin/category/update")
    public Result updateCategory(@RequestBody Category category){
        int flag = categoryService.updateCategory(category);
        if(flag == 1){
            return new Result("success","成功更新了目录！cid="+category.getCid());
        }
        else{
            return new Result("fail","目录更新失败！");
        }
    }
}
