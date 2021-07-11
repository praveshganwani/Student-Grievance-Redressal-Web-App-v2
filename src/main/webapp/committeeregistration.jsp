<%-- 
    Document   : committeeregistration
    Created on : 5 Jul, 2020, 10:00:47 PM
    Author     : Pravesh Ganwani
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Committee Registration | SGRP</title>
    </head>
    <body>
        <script>
            function changeSublevel(value){
                var url = "PopulateDropdownServlet?type="+value;
                
                var xhttp=new XMLHttpRequest();
                xhttp.onreadystatechange=function()
                {
                    if(xhttp.readyState==4 && xhttp.status==200)
                    {
                              document.getElementById('sublevel_id').innerHTML=xhttp.responseText;
                              if(xhttp.responseText != '<option value=\"None\" selected>Not Needed</option>')
                              {
                                  document.getElementById('display_universities').style.display = "block";
                              }
                    }
                };
                
                xhttp.open("get",url, true);
                xhttp.send();
            }
        </script>
        <jsp:include page="header.jsp"/>
        <div class="container mt-5">
        <center><h1 style="text-transform: uppercase;"><%=request.getParameter("type")%> Registration<i class="fas fa-user-check pl-3"></i></h1></center>
        <form action="RegistrationController?type=${param.type}" method="post">
            <c:if test="${param.type=='committee'}">
                <div class="form-group">
                    <label for="level">Select Committee Level</label>
                    <select class="form-control" name="level" id="level_id" onchange="changeSublevel(this.value)">
                        <option value="" selected="selected" disabled="disabled">Select Committee Level</option>
                        <option value="univ">University Level</option>
                        <option value="inst">Institute Level</option>
                    </select>
                </div>
                <div class="form-group" id="display_universities" style="display: none;">
                    <label for="sublevel">Select University (Only For Institutes)</label>
                    <select class="form-control" name="sublevel" id="sublevel_id">
                    </select>
                    <small id="emailHelp" class="form-text text-muted">Note: Only registered and verified universities are displayed. If you cannot find your university, please contact your university to register with us.</small>
                </div>
            </c:if>
            <div class="form-group">
              <label for="committeename">Committee Name</label>
              <input type="name" class="form-control" name="username" id="committeename" aria-describedby="name">
            </div>
            <div class="form-group">
              <label for="exampleInputEmail1">Email address</label>
              <input type="email" class="form-control" name="emailid" id="exampleInputEmail1" aria-describedby="emailHelp">
            </div>
            <div class="form-group">
              <label for="contact_no">Committee Contact Number</label>
              <input type="number" class="form-control" name="contact" id="contact_no" aria-describedby="contact" required>
            </div>
            <div class="form-group">
              <label for="exampleInputPassword1">Password</label>
              <input type="password" class="form-control" name="password" id="exampleInputPassword1">
            </div>
            <button type="submit" class="btn btn-block btn-primary">Register</button>
            <button type="reset" class="btn btn-block btn-danger">Reset</button>
        </form>
        </div>
        <jsp:include page="footer.jsp"/>
    </body>
</html>
