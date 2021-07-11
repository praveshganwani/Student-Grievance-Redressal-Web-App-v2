/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.govt.others;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Pravesh Ganwani
 */
public class DBConfig {
    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/sihdb?useSSL=false","root","root");
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return con;
    }
    
    public static String getDriverName(){
//        return "com.mysql.cj.jdbc.Driver";
        return "com.mysql.jdbc.Driver";
    }
    
    public static String getUrl() {
//        return "jdbc:mysql://sihdb.c3vyqhzl6aif.us-east-1.rds.amazonaws.com:3306/sgradb";
        return "jdbc:mysql://localhost:3306/sihdb?useSSL=false";
    }
    
    public static String getUsername() {
        return "root";
    }
    
    public static String getPassword() {
        return "root";
    }
    
    public static String getApiHost() {
//        return "http://sgrprestservice-env.eba-njsui4dk.us-east-1.elasticbeanstalk.com/webapi";
        return "http://localhost:8080/SGRPRestService/webapi";
    }
}
