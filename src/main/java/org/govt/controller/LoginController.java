/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.govt.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.govt.model.AuthenticatedAdmin;
import org.govt.model.AuthenticatedCommittee;
import org.govt.model.AuthenticatedStudent;
import org.govt.model.Committee;
import org.govt.model.Grievance;
import org.govt.model.Keyword;
import org.govt.model.Student;
import org.govt.model.User;
import org.govt.others.DBConfig;

/**
 *
 * @author Pravesh Ganwani
 */

@WebServlet("/LoginController")
public class LoginController extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");
        Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
        WebTarget webTarget = client.target(DBConfig.getApiHost()).path("login/"+req.getParameter("type"));
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        User user = new User();  
        user.setUserEmail(req.getParameter("useremail"));
        user.setUserPassword(req.getParameter("userpassword"));
        Response response = invocationBuilder.post(Entity.entity(user, MediaType.APPLICATION_JSON));
        HttpSession hs = req.getSession();
        if(type.equals("student")) {
            AuthenticatedStudent as = response.readEntity(AuthenticatedStudent.class);
            System.out.println(as.getStatus());
            switch (as.getStatus()) {
                case 1:
                    System.out.println(as.getStudentDetails().getStudentFullName());
                    hs.setAttribute("msg", "Login Successful");
                    hs.setAttribute("user", as);
                    hs.setAttribute("type", "student");
//                    hs.setAttribute("pagename", "student-dashboard.jsp");
                    hs.setAttribute("pagename", "index.jsp");
                    hs.setAttribute("popupType", "success");
                    resp.sendRedirect("popup.jsp");
                    break;
                case -2:
                    hs.setAttribute("msg", "User Not Verified. Please Wait For Verification");
                    hs.setAttribute("pagename", "index.jsp");
                    hs.setAttribute("popupType", "info");
                    resp.sendRedirect("popup.jsp");
                    break;
                case -1:
                    hs.setAttribute("msg", "Password Incorrect");
                    hs.setAttribute("pagename", "login.jsp?type="+req.getParameter("type"));
                    hs.setAttribute("popupType", "error");
                    resp.sendRedirect("popup.jsp");
                    break;
                case -3:
                    hs.setAttribute("msg", "User Not Registered. Please Register First");
                    hs.setAttribute("pagename", req.getParameter("type")+"registration.jsp?type="+req.getParameter("type"));
                    hs.setAttribute("popupType", "error");
                    resp.sendRedirect("popup.jsp");
                    break;
                default:
                    hs.setAttribute("msg", "Something Went Wrong. Please Try Again Later");
                    hs.setAttribute("pagename", "index.jsp");
                    hs.setAttribute("popupType", "info");
                    resp.sendRedirect("popup.jsp");
                    break;
            }
        }
        if(type.equals("committee")) {
            AuthenticatedCommittee ac = response.readEntity(AuthenticatedCommittee.class);
            System.out.println(ac.getStatus());
            switch (ac.getStatus()) {
                case 1:
                    System.out.println(ac.getCommitteeDetails().getCommitteeName());
                    hs.setAttribute("msg", "Login Successful");
                    hs.setAttribute("user", ac);
                    hs.setAttribute("type", "committee");
                    hs.setAttribute("pagename", "committee-dashboard.jsp");
                    hs.setAttribute("popupType", "success");
                    resp.sendRedirect("popup.jsp");
                    break;
                case -2:
                    hs.setAttribute("msg", "User Not Verified. Please Wait For Verification");
                    hs.setAttribute("pagename", "index.jsp");
                    hs.setAttribute("popupType", "info");
                    resp.sendRedirect("popup.jsp");
                    break;
                case -1:
                    hs.setAttribute("msg", "Password Incorrect");
                    hs.setAttribute("pagename", "login.jsp?type="+req.getParameter("type"));
                    hs.setAttribute("popupType", "error");
                    resp.sendRedirect("popup.jsp");
                    break;
                case -3:
                    hs.setAttribute("msg", "User Not Registered. Please Register First");
                    hs.setAttribute("pagename", req.getParameter("type")+"registration.jsp?type="+req.getParameter("type"));
                    hs.setAttribute("popupType", "error");
                    resp.sendRedirect("popup.jsp");
                    break;
                default:
                    hs.setAttribute("msg", "Something Went Wrong. Please Try Again Later");
                    hs.setAttribute("pagename", "index.jsp");
                    hs.setAttribute("popupType", "info");
                    resp.sendRedirect("popup.jsp");
                    break;
            }
        }
        if(type.equals("admin")) {
            AuthenticatedAdmin aa = response.readEntity(AuthenticatedAdmin.class);
            System.out.println(aa.getStatus());
            switch (aa.getStatus()) {
                case 1:
                    System.out.println(aa.getAdminDetails().getAdminName());
                    hs.setAttribute("msg", "Login Successful");
                    hs.setAttribute("user", aa);
                    hs.setAttribute("type", "admin");
                    hs.setAttribute("pagename", "admin-dashboard.jsp");
                    hs.setAttribute("popupType", "success");
                    resp.sendRedirect("popup.jsp");
                    break;
                case -2:
                    hs.setAttribute("msg", "User Not Verified. Please Wait For Verification");
                    hs.setAttribute("pagename", "index.jsp");
                    hs.setAttribute("popupType", "info");
                    resp.sendRedirect("popup.jsp");
                    break;
                case -1:
                    hs.setAttribute("msg", "Password Incorrect");
                    hs.setAttribute("pagename", "login.jsp?type="+req.getParameter("type"));
                    hs.setAttribute("popupType", "error");
                    resp.sendRedirect("popup.jsp");
                    break;
                case -3:
                    hs.setAttribute("msg", "Admin Not Registered.");
                    hs.setAttribute("pagename", "index.jsp");
                    hs.setAttribute("popupType", "error");
                    resp.sendRedirect("popup.jsp");
                    break;
                default:
                    hs.setAttribute("msg", "Something Went Wrong. Please Try Again Later");
                    hs.setAttribute("pagename", "index.jsp");
                    hs.setAttribute("popupType", "info");
                    resp.sendRedirect("popup.jsp");
                    break;
            }
        }
        
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp); //To change body of generated methods, choose Tools | Templates.
    }
    
}
