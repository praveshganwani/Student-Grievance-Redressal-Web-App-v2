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
import org.govt.model.AuthenticatedCommittee;
import org.govt.model.Category;
import org.govt.model.Committee;
import org.govt.model.Course;
import org.govt.model.Grievance;
import org.govt.model.Keyword;
import org.govt.model.Student;
import org.govt.others.DBConfig;
import org.govt.model.Email;

/**
 *
 * @author Pravesh Ganwani
 */

@WebServlet("/CommitteeDashboard")
public class CommitteeDashboardController extends HttpServlet{
    
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
        AuthenticatedCommittee ac = (AuthenticatedCommittee)hs.getAttribute("user");
        Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
        WebTarget webTarget = client.target(DBConfig.getApiHost()).path("grievances");
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();

        List<Grievance> listOfGrievances = response.readEntity(new GenericType<List<Grievance>>(){});

        webTarget = client.target(DBConfig.getApiHost()).path("keywords");
        invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        response = invocationBuilder.get();

        List<Keyword> listOfKeywords = response.readEntity(new GenericType<List<Keyword>>(){});
        
        webTarget = client.target(DBConfig.getApiHost()).path("activities");
        invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        response = invocationBuilder.get();
        
        List<Activity> listOfActivities = response.readEntity(new GenericType<List<Activity>>(){});
        
        List<Grievance> listofSpamGrievances = new ArrayList<Grievance>();
        List<Grievance> listofCommitteeGrievances = new ArrayList<Grievance>();
        List<Keyword> listofCommitteeKeywords = new ArrayList<Keyword>();
        List<Grievance> listofForwardedGrievances = new ArrayList<Grievance>();
        List<Keyword> listofForwardedKeywords = new ArrayList<Keyword>();
        List<Committee> listofForwardedActivity = new ArrayList<Committee>();
        List<Grievance> listofForwards = new ArrayList<Grievance>();
        List<Committee> listofForwardsActivities = new ArrayList<Committee>();
        List<Grievance> listofSolvedGrievances = new ArrayList<Grievance>();
        
        List<Grievance> listofEscalatedGrievances = new ArrayList<Grievance>();
        List<Keyword> listofEscalatedKeywords = new ArrayList<Keyword>();
        List<Committee> listofEscalatedActivity = new ArrayList<Committee>();
        List<Grievance> listofEscalates = new ArrayList<Grievance>();
        List<Committee> listofEscalatesActivities = new ArrayList<Committee>();
        
        System.out.println(response.getStatus());
        for (Iterator<Grievance> i = listOfGrievances.iterator(); i.hasNext();) {
            Grievance g = i.next();
            int flag = 0;
            if(g.getComplaintCommitteeId().equals(ac.getCommitteeDetails().getCommitteeId())) {
                if(g.getComplaintIsSolved() == 0) {
                    if(g.getComplaintIsSpam() == 0) {
                        for (Iterator<Activity> m = listOfActivities.iterator(); m.hasNext();) {
                            Activity activity = m.next();
                            if((activity.getActivityType().equals("forward") || activity.getActivityType().equals("escalate")) && activity.getActivityFrom().equals(g.getComplaintCommitteeId()) && activity.getComplaintId().equals(g.getComplaintId())) {
                                flag = 1;
                                break;
                            }
                        }
                        if(flag == 0) {
                            listofCommitteeGrievances.add(g);
                            pendings++;
                        }
                        else
                            continue;
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
                        listofCommitteeKeywords.add(finalKeyword);
                    }
                    else {
                        listofSpamGrievances.add(g);
                    }
                }
                else {
                    for (Iterator<Activity> m = listOfActivities.iterator(); m.hasNext();) {
                        Activity activity = m.next();
                        if((activity.getActivityType().equals("forward") || activity.getActivityType().equals("escalate")) && activity.getActivityFrom().equals(g.getComplaintCommitteeId()) && activity.getComplaintId().equals(g.getComplaintId())) {
                            flag = 1;
                            break;
                        }
                    }
                    if(flag == 0) {
                        listofSolvedGrievances.add(g);
                        solved++;
                    }
                    else
                        continue;
                }
            }
        }
        hs.setAttribute("pendingGrievances", listofCommitteeGrievances);
        hs.setAttribute("pendingKeywords", listofCommitteeKeywords);
        hs.setAttribute("solvedGrievances", listofSolvedGrievances);
        hs.setAttribute("spamGrievances", listofSpamGrievances);
        
