package pers.kamisaka.blog.controller;

import ch.qos.logback.core.util.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pers.kamisaka.blog.entity.Result;
import pers.kamisaka.blog.entity.TokenResult;
import pers.kamisaka.blog.entity.User;
import pers.kamisaka.blog.service.UserService;
import pers.kamisaka.blog.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@Api
public class UserController {
    @Autowired
    UserService userService;

    @ApiOperation("根据用户id查询信息")
    @ApiImplicitParams(
            @ApiImplicitParam(type = "path",name = "uid",value = "用户id",required = true,dataType = "String")
    )
    @GetMapping("/blog/user/{uid}")
    public User getUserById(@PathVariable String uid){
        return userService.getUserByUid(uid);
    }

    @ApiOperation("登陆")
    @PostMapping("/blog/admin/login")
    public Result Login(User user){
        TokenResult result = new TokenResult();
        User userInDB = userService.getUserByUsername(user.getUsername());
        if(userInDB == null){
            result.setStatus("fail");
            result.setMsg("无效的用户名");
        }
        else {
            if(userInDB.getPassword().equals(user.getPassword())){
                userInDB.setLastLoginDate(new Timestamp(System.currentTimeMillis()));
                userService.updateUser(userInDB);
                result.setStatus("success");
                result.setMsg(userInDB.getUid());
                result.setToken(JwtUtil.generateJwt(userInDB));
            }
            else{
                result.setStatus("fail");
                result.setMsg("密码错误");
            }
        }
        return result;
    }

    @ApiOperation("修改用户密码")
    @PostMapping("/blog/admin/user/changePassword")
    public Result updatePassword(@RequestParam("uid") String uid,@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword){
        User user = userService.getUserByUid(uid);
        System.out.println(uid);
        if(user.getPassword().equals(oldPassword)){
            user.setPassword(newPassword);
            int flag = userService.updateUser(user);
            if(flag == 1){
                return new Result("success","成功修改了用户信息！uid="+user.getUid());
            }
            else{
                return new Result("fail","用户信息修改失败！");
            }
        }
        else{
            return new Result("fail","密码错误，请重试");
        }

    }

    @ApiOperation("修改用户头像")
    @PostMapping("/blog/admin/user/uploadAvatar")
    public Result uploadAvatar(MultipartFile file,@RequestHeader("uid") String uid) throws IOException {
        String filename = file.getOriginalFilename();
        if(filename == null){
            return new Result("fail","服务器未接收到图片，请重试!");
        }
        //随机生成uuid作为文件名的加密前缀
        String code = UUID.randomUUID().toString().replaceAll("-","");
        String finalFilename = code+filename.substring(filename.lastIndexOf('.'));
        //存放在static目录，按日期生成的文件夹中
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate =  sdf.format(new Date());
        String resourcePath = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
        String filePath = resourcePath+ File.separator + "avatar" + File.separator + finalFilename;
        //创建目标文件
        File destFile = new File(filePath);
        //父目录不存在，优先创建父目录
        if(!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        file.transferTo(destFile);

        User user = userService.getUserByUid(uid);
        user.setAvatarPath(finalFilename);
        int flag = userService.updateUser(user);
        if(flag == 1){
            return new Result("success","成功修改了用户头像！uid="+user.getUid());
        }
        else{
            return new Result("fail","用户头像修改失败！");
        }
    }

    @ApiOperation("获取站长头像url")
    @GetMapping("/blog/avatar")
    public String getAvatar(){
        return userService.getUserByUsername("admin").getAvatarPath();
    }
}
