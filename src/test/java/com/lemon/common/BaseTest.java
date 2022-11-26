package com.lemon.common;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lemon.data.Constants;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.JDBCUtils;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import io.restassured.response.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * 所有测试用例的父类
 * ClassName:baseTest
 * Package:com.lemon.testcases
 *
 * @Data:2022/11/11 22:27
 * @Author:tanliming2018@126.com
 */
public class BaseTest {
    @BeforeTest
    public void GlobalSetup() throws FileNotFoundException {
        //RestAssuredConfig 全局配置
        //定义json小数返回的类型是BigDecimal
        RestAssured.config = RestAssuredConfig.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //BaseUrl全局配置
        RestAssured.baseURI = Constants.BASE_URL;
        //日志全局重定向到本地文件中
//        File file = new File(System.getProperty("user.dir")+"\\log");
//        if(!file.exists()){
//            //创建
//            file.mkdir();
//        }
//        PrintStream fileOutPutStream = new PrintStream(new File("log/test_all.log"));
//        RestAssured.filters(new RequestLoggingFilter(fileOutPutStream),new ResponseLoggingFilter(fileOutPutStream));
    }


    /**
     * 对get,post,patch.put做了二次封装
     *
     * @param excelPojo excel每行数据对应对象
     * @return 接口响应结果
     */
    public Response request(ExcelPojo excelPojo, String interfaceModuleName) {
        //创建目录 项目的根目录log
        String logFilePath;
        if (Constants.LOG_TO_FILE) {
            File dirPath = new File(System.getProperty("user.dir") + "\\log\\" + interfaceModuleName);
            if (!dirPath.exists()) {
                //创建目录层级 log/接口模块名
                dirPath.mkdirs();
            }
            logFilePath = dirPath + "\\test" + excelPojo.getCaseId() + ".log";
            PrintStream fileOutPutStream = null;
            try {
                fileOutPutStream = new PrintStream(new File(logFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(fileOutPutStream));
        }
        //接口请求地址
        String url = excelPojo.getUrl();
        //请求方法
        String method = excelPojo.getMethod();
        //请求头
        String hearders = excelPojo.getRequestHeader();
        //请求参数
        String params = excelPojo.getInputParams();
        //请求头转换成map
        Map<String, Object> headersMap = JSON.parseObject(hearders, Map.class);
        Response res = null;
        //对get、post、patch、put做封装
        if("get".equalsIgnoreCase(method)){
            res = given().log().all().headers(headersMap).when().get(url).then().log().all().extract().response();
        }else if("post".equalsIgnoreCase(method)){
            res= given().log().all().headers(headersMap).body(params).when().post(url).then().log().all().extract().response();
        }else if("patch".equalsIgnoreCase(method)){
            res= given().log().all().headers(headersMap).body(params).when().patch(url).then().log().all().extract().response();
        }
        //向allure报表中添加日志
        if (Constants.LOG_TO_FILE) {
            try {
                Allure.addAttachment("接口请求响应信息", new FileInputStream(logFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return res;
    }


    /**
     * 对响应结果断言
     *
     * @param excelPojo 用例数据实体对象
     * @param res       接口响应
     */
    public void assertResponse(ExcelPojo excelPojo, Response res) {
        //断言
        if (excelPojo.getExpected() != null) {
            Map<String, Object> expectedMap = JSONObject.parseObject(excelPojo.getExpected(), Map.class);
            for (String key : expectedMap.keySet()) {
                //获取map里面的value
                //获取期望结果
                Object expectedValue = expectedMap.get(key);
                // 获取接口返回的实际结果（jsonPath表达式）
                Object actualyValue = res.jsonPath().get(key);
                Assert.assertEquals(actualyValue, expectedValue);
            }
        }
    }

    /**
     * 读取Excel指定sheet里面的所有数据
     *
     * @param sheetNum sheet编号（从1开始）
     */
    public List<ExcelPojo> readAllExcelData(int sheetNum) {
        File file = new File(Constants.EXCEL_FILE_PATH);
        //导入的参数对象
        ImportParams importParams = new ImportParams();
        //读取第二个sheet
        importParams.setStartSheetIndex(sheetNum - 1);
        //读取Excel
        List<ExcelPojo> listDatas = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        return listDatas;
    }


    /**
     * 读取指定行的Excel表格数据
     *
     * @param sheetNum sheet编号（从1开始）
     * @param startRow 读取开始行（默认从0开始）
     * @param readRow  读取多少行
     * @return
     */
    public List<com.lemon.pojo.ExcelPojo> readSpecifyExcelData(int sheetNum, int startRow, int readRow) {
        File file = new File(Constants.EXCEL_FILE_PATH);
        //导入的参数对象
        ImportParams importParams = new ImportParams();
        //读取第二个sheet
        importParams.setStartSheetIndex(sheetNum - 1);
        //读取Excel
        //设置起始行，默认从0开始
        importParams.setStartRows(startRow);
        //设置读取的行数
        importParams.setReadRows(readRow);
        List<ExcelPojo> listDatas = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        return listDatas;
    }

    /**
     * 读取指定行开始所有Excel表格数据
     *
     * @param sheetNum sheet编号（从1开始）
     * @param startRow 读取开始行（默认从0开始）
     * @return
     */
    public List<ExcelPojo> readSpecifyExcelData(int sheetNum, int startRow) {
        File file = new File(Constants.EXCEL_FILE_PATH);
        //导入的参数对象
        ImportParams importParams = new ImportParams();
        //读取第二个sheet
        importParams.setStartSheetIndex(sheetNum - 1);
        //读取Excel
        //设置起始行，默认从0开始
        importParams.setStartRows(startRow);
        List<ExcelPojo> listDatas = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        return listDatas;
    }

    /**
     * 将对应的接口返回字段提取到环境变量中
     *
     * @param excelPojo 用例数据对象
     * @param res       接口返回Resonse对象
     */
    public void extractToEnvironment(ExcelPojo excelPojo, Response res) {
        Map<String, Object> extractMap = JSONObject.parseObject(excelPojo.getExtract(), Map.class);
//        System.out.println(extractMap);
        //循环遍历extractMap
        for (String key : extractMap.keySet()) {
            Object path = extractMap.get(key);
            //根据【提取返回数据】里面的路径表达式去提取实际接口对应返回字段的值
            Object value = res.jsonPath().get(path.toString());
            //存到环境变量中
            Environment.envData.put(key, value);
        }
    }


    /**
     * 从环境变量中取得对应值，进行替换
     *
     * @param orgStr 原始字符串
     * @return 替换之后的字符串
     */
    public static String regexReplace(String orgStr) {
        if (orgStr != null) {
            //pattern:正则表达式匹配器
            Pattern pattern = Pattern.compile("\\{\\{(.*?)}}");
            //matchaer:去匹配哪一个原始字符串，得到匹配对象
            Matcher matcher = pattern.matcher(orgStr);
            String result = orgStr;
            while (matcher.find()) {
                //group(0)表示获取到整个匹配到的内容
                String outerStr = matcher.group(0);  //{{phone}}
                //group(1)表示获取{{}}包裹着的内容
                String innerStr = matcher.group(1); //phone
                //从环境变量中取到实际的值 member_id
                Object replaceStr = Environment.envData.get(innerStr);
                //replace
                result = result.replace(outerStr, replaceStr + "");
            }
            return result;
        }
        return orgStr;
    }


    /**
     * 对用例数据进行替换（包括入参+请求头+接口地址+期望结果）
     *
     * @param excelPojo
     * @return
     */
    public ExcelPojo caseReplace(ExcelPojo excelPojo) {
        //正则替换-->参数输入
        String inputParams = regexReplace(excelPojo.getInputParams());
        excelPojo.setInputParams(inputParams);
        //正则替换-->请求头
        String requestHeader = regexReplace(excelPojo.getRequestHeader());
        excelPojo.setRequestHeader(requestHeader);
        //正则替换-->接口地址
        String url = regexReplace(excelPojo.getUrl());
        excelPojo.setUrl(url);
        //正则替换--期望的返回结果
        String expected = regexReplace(excelPojo.getExpected());
        excelPojo.setExpected(expected);
        //正则替换-->数据库校验
        String dbAssert = regexReplace(excelPojo.getDbAssert());
        excelPojo.setDbAssert(dbAssert);
        return excelPojo;
    }

    /**
     * 数据库断言
     *
     * @param excelPojo
     */
    public void assertSQL(ExcelPojo excelPojo) {
        String dbAssert = excelPojo.getDbAssert();
        if (dbAssert != null) {
            Map<String, Object> map = JSONObject.parseObject(dbAssert, Map.class);
            Set<String> keys = map.keySet();
            for (String key : keys) {
                //key其实就是我们执行的sql语句
                //value就是数据断言的期望值
                Object expectedValue = map.get(key);
                if (expectedValue instanceof BigDecimal) {
//                    System.out.println("expectedvalue类型" + expectedValue.getClass());
                    Object actualValue = JDBCUtils.querySingleData(key);
//                    System.out.println("actualValue类型" + actualValue.getClass());
                    Assert.assertEquals(actualValue, expectedValue);
                } else if (expectedValue instanceof Integer) {
                    //此时从excel里面读取到的是integer类型
                    //从数据库拿到的是long类型
                    Long expectedValue2 = ((Integer) expectedValue).longValue();
                    Object actualValue = JDBCUtils.querySingleData(key);
                    Assert.assertEquals(actualValue, expectedValue2);
                }
            }
        }

    }

    public static void main(String[] args) {
        //创建目录 项目的根目录log
//        File file = new File(System.getProperty("user.dir")+"\\log");
//        if(!file.exists()){
//            //创建
//            file.mkdir();
//        }
//        System.out.println(System.getProperty("user.dir"));

    }



}
