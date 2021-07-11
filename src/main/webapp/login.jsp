<%-- 
    Document   : login
    Created on : 5 Jul, 2020, 9:10:39 PM
    Author     : Pravesh Ganwani
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css" integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU" crossorigin="anonymous">
        <title>Login | SGRP</title>
    </head>
    <body>
        <%
            session.removeAttribute("user");
            session.removeAttribute("type");
            session.invalidate();
        %>
        <jsp:include page="header.jsp"/>
        <div class="container mt-5  ">
            <center><h1 style="text-transform: uppercase;"><%= request.getParameter("type")%> Login<i class="fas fa-sign-in-alt pl-3"></i></h1></center>
            <form action="LoginController?type=${param.type}" method="post">
                <div class="form-group">
                  <label for="exampleInputEmail1">Registered Email address</label>
                  <input type="email" class="form-control" id="exampleInputEmail1" name="useremail" aria-describedby="emailHelp" required>
                </div>
                <div class="form-group">
                  <label for="exampleInputPassword1">Password</label>
                  <input type="password" class="form-control" name="userpassword" id="exampleInputPassword1" required>
                </div>
                <button type="submit" class="btn btn-block btn-primary">Login</button>
            </form>
        </div>
        <jsp:include page="footer.jsp"/>
    </body>
</html>
