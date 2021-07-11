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

@WebServlet("/PopulateDropdownServlet")
public class PopulateDropdownServlet extends HttpServlet{

    // Student Registration
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        PrintWriter out = resp.getWriter();
        try 
            {
                Connection con = DBConfig.getConnection();
                PreparedStatement ps=con.prepareStatement("select * from committees where parent_id="+"? and committee_isverified=1");
                ps.setString(1, name);
                ResultSet rs=ps.executeQuery();
                out.write("<option disabled selected>Institutes List</option>");
                while(rs.next())
                {
                    out.write("<option value=\""+rs.getString("committee_id")+"\">"+rs.getString("committee_name")+"</option>");
                }
                con.close();
            }
            catch (Exception e) 
            {
                System.out.println(e);
            }
    }

    // Committee Registration
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");
        PrintWriter out=resp.getWriter();
        if(type.equals("inst")){
            try 
            {
                Connection con = DBConfig.getConnection();
                PreparedStatement ps=con.prepareStatement("select * from committees where committee_type="+"? and committee_isverified=1");
                ps.setString(1, "univ");
                ResultSet rs=ps.executeQuery();
                out.write("<option disabled selected>Please Select University First</option>");
                while(rs.next())
                {
                    out.write("<option value=\""+rs.getString("committee_id")+"\">"+rs.getString("committee_name")+"</option>");
                }
            }
            catch (Exception e) 
            {
                System.out.println(e);
            }
        }
        else{
            out.write("<option value=\"None\" selected>Not Needed</option>");
        }
    }
    
}
