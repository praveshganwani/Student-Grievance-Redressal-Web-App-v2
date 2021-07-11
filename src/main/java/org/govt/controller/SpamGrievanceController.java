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
import org.govt.model.Activity;
import org.govt.model.AuthenticatedAdmin;
import org.govt.model.AuthenticatedCommittee;
import org.govt.model.Committee;
import org.govt.model.ComplaintResponse;
import org.govt.model.Course;
import org.govt.model.Email;
import org.govt.model.Grievance;
import org.govt.model.Keyword;
import org.govt.model.NameById;
import org.govt.model.Status;
import org.govt.model.Student;
import org.govt.others.DBConfig;

/**
 *
 * @author Pravesh Ganwani
 */

@WebServlet("/SpamGrievanceController")
public class SpamGrievanceController extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
        WebTarget webTarget = client.target(DBConfig.getApiHost()).path("activities/activity/"+req.getParameter("complaintId")+"/ASC");
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();

        List<Activity> listOfActivities = response.readEntity(new GenericType<List<Activity>>(){});
        
        webTarget = client.target(DBConfig.getApiHost()).path("responses/response/"+req.getParameter("complaintId")+"/ASC");
        invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        response = invocationBuilder.get();

        List<ComplaintResponse> listOfResponses = response.readEntity(new GenericType<List<ComplaintResponse>>(){});
        
        HttpSession hs = req.getSession();
        AuthenticatedAdmin ac = (AuthenticatedAdmin)hs.getAttribute("user");
        try {
            String complaintId = req.getParameter("complaintId");
                int flag = 0;
                int index = -1;
                Grievance grievance = null;
                Keyword k = null;
                List<Grievance> listOfSpamGrievances = (List<Grievance>)hs.getAttribute("spamGrievances");
                List<Keyword> listOfSpamKeywords = (List<Keyword>)hs.getAttribute("spamKeywords");
               
                List<Activity> listOfComplaintActivities = new ArrayList<Activity>();
                List<NameById> listOfActivitiesFrom = new ArrayList<NameById>();
                List<NameById> listOfActivitiesTo = new ArrayList<NameById>();
                
                List<ComplaintResponse> listOfComplaintResponses = new ArrayList<ComplaintResponse>();
                List<NameById> listOfResponsesFrom = new ArrayList<NameById>();
                List<NameById> listOfResponsesTo = new ArrayList<NameById>();
                
                for (Iterator<Grievance> g = listOfSpamGrievances.iterator(); g.hasNext();) {
                    index++;
                    grievance = g.next();
                    if(grievance.getComplaintId().equals(complaintId)) {
                        flag = 1;
                        k = listOfSpamKeywords.get(index);
                        if(grievance.getComplaintIsAnonymous() != 1)
                        {
                            webTarget = client.target(DBConfig.getApiHost()).path("students/student/"+grievance.getComplaintStudentId());
                            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                            response = invocationBuilder.get();
                            Student s = response.readEntity(Student.class);
                            webTarget = client.target(DBConfig.getApiHost()).path("additional/"+s.getInstituteId());
                            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                            response = invocationBuilder.get();
                            NameById nbiInstituteName = response.readEntity(NameById.class);
                            hs.setAttribute("instituteName", nbiInstituteName);
                            hs.setAttribute("studentDetails", s);
                        }
                        for (Iterator<Activity> a = listOfActivities.iterator(); a.hasNext();) {
                            Activity activity = a.next();
                            if(activity.getComplaintId().equals(complaintId)) {
                                listOfComplaintActivities.add(activity);
                                client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
                                webTarget = client.target(DBConfig.getApiHost()).path("additional/"+activity.getActivityFrom());
                                invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                                response = invocationBuilder.get();
                                NameById nbiFrom = response.readEntity(NameById.class);
                                listOfActivitiesFrom.add(nbiFrom);

                                client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
                                webTarget = client.target(DBConfig.getApiHost()).path("additional/"+activity.getActivityTo());
                                invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                                response = invocationBuilder.get();
                                NameById nbiTo = response.readEntity(NameById.class);
                                listOfActivitiesTo.add(nbiTo);
                            }
                        }
                        
                        for (Iterator<ComplaintResponse> a = listOfResponses.iterator(); a.hasNext();) {
                            ComplaintResponse comResp = a.next();
                            if(comResp.getComplaintId().equals(complaintId)) {
                                listOfComplaintResponses.add(comResp);
                                client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
                                NameById nbiFrom = null;
                                if(grievance.getComplaintIsAnonymous() == 1) {
                                    if(comResp.getResponseFrom().startsWith("STUD")) {
                                        nbiFrom = new NameById();
                                        nbiFrom.setName("Anonymous");
                                    }
                                    else {
                                        webTarget = client.target(DBConfig.getApiHost()).path("additional/"+comResp.getResponseFrom());
                                        invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                                        response = invocationBuilder.get();
                                        nbiFrom = response.readEntity(NameById.class);
                                    }
                                }
                                else {
                                    webTarget = client.target(DBConfig.getApiHost()).path("additional/"+comResp.getResponseFrom());
                                    invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                                    response = invocationBuilder.get();
                                    nbiFrom = response.readEntity(NameById.class);
                                }
                                listOfResponsesFrom.add(nbiFrom);

                                client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
                                webTarget = client.target(DBConfig.getApiHost()).path("additional/"+comResp.getResponseTo());
                                invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                                response = invocationBuilder.get();
                                NameById nbiTo = response.readEntity(NameById.class);
                                listOfResponsesTo.add(nbiTo);
                            }
                        }
                        break;
                    }
                }
                
                if(flag == 1) {
                    hs.setAttribute("complaintDetails", grievance);
                    hs.setAttribute("complaintKeywords", k);
                    hs.setAttribute("complaintActivities", listOfComplaintActivities);
                    hs.setAttribute("activitiesFrom", listOfActivitiesFrom);
                    hs.setAttribute("activitiesTo", listOfActivitiesTo);
                    hs.setAttribute("complaintResponses", listOfComplaintResponses);
                    hs.setAttribute("responsesFrom", listOfResponsesFrom);
                    hs.setAttribute("responsesTo", listOfResponsesTo);
                }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
