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
import org.govt.model.AuthenticatedCommittee;
import org.govt.model.ComplaintResponse;
import org.govt.model.Grievance;
import org.govt.others.DBConfig;

/**
 *
 * @author Pravesh Ganwani
 */

@WebServlet("/ResponseController")
public class ResponseController extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession hs = req.getSession();
        AuthenticatedCommittee ac = (AuthenticatedCommittee)hs.getAttribute("user");
        
        Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
        
        Grievance gr = (Grievance)hs.getAttribute("complaintDetails");
        String formResponse = req.getParameter("response");
        
        ComplaintResponse cr = new ComplaintResponse();
        cr.setComplaintId(gr.getComplaintId());
        cr.setResponse(formResponse);
        cr.setResponseFrom(ac.getCommitteeDetails().getCommitteeId());
        cr.setResponseTo(gr.getComplaintStudentId());
        
        WebTarget webTarget = client.target(DBConfig.getApiHost()).path("responses/response");
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(cr, MediaType.APPLICATION_JSON));
        resp.sendRedirect(req.getHeader("referer"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp); //To change body of generated methods, choose Tools | Templates.
    }
    
}
