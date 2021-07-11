<%-- 
    Document   : admin-dashboard
    Created on : 29 Jul, 2020, 9:28:46 PM
    Author     : Pravesh Ganwani
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<!DOCTYPE html>
<html>
    <jsp:include page="/AdminDashboard"/>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard | Admin</title>
        <link href="css/dashboard.css" rel="stylesheet" type="text/css"/>
        <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
        <script>
            <c:forEach items="${unverified}" var="unverified" varStatus="loop">
                    $(document).ready(function() {
                        $('#${unverified.committeeId}').on('click', function() {
                            var url = "VerificationServlet?verificationId=${unverified.committeeId}";
                
                            var xhttp=new XMLHttpRequest();
                            xhttp.onreadystatechange=function()
                            {
                                if(xhttp.readyState==4 && xhttp.status==200)
                                {
                                    document.getElementById('${unverified.committeeId}').innerHTML="Verified";
                                    document.getElementById('${unverified.committeeId}').classList.add("btn-disabled");
                                }
                            };

                            xhttp.open("get",url, true);
                            xhttp.send();
                        });
                    });
                </c:forEach>
        </script>
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
        <script type="text/javascript">
          google.charts.load("current", {packages:["corechart"]});
          google.charts.setOnLoadCallback(drawChart);
          function drawChart() {
            var data = google.visualization.arrayToDataTable([
              ['Type', 'No. of Complaints'],
              ['No. of Solved Grievances = ${solved}',   parseInt("${solved}")],
              ['No. of Pending Grievances = ${pendings}',  parseInt("${pendings}")],
            ]);

            var options = {
              title: 'Portal Statistics',
              is3D: false,
              fontSize: 20,
              fontName: 'Nunito Sans',
              backgroundColor: { fill:'transparent' },
              chartArea : { left: "0%" },
            };

            var chart = new google.visualization.PieChart(document.getElementById('piechart_3d'));
            chart.draw(data, options);
          }
        </script>
    </head>
    <body>
        <jsp:include page="header.jsp"/>
            <div class="container maxwidth mt-5">
                <div class="ux-vertical-tabs">
                        <div class="tabs">
                            <button data-tab="tab1" class="active"><strong><i class="fa fa-chart-bar fa-lg pr-3"></i>Portal Statistics</strong><span></span></button>
                            <button data-tab="tab3"><strong><i class="fas fa-university fa-lg pr-3"></i>Verify Universities</strong><span></span></button>
                            <button data-tab="tab2"><strong><i class="fas fa-user-lock fa-lg pr-3"></i>Manage Spam Grievances</strong><span></span></button>
                            <button data-tab="tab5"><strong><i class="fas fa-user-lock fa-lg pr-3"></i>Portal Feedbacks</strong><span></span></button>
                            <button data-tab="tab4"><strong><i class="fa fa-id-card fa-lg pr-3"></i>Your Profile</strong><span></span></button>
                            <button class="empty"></button>
                        </div>
                        <div class="maincontent">
                                <div data-tab="tab1" class="tabcontent active">
                                        <div class="ux-text">
                                                <h2><i class="fas fa-chart-pie pr-3"></i>Statistics of Complaints</h2>
                                                <div id="piechart_3d" style="width: 800px; height: 300px; text-align: left;"></div>
                                                <p>For more detailed statistics: <a href="https://public.tableau.com/profile/sakshi.chheda#!/vizhome/KB220_SIH2020/COVER" target="_blank">Click Here</a></p>
                                        </div>
                                </div>
                            
                                <div data-tab="tab2" class="tabcontent">
                                        <div class="ux-text">
                                            <h2><i class="fas fa-user-clock pr-3"></i>List of Spam Grievances</h2>
                                                <div class="mb-3 mt-3 row">
                                                    <div class="col-4"><input type="text" class="form-control" id="filterInput" onkeyup="searchFunction()" placeholder="Search for keywords.." title="Type in a keyword"></div>
                                                    <div class="col-8">
                                                        <div class="dropdown">
                                                            <label class="mr-2">Filter By Category: </label>
                                                            <select class="form-control" name="level" onchange="changeCategory(this.value, 'spamGrievances')">
                                                                <option value="dropdown" selected disabled>Categories List</option>
                                                                <sql:setDataSource driver="com.mysql.cj.jdbc.Driver" 
                                                                                   url="jdbc:mysql://localhost:3306/sihdb"
                                                                                   user="root"
                                                                                   password="root"
                                                                                   var="con"
                                                                                   />
                                                                <sql:query dataSource="${con}" var="data">
                                                                    select * from grievance_category
                                                                </sql:query>
                                                                    <option value="All">All</option>
                                                                <c:forEach items="${data.rows}" var="row">
                                                                    <option value="${row.category_name}">${row.category_name}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <table class="table table-bordered" id="spamGrievances">
                                                    <thead>
                                                      <tr>
                                                        <th scope="col">#</th>
                                                        <th scope="col">Complaint Title</th>
                                                        <th scope="col">Complaint Category</th>
                                                        <th scope="col">Keywords</th>
                                                        <th scope="col">Actions</th>
                                                      </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${spamGrievances}" var="spamGrievance" varStatus="loop">
                                                        <c:if test="${spamGrievance.complaintIsRedFlag==1}">
                                                            <tr style="background-color: #ffcccb">
                                                        </c:if>
                                                        <c:if test="${spamGrievance.complaintIsRedFlag==0}">
                                                            <tr>
                                                        </c:if>
                                                                <th scope="row">${loop.index+1}</th>
                                                                <td>${spamGrievance.complaintTitle}</td>
                                                                <td>${categories[spamGrievance.categoryId - 1].categoryName}</td>
                                                                <td>${spamKeywords[loop.index].keywordName}</td>
                                                                <td><a href="spam-grievance.jsp?complaintId=${spamGrievance.complaintId}" target="_blank" class="btn btn-success" style="color: white;">Take Action</a></td>
                                                            </tr>
                                                      </c:forEach>
                                                      <tfoot>
                                                        <tr>
                                                          <td colspan="5" class="text-center">Data retrieved from Complaint Database.</td>
                                                        </tr>
                                                      </tfoot>
                                                    </tbody>
                                                </table>
                                        </div>
                                </div>
                            
                                <div data-tab="tab3" class="tabcontent">
                                        <div class="ux-text">
                                                <h2><i class="fas fa-sitemap pr-3"></i>List Of Unverified Universities</h2>
                                                <table class="table table-bordered">
                                                    <thead>
                                                      <tr>
                                                        <th scope="col">#</th>
                                                            <th scope="col">University ID</th>
                                                            <th scope="col">University Name</th>
                                                            <th scope="col">University Email</th>
                                                            <th scope="col">Actions</th>
                                                      </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${unverified}" var="unverified" varStatus="loop">
                                                        <tr>
                                                            <th scope="row">${loop.index+1}</th>
                                                            <td>${unverified.committeeId}</td>
                                                            <td>${unverified.committeeName}</td>
                                                            <td>${unverified.committeeEmail}</td>
                                                            <td><a class="btn btn-success" style="color: white;" id="${unverified.committeeId}"><i class="fa fa-check pr-2"></i>Verify</a></td>
                                                        </tr>
                                                      </c:forEach>
                                                      <tfoot>
                                                        <tr>
                                                            <td colspan="5" class="text-center">Data retrieved from Committee Database.</td>
                                                        </tr>
                                                      </tfoot>
                                                    </tbody>
                                                </table>
                                        </div>
                                </div>
                            
                                <div data-tab="tab4" class="tabcontent">
                                        <div class="ux-text">
                                                <h2><i class="fas fa-info-circle pr-3"></i>Your Details</h2>
                                                <div class="mb-3 mt-3">
                                                    <a href="#" class="btn btn-primary"><i class="fas fa-pen pr-3"></i>Edit Your Details</a>
                                                </div>
                                                <table class="table mt-4">
                                                    <tbody>
                                                      <tr>
                                                        <th><i class="fas fa-users pr-3"></i>Admin Name</th>
                                                        <td>${user.adminDetails.adminName}</td>
                                                      </tr>
                                                      <tr>
                                                        <th><i class="fas fa-at pr-3"></i>Admin Email</th>
                                                        <td>${user.adminDetails.adminEmailId}</td>
                                                      </tr>
                                                    </tbody>
                                                  </table>
                                        </div>
                                </div>
                                                      
                                <div data-tab="tab5" class="tabcontent">
                                        <div class="ux-text">
                                                <h2><i class="fas fa-sitemap pr-3"></i>List Of Portal Feedback</h2>
                                                <table class="table table-bordered">
                                                    <thead>
                                                      <tr>
                                                        <th scope="col">#</th>
                                                        <th scope="col">Feedback From</th>
                                                        <th scope="col">Email</th>
                                                        <th scope="col">Actions</th>
                                                      </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${feedbacks}" var="feedback" varStatus="loop">
                                                        <tr>
                                                            <th scope="row">${loop.index+1}</th>
                                                            <td>${feedback.name}</td>
                                                            <td>${feedback.email}</td>
                                                            <td><a class="btn btn-success" style="color: white;" data-toggle="modal" data-target="#feedback_${loop.index+1}"">See Feedback</a></td>
                                                        </tr>
                                                      </c:forEach>
                                                      <tfoot>
                                                        <tr>
                                                            <td colspan="5" class="text-center">Data retrieved from Feedback Database.</td>
                                                        </tr>
                                                      </tfoot>
                                                    </tbody>
                                                </table>
                                        </div>
                                </div>
                        </div>
                </div>
        </div>
        <c:forEach items="${feedbacks}" var="feedback" varStatus="loop">
            <div class="modal fade" id="feedback_${loop.index+1}" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered" role="document">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h5 class="modal-title" id="exampleModalLongTitle">Feedback Details</h5>
                      <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                      </button>
                    </div>
                    <div class="modal-body">
                      <h4 class="card-subtitle mb-2">Feedback Comments:</h4>
                      <p class="card-text pl-2 pr-2 pt-3 pb-3 alert alert-secondary">${feedback.feedback}</p>
                    </div>
                    <div class="modal-footer">
                      <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                  </div>
                </div>
            </div>
        </c:forEach>
        <!-- partial -->
        <script src='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js'></script>
        <script src='https://cdnjs.cloudflare.com/ajax/libs/animejs/2.2.0/anime.min.js'></script>
        <script src="js/dashboard.js" type="text/javascript"></script>
        <jsp:include page="footer.jsp"/>
    </body>
</html>
