/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycode;

import java.beans.Statement;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author ryo
 */
public class AccessToDatabase {
    //String url =  "jdbc:postgresql://localhost/postgres";

    String url = "jdbc:postgresql://ec2-23-23-226-41.compute-1.amazonaws.com:5432/d4ppsmhem0c3dv";
    //String user = "postgres";
    String user = "srzpgehvjaipad";
    //String password = "postgres";
    int numberOfRow = 0;
    String password = "CH-M4-l4EC9xR72a-6i5F-YAH_";
    //これはどっから？
    String odateturo = "jdbc:postgresql://ec2-23-23-226-41.compute-1.amazonaws.com:5432/d4ppsmhem0c3dv?sslmode=require&user=srzpgehvjaipad&password=CH-M4-l4EC9xR72a-6i5F-YAH_&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
    //numberOfRow = 0;
    LinkedHashMap hashdata;
    String aritleTitle;
    String aritleContent;
    String timestamp;
    LinkedHashMap<Integer, LinkedHashMap<String, String>> outerMap = new LinkedHashMap<Integer, LinkedHashMap<String, String>>();
    LinkedHashMap<String, String> innerMap;
    Connection con;
    ResultSet executeQuery;

    public AccessToDatabase() throws SQLException, ClassNotFoundException, URISyntaxException {
        // ドライバクラスをロード 
        Class.forName("org.postgresql.Driver"); // PostgreSQLの場合 
        //Connection con = DriverManager.getConnection(url, user, password);//localの場合
        con = DriverManager.getConnection(odateturo);
        hashdata = new LinkedHashMap();

        System.out.print("conection ok");

        try (java.sql.Statement stmt = con.createStatement() //なぜこの書き方？
                ) {
            String sql = "select * FROM notebook_posts;";
            // テーブル照会実行
            executeQuery = stmt.executeQuery(sql);
            while (executeQuery.next()) {
                innerMap = new LinkedHashMap<String, String>();
                numberOfRow++;
                aritleTitle = executeQuery.getString("post_title");
                aritleContent = executeQuery.getString("post_content");
                timestamp = executeQuery.getString("post_timestamp");
                hashdata.put(numberOfRow, aritleTitle);
                innerMap.put("title", aritleTitle);
                innerMap.put("content", aritleContent);
                innerMap.put("timestamp", timestamp);
                outerMap.put(numberOfRow, innerMap);
            }
        }
        executeQuery.close();

    }

    public void postDataToDatabase(String title, String content) throws SQLException {
        //とりあえず引数なしで行ってみる
        // 1) create a java calendar instance
        Calendar calendar = Calendar.getInstance();
        // 2) get a java.util.Date from the calendar instance.
        //    this date will represent the current instant, or "now".
        java.util.Date now = calendar.getTime();
        // 3) a java current time (now) instance
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
        //chage to String 
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTimestamp);
        System.out.println(timestamp);
        //timestamp = "2016-11-06 00:00:00";

        //String sql = "insert into notebook_posts (post_title,post_content,post_timestamp) values (" + title + "," + content + "," + timestamp + ");";
        String sql = "insert into notebook_posts (post_title,post_content,post_timestamp) values ('"+title+"','"+content+"','"+timestamp+"');";
        
        // テーブル照会実行
        java.sql.Statement stmt = con.createStatement();
        //stmt.executeQuery(sql);
        java.sql.Statement resultStmt;
        //executeQuery.close();
        resultStmt = con.createStatement();
        ResultSet rs;
        resultStmt.execute(sql);//not 必要なし//run

    }

    public int getDataFromDatabase() throws SQLException, ClassNotFoundException {
        return numberOfRow;
    }

    public LinkedHashMap getContents() {
        //return hashdata;
        return outerMap;
    }
    //この書き方だめっぽい。
//        public ResultSet getDataFromDatabase() throws SQLException, ClassNotFoundException{
//        Class.forName("org.postgresql.Driver"); // PostgreSQLの場合 
//        Connection con = DriverManager.getConnection(url, user, password);
//        System.out.print("conection ok");
//        ResultSet executeQuery;
//         try (java.sql.Statement stmt = con.createStatement() //なぜこの書き方？
//         ) {
//             String sql = "select * FROM playground;";
//             // テーブル照会実行
//             executeQuery = stmt.executeQuery(sql);
//         }
//        return executeQuery;
//    }
}
