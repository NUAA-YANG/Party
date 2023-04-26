package com.yzx.party.controller.superadmin;

import com.yzx.party.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/3/25 11:17
 */
//用于数据库的备份
@Controller
//这个是全局都会生效的路径
@RequestMapping("/admin/superAdmin")
public class DatabaseController {
    @Autowired
    DatabaseService databaseService;

    //定向页面
    @GetMapping("/databaseOpera")
    public String databaseOpera(){
        return "superadmin/database";
    }

    //数据库备份
    @GetMapping("/backup")
    public String backup(Model model) throws IOException {
        //参数依次是IP、账号、密码、数据库名
        String command = "mysqldump -hlocalhost -uroot -p123456 party";
        //设置文件存储路径
        String parentPath = "./databaseBackupsDir/";
        //获得文件最后的路径
        String path = databaseService.createFile(parentPath);
        //备份数据库
        boolean b = databaseService.backup(command, path);
        if(b){
            model.addAttribute("message","数据库备份成功，备份文件"+path);
        }else {
            model.addAttribute("errorMessage","数据库备份失败");
        }
        return "superadmin/database";
    }


    //数据库恢复备份
    @GetMapping("/recover")
    public String recover(Model model) throws IOException {
        //参数依次是IP、账号、密码、编码格式、数据库名
        String command = "mysql -hlocalhost -uroot -p123456 --default-character-set=utf8 party";
        //设置获取文件存储路径
        String parentPath = "./databaseBackupsDir/";
        //获得文件最后的路径(返回数组第一次参数为文件名，第二个参数为文件夹+文件名)
        String[] path = databaseService.getFilePath(parentPath);
        //备份数据库
        boolean b = databaseService.recover(command, path[1]);
        if(b){
            model.addAttribute("message","数据库恢复备份成功");
        }else {
            model.addAttribute("errorMessage","数据库恢复失败");
        }
        return "superadmin/database";
    }


    //下载数据库,下载之前一定先帮助备份文件
    @GetMapping("/downloadDatabase")
    public void downloadDatabase(Model model ,HttpServletResponse response) throws IOException {
        //参数依次是IP、账号、密码、数据库名
        String command = "mysqldump -hlocalhost -uroot -p123456 party";
        //设置获取文件存储路径
        String parentPath = "./databaseBackupsDir/";
        //获得文件最后的路径
        String savePath = databaseService.createFile(parentPath);
        //先备份数据库
        boolean b1 = databaseService.backup(command, savePath);
        //获得文件最后的路径(返回数组第一次参数为文件名，第二个参数为文件夹+文件名)
        String[] path = databaseService.getFilePath(parentPath);
        //再下载数据库（路径名，文件名）
        boolean b2 = databaseService.downloadDatabase(parentPath, path[0], response);
    }


}
