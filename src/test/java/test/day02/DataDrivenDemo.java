package test.day02;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * ClassName:DataDrivenDemo
 * Package:test.day02
 *
 * @Data:2022/11/1 11:08
 * @Author:tanliming2018@126.com
 */
public class DataDrivenDemo {
    @Test(dataProvider = "getLoginDatas02")
    public void testLogin(ExcelPojo excelPojo){
        //RestAssuredConfig 全局配置
        //json小数返回的类型是BigDecimal
        RestAssured.config = RestAssuredConfig.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //BaseUrl全局配置
        RestAssured.baseURI = "http://api.lemonban.com:8788/futureloan";

       //接口入参
        String inputParams = excelPojo.getInputParams();
        //接口地址
        String url = excelPojo.getUrl();
        //请求头
        String requestHeader = excelPojo.getRequestHeader();
        //把请求头转成map
        Map requestHeaderMap = (Map) JSON.parse(requestHeader);
        //期望的响应结果
        String expected = excelPojo.getExpected();
        //把响应结果转成map
        //把key的类型声明为String
        Map<String ,Object> expectedMap = (Map) JSON.parse(expected);

        //登录
        Response res =
                given().
                        body(inputParams).
                        headers(requestHeaderMap).
                when().
                        post(url).
                then().
                        log().all().
                        extract().response();
        //断言
        //读取响应map里的每一个key
        //思路：循环遍历响应map,取到里面每一个key(实际上就是我们设计的jsonPath表达式)
        //通过res.jsonPath.get(key)取到实际的结果，再跟期望的结果做比对（key对应value）
        for (String key : expectedMap.keySet()) {
            //获取map里面的key
//            System.out.println(key);
            //获取期望结果
            Object expectedValue = expectedMap.get(key);

            //获取接口返回的实际结果(jsonPath表达式)
            //注意;这里要用object类型来接收
            Object actualValue = res.jsonPath().get(key);
            Assert.assertEquals(actualValue,expectedValue);
        }




    }

    @DataProvider
    public Object[][] getLoginDatas(){
        Object[][] datas = {{"15013131042","12345678"},
                {"15013131060","12345678"},
                {"15013131030","12345678"}
        };
        return datas;
    }

    @DataProvider
    public Object[] getLoginDatas02(){
        File file = new File("C:\\Users\\MrTan\\Desktop\\api_testcases_futureloan_v1.xls");
        //导入的参数对象
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(1);
        List<ExcelPojo> listDatas = ExcelImportUtil.importExcel(file,ExcelPojo.class,importParams);
        //为什么这里的数据类型是ExcelPojo?因为后面要通过ExcelPojo的对象获取数据
        // 把集合转换成一个一维数组
        return listDatas.toArray();
    }

    public static void main(String[] args) {
        //读取Excel
        File file = new File("C:\\Users\\MrTan\\Desktop\\api_testcases_futureloan_v1.xls");
        //导入的参数对象
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(1); //sheet表单的索引
        List<Object> listDatas = ExcelImportUtil.importExcel(file,ExcelPojo.class,importParams);
        for (Object object: listDatas) {
            System.out.println(object);
        }
    }
}
