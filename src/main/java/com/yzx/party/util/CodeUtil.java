package com.yzx.party.util;

import java.util.Random;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/3/14 15:11
 */
//用于生成验证码
public class CodeUtil {

    //参数为生成的验证码长度
    public String CodeUtilHelper(Integer size){
        String code = "";
        Random random = new Random();
        //字符集
        char[] chars = new char[]{'a','b','c','d','e' ,'f','g','h','i','j','k' ,'m','n','p','q','r','s' ,'t','u','v','w','x','y','z',
                'A','B','C','D','E' ,'F','G','H','I','J','K' ,'M','N','P','Q','R','S' ,'T','U','V','W','X','Y','Z',
                '1','2','3','4','5','6','7','8','9'};
        for (int i =0;i<size;i++ ){
            //随机取字符集长度范围内的下标
            int index=random.nextInt(chars.length);
            code+=chars[index];
        }
        return code;
    }

}
