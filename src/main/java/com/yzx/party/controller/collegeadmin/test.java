package com.yzx.party.controller.collegeadmin;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/3/12 10:22
 */
public class test {
    public static void main(String[] args) throws ParseException, IOException {
        //获得当前时间,用于给不同数据库备份命名
        Date date = new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String formatTime = sdf.format(date);
        //测试备份
        String command = "mysqldump -hlocalhost -uroot -p123456 party";//参数依次是IP、账号、密码、数据库名
        //文件名称
        String databaseFileName =formatTime+"_party.sql";
        //文件存储路径
        String savePath = "./databaseBackupsDir/";
        //完整路径
        String path = savePath+databaseFileName;
        File file = new File(path);
        //因此这里使用.getParentFile()，目的就是取文件前面目录的路径
        if (!file.getParentFile().exists()) {
            // 新建文件夹
            file.getParentFile().mkdirs();
            System.out.println("创建目录成功");
        }
        //不存在文件就创建
        if (!file.exists()){
            file.createNewFile();
            System.out.println("创建文件成功");
        }
        boolean b1 = new test().backup(command, path);
        if(b1){
            System.out.println("备份成功");
            System.out.println(path);
        }else {
            System.out.println("备份失败");
        }
        new test().recover("0",savePath);


    }


    /**
     * mysql的备份方法
     *
     * @param command  命令行
     * @param savePath 备份路径
     * @return
     */
    public boolean backup(String command, String savePath) {
        boolean flag;
        // 获得与当前应用程序关联的Runtime对象
        Runtime r = Runtime.getRuntime();
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            // 在单独的进程中执行指定的字符串命令
            Process p = r.exec(command);
            // 获得连接到进程正常输出的输入流，该输入流从该Process对象表示的进程的标准输出中获取数据
            InputStream is = p.getInputStream();
            // InputStreamReader是从字节流到字符流的桥梁：它读取字节，并使用指定的charset将其解码为字符
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            //BufferedReader从字符输入流读取文本，缓冲字符，提供字符，数组和行的高效读取
            br = new BufferedReader(isr);
            String s;
            StringBuffer sb = new StringBuffer("");
            // 组装字符串
            while ((s = br.readLine()) != null) {
                sb.append(s + System.lineSeparator());
            }
            s = sb.toString();
            // 创建文件输出流
            FileOutputStream fos = new FileOutputStream(savePath);
            // OutputStreamWriter是从字符流到字节流的桥梁，它使用指定的charset将写入的字符编码为字节
            OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
            // BufferedWriter将文本写入字符输出流，缓冲字符，以提供单个字符，数组和字符串的高效写入
            bw = new BufferedWriter(osw);
            bw.write(s);
            bw.flush();
            flag = true;
        } catch (IOException e) {
            flag = false;
            e.printStackTrace();
        } finally {
            //由于输入输出流使用的是装饰器模式，所以在关闭流时只需要调用外层装饰类的close()方法即可，
            //它会自动调用内层流的close()方法
            try {
                if (null != bw) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (null != br) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }



    /**
     * mysql的还原方法
     *
     * @param command  命令行
     * @param filePath 还原路径
     * @return
     */
    public boolean recover(String command, String filePath) {
        //获取其file对象
        File file = new File(filePath);
        //如果目录不存在，那么直接返回空
        if (!file.exists() && !file .isDirectory()){
            System.out.println("目录不存在");
            return false;
        }else {
            //获得里面的全部文件
            String[] listFileName = file.list();
            String fileName = listFileName[listFileName.length-1];
            //获得最新备份的文件名
            System.out.println(fileName);
            //获取完整的文件路径
            System.out.println(filePath+fileName);
        }
        return true;
    }

}
