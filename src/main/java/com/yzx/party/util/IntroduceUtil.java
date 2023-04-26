package com.yzx.party.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/7 10:08
 */
//用于抽取文章主题内容的前100个字用来匹配
//最后筛选出70个字作为文章内容的概述用于前端的展示
public class IntroduceUtil {
    // 需要截取的文本 前多少个字截取 截取出来多少个字
    public String IntroduceHelper(String content, Integer contentSub, Integer resSub) {
        //设置获取的前多少个字数量一定小于总的数量
        if (content.length() > contentSub) {
            content = content.substring(0, contentSub - 1);
        }
        Pattern p = Pattern.compile("[\u4e00-\u9fa5:：，,.。!！a-zA-Z0-9]+");
        Matcher m = p.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            sb.append(m.group());
        }
        String res = sb.toString();
        //最后无论content数量是多少，结果一定会小于等于resSub
        if (res.length() > resSub) {
            res = res.substring(0, resSub - 1);
        }
        return res;
    }
}