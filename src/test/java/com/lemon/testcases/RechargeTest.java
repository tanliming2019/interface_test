package com.lemon.testcases;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSONObject;
import com.lemon.common.BaseTest;
import com.alibaba.fastjson.JSON;
import com.lemon.data.Constants;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.PhoneRandomUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * ClassName:RechargeTest
 * Package:com.lemon.testcases
 *
 * @Data:2022/11/11 1:11
 * @Author:tanliming2018@126.com
 */
public class RechargeTest extends BaseTest {
    @BeforeClass
    public void setup() {
        //生成一个没有注册过的手机号码
        String phone = PhoneRandomUtil.getUnregisterPhone();
        //保存到环境变量里面
        Environment.envData.put("phone",phone);
        //读取Excel里面的第一条数据—>执行—>生成一条注册过的手机号码
        List<ExcelPojo> listDatas = readSpecifyExcelData(3, 0, 2);
        //参数替换，替换{{phone}}
        ExcelPojo excelPojo = caseReplace(listDatas.get(0));
        //发起接口请求
        //注册请求
        Response resRegister = request(excelPojo,"充值模块");
        //获取【提取返回数据（extract）】
        //提取接口返回对应的字段，保存到环境变量中
        extractToEnvironment(listDatas.get(0),resRegister);
        //参数替换，替换{{phone}}
        caseReplace(listDatas.get(1));

        //登录请求
        Response resLogin = request(listDatas.get(1),"充值模块");
        //得到【提取返回数据】这一列,
        extractToEnvironment(listDatas.get(1), resLogin);

    }


     //充值
    @Test(dataProvider = "getRechargeDatas")
    public void testRecharge(ExcelPojo excelPojo) {
        //用例执行之前替换{{member_id}} 为环境变量中保存的对应的值
        excelPojo = caseReplace(excelPojo);
        Response res = request(excelPojo,"充值模块");
        //断言
        assertResponse(excelPojo,res);

    }

    @DataProvider
    public Object[] getRechargeDatas() {
        List<ExcelPojo> listDatas = readSpecifyExcelData(3, 2);
        return listDatas.toArray();
    }


}
