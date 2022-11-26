package test.day01;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static java.lang.System.*;

/**
 * ClassName:GetResponse
 * Package:test.day01
 *
 * @Data:2022/10/28 10:34
 * @Author:tanliming2018@126.com
 */
public class GetResponse {
    @Test
    public void getResponseHeader() {
        Response res =
                given().
                        when().
                        post("http://www.httpbin.org/post").
                        then().
                        log().all().extract().response();
        out.println("接口响应时间：" + res.time());
        out.println(res.getHeader("Content-Type"));
    }

    @Test
    public void getResponseJson01() {
        String json = "{\"mobile_phone\":\"15013131042\",\"pwd\":\"12345678\"}";
        Response res =
                given().
                        body(json).
                        header("Content-type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v1").
                        when().
                        post("http://api.lemonban.com/futureloan/member/login").
                        then().
                        log().all().extract().response();
        out.println(res.jsonPath().getInt("data.id"));
    }

    //json提取
    @Test
    public void getResponseJson02() {

        Response res =
                given().

                        when().
                        get("http://httpbin.org/json").
                        then().
                        log().all().extract().response();
        List<String> list = res.jsonPath().getList("slideshow.slides.title");
        out.println(list.get(0));
        out.println(list.get(1));
    }

    //html提取
    @Test
    public void getResponseHtml() {
        Response res =
                given().

                        when().
                        get("http://www.baidu.com").
                        then().
                        log().all().extract().response();
        out.println(res.htmlPath().getString("html.head.title"));
        out.println(res.htmlPath().getString("html.head.meta[0].@content")); //获取某个属性
    }

    //xml提取
    @Test
    public void getResponsexml() {

        Response res =
                given().
                        when().
                        get("http://httpbin.org/xml").
                        then().
                        log().all().extract().response();
        out.println(res.xmlPath().getString("slideshow.slide[1].title"));
        out.println(res.xmlPath().getString("slideshow.slide[1].@type"));

    }

    @Test
    public void loginRecharge(){
        String json = "{\"mobile_phone\":\"15013131042\",\"pwd\":\"12345678\"}";
        //登录
        Response res =
                given().
                        body(json).
                        header("Content-type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                when().
                        post("http://api.lemonban.com/futureloan/member/login").
                then().
                        extract().response();
        //获取id
        int memberId = res.jsonPath().get("data.id");
        //获取token
        String token = res.jsonPath().getString("data.token_info.token");
//        out.println(memberId);
//        out.println(token);

        //充值
        String jsonData = "{\"member_id\":"+memberId+",\"amount\":10000}";
        Response res2 =
                given().
                        body(jsonData).
                        header("Content-type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                        header("Authorization","Bearer "+token).
                when().
                        post("http://api.lemonban.com/futureloan/member/recharge").
               then().
                        log().all().extract().response();

        out.println("当前可用的余额为："+res2.jsonPath().getString("data.leave_amount"));
    }


}