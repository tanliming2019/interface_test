package test.day02;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * ClassName:AssertDemo
 * Package:test.day02
 *
 * @Data:2022/10/30 19:18
 * @Author:tanliming2018@126.com
 */
public class AssertDemo {
    @Test
    public void testAssertDemo(){
        //RestAssuredConfig 全局配置
        //json小数返回的类型是BigDecimal
        RestAssured.config = RestAssuredConfig.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //BaseUrl全局配置
        RestAssured.baseURI = "http://api.lemonban.com/futureloan";
        String json = "{\"mobile_phone\":\"15013131042\",\"pwd\":\"12345678\"}";
        //登录
        Response res =
                given().
                        body(json).
                        header("Content-type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                when().
                        post("/member/login").
                then().
                        log().all().
                        extract().response();
        //1.响应结果断言
        //整数类型
        int code = res.jsonPath().get("code");
        //字符串类型
        String msg = res.jsonPath().get("msg");
        Assert.assertEquals(code,0);
        Assert.assertEquals(msg,"OK");
        //小数类型
        //注意：restassure利明如果返回json小数，那么其类型是float
        //丢失精度问题解决方案：声明restassured返回json小数的其类型是BigDecimal
        BigDecimal leaveAmount = res.jsonPath().get("data.leave_amount");
        BigDecimal expected = BigDecimal.valueOf(530000.02);
        Assert.assertEquals(leaveAmount,expected);

        //2.数据库断言

        int memberId = res.jsonPath().getInt("data.id");
        String token = res.jsonPath().getString("data.token_info.token");
        String jsonData = "{\"member_id\":"+memberId+",\"amount\":10000}";
        Response res2 =
                given().
                        body(jsonData).
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                        header("Authorization","Bearer "+token).
                when().
                        post("/member/recharge").
                then()
                        .log().all().
                        extract().response();
        BigDecimal actual2 = res2.jsonPath().get("data.leave_amount");
        BigDecimal expected2 = BigDecimal.valueOf(540000.02);
        Assert.assertEquals(actual2,expected2);
        System.out.println("当期可用余额："+res2.jsonPath().get("data.leave_amount"));
    }

}
