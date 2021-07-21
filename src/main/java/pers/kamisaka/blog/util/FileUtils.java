package pers.kamisaka.blog.util;

import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;
import pers.kamisaka.blog.entity.Result;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FileUtils {
    public static String storeFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        //随机生成uuid作为文件名的加密前缀
        String code = UUID.randomUUID().toString().replaceAll("-","");
        String finalFilename = code+filename.substring(filename.lastIndexOf('.'));
        //存放在static目录，按日期生成的文件夹中
        String resourcePath = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
        String filePath = resourcePath+ File.separator + "pic" + File.separator + finalFilename;
        //创建目标文件
        File destFile = new File(filePath);
        //父目录不存在，优先创建父目录
        if(!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        file.transferTo(destFile);
        return finalFilename;
    }
}
