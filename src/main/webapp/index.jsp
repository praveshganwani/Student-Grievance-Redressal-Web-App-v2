
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Home | SGRP</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <link href="css/index.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        <div id="myCarousel" class="carousel slide" data-ride="carousel">
            <ol class="carousel-indicators">
              <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
              <li data-target="#myCarousel" data-slide-to="1"></li>
            </ol>
            <div class="carousel-inner">
              <div class="carousel-item active">
                <svg class="bd-placeholder-img" width="100%" height="100%" xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid slice" focusable="false" role="img"><rect width="100%" height="100%" fill="#777"/><img src="images/img2.jpg" alt=""/></svg>
                <div class="container">
                    <div class="carousel-caption text-left" style="color: black;">
                    <h1>Your Feedback Matters.</h1>
                    <p>We take your feedback seriously and this is why we keep our statistical data open to public interpretation.</p>
                  </div>
                </div>
              </div>
              <div class="carousel-item">
                  <svg class="bd-placeholder-img" width="100%" height="100%" xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid slice" focusable="false" role="img"><rect width="100%" height="100%" fill="#777"/><img src="images/img1.jpg" alt=""/></svg>              
                <div class="container">
                  <div class="carousel-caption text-left" style="color: black;">
                    <h1>You Question, We Answer</h1>
                    <p>Feel free to lodge any complaint with us. We will serve you to fullest of our efforts.</p>
                  </div>
                </div>
              </div>
            </div>
            <a class="carousel-control-prev" href="#myCarousel" role="button" data-slide="prev">
              <span class="carousel-control-prev-icon" aria-hidden="true"></span>
              <span class="sr-only">Previous</span>
            </a>
            <a class="carousel-control-next" href="#myCarousel" role="button" data-slide="next">
              <span class="carousel-control-next-icon" aria-hidden="true"></span>
              <span class="sr-only">Next</span>
            </a>
        </div>
        <c:if test="${user==null}">
        <div class="custom-container container">
<!--            <div class="card">
                <div class="face face1">
                  <div class="content">
                      <p>Not Yet Registered? <a href="studentregistration.jsp?type=student" style="color: #8bb98f">Register here</a> as a Student</p>
                    <a href="login.jsp?type=student" class="btn10">
                      <span>Login</span>
                      <div class="transition"></div>
                    </a>
                  </div>
                </div>
                <div class="face face2">
                  <h2>Student Login</h2>
                </div>
            </div>-->
            <div class="card">
              <div class="face face1">
                <div class="content">
                    <p>Not Yet Registered? <a href="committeeregistration.jsp?type=committee" style="color: #8bb98f">Register here</a> as a Committee</p>
                  <a href="login.jsp?type=committee" class="btn10">
                    <span>Login</span>
                    <div class="transition"></div>
                  </a>
                </div>
              </div>
              <div class="face face2">
                <h2>Committee Login</h2>
              </div>
            </div>
            <div class="card">
              <div class="face face1">
                <div class="content">
                  <p>Contact Administrative Office for Registrations</p>
                  <a href="login.jsp?type=admin" class="btn10">
                    <span>Login</span>
                    <div class="transition"></div>
                  </a>
                </div>
              </div>
              <div class="face face2">
                <h2>Admin Login</h2>
              </div>
            </div>
        </div>
        </c:if>
        <jsp:include page="footer.jsp"/>
    </body>
</html>
