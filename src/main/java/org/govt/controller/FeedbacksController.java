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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.govt.model.Feedback;
import org.govt.model.Status;
import org.govt.others.DBConfig;

/**
 *
 * @author Pravesh Ganwani
 */

@WebServlet("/FeedbacksController")
public class FeedbacksController extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Feedback f = new Feedback();
        f.setName(req.getParameter("feedbackName"));
        f.setEmail(req.getParameter("feedbackEmail"));
        f.setFeedback(req.getParameter("feedback"));
        Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
        WebTarget webTarget = client.target(DBConfig.getApiHost()).path("feedbacks/feedback");
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(f, MediaType.APPLICATION_JSON));
        Status st = response.readEntity(Status.class);
        resp.sendRedirect("index.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp); //To change body of generated methods, choose Tools | Templates.
    }
    
}
