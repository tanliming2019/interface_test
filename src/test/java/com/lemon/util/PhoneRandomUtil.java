package com.lemon.util;

import org.apache.poi.ss.formula.functions.Roman;

import java.util.Random;

/**
 * ClassName:PhoneRandomUtil
 * Package:com.lemon.util
 *
 * @Data:2022/11/17 17:46
 * @Author:tanliming2018@126.com
 */
public class PhoneRandomUtil {
    //思路1：先查询手机号码字段，按照倒叙排列，取得最大的手机号+1
    //思路2：先去生成一个随机的手机号码，再通过该号码进入到数据库查询，如果查询有记录，再来生成一个，否则说明该号码没有被注册（循环）
    public static void main(String[] args) {
        getUnregisterPhone();
    }

    public static String getRandomPhone(){
        Random random = new Random();
        //nextInt随机生成一个整数，范围是从0-你的参数范围之内
        String phonePrefix = "138";
        for (int i = 0; i < 8 ; i++){
            int num = random.nextInt(9);
            phonePrefix = phonePrefix + num;
        }
//        System.out.println(phonePrefix);
        return phonePrefix;
    }

    public static String getUnregisterPhone(){
        String phone = "";
        while (true){
            phone = getRandomPhone();
            //查询数据
            Object result = JDBCUtils.querySingleData("select count(*) from member where mobile_phone="+phone);
//            System.out.println(result);
            if((long)result == 0){
                break;
            }
//            else {
//                continue;
//            }
        }
        return phone;
    }

}
