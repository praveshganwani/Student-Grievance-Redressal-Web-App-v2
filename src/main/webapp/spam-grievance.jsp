<%-- 
    Document   : spam-grievance
    Created on : 1 Aug, 2020, 4:03:40 PM
    Author     : Pravesh Ganwani
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Spam Grievance | Admin</title>
        <link href="css/solvegrievance.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <jsp:include page="/SpamGrievanceController"/>
        <jsp:include page="header.jsp"/>
        <fmt:setTimeZone value="IST"/>  
        <div class="container mt-5">
            <center>
                <div style="width: 80%;">
                    <a href="admin-dashboard.jsp" class="btn btn-success btn-lg mb-3" style="float: left;"><i class="fas fa-chevron-left pr-2"></i>Go Back To Dashboard</a>
                </div>
                <div class="card" style="width: 80%;">
                    <c:if test="${complaintDetails.complaintIsRedFlag == 1}">
                        <div class="card-body" style="background-color: #ffcccb">
                    </c:if>
                    <c:if test="${complaintDetails.complaintIsRedFlag == 0}">
                        <div class="card-body">
                    </c:if>
                      <h2 class="card-title mb-3">Complaint Title: ${complaintDetails.complaintTitle}</h2>
                      <h4 class="card-subtitle mb-3 text-muted">Complaint ID: ${complaintDetails.complaintId}</h4>
                      <h4 class="card-subtitle mb-3 text-muted">Category: ${categories[complaintDetails.categoryId-1].categoryName}</h4>
                      <c:if test="${param.complaintType!='solved'}"><h4 class="card-subtitle mb-3 text-muted">Keywords: ${complaintKeywords.keywordName}</h4></c:if>
                      <div style="text-align: left;">
                        <h4 class="card-subtitle mb-2">Complaint Details:</h4>
                        <p class="card-text pl-2 pr-2 pt-3 pb-3 alert alert-secondary">${complaintDetails.complaintDetail}</p>
                        <c:if test="${not empty complaintDetails.imageUrl}">
                            <a href="${complaintDetails.imageUrl}" target="_blank" class="card-subtitle mb-2 btn btn-primary">Download Attachments</a>
                        </c:if>
                        <fmt:parseDate pattern="yyyy-MM-dd HH:mm:ss" value="${complaintDetails.complaintDateTime}" var="parsedDate" />
                        <small class="form-text pl-2 pr-2 text-muted mb-2 alert alert-light">
                            Submitted on: <fmt:formatDate value="${parsedDate}" pattern="dd MMMM yyyy" /> by 
                            <c:if test="${complaintDetails.complaintIsAnonymous == 1}">
                                Anonymous
                            </c:if>
                            <c:if test="${complaintDetails.complaintIsAnonymous == 0}">
                                ${studentDetails.studentFullName} <a href="#" data-toggle="modal" class="alert-link" data-target="#studentDetails">Click Here To View Student Details</a>
                            </c:if>
                        </small>
                      </div>
                      <hr>
                    
                    <a href="#" data-toggle="modal" data-target="#spamComplaint" class="btn btn-primary">Mark As Spam</a>
                    <a href="#" data-toggle="modal" data-target="#appropriateComplaint" class="btn btn-secondary">Mark As Appropriate</a>
                    <a href="#" data-toggle="modal" data-target="#complaintActivity" class="btn btn-info">Complaint Activity Log</a>
                    <a href="#" data-toggle="modal" data-target="#complaintResponse" class="btn btn-warning">Complaint Responses Log</a>
                    </div>
                </div>
            </center>
            <c:if test="${complaintDetails.complaintIsAnonymous == 0}">
            <div class="modal fade" id="studentDetails" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered" role="document">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h5 class="modal-title" id="exampleModalLongTitle">Student Details</h5>
                      <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                      </button>
                    </div>
                    <div class="modal-body">
                      <table class="table mt-4">
                        <tbody>
                          <tr>
                            <th><i class="fas fa-university pr-3"></i>Student Institute Name</th>
                            <td>${instituteName.name}</td>
                          </tr>
                          <tr>
                            <th><i class="fas fa-users pr-3"></i>Student Name</th>
                            <td>${studentDetails.studentFullName}</td>
                          </tr>
                          <tr>
                            <th><i class="fas fa-at pr-3"></i>Student Email</th>
                            <td>${studentDetails.studentEmail}</td>
                          </tr>
                          <tr>
                            <th><i class="fas fa-address-book pr-3"></i>Student Unique ID</th>
                            <td>${studentDetails.studentUID}</td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                    <div class="modal-footer">
                      <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                  </div>
                </div>
            </div>
            </c:if>
            <div class="modal bd-example-modal-lg fade" id="complaintActivity" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h5 class="modal-title" id="exampleModalLongTitle">Complaint Activity Log</h5>
                      <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                      </button>
                    </div>
                    <div class="modal-body">
                      <div class="row example-basic">
                        <div class="col-md-12 example-title">
                            <h2>Timeline for the Complaint</h2>
                        </div>
                        <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2">
                            <ul class="timeline">
                                <c:forEach items="${complaintActivities}" var="complaintActivity" varStatus="loop">
                                    <li class="timeline-item">
                                        <div class="timeline-info">
                                            <fmt:parseDate pattern="yyyy-MM-dd HH:mm:ss" value="${complaintActivity.activityTime}" var="parsedDate" />
                                            <span><fmt:formatDate value="${parsedDate}" pattern="EEEEE, dd MMM yyyy" /></span>
                                        </div>
                                        <div class="timeline-marker"></div>
                                        <div class="timeline-content">
                                            <c:if test="${complaintActivity.activityType == 'view'}">
                                                <h3 class="timeline-title">Complaint Viewed</h3>
                                                <p>Complaint was viewed by ${activitiesFrom[loop.index].name} 
                                                <c:if test="${complaintDetails.complaintIsAnonymous == 0}">
                                                    and a notification was sent to ${activitiesTo[loop.index].name}</p>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${complaintActivity.activityType == 'forward'}">
                                                <h3 class="timeline-title">Complaint Forwarded</h3>
                                                <p>Complaint was forwarded by ${activitiesFrom[loop.index].name} to ${activitiesTo[loop.index].name}.</p>
                                                <c:if test="${not empty complaintActivity.activityComment}">
                                                    <p>Comments from ${activitiesFrom[loop.index].name}: <em>${complaintActivity.activityComment}</em></p>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${complaintActivity.activityType == 'escalate'}">
                                                <h3 class="timeline-title">Complaint Escalated</h3>
                                                <p>Complaint was escalated from ${activitiesFrom[loop.index].name} to ${activitiesTo[loop.index].name}.</p>
                                            </c:if>
                                            <c:if test="${complaintActivity.activityType == 'solve'}">
                                                <h3 class="timeline-title">Complaint Solved</h3>
                                                <p>Complaint was solved by ${activitiesFrom[loop.index].name} 
                                                <c:if test="${complaintDetails.complaintIsAnonymous == 0}">
                                                    and a notification was sent to ${activitiesTo[loop.index].name}</p>
                                                </c:if>
                                                <c:if test="${not empty complaintActivity.activityComment}">
                                                    <p>Comments from ${activitiesFrom[loop.index].name}: <em>${complaintActivity.activityComment}</em></p>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${complaintActivity.activityType == 'Marked as Spam'}">
                                                <h3 class="timeline-title">Marked As Spam</h3>
                                                <p>Complaint was solved by ${activitiesFrom[loop.index].name} 
                                                <c:if test="${complaintDetails.complaintIsAnonymous == 0}">
                                                    and a notification was sent to ${activitiesTo[loop.index].name}</p>
                                                </c:if>
                                                <c:if test="${not empty complaintActivity.activityComment}">
                                                    <p>Comments from ${activitiesFrom[loop.index].name}: <em>${complaintActivity.activityComment}</em></p>
                                                </c:if>
                                            </c:if>
                                        </div>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                    </div>
                    <div class="modal-footer">
                      <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                  </div>
                </div>
            </div>
                            
            <div class="modal bd-example-modal-lg fade" id="complaintResponse" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h5 class="modal-title" id="exampleModalLongTitle">Complaint Responses Log</h5>
                      <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                      </button>
                    </div>
                    <div class="modal-body">
                      <div>
                        <div class="col-md-12 example-title">
                            <h2>Responses for the Complaint</h2>
                        </div>
                        <c:if test="${complaintResponses.size()>0}">
                        <c:forEach items="${complaintResponses}" var="complaintResponse" varStatus="loop">
                            <c:if test="${responsesFrom[loop.index].name==user.committeeDetails.committeeName}">
                                <div class="alert alert-secondary m-3 float-right" style="width: 80%;" role="alert">
                                    <p>${complaintResponse.response}</p>
                                    <hr>
                                    <small class="mb-0"> By You on
                                        <fmt:parseDate pattern="yyyy-MM-dd HH:mm:ss" value="${complaintResponse.responseTime}" var="parsedDate" />
                                        <span>
                                            <fmt:formatDate value="${parsedDate}" pattern="EEEEE, dd MMM yyyy" />
                                        </span>
                                    </small>
                                </div>
                            </c:if> 
                            <c:if test="${responsesFrom[loop.index].name!=user.committeeDetails.committeeName}">
                                <div class="alert alert-info m-3 float-left" style="width: 80%;" role="alert">
                                    <p>${complaintResponse.response}</p>
                                    <hr>
                                    <small class="mb-0">From ${responsesFrom[loop.index].name} on
                                        <fmt:parseDate pattern="yyyy-MM-dd HH:mm:ss" value="${complaintResponse.responseTime}" var="parsedDate" />
                                        <span>
                                            <fmt:formatDate value="${parsedDate}" pattern="EEEEE, dd MMM yyyy" />
                                        </span>
                                    </small>
                                </div>
                            </c:if> 
                        </c:forEach>
                        </c:if>
                        <c:if test="${complaintResponses.size()==0}">
                            <h5 class="mb-5 mt-5">No Responses Yet</h5>
                        </c:if>
                        <c:if test="${param.complaintType!='solved'}">
                        <form id="sendResponse" action="ResponseController" method="post"> 
                            <div class="input-group mb-3">
                                <textarea type="text" class="form-control" placeholder="Send a new chat.." name="response" required></textarea>
                                <div class="input-group-append">
                                    <button type="submit" class="btn btn-primary"><i class="fas fa-paper-plane pr-3"></i>Send</button>
                                </div>
                            </div>
                        </form>
                        </c:if>
                    </div>
                    </div>
                    <div class="modal-footer">
                      <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                  </div>
                </div>
            </div>
                   
            <div class="modal bd-example-modal-lg fade" id="spamComplaint" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered" role="document">
                    <div class="modal-content">
                      <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLongTitle">Block User</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                          <span aria-hidden="true">&times;</span>
                        </button>
                      </div>
                        <div class="modal-body">
                            <form id="spamComplaint" action="BlockUserController" method="post"> 
                              <div class="form-group">
                                <label for="exampleSolve">Please State Your Reason</label>
                                <textarea class="form-control" id="exampleSolve" rows="3" name="finalComments"></textarea>
                              </div>
                              <h5>Are You Sure You Want To Mark This Complaint As Spam?</h5>
                              <button type="submit" class="btn btn-danger btn-block">Yes</button>
                              <button type="button" class="btn btn-success btn-block" data-dismiss="modal">No</button>
                          </form>
                      </div>
                      <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                      </div>
                    </div>
                  </div>
            </div>
                            
            <div class="modal bd-example-modal-lg fade" id="appropriateComplaint" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered" role="document">
                    <div class="modal-content">
                      <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLongTitle">Block User</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                          <span aria-hidden="true">&times;</span>
                        </button>
                      </div>
                        <div class="modal-body">
                            <form id="appropriateComplaint" action="AppropriateComplaintsController" method="post"> 
                              <div class="form-group">
                                <label for="exampleSolve">Please State Your Reason</label>
                                <textarea class="form-control" id="exampleSolve" rows="3" name="appropriateComments"></textarea>
                              </div>
                              <h5>Are You Sure You Want To Mark This Complaint As Appropriate?</h5>
                              <button type="submit" class="btn btn-danger btn-block">Yes</button>
                              <button type="button" class="btn btn-success btn-block" data-dismiss="modal">No</button>
                          </form>
                      </div>
                      <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                      </div>
                    </div>
                  </div>
            </div>
                            
            <jsp:include page="footer.jsp"/>
        </div>
    </body>
</html>
