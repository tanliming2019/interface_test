package com.lemon.testcases;

import com.alibaba.fastjson.JSONObject;
import com.lemon.common.BaseTest;
import com.lemon.data.Environment;

import com.lemon.pojo.ExcelPojo;
import com.lemon.util.JDBCUtils;
import com.lemon.util.PhoneRandomUtil;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ClassName:RegisterTest
 * Package:com.lemon.testcases
 *
 * @Data:2022/11/20 2:15
 * @Author:tanliming2018@126.com
 */
public class RegisterTest extends BaseTest {
    @BeforeClass
    public void setup() {
        //随机生成没有注册过的手机号码
        String phone1 = PhoneRandomUtil.getUnregisterPhone();
        String phone2 = PhoneRandomUtil.getUnregisterPhone();
        String phone3 = PhoneRandomUtil.getUnregisterPhone();
        Environment.envData.put("phone1",phone1);
        Environment.envData.put("phone2",phone2);
        Environment.envData.put("phone3",phone3);
    }

    @Test(dataProvider = "getRegisterDatas")
    public void testRegister(ExcelPojo excelPojo) throws FileNotFoundException {
        excelPojo = caseReplace(excelPojo);
        //发起注册请求
        Response res = request(excelPojo,"注册模块");
        //响应断言
        assertResponse(excelPojo,res);
        //数据库断言
        assertSQL(excelPojo);
    }

    @DataProvider
    public Object[] getRegisterDatas() {
        List<ExcelPojo> listDatas = readSpecifyExcelData(1,0);
        //把集合转换为一个一维数组
        return listDatas.toArray();
    }
//
//    @AfterTest
//    public void teardown(){
//        //清空环境变量
//        Environment.envData.clear();
//    }
}

