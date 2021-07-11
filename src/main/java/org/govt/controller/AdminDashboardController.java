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
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.govt.model.Activity;
import org.govt.model.AuthenticatedAdmin;
import org.govt.model.Category;
import org.govt.model.Committee;
import org.govt.model.Course;
import org.govt.model.Feedback;
import org.govt.model.Grievance;
import org.govt.model.Keyword;
import org.govt.model.Student;
import org.govt.others.DBConfig;

/**
 *
 * @author Pravesh Ganwani
 */

@WebServlet("/AdminDashboard")
public class AdminDashboardController extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int pendings = 0;
        int solved = 0;
        int total = 0;
        
        try {
            HttpSession hs = req.getSession();
            AuthenticatedAdmin aa = (AuthenticatedAdmin)hs.getAttribute("user");
            Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
            WebTarget webTarget = client.target(DBConfig.getApiHost()).path("grievances");
            Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();

            List<Grievance> listOfGrievances = response.readEntity(new GenericType<List<Grievance>>(){});
            
            webTarget = client.target(DBConfig.getApiHost()).path("keywords");
            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
            response = invocationBuilder.get();

            List<Keyword> listOfKeywords = response.readEntity(new GenericType<List<Keyword>>(){});
            
            webTarget = client.target(DBConfig.getApiHost()).path("feedbacks");
            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
            response = invocationBuilder.get();
            
            List<Feedback> listOfFeedbacks = response.readEntity(new GenericType<List<Feedback>>(){});
            
            List<Grievance> listOfSpamGrievances = new ArrayList<Grievance>();
            List<Keyword> listOfSpamKeywords = new ArrayList<Keyword>();

            webTarget = client.target(DBConfig.getApiHost()).path("activities");
            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
            response = invocationBuilder.get();

            List<Activity> listOfActivities = response.readEntity(new GenericType<List<Activity>>(){});

            System.out.println(response.getStatus());
            for (Iterator<Grievance> i = listOfGrievances.iterator(); i.hasNext();) {
                Grievance g = i.next();
                int flag = 0;
                if(g.getComplaintIsSolved() == 0) {
                    pendings++;
                    if(g.getComplaintIsSpam() == 1) {
                        listOfSpamGrievances.add(g);
                        String finalListOfKeywords = "<strong>";
                        int count = 0;
                        for (Iterator<Keyword> k = listOfKeywords.iterator(); k.hasNext();) {
                            Keyword key = k.next();
                            if(key.getComplaintId().equals(g.getComplaintId())) {
                                if(count == 0)
                                    finalListOfKeywords += key.getKeywordName()+"</strong>";
                                else 
                                    finalListOfKeywords += "<strong> | " + key.getKeywordName() + "</strong>";
                                count = 1;
                            }
                        }
                        Keyword finalKeyword = new Keyword();
                        finalKeyword.setKeywordName(finalListOfKeywords);
                        listOfSpamKeywords.add(finalKeyword);
                    }
                }
                else {
                    solved++;
                }
            }

            client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
            webTarget = client.target(DBConfig.getApiHost()).path("committees");
            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
            response = invocationBuilder.get();
            List<Committee> listOfCommittees = response.readEntity(new GenericType<List<Committee>>(){});
            List<Committee> unverifiedCommittees = new ArrayList<Committee>();
            for (Iterator<Committee> c = listOfCommittees.iterator(); c.hasNext();) {
                Committee com = c.next();
                if(com.getIsVerified() == 0 && com.getCommitteeType().equals("univ")) {
                    unverifiedCommittees.add(com);
                }
            }
            
            webTarget = client.target(DBConfig.getApiHost()).path("categories");
            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
            response = invocationBuilder.get();

            List<Category> listOfCategories = response.readEntity(new GenericType<List<Category>>(){});
            hs.setAttribute("categories", listOfCategories);
            
            hs.setAttribute("unverified", unverifiedCommittees);
            hs.setAttribute("pendings", pendings);
            hs.setAttribute("solved", solved);
            hs.setAttribute("total", pendings+solved);
            hs.setAttribute("spamGrievances", listOfSpamGrievances);
            hs.setAttribute("spamKeywords", listOfSpamKeywords);
            hs.setAttribute("feedbacks", listOfFeedbacks);
        }
        catch (Exception e) {
            resp.sendRedirect("index.jsp");
        }
    }
    
}
