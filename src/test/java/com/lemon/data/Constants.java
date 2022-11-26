package com.lemon.data;

import java.security.PublicKey;

/** 常量类
 * ClassName:Constants
 * Package:com.lemon.data
 *
 * @Data:2022/11/13 19:17
 * @Author:tanliming2018@126.com
 */
public class Constants {
    //日志输出的配置：控制台 or 日志文件
    public static final boolean LOG_TO_FILE = true;
    //  Excel文件的路径
    public static final String EXCEL_FILE_PATH = "src/test/resources/api_testcases_futureloan_v3.xls";
    //接口BaseUrl地址
    public static final String BASE_URL= "http://api.lemonban.com:8788/futureloan";
    //数据库baseuri
    public static final String DB_BASE_URI = "api.lemonban.com";
    //数据库名
    public static final String DB_NAME = "futureloan";
    public static final String DB_USENAME = "future";
    public static final String DB_PWD = "123456";

}
