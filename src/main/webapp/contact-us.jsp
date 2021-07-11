<%-- 
    Document   : contact-us
    Created on : 2 Aug, 2020, 10:38:20 PM
    Author     : Pravesh Ganwani
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SGRP | Contact Us</title>
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        <div class="container mt-5">
            <h2>Contact Us On These Numbers</h2>
            <div class="row mb-2">
                <div class="col-sm-6">
                  <div class="card border border-secondary rounded-lg">
                    <div class="card-body">
                      <h5 class="card-title">Andhra University</h5>
                      <p class="card-text">022 21010740</p>
                    </div>
                  </div>
                </div>
                <div class="col-sm-6">
                  <div class="card border border-secondary rounded-lg">
                    <div class="card-body">
                      <h5 class="card-title">Central Board Of Education</h5>
                      <p class="card-text">022 37827427</p>
                    </div>
                  </div>
                </div>
              </div>
            <hr>
            <h2 class="mt-3">Get In Touch With Us. Send Us Your Feedback</h2>
            <form class="border border-secondary p-3 rounded-lg" action="FeedbacksController" method="post">
            <div class="form-row">
              <div class="form-group col-md-6">
                <label for="inputPassword4">Name</label>
                <input type="text" class="form-control" id="inputPassword4" name="feedbackName" placeholder="Enter Full Name Here..">
              </div>
              <div class="form-group col-md-6">
                <label for="inputEmail4">Email</label>
                <input type="email" class="form-control" id="inputEmail4" name="feedbackEmail" placeholder="Enter Email Here..">
              </div>
            </div>
            <div class="form-group">
              <label for="inputAddress">Feedback</label>
              <textarea class="form-control" id="inputAddress" name="feedback" placeholder="Your Feedback.." rows="5"></textarea>
            </div>
            <button type="submit" class="btn btn-primary btn-block">Send Feedback</button>
          </form>
        <jsp:include page="footer.jsp"/>
        </div>
    </body>
</html>
