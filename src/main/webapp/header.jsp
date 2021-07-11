<%-- 
    Document   : header
    Created on : 5 Jul, 2020, 5:47:26 PM
    Author     : Pravesh Ganwani
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css" integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU" crossorigin="anonymous">
        <link href="css/header.css" rel="stylesheet" type="text/css"/>
        <!--Bootstrap CSS-->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
        <!-- JS, Popper.js, and jQuery -->
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js" integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI" crossorigin="anonymous"></script>
        <title>Header | SGRP</title>
    </head>
    <body class="p-0">
        <header>
            <nav class="navbar navbar-expand-md">
                <a class="navbar-brand" href="index.jsp">
                    <img src="images/govt_logo.jpeg" alt="" width="100px"/>
                </a>
                <div class="collapse navbar-collapse">
                    <span class="logo-heading navbar-brand ml-auto"><a style="text-decoration: none; color: black;" href="index.jsp">Government of Andhra Pradesh</a></span>
                    <ul class="nav navbar-nav ml-auto">
                        <li class="mr-2">
                            <a href="#" class="nav-link social-link"><i class="fa fa-facebook fa-lg"></i></a>
                        </li>
                        <li class="mr-2">
                            <a href="#" class="nav-link social-link"><i class="fa fa-twitter fa-lg"></i></a>
                        <li>
                            <a href="#" class="nav-link social-link"><i class="fa fa-instagram fa-lg"></i></a>
                        </li>
                    </ul>
                </div>
            </ul>
            </nav>
            <nav class="navbar navbar-expand-md navbar-dark custom-nav">
              <a class="navbar-brand" href="index.jsp"><i class="fas fa-mail-bulk pr-2"></i>Student Grievance Redressal Portal</a>
              <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
              </button>
              <div class="collapse navbar-collapse" id="navbarCollapse">
                <ul class="navbar-nav ml-auto">
                  <li class="nav-item">
                      <a class="nav-link" href="index.jsp"><i class="fas fa-home pr-2"></i>Home <span class="sr-only">(current)</span></a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="#"><i class="fas fa-globe-asia pr-2"></i>About Us</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="contact-us.jsp"><i class="fas fa-user-cog pr-2"></i>Contact Us</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="#"><i class="fas fa-clipboard-list pr-2"></i>Notices</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="#"><i class="fas fa-question pr-2"></i>FAQs</a>
                  </li>
                  <c:if test="${user!=null}">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <c:if test="${type=='committee'}">
                                ${user.committeeDetails.committeeName}
                            </c:if>
                            <c:if test="${type=='student'}">
                                ${user.studentDetails.studentFirstName}
                            </c:if>
                            <c:if test="${type=='admin'}">
                                ${user.adminDetails.adminName}
                            </c:if>
                        </a>
                        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                          <a class="dropdown-item" href="${type}-dashboard.jsp">Go to Dashboard</a>
                          <a class="dropdown-item" href="login.jsp?type=${type}">Log Out</a>
                        </div>
                    </li>
                  </c:if>
                </ul>
              </div>
            </nav>
        </header>
    </body>
</html>
