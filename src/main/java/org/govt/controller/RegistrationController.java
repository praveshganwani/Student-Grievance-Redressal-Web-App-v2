/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.govt.controller;

import java.io.IOException;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.govt.model.Committee;
import org.govt.model.Status;
import org.govt.model.Student;
import org.govt.others.DBConfig;

/**
 *
 * @author Pravesh Ganwani
 */
@WebServlet("/RegistrationController")
public class RegistrationController extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");
        HttpSession hs = req.getSession();
        if(type.equals("committee"))
        {
            Committee cd = new Committee();
            cd.setCommitteeName(req.getParameter("username"));
            cd.setCommitteeEmail(req.getParameter("emailid"));
            cd.setCommitteeContact(req.getParameter("contact"));
            cd.setCommitteePassword(req.getParameter("password"));
            cd.setCommitteeType(req.getParameter("level"));
            if(req.getParameter("sublevel")==null)
            {
                hs.setAttribute("msg", "University Not Yet Registered");
                hs.setAttribute("pagename", "index.jsp");
                hs.setAttribute("popupType", "info");
                resp.sendRedirect("popup.jsp");
            }
            if(req.getParameter("sublevel").equals("None"))
                cd.setParentId("0");
            else
            {
                cd.setParentId(req.getParameter("sublevel"));
            }
            cd.setIsVerified(0);
            cd.setIsActive(1);
            Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
            WebTarget webTarget = client.target(DBConfig.getApiHost()).path("committees/committee");
            Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.post(Entity.entity(cd, MediaType.APPLICATION_JSON));
            Status st = response.readEntity(Status.class);
            System.out.println(response.getStatus());
            if(st.getStatus() == 1) {
                hs.setAttribute("msg", "Registered SuccessFully. Please Wait For Your Account To Be Verified.");
                hs.setAttribute("pagename", "index.jsp");
                hs.setAttribute("popupType", "success");
                resp.sendRedirect("popup.jsp");
            }
            else {
                hs.setAttribute("msg", "Something Went Wrong. Please Try Again Later.");
                hs.setAttribute("pagename", "index.jsp");
                hs.setAttribute("popupType", "info");
                resp.sendRedirect("popup.jsp");
            }
        }
        else if(type.equals("student"))
        {
            Student sd = new Student();
            sd.setStudentFirstName(req.getParameter("firstname"));
            sd.setStudentMiddleName(req.getParameter("middlename"));
            sd.setStudentLastName(req.getParameter("lastname"));
            sd.setCourseId(Integer.parseInt(req.getParameter("coursename")));
            sd.setStudentUID(req.getParameter("enrollment"));
            sd.setStudentEmail(req.getParameter("emailid"));
            sd.setStudentPassword(req.getParameter("password"));
            String collegeName = req.getParameter("sublevel");
            if(collegeName==null){
                hs.setAttribute("msg", "Your College Is Not Yet Registered");
                hs.setAttribute("popupType", "info");
                hs.setAttribute("pagename", "index.jsp");
                resp.sendRedirect("popup.jsp");
            }
            sd.setInstituteId(req.getParameter("sublevel"));
            sd.setIsVerified(0);
            sd.setIsActive(1);
            Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
            WebTarget webTarget = client.target(DBConfig.getApiHost()).path("students/student");
            Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.post(Entity.entity(sd, MediaType.APPLICATION_JSON));
            Status st = response.readEntity(Status.class);
            System.out.println(response.getStatus());
            if(st.getStatus() == 1) {
                hs.setAttribute("msg", "Registered SuccessFully. Please Wait For Your Account To Be Verified.");
                hs.setAttribute("pagename", "index.jsp");
                hs.setAttribute("popupType", "success");
                resp.sendRedirect("popup.jsp");
            }
            else {
                hs.setAttribute("msg", "Something Went Wrong. Please Try Again Later.");
                hs.setAttribute("pagename", "index.jsp");
                hs.setAttribute("popupType", "info");
                resp.sendRedirect("popup.jsp");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp); //To change body of generated methods, choose Tools | Templates.
    }
    
}
