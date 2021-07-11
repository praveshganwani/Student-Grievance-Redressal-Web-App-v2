/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.govt.others;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Pravesh Ganwani
 */

@WebServlet("/PopulateCourses")
public class PopulateCourses extends HttpServlet{
    
    // Courses
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        PrintWriter out = resp.getWriter();
        try {
            Connection con = DBConfig.getConnection();
            PreparedStatement ps = con.prepareStatement("select * from courses where institute_id=?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            out.write("<option disabled selected>Courses List</option>");
                while(rs.next())
                {
                    out.write("<option value=\""+rs.getString("course_id")+"\">"+rs.getString("course_name")+"</option>");
                }
                con.close();
        } catch (Exception e) {
        }
    }

    // Committee Registration
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
    }
    
}
