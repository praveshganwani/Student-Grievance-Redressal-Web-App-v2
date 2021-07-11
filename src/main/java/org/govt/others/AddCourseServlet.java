/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.govt.others;

import java.io.IOException;
import java.io.PrintWriter;
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
import org.govt.model.Course;
import org.govt.model.Status;
import org.govt.model.Student;

/**
 *
 * @author Pravesh Ganwani
 */

@WebServlet("/AddCourseServlet")
public class AddCourseServlet extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession hs = req.getSession();
        String courseName = req.getParameter("courseName");
        PrintWriter out=resp.getWriter();
        String type = (String)hs.getAttribute("type");
        if(type.equals("committee")) {
            AuthenticatedCommittee ac = (AuthenticatedCommittee)hs.getAttribute("user");
            if(ac.getCommitteeDetails().getCommitteeType().equals("inst")) {
                Course c = new Course();
                c.setInstituteId(ac.getCommitteeDetails().getCommitteeId());
                c.setCourseName(courseName);
                Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
                WebTarget webTarget = client.target(DBConfig.getApiHost()).path("courses/course");
                Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                Response response = invocationBuilder.post(Entity.entity(c, MediaType.APPLICATION_JSON));
                Status st = response.readEntity(Status.class);
                if(st.getStatus() == 1)
                {
                    out.write("Verified");
                }
            }
        }
    }
    
}
