package test.day01;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * ClassName:HomeWork
 * Package:test.day01
 *
 * @Data:2022/10/31 11:30
 * @Author:tanliming2018@126.com
 */
public class HomeWork {
    //全局变量
    String mobilphone = "15013131060";
    String pwd = "12345678";
    int type = 1;
    int memberId;
    String token;
    @Test
    public void testRegister(){
        String json = "{\"mobile_phone\":\""+mobilphone+"\",\"pwd\":\""+pwd+"\",\"type\":"+type+"}";
        Response res =
                given().
                        body(json).
                        header("Content-type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                when().
                        post("http://api.lemonban.com/futureloan/member/register").
                then()
                        .log().all().
                        extract().response();
    }

    @Test(dependsOnMethods = "testRegister")
    public void testLogin(){
        String json = "{\"mobile_phone\":\"15013131042\",\"pwd\":\"12345678\"}";
        //登录
        Response res =
                given().
                        body(json).
                        header("Content-type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
               when().
                        post("http://api.lemonban.com/futureloan/member/login").
               then()
                        .log().all().
                        extract().response();
        //1.先获取id
        memberId = res.jsonPath().getInt("data.id");
        System.out.println(memberId);
        //2.获取token
        token = res.jsonPath().getString("data.token_info.token");
        System.out.println(token);
    }

    @Test(dependsOnMethods = "testLogin")
    public void testRecharge(){
        //发起“充值”接口请求
        String jsonData = "{\"member_id\":"+memberId+",\"amount\":0.01}";
        Response res2 =
                given().
                        body(jsonData).
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                        header("Authorization","Bearer "+token).
                when().
                        post("http://api.lemonban.com/futureloan/member/recharge").
                then()
                        .log().all().
                        extract().response();
        System.out.println("当期可用余额："+res2.jsonPath().get("data.leave_amount"));
    }
}
