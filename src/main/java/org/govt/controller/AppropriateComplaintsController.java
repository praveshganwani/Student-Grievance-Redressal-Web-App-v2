/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.govt.controller;

import java.io.IOException;
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
import org.govt.model.Activity;
import org.govt.model.AuthenticatedAdmin;
import org.govt.model.Committee;
import org.govt.model.Email;
import org.govt.model.Grievance;
import org.govt.model.Status;
import org.govt.model.Student;
import org.govt.others.DBConfig;

/**
 *
 * @author Pravesh Ganwani
 */

@WebServlet("/AppropriateComplaintsController")
public class AppropriateComplaintsController extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession hs = req.getSession();
        AuthenticatedAdmin ac = (AuthenticatedAdmin)hs.getAttribute("user");
        
        Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
        
        Grievance gr = (Grievance)hs.getAttribute("complaintDetails");
        
        WebTarget webTarget = client.target(DBConfig.getApiHost()).path("activities/activity/"+gr.getComplaintId());
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        
        List<Activity> listOfActivities = response.readEntity(new GenericType<List<Activity>>(){});
        
        if(ac != null) {
            int flag = 0;
            for (Iterator<Activity> i = listOfActivities.iterator(); i.hasNext();) {
                Activity activity = i.next();
                if(activity.getActivityType().equals("solve")) {
                    flag = 1;
                    break;
                }
            }
            if(flag == 0) {
                gr.setComplaintIsSpam(0);
                client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
                webTarget = client.target(DBConfig.getApiHost()).path("grievances/spam");
                invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                response = invocationBuilder.post(Entity.entity(gr, MediaType.APPLICATION_JSON));
                Status st = response.readEntity(Status.class);
                webTarget = client.target(DBConfig.getApiHost()).path("students/student/"+gr.getComplaintStudentId());
                invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                response = invocationBuilder.get();
                Student s = response.readEntity(Student.class);
                webTarget = client.target(DBConfig.getApiHost()).path("committees/committee/"+gr.getComplaintCommitteeId());
                invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                response = invocationBuilder.get();
                Committee c = response.readEntity(Committee.class);
                
                Email e = new Email();
                e.setActivityFrom(ac.getAdminDetails().getAdminName());
                e.setG(gr);
                e.setSubject("Complaint Appropriate");
                e.setTitle("Complaint Was Marked Appropriate. Reason: "+req.getParameter("appropriateComments"));
                e.setTo(s.getStudentEmail().toLowerCase());
                webTarget = client.target(DBConfig.getApiHost()).path("additional/email");
                invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                response = invocationBuilder.post(Entity.entity(e, MediaType.APPLICATION_JSON));
                
                Email e2 = new Email();
                e2.setActivityFrom(ac.getAdminDetails().getAdminName());
                e2.setG(gr);
                e2.setSubject("Complaint Appropriate");
                e2.setTitle("Complaint Was Marked Appropriate. Reason: "+req.getParameter("appropriateComments"));
                e2.setTo(c.getCommitteeEmail().toLowerCase());
                webTarget = client.target(DBConfig.getApiHost()).path("additional/email");
                invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                response = invocationBuilder.post(Entity.entity(e2, MediaType.APPLICATION_JSON));
                resp.sendRedirect("admin-dashboard.jsp");   
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp); //To change body of generated methods, choose Tools | Templates.
    }
    
}