        for (Iterator<Grievance> i = listOfGrievances.iterator(); i.hasNext();) {
            Grievance g = i.next();
            int flag = 0;
            webTarget = client.target(DBConfig.getApiHost()).path("activities/activity/"+g.getComplaintId());
            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
            response = invocationBuilder.get();
            List<Activity> activities = response.readEntity(new GenericType<List<Activity>>(){});
            for (Iterator<Activity> act = activities.iterator(); act.hasNext();) {
                Activity activ = act.next();
                if(activ.getActivityType().equals("escalate")) {
                    if(activ.getActivityTo().equals(ac.getCommitteeDetails().getCommitteeId())) {
                        if(g.getComplaintIsSolved() == 0) {
                            listofEscalatedGrievances.add(g);
                            pendings++;
                            webTarget = client.target(DBConfig.getApiHost()).path("committees/committee/"+activ.getActivityFrom());
                            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                            response = invocationBuilder.get();
                            Committee com = response.readEntity(Committee.class);
                            listofEscalatedActivity.add(com);
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
                            listofEscalatedKeywords.add(finalKeyword);
                            break;
                        }
                        else {
                            listofSolvedGrievances.add(g);
                            solved++;
                        }
                    }
                    else if(activ.getActivityFrom().equals(ac.getCommitteeDetails().getCommitteeId())) {
                        listofEscalates.add(g);
                        webTarget = client.target(DBConfig.getApiHost()).path("committees/committee/"+activ.getActivityTo());
                        invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                        response = invocationBuilder.get();
                        Committee com = response.readEntity(Committee.class);
                        listofEscalatesActivities.add(com);
                    }
                }
                else if(activ.getActivityType().equals("forward")) {
                    if(activ.getActivityTo().equals(ac.getCommitteeDetails().getCommitteeId())) {
                        if(g.getComplaintIsSolved() == 0) {
                            listofForwardedGrievances.add(g);
                            pendings++;
                            webTarget = client.target(DBConfig.getApiHost()).path("committees/committee/"+activ.getActivityFrom());
                            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                            response = invocationBuilder.get();
                            Committee com = response.readEntity(Committee.class);
                            listofForwardedActivity.add(com);
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
                            listofForwardedKeywords.add(finalKeyword);
                            break;
                        }
                        else {
                            listofSolvedGrievances.add(g);
                            solved++;
                        }
                    }
                    else if(activ.getActivityFrom().equals(ac.getCommitteeDetails().getCommitteeId())) {
                        listofForwards.add(g);
                        webTarget = client.target(DBConfig.getApiHost()).path("committees/committee/"+activ.getActivityTo());
                        invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                        response = invocationBuilder.get();
                        Committee com = response.readEntity(Committee.class);
                        listofForwardsActivities.add(com);
                    }
                }
            }
//            if(a.getActivityType().equals("escalate")) {
//                if(a.getActivityTo().equals(ac.getCommitteeDetails().getCommitteeId())) {
//                    webTarget = client.target(DBConfig.getApiHost()).path("grievances/grievance/"+a.getComplaintId());
//                    invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
//                    response = invocationBuilder.get();
//                    Grievance grievance = response.readEntity(Grievance.class);
//                    if(grievance.getComplaintIsSolved() == 0) {
//                        listofEscalatedGrievances.add(grievance);
//                        pendings++;
//                        webTarget = client.target(DBConfig.getApiHost()).path("committees/committee/"+a.getActivityFrom());
//                        invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
//                        response = invocationBuilder.get();
//                        Committee com = response.readEntity(Committee.class);
//                        listofEscalatedActivity.add(com);
//                        String finalListOfKeywords = "<strong>";
//                        int count = 0;
//                        for (Iterator<Keyword> k = listOfKeywords.iterator(); k.hasNext();) {
//                            Keyword key = k.next();
//                            if(key.getComplaintId().equals(grievance.getComplaintId())) {
//                                if(count == 0)
//                                    finalListOfKeywords += key.getKeywordName()+"</strong>";
//                                else 
//                                    finalListOfKeywords += "<strong> | " + key.getKeywordName() + "</strong>";
//                                count = 1;
//                            }
//                        }
//                        Keyword finalKeyword = new Keyword();
//                        finalKeyword.setKeywordName(finalListOfKeywords);
//                        listofEscalatedKeywords.add(finalKeyword);
//                    }
//                    else {
//                        listofSolvedGrievances.add(grievance);
//                        solved++;
//                    }
//                }
//                else if(a.getActivityFrom().equals(ac.getCommitteeDetails().getCommitteeId())) {
//                    webTarget = client.target(DBConfig.getApiHost()).path("grievances/grievance/"+a.getComplaintId());
//                    invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
//                    response = invocationBuilder.get();
//                    Grievance grievance = response.readEntity(Grievance.class);
//                    listofForwards.add(grievance);
//                    webTarget = client.target(DBConfig.getApiHost()).path("committees/committee/"+a.getActivityTo());
//                    invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
//                    response = invocationBuilder.get();
//                    Committee com = response.readEntity(Committee.class);
//                    listofForwardsActivities.add(com);
//                }
//            }
//            else if(a.getActivityType().equals("escalate")) {
//                if(a.getActivityTo().equals(ac.getCommitteeDetails().getCommitteeId())) {
//                    webTarget = client.target(DBConfig.getApiHost()).path("grievances/grievance/"+a.getComplaintId());
//                    invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
//                    response = invocationBuilder.get();
//                    Grievance grievance = response.readEntity(Grievance.class);
//                    if(grievance.getComplaintIsSolved() == 0) {
//                        listofEscalatedGrievances.add(grievance);
//                        pendings++;
//                        webTarget = client.target(DBConfig.getApiHost()).path("committees/committee/"+a.getActivityFrom());
//                        invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
//                        response = invocationBuilder.get();
//                        Committee com = response.readEntity(Committee.class);
//                        listofEscalatedActivity.add(com);
//                        String finalListOfKeywords = "<strong>";
//                        int count = 0;
//                        for (Iterator<Keyword> k = listOfKeywords.iterator(); k.hasNext();) {
//                            Keyword key = k.next();
//                            if(key.getComplaintId().equals(grievance.getComplaintId())) {
//                                if(count == 0)
//                                    finalListOfKeywords += key.getKeywordName()+"</strong>";
//                                else 
//                                    finalListOfKeywords += "<strong> | " + key.getKeywordName() + "</strong>";
//                                count = 1;
//                            }
//                        }
//                        Keyword finalKeyword = new Keyword();
//                        finalKeyword.setKeywordName(finalListOfKeywords);
//                        listofEscalatedKeywords.add(finalKeyword);
//                    }
//                    else {
//                        listofSolvedGrievances.add(grievance);
//                        solved++;
//                    }
//                }
//                else if(a.getActivityFrom().equals(ac.getCommitteeDetails().getCommitteeId())) {
//                    webTarget = client.target(DBConfig.getApiHost()).path("grievances/grievance/"+a.getComplaintId());
//                    invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
//                    response = invocationBuilder.get();
//                    Grievance grievance = response.readEntity(Grievance.class);
//                    listofEscalates.add(grievance);
//                    webTarget = client.target(DBConfig.getApiHost()).path("committees/committee/"+a.getActivityTo());
//                    invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
//                    response = invocationBuilder.get();
//                    Committee com = response.readEntity(Committee.class);
//                    listofEscalatesActivities.add(com);
//                }
//            }
            
        }
        
