    <%-- 
    Document   : committee-dashboard
    Created on : 21 Jul, 2020, 5:26:31 PM
    Author     : Pravesh Ganwani
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<!DOCTYPE html>
<html>
    <jsp:include page="/CommitteeDashboard"/>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard | Committee</title>
        <link href="css/dashboard.css" rel="stylesheet" type="text/css"/>
        <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
        <script>
            
            function changeCategory(value, id) {
            var input, filter, table, tr, td, i, txtValue;
            input = value.toUpperCase();
            table = document.getElementById(id);
            tr = table.getElementsByTagName("tr");
            for (i = 0; i < tr.length; i++) {
              td = tr[i].getElementsByTagName("td")[1];
              if (td) {
                txtValue = td.textContent || td.innerText;
                if(input == "ALL"){
                    tr[i].style.display = "";
                    continue;
                }
                if (txtValue.toUpperCase().indexOf(input) > -1) {
                  tr[i].style.display = "";
                } else {
                  tr[i].style.display = "none";
                }
              }       
            }
          }     
            
            <c:if test="${user.committeeDetails.committeeType=='inst'}">
                <c:forEach items="${unverified}" var="unverified" varStatus="loop">
                    $(document).ready(function() {
                        $('#${unverified.studentId}').on('click', function() {
                            var url = "VerificationServlet?verificationId=${unverified.studentId}";
                
                            var xhttp=new XMLHttpRequest();
                            xhttp.onreadystatechange=function()
                            {
                                if(xhttp.readyState==4 && xhttp.status==200)
                                {
                                    document.getElementById('${unverified.studentId}').innerHTML="Verified";
                                    document.getElementById('${unverified.studentId}').classList.add("btn-disabled");
                                }
                            };

                            xhttp.open("get",url, true);
                            xhttp.send();
                        });
                    });
                </c:forEach>
                
                <c:forEach items="${blocked}" var="block" varStatus="loop">
                    $(document).ready(function() {
                        $('#${block.studentId}').on('click', function() {
                            var url = "UnblockServlet?studentId=${block.studentId}";
                
                            var xhttp=new XMLHttpRequest();
                            xhttp.onreadystatechange=function()
                            {
                                if(xhttp.readyState==4 && xhttp.status==200)
                                {
                                    document.getElementById('${block.studentId}').innerHTML="Un-blocked";
                                    document.getElementById('${block.studentId}').classList.add("btn-disabled");
                                }
                            };

                            xhttp.open("get",url, true);
                            xhttp.send();
                        });
                    });
                </c:forEach>
    
                $(document).ready(function() {
                        $('#addCourseForm').on('submit', function(e) {
                            e.preventDefault();
                            var cName = document.getElementById("cName").value;
                            document.getElementById("cName").innerHTML =  "<i class=\"fas fa-spinner pl-2\"></i>Please Wait"
                            console.log(cName);
                            var url = "AddCourseServlet?courseName="+cName;
                            var xhttp=new XMLHttpRequest();
                            xhttp.onreadystatechange=function()
                            {
                                if(xhttp.readyState==4 && xhttp.status==200)
                                {
                                    document.getElementById('addCourseModalBody').innerHTML="<h3>Course Added Successfully. Please reload the web page to view the changes.</h3>";
                                }
                            };

                            xhttp.open("get",url, true);
                            xhttp.send();
                        });
                    });
            </c:if>
            <c:if test="${user.committeeDetails.committeeType=='univ'}">
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
            </c:if>
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
              title: 'Committee Statistics',
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
                            <button data-tab="tab7" class="active"><strong><i class="fa fa-chart-bar fa-lg pr-3"></i>Your Statistics</strong><span></span></button>
                            <c:if test="${user.committeeDetails.committeeType=='inst'}">
                                <button data-tab="tab1"><strong><i class="fas fa-stopwatch fa-lg pr-3"></i>Pending Grievances</strong><span></span></button>
                            </c:if>
                            <button data-tab="tab2"><strong><i class="fas fa-share fa-lg pr-3"></i>Forwarded Grievances</strong><span></span></button>
                            <button data-tab="tab8"><strong><i class="fas fa-arrow-up fa-lg pr-3"></i>Escalated Grievances</strong><span></span></button>
                            <c:if test="${user.committeeDetails.committeeType=='inst'}">
                                <button data-tab="tab9"><strong><i class="fas fa-user-graduate fa-lg pr-3"></i>Spam Grievances</strong><span></span></button>
                            </c:if>
                            <button data-tab="tab6"><strong><i class="fas fa-check fa-lg pr-3"></i>Solved Grievances</strong><span></span></button>
                                <c:if test="${user.committeeDetails.committeeType=='univ'}">
                                    <button data-tab="tab3"><strong><i class="fas fa-university fa-lg pr-3"></i>Verify Institutes</strong><span></span></button>
                                </c:if>
                                <c:if test="${user.committeeDetails.committeeType=='inst'}">
                                    <button data-tab="tab3"><strong><i class="fas fa-user-graduate fa-lg pr-3"></i>Manage Students</strong><span></span></button>
                                </c:if>
                                <c:if test="${user.committeeDetails.committeeType=='inst'}">
                                    <button data-tab="tab5"><strong><i class="fa fa-book fa-lg pr-3"></i>Manage Courses</strong><span></span></button>
                                </c:if> 
                                <button data-tab="tab4"><strong><i class="fa fa-id-card fa-lg pr-3"></i>Your Profile</strong><span></span></button>
                                <button class="empty"></button>
                        </div>
                        <div class="maincontent">
                                <div data-tab="tab1" class="tabcontent">
                                        <div class="ux-text">
                                                <h2><i class="fas fa-user-clock pr-3"></i>List of Pending Grievances</h2>
                                                <div class="mb-3 mt-3 row">
                                                    <div class="col-4"><input type="text" class="form-control" id="filterInput" onkeyup="searchFunction()" placeholder="Search for keywords.." title="Type in a keyword"></div>
                                                    <div class="col-8">
                                                        <div class="dropdown">
                                                            <label class="mr-2">Filter By Category: </label>
                                                            <select class="form-control" name="level" onchange="changeCategory(this.value, 'pendingGrievances')">
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
                                                <table class="table table-bordered" id="pendingGrievances">
                                                    <thead>
                                                      <tr>
                                                        <th scope="col">#</th>
                                                        <th scope="col">Complaint Title</th>
                                                        <th scope="col">Complaint Category</th>
                                                        <th scope="col">Keywords</th>
                                                        <th scope="col">Time Remaining</th>
                                                        <th scope="col">Actions</th>
                                                      </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${pendingGrievances}" var="pendingGrievance" varStatus="loop">
                                                        <c:if test="${pendingGrievance.complaintIsRedFlag==1}">
                                                            <tr style="background-color: #ffcccb">
                                                        </c:if>
                                                        <c:if test="${pendingGrievance.complaintIsRedFlag==0}">
                                                            <tr>
                                                        </c:if>
                                                                <th scope="row">${loop.index+1}</th>
                                                                <td>${pendingGrievance.complaintTitle}</td>
                                                                <td>${categories[pendingGrievance.categoryId - 1].categoryName}</td>
                                                                <td>${pendingKeywords[loop.index].keywordName}</td>
                                                                <c:if test="${pendingGrievance.complaintIsDelayed == 0}">
                                                                    <td>${7 - pendingGrievance.daysElapsed} Days</td>
                                                                </c:if>
                                                                <c:if test="${pendingGrievance.complaintIsDelayed == 1}">
                                                                    <td>Delayed</td>
                                                                </c:if>
                                                                <td><a href="solve-grievance.jsp?complaintType=pending&complaintId=${pendingGrievance.complaintId}" target="_blank" class="btn btn-success" style="color: white;">Take Action</a></td>
                                                            </tr>
                                                      </c:forEach>
                                                      <tfoot>
                                                        <tr>
                                                          <td colspan="6" class="text-center">Data retrieved from Complaint Database.</td>
                                                        </tr>
                                                      </tfoot>
                                                    </tbody>
                                                </table>
                                        </div>
                                </div>

                                <div data-tab="tab2" class="tabcontent">
                                        <div class="ux-text">
                                                <h2><i class="fas fa-user-clock pr-3"></i>List of Received Grievances</h2>
                                                <div class="mb-3 mt-3 row">
                                                    <input class="col-4" type="text" class="form-control" id="receivedGrievancesInput" onkeyup="searchReceivedGrievances()" placeholder="Search for keywords.." title="Type in a keyword">
                                                    <div class="col-8">
                                                        <div class="dropdown">
                                                            <label class="mr-2">Filter By Category: </label>
                                                            <select class="form-control" name="level" onchange="changeCategory(this.value, 'receivedGrievances')">
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
                                                <table class="table table-bordered" id="receivedGrievances">
                                                    <thead>
                                                      <tr>
                                                        <th scope="col">#</th>
                                                        <th scope="col">Received From</th>
                                                        <th scope="col">Complaint Title</th>
                                                        <th scope="col">Complaint Category</th>
                                                        <th scope="col">Keywords</th>
                                                        <th scope="col">Time Remaining</th>
                                                        <th scope="col">Actions</th>
                                                      </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${forwardedGrievances}" var="forwardedGrievance" varStatus="loop">
                                                        <c:if test="${forwardedGrievance.complaintIsRedFlag==1}">
                                                            <tr style="background-color: #ffcccb">
                                                        </c:if>
                                                        <c:if test="${forwardedGrievance.complaintIsRedFlag==0}">
                                                            <tr>
                                                        </c:if>
                                                            <th scope="row">${loop.index+1}</th>
                                                            <td>${forwardedActivities[loop.index].committeeName}</td>
                                                            <td>${forwardedGrievance.complaintTitle}</td>
                                                            <td>${categories[forwardedGrievance.categoryId - 1].categoryName}</td>
                                                            <td>${forwardedKeywords[loop.index].keywordName}</td>
                                                            <c:if test="${forwardedGrievance.complaintIsDelayed == 0}">
                                                                <td>${7 - forwardedGrievance.daysElapsed} Days</td>
                                                            </c:if>
                                                            <c:if test="${forwardedGrievance.complaintIsDelayed == 1}">
                                                                <td>Delayed</td>
                                                            </c:if>
                                                            <td><a href="solve-grievance.jsp?complaintType=received&complaintId=${forwardedGrievance.complaintId}" target="_blank" class="btn btn-success" style="color: white;">Take Action</a></td>
                                                        </tr>
                                                      </c:forEach>
                                                      <tfoot>
                                                        <tr>
                                                          <td colspan="7" class="text-center">Data retrieved from Complaint Database.</td>
                                                        </tr>
                                                      </tfoot>
                                                    </tbody>
                                                </table>
                                                <h2><i class="fas fa-paper-plane pr-3"></i>List of Forwarded Grievances</h2>
                                                <table class="table table-bordered" id="forwardedGrievances">
                                                    <thead>
                                                      <tr>
                                                        <th scope="col">#</th>
                                                        <th scope="col">Complaint Title</th>
                                                        <th scope="col">Complaint Category</th>
                                                        <th scope="col">Forwarded To</th>
                                                        <th scope="col">Status</th>
                                                      </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${forwards}" var="forward" varStatus="loop">
                                                        <c:if test="${forward.complaintIsSolved==1}">
                                                            <tr style="background-color: #90ee90">
                                                        </c:if>
                                                        <c:if test="${forward.complaintIsSolved==0}">
                                                            <tr>
                                                        </c:if>
                                                            <th scope="row">${loop.index+1}</th>
                                                            <td>${forward.complaintTitle}</td>
                                                            <td>${categories[forward.categoryId - 1].categoryName}</td>
                                                            <td>${forwardsActivities[loop.index].committeeName}</td>
                                                            <c:if test="${forward.complaintIsSolved==1}">
                                                                <td>Solved</td>
                                                            </c:if>
                                                            <c:if test="${forward.complaintIsSolved==0}">
                                                                <td>Pending</td>
                                                            </c:if>
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
                                            <c:if test="${user.committeeDetails.committeeType=='univ'}">
                                                <h2><i class="fas fa-sitemap pr-3"></i>List Of Verified Institutes</h2>
                                            </c:if>
                                            <c:if test="${user.committeeDetails.committeeType=='inst'}">
                                                <h2 class="mb-4"><i class="fa fa-graduation-cap pr-3"></i>List Of Verified Students<a href="#" class="btn btn-success float-right" data-toggle="modal" data-target="#addAccounts"><i class="fas fa-plus pr-3"></i>Create New Accounts</a></h2>
                                            </c:if>
                                                <table class="table table-bordered">
                                                    <thead>
                                                      <tr>
                                                        <th scope="col">#</th>
                                                        <c:if test="${user.committeeDetails.committeeType=='univ'}">
                                                            <th scope="col">Institute ID</th>
                                                            <th scope="col">Institute Name</th>
                                                            <th scope="col">Institute Email</th>
                                                            <th scope="col">Actions</th>
                                                        </c:if>
                                                        <c:if test="${user.committeeDetails.committeeType=='inst'}">
                                                            <th scope="col">Student Unique ID</th>
                                                            <th scope="col">Student Name</th>
                                                            <th scope="col">Student Email</th>
                                                            <th scope="col">Actions</th>
                                                        </c:if>
                                                      </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${verified}" var="verified" varStatus="loop">
                                                        <tr>
                                                            <th scope="row">${loop.index+1}</td>
                                                            <c:if test="${user.committeeDetails.committeeType=='univ'}">
                                                                <td>${verified.committeeId}</td>
                                                                <td>${verified.committeeName}</td>
                                                                <td>${verified.committeeEmail}</td>
                                                                <td>Verified</td>
                                                            </c:if>
                                                            <c:if test="${user.committeeDetails.committeeType=='inst'}">
                                                                <td>${verified.studentUID}</td>
                                                                <td>${verified.studentFirstName} ${verified.studentMiddleName} ${verified.studentLastName} </td>
                                                                <td>${verified.studentEmail}</td>
                                                                <td>Verified</td>
                                                            </c:if>
                                                        </tr>
                                                      </c:forEach>
                                                      <tfoot>
                                                        <tr>
                                                            <c:if test="${user.committeeDetails.committeeType=='inst'}">
                                                                <td colspan="5" class="text-center">Data retrieved from Student Database.</td>
                                                            </c:if>
                                                            <c:if test="${user.committeeDetails.committeeType=='univ'}">
                                                                <td colspan="5" class="text-center">Data retrieved from Committee Database.</td>
                                                            </c:if>
                                                        </tr>
                                                      </tfoot>
                                                    </tbody>
                                                </table>
                                                <c:if test="${user.committeeDetails.committeeType=='inst'}">
                                                    <h2><i class="fa fa-graduation-cap pr-3"></i>List Of Blocked Students</h2>
                                                </c:if>
                                                <table class="table table-bordered">
                                                    <thead>
                                                      <tr>
                                                        <th scope="col">#</th>
                                                        <c:if test="${user.committeeDetails.committeeType=='inst'}">
                                                            <th scope="col">Student Unique ID</th>
                                                            <th scope="col">Student Name</th>
                                                            <th scope="col">Student Email</th>
                                                            <th scope="col">Actions</th>
                                                        </c:if>
                                                      </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${blocked}" var="block" varStatus="loop">
                                                        <tr>
                                                            <th scope="row">${loop.index+1}</th>
                                                            <c:if test="${user.committeeDetails.committeeType=='inst'}">
                                                                <td>${block.studentUID}</td>
                                                                <td>${block.studentFirstName} ${block.studentMiddleName} ${block.studentLastName} </td>
                                                                <td>${block.studentEmail}</td>
                                                                <td><a class="btn btn-success" style="color: white;" id="${block.studentId}"><i class="fa fa-check pr-2"></i>Un-block</a></td>
                                                            </c:if>
                                                        </tr>
                                                      </c:forEach>
                                                      <tfoot>
                                                        <tr>
                                                            <c:if test="${user.committeeDetails.committeeType=='inst'}">
                                                                <td colspan="5" class="text-center">Data retrieved from Student Database.</td>
                                                            </c:if>
                                                        </tr>
                                                      </tfoot>
                                                    </tbody>
                                                </table>
                                                    
                                            <c:if test="${user.committeeDetails.committeeType=='univ'}">
                                                <h2><i class="fas fa-sitemap pr-3"></i>List Of Unverified Institutes</h2>
                                            </c:if>
                                            <c:if test="${user.committeeDetails.committeeType=='inst'}">
                                                <h2><i class="fa fa-graduation-cap pr-3"></i>List Of Unverified Students</h2>
                                            </c:if>
                                                <table class="table table-bordered">
                                                    <thead>
                                                      <tr>
                                                        <th scope="col">#</th>
                                                        <c:if test="${user.committeeDetails.committeeType=='univ'}">
                                                            <th scope="col">Institute ID</th>
                                                            <th scope="col">Institute Name</th>
                                                            <th scope="col">Institute Email</th>
                                                            <th scope="col">Actions</th>
                                                        </c:if>
                                                        <c:if test="${user.committeeDetails.committeeType=='inst'}">
                                                            <th scope="col">Student Unique ID</th>
                                                            <th scope="col">Student Name</th>
                                                            <th scope="col">Student Email</th>
                                                            <th scope="col">Actions</th>
                                                        </c:if>
                                                      </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${unverified}" var="unverified" varStatus="loop">
                                                        <tr>
                                                            <th scope="row">${loop.index+1}</td>
                                                            <c:if test="${user.committeeDetails.committeeType=='univ'}">
                                                                <td>${unverified.committeeId}</td>
                                                                <td>${unverified.committeeName}</td>
                                                                <td>${unverified.committeeEmail}</td>
                                                                <td><a class="btn btn-success" style="color: white;" id="${unverified.committeeId}"><i class="fa fa-check pr-2"></i>Verify</a></td>
                                                            </c:if>
                                                            <c:if test="${user.committeeDetails.committeeType=='inst'}">
                                                                <td>${unverified.studentUID}</td>
                                                                <td>${unverified.studentFirstName} ${unverified.studentMiddleName} ${unverified.studentLastName} </td>
                                                                <td>${unverified.studentEmail}</td>
                                                                <td><a class="btn btn-success" style="color: white;" id="${unverified.studentId}"><i class="fa fa-check pr-2"></i>Verify</a></td>
                                                            </c:if>
                                                        </tr>
                                                      </c:forEach>
                                                      <tfoot>
                                                        <tr>
                                                            <c:if test="${user.committeeDetails.committeeType=='inst'}">
                                                                <td colspan="5" class="text-center">Data retrieved from Student Database.</td>
                                                            </c:if>
                                                            <c:if test="${user.committeeDetails.committeeType=='univ'}">
                                                                <td colspan="5" class="text-center">Data retrieved from Committee Database.</td>
                                                            </c:if>
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
                                                        <th><i class="fas fa-users pr-3"></i>Committee Name</th>
                                                        <td>${user.committeeDetails.committeeName}</td>
                                                      </tr>
                                                      <tr>
                                                        <th><i class="fas fa-at pr-3"></i>Committee Email</th>
                                                        <td>${user.committeeDetails.committeeEmail}</td>
                                                      </tr>
                                                      <tr>
                                                        <th><i class="fas fa-address-book pr-3"></i>Committee Contact Details</th>
                                                        <td>${user.committeeDetails.committeeContact}</td>
                                                      </tr>
                                                    </tbody>
                                                  </table>
                                        </div>
                                </div>
                            <c:if test="${user.committeeDetails.committeeType=='inst'}">
                                <div data-tab="tab5" class="tabcontent">
                                        <div class="ux-text">
                                                <h2><i class="fas fa-chalkboard-teacher pr-3"></i>List Of Offered Courses</h2>
                                                <div class="mb-3 mt-3">
                                                    <a href="#" class="btn btn-success" data-toggle="modal" data-target="#addCourse"><i class="fas fa-plus pr-3"></i>Add A New Course</a>
                                                </div>
                                                <table class="table table-bordered mt-4">
                                                    <thead>
                                                      <tr>
                                                        <th scope="col">#</th>
                                                        <th scope="col">Course Name</th>
                                                        <th scope="col">Actions</th>
                                                      </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${courses}" var="courses" varStatus="loop">
                                                        <tr>
                                                            <th scope="row">${loop.index+1}</th>
                                                            <td>${courses.courseName}</td>
                                                            <td><a class="btn btn-primary" style="color: white;"><i class="fas fa-pen pr-2"></i>Update</a></td>
                                                        </tr>
                                                      </c:forEach>
                                                      <tfoot>
                                                        <tr>
                                                          <td colspan="5" class="text-center">Data retrieved from Courses Database.</td>
                                                        </tr>
                                                      </tfoot>
                                                    </tbody>
                                                </table>
                                        </div>
                                </div>
                                
                                <div class="modal fade" id="addCourse" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                                    <div class="modal-dialog modal-dialog-centered" role="document">
                                      <div class="modal-content">
                                        <div class="modal-header">
                                          <h5 class="modal-title" id="exampleModalLongTitle">Add A New Course</h5>
                                          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                          </button>
                                        </div>
                                          <div class="modal-body" id="addCourseModalBody">
                                            <form id="addCourseForm"> 
                                                <div class="form-group">
                                                  <label for="cName">Course Name</label>
                                                  <input type="text" id="cName" class="form-control" aria-describedby="emailHelp" placeholder="Enter Course Name">
                                                </div>
                                                <button type="submit" class="btn btn-primary">Submit</button>
                                            </form>
                                        </div>
                                        <div class="modal-footer">
                                          <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                        </div>
                                      </div>
                                    </div>
                                </div>
                                
                                <div class="modal fade" id="addAccounts" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                                    <div class="modal-dialog modal-dialog-centered" role="document">
                                      <div class="modal-content">
                                        <div class="modal-header">
                                          <h5 class="modal-title" id="exampleModalLongTitle">Add New Accounts</h5>
                                          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                          </button>
                                        </div>
                                          <div class="modal-body" id="addCourseModalBody">
                                            <form action="AddAccountsController" id="addAccountsForm" method="post" enctype="multipart/form-data"> 
                                                <div class="form-group">
                                                  <label for="fileName">Please Select A File</label>
                                                  <input type="file" id="fileName" class="form-control" name="excelFile" accept="application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet">
                                                </div>
                                                <button type="submit" class="btn btn-primary">Submit</button>
                                            </form>
                                        </div>
                                        <div class="modal-footer">
                                          <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                        </div>
                                      </div>
                                    </div>
                                </div>
                                
                            </c:if>
                            <div data-tab="tab6" class="tabcontent">
                                        <div class="ux-text">
                                                <h2><i class="fas fa-user-check pr-3"></i>List of Solved Grievances</h2>
                                                <table class="table table-bordered" id="pendingGrievances">
                                                    <thead>
                                                      <tr>
                                                        <th scope="col">#</th>
                                                        <th scope="col">Complaint Title</th>
                                                        <th scope="col">Complaint Category</th>
                                                        <th scope="col">Details</th>
                                                      </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${solvedGrievances}" var="solvedGrievance" varStatus="loop">
                                                        
                                                            <tr style="background-color: #90ee90">
                                                                <th scope="row">${loop.index+1}</th>
                                                                <td>${solvedGrievance.complaintTitle}</td>
                                                                <td>${categories[solvedGrievance.categoryId - 1].categoryName}</td>
                                                                <td><a href="solve-grievance.jsp?complaintType=solved&complaintId=${solvedGrievance.complaintId}" target="_blank" class="btn btn-primary" style="color: white;">See Details</a></td>
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
                                                      
                                <div data-tab="tab7" class="tabcontent active">
                                        <div class="ux-text">
                                                <h2><i class="fas fa-chart-pie pr-3"></i>Statistics of Complaints</h2>
                                                <div id="piechart_3d" style="width: 800px; height: 300px; text-align: left;"></div>
                                        </div>
                                </div>
                                                      
                                <div data-tab="tab8" class="tabcontent">
                                        <div class="ux-text">
                                            <c:if test="${user.committeeDetails.committeeType=='univ'}">
                                                <h2><i class="fas fa-user-clock pr-3"></i>List of Received Grievances</h2>
                                                <div class="mb-3 mt-3">
                                                    <input type="text" class="form-control" id="escalatedGrievancesInput" onkeyup="searchEscalatedGrievances()" placeholder="Search for keywords.." title="Type in a keyword">
                                                    
                                                </div>
                                                <table class="table table-bordered" id="escalatedGrievances">
                                                    <thead>
                                                      <tr>
                                                        <th scope="col">#</th>
                                                        <th scope="col">Escalated From</th>
                                                        <th scope="col">Complaint Title</th>
                                                        <th scope="col">Complaint Category</th>
                                                        <th scope="col">Keywords</th>
                                                        <th scope="col">Time Remaining</th>
                                                        <th scope="col">Actions</th>
                                                      </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${escalatedGrievances}" var="escalatedGrievance" varStatus="loop">
                                                        <c:if test="${escalatedGrievance.complaintIsRedFlag==1}">
                                                            <tr style="background-color: #ffcccb">
                                                        </c:if>
                                                        <c:if test="${escalatedGrievance.complaintIsRedFlag==0}">
                                                            <tr>
                                                        </c:if>
                                                            <th scope="row">${loop.index+1}</th>
                                                            <td>${escalatedActivities[loop.index].committeeName}</td>
                                                            <td>${escalatedGrievance.complaintTitle}</td>
                                                            <td>${categories[escalatedGrievance.categoryId - 1].categoryName}</td>
                                                            <td>${escalatedKeywords[loop.index].keywordName}</td>
                                                            <c:if test="${escalatedGrievance.complaintIsDelayed == 0}">
                                                                <td>${7 - escalatedGrievance.daysElapsed} Days</td>
                                                            </c:if>
                                                            <c:if test="${escalatedGrievance.complaintIsDelayed == 1}">
                                                                <td>Delayed by ${escalatedGrievance.daysElapsed - 7} Days</td>
                                                            </c:if>
                                                            <td><a href="solve-grievance.jsp?complaintType=escalated&complaintId=${escalatedGrievance.complaintId}" target="_blank" class="btn btn-success" style="color: white;">Take Action</a></td>
                                                        </tr>
                                                      </c:forEach>
                                                      <tfoot>
                                                        <tr>
                                                          <td colspan="7" class="text-center">Data retrieved from Complaint Database.</td>
                                                        </tr>
                                                      </tfoot>
                                                    </tbody>
                                                </table>
                                                </c:if>
                                                <c:if test="${user.committeeDetails.committeeType=='inst'}">
                                                <h2><i class="fas fa-paper-plane pr-3"></i>List of Escalated Grievances</h2>
                                                <table class="table table-bordered" id="forwardedGrievances">
                                                    <thead>
                                                      <tr>
                                                        <th scope="col">#</th>
                                                        <th scope="col">Complaint Title</th>
                                                        <th scope="col">Complaint Category</th>
                                                        <th scope="col">Forwarded To</th>
                                                        <th scope="col">Status</th>
                                                      </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${escalates}" var="escalate" varStatus="loop">
                                                        <c:if test="${escalate.complaintIsSolved==1}">
                                                            <tr style="background-color: #90ee90">
                                                        </c:if>
                                                        <c:if test="${escalate.complaintIsSolved==0}">
                                                            <tr>
                                                        </c:if>
                                                            <th scope="row">${loop.index+1}</th>
                                                            <td>${escalate.complaintTitle}</td>
                                                            <td>${categories[escalate.categoryId - 1].categoryName}</td>
                                                            <td>${escalatesActivities[loop.index].committeeName}</td>
                                                            <c:if test="${escalate.complaintIsSolved==1}">
                                                                <td>Solved</td>
                                                            </c:if>
                                                            <c:if test="${escalate.complaintIsSolved==0}">
                                                                <td>Pending</td>
                                                            </c:if>
                                                        </tr>
                                                      </c:forEach>
                                                      <tfoot>
                                                        <tr>
                                                          <td colspan="5" class="text-center">Data retrieved from Complaint Database.</td>
                                                        </tr>
                                                      </tfoot>
                                                    </tbody>
                                                </table>
                                                </c:if>
                                        </div>
                                </div>
                                <c:if test="${user.committeeDetails.committeeType=='inst'}">
                                <div data-tab="tab9" class="tabcontent">
                                        <div class="ux-text">
                                                <h2><i class="fas fa-paper-plane pr-3"></i>List of Spam Grievances</h2>
                                                <table class="table table-bordered" id="forwardedGrievances">
                                                    <thead>
                                                      <tr>
                                                        <th scope="col">#</th>
                                                        <th scope="col">Complaint Title</th>
                                                        <th scope="col">Complaint Category</th>
                                                        <th scope="col">Forwarded To</th>
                                                        <th scope="col">Status</th>
                                                      </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${spamGrievances}" var="spam" varStatus="loop">
                                                        <c:if test="${spam.complaintIsSolved==1}">
                                                            <tr style="background-color: #90ee90">
                                                        </c:if>
                                                        <c:if test="${spam.complaintIsSolved==0}">
                                                            <tr>
                                                        </c:if>
                                                            <th scope="row">${loop.index+1}</th>
                                                            <td>${spam.complaintTitle}</td>
                                                            <td>${categories[spam.categoryId - 1].categoryName}</td>
                                                            <td>Admin</td>
                                                            <c:if test="${spam.complaintIsSolved==1}">
                                                                <td>Approved</td>
                                                            </c:if>
                                                            <c:if test="${spam.complaintIsSolved==0}">
                                                                <td>Pending</td>
                                                            </c:if>
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
                                </c:if>
                        </div>
                </div>
        </div>
        <!-- partial -->
        <script src='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js'></script>
        <script src='https://cdnjs.cloudflare.com/ajax/libs/animejs/2.2.0/anime.min.js'></script>
        <script src="js/dashboard.js" type="text/javascript"></script>
        <script>
            function searchFunction() {
            var input, filter, table, tr, td, i, txtValue;
            input = document.getElementById("filterInput");
            filter = input.value.toUpperCase();
            table = document.getElementById("pendingGrievances");
            tr = table.getElementsByTagName("tr");
            for (i = 0; i < tr.length; i++) {
              td = tr[i].getElementsByTagName("td")[2];
              if (td) {
                txtValue = td.textContent || td.innerText;
                if (txtValue.toUpperCase().indexOf(filter) > -1) {
                  tr[i].style.display = "";
                } else {
                  tr[i].style.display = "none";
                }
              }       
            }
          }     
          
          function searchReceivedGrievances() {
            var input, filter, table, tr, td, i, txtValue;
            input = document.getElementById("receivedGrievancesInput");
            filter = input.value.toUpperCase();
            table = document.getElementById("receivedGrievances");
            tr = table.getElementsByTagName("tr");
            for (i = 0; i < tr.length; i++) {
              td = tr[i].getElementsByTagName("td")[3];
              if (td) {
                txtValue = td.textContent || td.innerText;
                if (txtValue.toUpperCase().indexOf(filter) > -1) {
                  tr[i].style.display = "";
                } else {
                  tr[i].style.display = "none";
                }
              }       
            }
          }  
          
          function searchEscalatedGrievances() {
            var input, filter, table, tr, td, i, txtValue;
            input = document.getElementById("escalatedGrievancesInput");
            filter = input.value.toUpperCase();
            table = document.getElementById("escalatedGrievances");
            tr = table.getElementsByTagName("tr");
            for (i = 0; i < tr.length; i++) {
              td = tr[i].getElementsByTagName("td")[3];
              if (td) {
                txtValue = td.textContent || td.innerText;
                if (txtValue.toUpperCase().indexOf(filter) > -1) {
                  tr[i].style.display = "";
                } else {
                  tr[i].style.display = "none";
                }
              }       
            }
          }
        </script>
        </script>
        <jsp:include page="footer.jsp"/>
    </body>
</html>
