/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.govt.controller;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.govt.model.AuthenticatedCommittee;
import org.govt.model.RegistrationEmail;
import org.govt.model.Status;
import org.govt.model.Student;
import org.govt.others.DBConfig;

/**
 *
 * @author Pravesh Ganwani
 */

@WebServlet("/AddAccountsController")
@MultipartConfig(maxFileSize = 9000000000L)
public class AddAccountsController extends HttpServlet{
    
    public String randomPasswordGenerator() {
        int len = 10;
        
        // ASCII range - alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@!#$%&";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // each iteration of loop choose a character randomly from the given ASCII range
        // and append it to StringBuilder instance

        for (int i = 0; i < len; i++) {
                int randomIndex = random.nextInt(chars.length());
                sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession hs = req.getSession();
        AuthenticatedCommittee ac = (AuthenticatedCommittee)hs.getAttribute("user");
        Part part=req.getPart("excelFile");
        InputStream is = part.getInputStream();
        
        //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(is);
 
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
            
            // Create a DataFormatter to format and get each cell's value as String
            DataFormatter dataFormatter = new DataFormatter();
            
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            int flag = 0;
            while (rowIterator.hasNext()) 
            {
                Row row = rowIterator.next();
                flag++;
                if(flag == 1){
                    continue;
                }
                
                Student s = new Student();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                int cellNo = 0;
                while (cellIterator.hasNext()) 
                {
                    cellNo++;
                    Cell cell = cellIterator.next();
                    //Check the cell type and format accordingly
                    String cellValue = dataFormatter.formatCellValue(cell);
                    switch(cellNo){
                        case 1: 
                        {
                            s.setStudentFirstName(cellValue);
                            break;
                        }
                        case 2: 
                        {
                            s.setStudentMiddleName(cellValue);
                            break;
                        }
                        case 3: 
                        {
                            s.setStudentLastName(cellValue);
                            break;
                        }
                        case 4: 
                        {
                            s.setStudentEmail(cellValue);
                            break;
                        }
                        case 5: 
                        {
                            s.setStudentUID(cellValue);
                            break;
                        }
                        case 6: 
                        {
                            s.setCourseId(Integer.parseInt(cellValue));
                            break;
                        }
                        case 7:
                        {
                            s.setStudentMobileNo(cellValue);
                            break;
                        }
                    }
                }
                s.setStudentPassword(randomPasswordGenerator());
                s.setInstituteId(ac.getCommitteeDetails().getCommitteeId());
                s.setIsVerified(1);
                s.setIsActive(1);
                Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFeature.class ) );
                WebTarget webTarget = client.target(DBConfig.getApiHost()).path("students/student");
                Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                Response response = invocationBuilder.post(Entity.entity(s, MediaType.APPLICATION_JSON));
                Status st = response.readEntity(Status.class);
                System.out.println("Student Registered");
                RegistrationEmail rm = new RegistrationEmail();
                rm.setRegisteredEmail(s.getStudentEmail());
                rm.setTitle("Your account has been registered. Here are your credentials");
                rm.setSubject("User Registered");
                rm.setRegisteredPassword(s.getStudentPassword());
                rm.setTo(s.getStudentEmail());
                webTarget = client.target(DBConfig.getApiHost()).path("additional/registration-email");
                invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                response = invocationBuilder.post(Entity.entity(rm, MediaType.APPLICATION_JSON));
            }
            is.close();
            resp.sendRedirect("committee-dashboard.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp); //To change body of generated methods, choose Tools | Templates.
    }
    
}
