package com.lemon.testcases;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lemon.common.BaseTest;
import com.lemon.data.Constants;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.io.File;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * ClassName:LoginTest
 * Package:com.lemon.testcases
 *
 * @Data:2022/11/12 17:08
 * @Author:tanliming2018@126.com
 */
public class LoginTest extends BaseTest {
    @BeforeClass
    public void setup() {
        //生成一个没有注册过的手机号码
        String phone = PhoneRandomUtil.getUnregisterPhone();
        //保存到环境变量里面
        Environment.envData.put("phone",phone);

        //前置条件
        //读取Excel里面的第一条数据—>执行—>生成一条注册过的手机号码
        List<ExcelPojo> listDatas = readSpecifyExcelData(2,0,1);
        //替换
         ExcelPojo excelPojo = caseReplace(listDatas.get(0));
        //执行注册接口请求
        //headers只能接收map类型，所以要先把请求头转成map类型
        Map requestHeaderMap = JSON.parseObject(excelPojo.getRequestHeader());
        Response res = request(excelPojo,"登录模块");
        //提取注册返回的手机号码，保存到环境变量中
        extractToEnvironment(excelPojo,res);

    }

    @Test(dataProvider = "getLoginDatas")
    public void testLogin(ExcelPojo excelPojo) {
        //替换用例数据
        excelPojo = caseReplace(excelPojo);
        //发起请求
        Response res = request(excelPojo,"登录模块");

        //断言
        assertResponse(excelPojo,res);
        //读取响应map里的每一个key
        //思路：循环遍历响应map,取到里面每一个key(实际上就是我们设计的jsonPath表达式)
        //通过res.jsonPath.get(key)取到实际的结果，再跟期望的结果做比对（key对应value）
//        Map<String,Object> expectedMap = JSONObject.parseObject(excelPojo.getExpected(),Map.class);
//        for (String key : expectedMap.keySet()) {
//            //获取map里面的key
////            System.out.println(key);
//            //获取期望结果
//            Object expectedValue = expectedMap.get(key);
//            //获取接口返回的实际结果(jsonPath表达式)
//            //注意;这里要用object类型来接收
//            Object actualValue = res.jsonPath().get(key);
//            Assert.assertEquals(actualValue, expectedValue);
//        }


    }


    @DataProvider
    public Object[] getLoginDatas() {
        List<ExcelPojo> listDatas = readSpecifyExcelData(2,1,12);
        return listDatas.toArray();
    }


}