        hs.setAttribute("forwardedGrievances", listofForwardedGrievances);
        hs.setAttribute("forwardedKeywords", listofForwardedKeywords);
        hs.setAttribute("forwardedActivities", listofForwardedActivity);
        hs.setAttribute("forwards", listofForwards);
        hs.setAttribute("forwardsActivities", listofForwardsActivities);
        hs.setAttribute("escalatedGrievances", listofEscalatedGrievances);
        hs.setAttribute("escalatedKeywords", listofEscalatedKeywords);
        hs.setAttribute("escalatedActivities", listofEscalatedActivity);
        hs.setAttribute("escalates", listofEscalates);
        hs.setAttribute("escalatesActivities", listofEscalatesActivities);
        hs.setAttribute("solvedGrievances", listofSolvedGrievances);
        
        if(ac.getCommitteeDetails().getCommitteeType().equals("inst")) {
            client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
            webTarget = client.target(DBConfig.getApiHost()).path("students");
            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
            response = invocationBuilder.get();
            List<Student> listOfStudents = response.readEntity(new GenericType<List<Student>>(){});
            List<Student> unverifiedStudents = new ArrayList<Student>();
            List<Student> verifiedStudents = new ArrayList<Student>();
            List<Student> blockedStudents = new ArrayList<Student>();
            for (Iterator<Student> s = listOfStudents.iterator(); s.hasNext();) {
                Student stud = s.next();
                if(stud.getInstituteId().equals(ac.getCommitteeDetails().getCommitteeId()) && stud.getIsVerified() == 0) {
                    unverifiedStudents.add(stud);
                }
                if(stud.getInstituteId().equals(ac.getCommitteeDetails().getCommitteeId()) && stud.getIsVerified() == 1) {
                    verifiedStudents.add(stud);
                }
                if(stud.getInstituteId().equals(ac.getCommitteeDetails().getCommitteeId()) && stud.getIsActive() == 0) {
                    blockedStudents.add(stud);
                }
            }
            hs.setAttribute("unverified", unverifiedStudents);
            hs.setAttribute("blocked", blockedStudents);
            hs.setAttribute("verified", verifiedStudents);
            client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
            webTarget = client.target(DBConfig.getApiHost()).path("courses");
            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
            response = invocationBuilder.get();
            List<Course> listOfCourses = response.readEntity(new GenericType<List<Course>>(){});
            List<Course> listOfCommitteeCourses = new ArrayList<Course>();
            for (Iterator<Course> c = listOfCourses.iterator(); c.hasNext();) {
                Course course = c.next();
                if(course.getInstituteId().equals(ac.getCommitteeDetails().getCommitteeId())) {
                    listOfCommitteeCourses.add(course);
                }
            }
            hs.setAttribute("courses", listOfCommitteeCourses);
        } 
        else if(ac.getCommitteeDetails().getCommitteeType().equals("univ")) {
            client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
            webTarget = client.target(DBConfig.getApiHost()).path("committees");
            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
            response = invocationBuilder.get();
            List<Committee> listOfCommittees = response.readEntity(new GenericType<List<Committee>>(){});
            List<Committee> unverifiedCommittees = new ArrayList<Committee>();
            List<Committee> verifiedCommittees = new ArrayList<Committee>();
            for (Iterator<Committee> c = listOfCommittees.iterator(); c.hasNext();) {
                Committee com = c.next();
                if(com.getParentId().equals(ac.getCommitteeDetails().getCommitteeId()) && com.getIsVerified() == 0) {
                    unverifiedCommittees.add(com);
                }
                if(com.getParentId().equals(ac.getCommitteeDetails().getCommitteeId()) && com.getIsVerified() == 1) {
                    verifiedCommittees.add(com);
                }
            }
            hs.setAttribute("unverified", unverifiedCommittees);
            hs.setAttribute("verified", verifiedCommittees);
        }
        
        webTarget = client.target(DBConfig.getApiHost()).path("categories");
        invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        response = invocationBuilder.get();

        List<Category> listOfCategories = response.readEntity(new GenericType<List<Category>>(){});
        hs.setAttribute("categories", listOfCategories);
        hs.setAttribute("pendings", pendings);
        hs.setAttribute("solved", solved);
        hs.setAttribute("total", pendings+solved);
    }
    catch (Exception e) {
        resp.sendRedirect("index.jsp");
    }
    }
}
