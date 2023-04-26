package com.yzx.party.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/3/25 11:20
 */
@Service
public class DatabaseServiceImpl implements DatabaseService {

    @Override
    public String createFile(String parentPath) throws IOException {
        Date date = new Date();//获得当前时间,用于给不同数据库备份命名
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String formatTime = sdf.format(date);
        String databaseFileName =formatTime+"_party.sql";//文件名称
        String path = parentPath+databaseFileName;//完整路径
        File file = new File(path);//形成文件
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();// 新建文件夹
        }
        if (!file.exists()){
            file.createNewFile();//创建文件
        }
        return path;
    }

    @Override
    public String[] getFilePath(String parentPath) {
        String[] result = new String[2];
        //创建父级目录文件
        File file = new File(parentPath);
        if (!file.exists() && !file .isDirectory()){
            //目录不存在，返回空
            return null;
        }else {
            //获得里面的全部文件名称
            String[] listFileName = file.list();
            //如果没有文件，也是返回空
            if (listFileName.length==0){
                return null;
            }else {
                //获得当前最新的备份文件
                String fileName = listFileName[listFileName.length-1];
                String path = parentPath+fileName;
                result[0] = fileName;
                result[1] = path;
                //返回完整的文件名
                return result;
            }
        }
    }


    @Override
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



    @Override
    public boolean recover(String command, String savePath) {
        boolean flag;
        Runtime r = Runtime.getRuntime();
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            Process p = r.exec(command);
            OutputStream os = p.getOutputStream();
            FileInputStream fis = new FileInputStream(savePath);
            InputStreamReader isr = new InputStreamReader(fis, "utf-8");
            br = new BufferedReader(isr);
            String s;
            StringBuffer sb = new StringBuffer("");
            while ((s = br.readLine()) != null) {
                sb.append(s + System.lineSeparator());
            }
            s = sb.toString();
            OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
            bw = new BufferedWriter(osw);
            bw.write(s);
            bw.flush();
            flag = true;
        } catch (IOException e) {
            flag = false;
            e.printStackTrace();
        } finally {
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

    @Override
    public boolean downloadDatabase(String pathName,String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        //完整的文件名
        String path = pathName+fileName;
        //获取文件
        File file = new File(path);
        //如果文件存在就下载
        if (file.exists()){
            //设置编码
            response.setCharacterEncoding("UTF-8");
            // 设置文件名
            response.setHeader("Content-Disposition", "attachment;filename="+  fileName +";filename*=utf-8''"+ URLEncoder.encode(fileName,"UTF-8"));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + file.length());
            // 设置强制下载不打开
            response.setContentType("application/force-download");
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally { // 做关闭操作
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

}
