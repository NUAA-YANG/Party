package com.yzx.party.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/3/25 11:18
 */
public interface DatabaseService {
    //创建备份空文件(parentPath顶级目录) 返回的是创建的整个文件夹+文件名
    String createFile(String parentPath) throws IOException;
    //获得最新的备份文件路径(parentPath顶级目录) 返回数组第一次参数为文件名，第二个参数为文件夹+文件名
    String[] getFilePath(String parentPath);
    //数据库备份(sql备份语句,文件存储路径)
    boolean backup(String command, String savePath);
    //数据库恢复备份(sql运行语句,读取文件存储路径)
    boolean recover(String command, String savePath);
    //下载数据库（路径名，文件名）
    boolean downloadDatabase(String pathName,String fileName, HttpServletResponse response) throws UnsupportedEncodingException;
}
