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
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.govt.model.Activity;
import org.govt.model.AuthenticatedCommittee;
import org.govt.model.Grievance;
import org.govt.model.Keyword;
import org.govt.others.DBConfig;
import java.util.ArrayList;
import javax.ws.rs.client.Entity;
import org.govt.model.Committee;
import org.govt.model.Course;
import org.govt.model.NameById;
import org.govt.model.Status;
import org.govt.model.Student;
import org.govt.model.ComplaintResponse;
import org.govt.model.Email;

/**
 *
 * @author Pravesh Ganwani
 */

@WebServlet("/SolveGrievanceController")
public class SolveGrievanceController extends HttpServlet{

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
        
        try {
            String complaintId = req.getParameter("complaintId");
            String type = req.getParameter("complaintType");
            HttpSession hs = req.getSession();
            AuthenticatedCommittee ac = (AuthenticatedCommittee)hs.getAttribute("user");
            if(type.equals("pending")) {
                int flag = 0;
                int index = -1;
                Grievance grievance = null;
                Keyword k = null;
                List<Grievance> listOfCommitteeGrievances = (List<Grievance>)hs.getAttribute("pendingGrievances");
                List<Keyword> listOfCommitteeKeywords = (List<Keyword>)hs.getAttribute("pendingKeywords");
               
                List<Activity> listOfComplaintActivities = new ArrayList<Activity>();
                List<NameById> listOfActivitiesFrom = new ArrayList<NameById>();
                List<NameById> listOfActivitiesTo = new ArrayList<NameById>();
                
                List<ComplaintResponse> listOfComplaintResponses = new ArrayList<ComplaintResponse>();
                List<NameById> listOfResponsesFrom = new ArrayList<NameById>();
                List<NameById> listOfResponsesTo = new ArrayList<NameById>();
                
                for (Iterator<Grievance> g = listOfCommitteeGrievances.iterator(); g.hasNext();) {
                    index++;
                    grievance = g.next();
                    if(grievance.getComplaintId().equals(complaintId)) {
                        flag = 1;
                        k = listOfCommitteeKeywords.get(index);
                        if(grievance.getComplaintIsAnonymous() != 1)
                        {
                            webTarget = client.target(DBConfig.getApiHost()).path("students/student/"+grievance.getComplaintStudentId());
                            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                            response = invocationBuilder.get();
                            Student s = response.readEntity(Student.class);
                            List<Course> committeeCourses = (List<Course>)hs.getAttribute("courses");
                            for (Iterator<Course> c = committeeCourses.iterator(); c.hasNext();) {
                                Course course = c.next();
                                if(s.getCourseId() == course.getCourseId()) {
                                    hs.setAttribute("courseName", course.getCourseName());
                                    break;
                                }
                            }
                            webTarget = client.target(DBConfig.getApiHost()).path("additional/"+s.getInstituteId());
                            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                            response = invocationBuilder.get();
                            NameById nbiInstituteName = response.readEntity(NameById.class);
                            hs.setAttribute("instituteName", nbiInstituteName);
                            hs.setAttribute("studentDetails", s);
                        }
                        if(grievance.getLastActivity() == null || grievance.getLastActivity().equals("")) {
                            Activity viewActivity = new Activity();
                            viewActivity.setComplaintId(complaintId);
                            viewActivity.setActivityFrom(ac.getCommitteeDetails().getCommitteeId());
                            viewActivity.setActivityTo(grievance.getComplaintStudentId());
                            viewActivity.setActivityType("view");
                            webTarget = client.target(DBConfig.getApiHost()).path("activities/activity");
                            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                            response = invocationBuilder.post(Entity.entity(viewActivity, MediaType.APPLICATION_JSON));
                            grievance.setLastActivity("view");
                            client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
                            webTarget = client.target(DBConfig.getApiHost()).path("grievances/activity");
                            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                            response = invocationBuilder.post(Entity.entity(grievance, MediaType.APPLICATION_JSON));
                            Status st = response.readEntity(Status.class);
                            webTarget = client.target(DBConfig.getApiHost()).path("students/student/"+grievance.getComplaintStudentId());
                            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                            response = invocationBuilder.get();
                            Student s = response.readEntity(Student.class);
                            Email e = new Email();
                            e.setActivityFrom(ac.getCommitteeDetails().getCommitteeName());
                            e.setG(grievance);
                            e.setSubject("Complaint Viewed");
                            e.setTitle("Your Complaint Was Viewed");
                            e.setTo(s.getStudentEmail().toLowerCase());
                            webTarget = client.target(DBConfig.getApiHost()).path("additional/email");
                            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                            response = invocationBuilder.post(Entity.entity(e, MediaType.APPLICATION_JSON));
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
                
                List<Committee> forwardsAvailable = new ArrayList<Committee>();
                if(ac.getCommitteeDetails().getCommitteeType().equals("inst")) {
                    webTarget = client.target(DBConfig.getApiHost()).path("committees/committee/"+ac.getCommitteeDetails().getParentId());
                    invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                    response = invocationBuilder.get();
                    Committee c = response.readEntity(Committee.class);
                    forwardsAvailable.add(c);
                }
                else if(ac.getCommitteeDetails().getCommitteeType().equals("univ")) {
                    webTarget = client.target(DBConfig.getApiHost()).path("committees");
                    invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                    response = invocationBuilder.get();
                    List<Committee> availableCommittees = response.readEntity(new GenericType<List<Committee>>(){});
                    for (Iterator<Committee> c = availableCommittees.iterator(); c.hasNext();) {
                        Committee com = c.next();
                        if(com.getParentId().equals(ac.getCommitteeDetails().getCommitteeId())) {
                            forwardsAvailable.add(com);
                        }
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
                    hs.setAttribute("forwardsAvailable", forwardsAvailable);
                }
            }
            else if(type.equals("received")) {
                List<Grievance> listOfReceivedGrievances = (List<Grievance>)hs.getAttribute("forwardedGrievances");
                List<Keyword> listOfReceivedKeywords = (List<Keyword>)hs.getAttribute("forwardedKeywords");
                List<Activity> listOfComplaintActivities = new ArrayList<Activity>();
                List<NameById> listOfActivitiesFrom = new ArrayList<NameById>();
                List<NameById> listOfActivitiesTo = new ArrayList<NameById>();
                
                List<ComplaintResponse> listOfComplaintResponses = new ArrayList<ComplaintResponse>();
                List<NameById> listOfResponsesFrom = new ArrayList<NameById>();
                List<NameById> listOfResponsesTo = new ArrayList<NameById>();
                
                int flag = 0;
                int index = -1;
                Grievance grievance = null;
                Keyword k = null;
                client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
                for (Iterator<Grievance> g = listOfReceivedGrievances.iterator(); g.hasNext();) {
                    index++;
                    grievance = g.next();
                    if(grievance.getComplaintId().equals(complaintId)) {
                        flag = 1;
                        k = listOfReceivedKeywords.get(index);
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
                        int activityFlag = 0;
                        for (Iterator<Activity> a = listOfActivities.iterator(); a.hasNext();) {
                            Activity activity = a.next();
                            if(activity.getComplaintId().equals(grievance.getComplaintId()) && activity.getActivityType().equals("view") && activity.getActivityFrom().equals(ac.getCommitteeDetails().getCommitteeId()) && activity.getActivityTo().equals(grievance.getComplaintStudentId())) {
                                activityFlag = 1;
                                break;
                            }
                        }
                        if(activityFlag == 0) {
                            Activity viewActivity = new Activity();
                            viewActivity.setComplaintId(complaintId);
                            viewActivity.setActivityFrom(ac.getCommitteeDetails().getCommitteeId());
                            viewActivity.setActivityTo(grievance.getComplaintStudentId());
                            viewActivity.setActivityType("view");
                            webTarget = client.target(DBConfig.getApiHost()).path("activities/activity");
                            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                            response = invocationBuilder.post(Entity.entity(viewActivity, MediaType.APPLICATION_JSON));
                            grievance.setLastActivity("view");
                            client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
                            webTarget = client.target(DBConfig.getApiHost()).path("grievances/activity");
                            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                            response = invocationBuilder.post(Entity.entity(grievance, MediaType.APPLICATION_JSON));
                            Status st = response.readEntity(Status.class);
                            webTarget = client.target(DBConfig.getApiHost()).path("students/student/"+grievance.getComplaintStudentId());
                            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                            response = invocationBuilder.get();
                            Student s = response.readEntity(Student.class);
                            Email e = new Email();
                            e.setActivityFrom(ac.getCommitteeDetails().getCommitteeName());
                            e.setG(grievance);
                            e.setSubject("Complaint Viewed");
                            e.setTitle("Your Complaint Was Viewed");
                            e.setTo(s.getStudentEmail().toLowerCase());
                            webTarget = client.target(DBConfig.getApiHost()).path("additional/email");
                            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                            response = invocationBuilder.post(Entity.entity(e, MediaType.APPLICATION_JSON));
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
            }
            else if(type.equals("solved")) {
                webTarget = client.target(DBConfig.getApiHost()).path("grievances/grievance/"+complaintId);
                invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                response = invocationBuilder.get();
                Grievance grievance = response.readEntity(Grievance.class);
                List<Activity> listOfComplaintActivities = new ArrayList<Activity>();
                List<NameById> listOfActivitiesFrom = new ArrayList<NameById>();
                List<NameById> listOfActivitiesTo = new ArrayList<NameById>();
                
                List<ComplaintResponse> listOfComplaintResponses = new ArrayList<ComplaintResponse>();
                List<NameById> listOfResponsesFrom = new ArrayList<NameById>();
                List<NameById> listOfResponsesTo = new ArrayList<NameById>();
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
                hs.setAttribute("complaintDetails", grievance);
                hs.setAttribute("complaintActivities", listOfComplaintActivities);
                hs.setAttribute("activitiesFrom", listOfActivitiesFrom);
                hs.setAttribute("activitiesTo", listOfActivitiesTo);
                hs.setAttribute("complaintResponses", listOfComplaintResponses);
                hs.setAttribute("responsesFrom", listOfResponsesFrom);
                hs.setAttribute("responsesTo", listOfResponsesTo);
            }
            else if(type.equals("escalated")) {
                List<Grievance> listOfReceivedGrievances = (List<Grievance>)hs.getAttribute("escalatedGrievances");
                List<Keyword> listOfReceivedKeywords = (List<Keyword>)hs.getAttribute("escalatedKeywords");
                List<Activity> listOfComplaintActivities = new ArrayList<Activity>();
                List<NameById> listOfActivitiesFrom = new ArrayList<NameById>();
                List<NameById> listOfActivitiesTo = new ArrayList<NameById>();
                
                List<ComplaintResponse> listOfComplaintResponses = new ArrayList<ComplaintResponse>();
                List<NameById> listOfResponsesFrom = new ArrayList<NameById>();
                List<NameById> listOfResponsesTo = new ArrayList<NameById>();
                
                int flag = 0;
                int index = -1;
                Grievance grievance = null;
                Keyword k = null;
                client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
                for (Iterator<Grievance> g = listOfReceivedGrievances.iterator(); g.hasNext();) {
                    index++;
                    grievance = g.next();
                    if(grievance.getComplaintId().equals(complaintId)) {
                        flag = 1;
                        k = listOfReceivedKeywords.get(index);
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
                        int activityFlag = 0;
                        for (Iterator<Activity> a = listOfActivities.iterator(); a.hasNext();) {
                            Activity activity = a.next();
                            if(activity.getComplaintId().equals(grievance.getComplaintId()) && activity.getActivityType().equals("view") && activity.getActivityFrom().equals(ac.getCommitteeDetails().getCommitteeId()) && activity.getActivityTo().equals(grievance.getComplaintStudentId())) {
                                activityFlag = 1;
                                break;
                            }
                        }
                        if(activityFlag == 0) {
                            Activity viewActivity = new Activity();
                            viewActivity.setComplaintId(complaintId);
                            viewActivity.setActivityFrom(ac.getCommitteeDetails().getCommitteeId());
                            viewActivity.setActivityTo(grievance.getComplaintStudentId());
                            viewActivity.setActivityType("view");
                            webTarget = client.target(DBConfig.getApiHost()).path("activities/activity");
                            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                            response = invocationBuilder.post(Entity.entity(viewActivity, MediaType.APPLICATION_JSON));
                            grievance.setLastActivity("view");
                            client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
                            webTarget = client.target(DBConfig.getApiHost()).path("grievances/activity");
                            invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                            response = invocationBuilder.post(Entity.entity(grievance, MediaType.APPLICATION_JSON));
                            Status st = response.readEntity(Status.class);
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
            }
        } catch (Exception e) {
            System.out.println(e);
        }
     }
    
}
