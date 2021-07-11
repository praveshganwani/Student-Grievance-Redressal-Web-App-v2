<%-- 
    Document   : studentregistration
    Created on : 5 Jul, 2020, 11:51:56 PM
    Author     : Pravesh Ganwani
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Student Registration | SGRP</title>
    </head>
    <body>
        <script>
            function changeCollege(value){
                var url = "PopulateDropdownServlet?name="+value;
                
                var xhttp=new XMLHttpRequest();
                xhttp.onreadystatechange=function()
                {
                    if(xhttp.readyState==4 && xhttp.status==200)
                    {
                              document.getElementById('college').innerHTML=xhttp.responseText;
                              document.getElementById('display_colleges').style.display = "block";
                    }
                };
                
                xhttp.open("post",url, true);
                xhttp.send();
            }
            
            function changeCourse(value){
                var url = "PopulateCourses?id="+value;
                
                var xhttp=new XMLHttpRequest();
                xhttp.onreadystatechange=function()
                {
                    if(xhttp.readyState==4 && xhttp.status==200)
                    {
                              document.getElementById('course').innerHTML=xhttp.responseText;
                              document.getElementById('display_courses').style.display = "block";
                    }
                };
                
                xhttp.open("post",url, true);
                xhttp.send();
            }
        </script>
        <jsp:include page="header.jsp"/>
        <div class="container mt-5">
            <center><h1 style="text-transform: uppercase;"><%=request.getParameter("type")%> Registration<i class="fas fa-user-check pl-3"></i></h1></center>
            <form action="RegistrationController?type=${param.type}" method="post">
                <div class="form-group">
                    <label for="level">Please Select Your University</label>
                    <select class="form-control" name="level" id="university" onchange="changeCollege(this.value)">
                        <option value="dropdown" selected disabled>Universities List</option>
                        <sql:setDataSource driver="com.mysql.cj.jdbc.Driver" 
                                           url="jdbc:mysql://sihdb.c3vyqhzl6aif.us-east-1.rds.amazonaws.com:3306/sgradb"
                                           user="sihadmin"
                                           password="sihdb123"
                                           var="con"
                                           />
                        <sql:query dataSource="${con}" var="data">
                            select * from committees where committee_type=? and committee_isverified=1
                            <sql:param>univ</sql:param>
                        </sql:query>
                        <c:forEach items="${data.rows}" var="row">
                            <option value="${row.committee_id}">${row.committee_name}</option>
                        </c:forEach>
                    </select>
                    <small id="emailHelp" class="form-text text-muted">Note: Only registered and verified universities are displayed. If you cannot find your university, please contact your university to register with us.</small>
                </div>
                <div class="form-group" id="display_colleges" style="display: none;">
                  <label for="sublevel">Please Select Your Institute</label>
                  <select class="form-control" name="sublevel" id="college" onchange="changeCourse(this.value)">
                      <option selected="selected" disabled="disabled">Please Select University First</option>
                  </select>
                  <small id="emailHelp" class="form-text text-muted">Note: Only registered and verified institutes are displayed. If you cannot find your institute, please contact your institute to register with us.</small>
                </div>
                <div class="form-group">
                  <label for="firstname">First Name</label>
                  <input type="text" class="form-control" id="firstname" name="firstname" required>
                </div>
                <div class="form-group">
                  <label for="middlename">Middle Name</label>
                  <input type="text" class="form-control" id="middlename" name="middlename" required>
                </div>
                <div class="form-group">
                  <label for="lastname">Last Name</label>
                  <input type="text" class="form-control" id="lastname" name="lastname" required>
                </div>
                <div class="form-group" id="display_courses" style="display: none;">
                  <label for="sublevel">Please Select Your Course</label>
                  <select class="form-control" name="coursename" id="course">
                      <option selected="selected" disabled="disabled">Please Select Institute First</option>
                  </select>
                  <small id="emailHelp" class="form-text text-muted">Note: Only courses offered by the institute are displayed. In case of any discrepancies contact your institute.</small>
                </div>
                <div class="form-group">
                  <label for="uid">Student Unique ID</label>
                  <input type="text" class="form-control" name="enrollment" id="uid" required>
                </div>
                <div class="form-group">
                  <label for="exampleInputEmail1">Email address</label>
                  <input type="email" class="form-control" name="emailid" id="exampleInputEmail1" aria-describedby="emailHelp" required>
                </div>
                <div class="form-group">
                  <label for="exampleInputPassword1">Password</label>
                  <input type="password" class="form-control" name="password" id="exampleInputPassword1" required>
                </div>
                <div class="form-group form-check">
                  <input type="checkbox" class="form-check-input" id="exampleCheck1">
                  <label class="form-check-label" for="exampleCheck1">Check me out</label>
                </div>
                <button type="submit" class="btn btn-block btn-primary">Submit</button>
                <button type="reset" class="btn btn-block btn-danger">Reset</button>
            </form>
        </div>
        <jsp:include page="footer.jsp"/>
    </body>
</html>
