package com.iih5.actor.util;

/**
 * Created by XUEYI on 2016/4/1.
 */
public class ConsoleUtil {
    /**
     * exception字符串格式化
     * @param e
     * @return
     */
    public static String exceptionToString(Exception e){
        StringBuffer buffer = new StringBuffer();
        buffer.append(e.getMessage()+"\n");
        StackTraceElement[] errElements= e.getStackTrace();
        for (StackTraceElement s : errElements) {
            buffer.append(s.toString()+"\n");
        }
        return buffer.toString();
    }
}
