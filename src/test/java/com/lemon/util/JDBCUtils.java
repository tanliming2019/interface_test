package com.lemon.util;

import com.lemon.data.Constants;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * ClassName:JDBCUtils
 * Package:com.lemon.util
 *
 * @Data:2022/11/16 13:55
 * @Author:tanliming2018@126.com
 */
public class JDBCUtils {
    public static Connection getConnection(){
        //定义数据库连接
        //Oracle：jdbc:oracle:thin:@localhost:1521:DBName
        //SqlServer：jdbc:microsoft:sqlserver://localhost:1433; DatabaseName=DBName
        //MySql：jdbc:mysql://localhost:3306/DBName
        String url = "jdbc:mysql://"+ Constants.DB_BASE_URI+"/"+ Constants.DB_NAME+"?useUnicode=true&characterEncoding=utf-8";
        String user = Constants.DB_USENAME;
        String password = Constants.DB_PWD;
        //定义数据库连接对象
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(url,user,password);
        }catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) {
        //建立数据库连接
        Connection connection = getConnection();
        //实例化数据库操作对象
        QueryRunner queryRunner = new QueryRunner();
        //插入sql语句
//        String sql_insert = "INSERT INTO `futureloan`.`member`(`id`, `reg_name`, `pwd`, `mobile_phone`, `type`, `leave_amount`, `reg_time`) VALUES (NULL, 'ceshi01', 'EA71297181626AF8F69FE93F57BA5D8F', '18837608242', 0, 0, '2022-11-16 15:36:09')";
//        //修改sql语句
//        String sql_update = "UPDATE `futureloan`.`member` SET `reg_name` = 'ceshi03' WHERE `id` = 1572484";
//        //对数据库进行更新操作
//        try {
//            queryRunner.update(connection,sql_update);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
        //查询数据库
        String sql_query = "SELECT count(*) FROM member WHERE id < 10";
        try {
            Long result = queryRunner.query(connection,sql_query,new ScalarHandler<Long>());
            System.out.println(result);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 关闭数据库连接
     * @param connection 数据库连接对象
     */
    public static void closeConnection(Connection connection){
        //判空
        if(connection != null) {
            //关闭数据库连接
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * sql的更新操作（包括增加+修改+删除）
     * @param sql
     */
    public static void update(String sql){
        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            queryRunner.update(connection,sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            //关闭数据库连接
            closeConnection(connection);
        }
    }

    /**
     * 查询所有的结果集
      * @param sql 要执行的sql语句
     * @return 返回的结果集
     */
    public static List<Map<String ,Object>> queryAll(String sql) {
        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        List<Map<String, Object>> result = null;
        try {
            result = queryRunner.query(connection, sql, new MapListHandler());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            closeConnection(connection);
        }
        return result;
    }

    /**
     * 查询结果中的第一条
     * @param sql
     * @return
     */
    public static Map<String ,Object> queryOne(String sql){
        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        Map<String, Object> result = null;
        try {
            result = queryRunner.query(connection, sql, new MapHandler());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            closeConnection(connection);
        }
        return result;
    }

    /**
     * 查询单条的数据
     * @param sql
     * @return
     */
    public static Object querySingleData(String sql){
        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        Object result = null;
        try {
            result = queryRunner.query(connection, sql, new ScalarHandler<Object>());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            closeConnection(connection);
        }
        return result;
    }
}
