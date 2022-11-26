package com.lemon.testcases;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.JSONObject;
import com.lemon.common.BaseTest;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import java.util.List;
import java.util.Map;

/**
 * ClassName:AddloanTest
 * Package:com.lemon.testcases
 *
 * @Data:2022/11/19 14:17
 * @Author:tanliming2018@126.com
 */
public class AddloanTest extends BaseTest {
    @BeforeClass
    public void setup(){
        //生成三个随机手机号码（借款人+管理员）
        String borrowserPhone = PhoneRandomUtil.getUnregisterPhone();
        String adminPhone = PhoneRandomUtil.getUnregisterPhone();
        Environment.envData.put("borrower_phone",borrowserPhone);
        Environment.envData.put("admin_phone",adminPhone);
        //读取用例数据--前4条
        List<ExcelPojo> list = readSpecifyExcelData(4,0,4);
        for(int i=0;i<list.size();i++){
            ExcelPojo excelPojo = list.get(i);
            excelPojo = caseReplace(excelPojo);
            //发起请求
            Response res = request(excelPojo,"加标模块");
            //判断是否要提取响应数据
            if(excelPojo.getExtract() != null){
                extractToEnvironment(excelPojo,res);
            }
        }
    }

    @Test(dataProvider = "getAddLoadDatas")
    public void testAddLoan(ExcelPojo excelPojo){
        excelPojo = caseReplace(excelPojo);
        Response res = request(excelPojo,"加标模块");
        //断言
        assertResponse(excelPojo,res);

    }

    @DataProvider
    public Object[] getAddLoadDatas(){
        List<ExcelPojo> listDatas = readSpecifyExcelData(4,4);
        //把集合转换为一个一维数组
        return listDatas.toArray();
    }

    @AfterTest
    public void teardown(){

    }
}
