package com.yzx.party.util;

import org.springframework.util.DigestUtils;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2021/12/17 15:50
 */

//加密
public class MD5Util {

    //使用springboot自带的加密工具
    public String md5(String password){
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        return md5Password;
    }


    /*为了怕忘记密码，记录一下，为了防止忘记，就用这两个密码
        原始密码：666666   加密密码：f379eaf3c831b04de153469d1bec345e
    *   原始密码：123456   加密密码：e10adc3949ba59abbe56e057f20f883e
    *
    * */
    public static void main(String[] args) {
        System.out.println(new MD5Util().md5("666666"));
        System.out.println(new MD5Util().md5("123456"));
    }

}
