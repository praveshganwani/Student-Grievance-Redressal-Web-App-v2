# StudentGrievanceRedressalWebAppv2

A Student Grievance Redressal Portal for the Government of Andhra Pradesh made using JSP, Java Servlets and MySQL.

## Problem Statement

The application is a Students Grievance Support System. Expectations from software solution/webapp are -
- It must be an easy access application, accessible to students, members of Student Grievance Redressal Committees, respective heads.
- Students should be able to post grievances under different categories, Department Level, Institute/College Level and University Level. Again, these categories would be subdivided among sub categories such as Admission, Finance, Examination, Lecture Timetable/Learning, Paper Re-Evaluation, etc.
- Members of Students Grievance Redressal Committee should be able to sort complains based on keywords.
- The Portal should link students with respective Department/Institutions/College and University Students Grievance Redressal Committees.

## Scope of the Project

The application is simply a web portal where all the lodged grievances can be accessed by relevant verified users. The application is one sub part of a larger project and therefore this document is focused only on one specific sub part.

The portal is accessible by only 2 types of users - the committees and the administrators. The committees are further sub-divided into 2 types – the universities and the institutes. A proper hierarchical structure is maintained between these users (Please refer section 2.3 to understand the structure).

## Architecture of the Project

The application is a component of a larger system which is dependent on the other components of the entire system. The entire system basically has three different components:
- A REST API service hosted on a Linux server, which has specific routes to interact with the database. All operations of the system that require data need to access this component. The SGRP website is dependent on this component for fetching the data from the database. These API routes are responsible for SPAM detection, RED FLAG detection in all the grievances pushed through the service.
- A mobile app for the students (3rd type of users to the system) which allows the students to lodge a grievance, track the progress of their grievance. This component interacts with the API service to make changes to the database.
- The SGRP website for the committees and the administrators to access these grievances -and work on it. This component again has to interact with the API service and make changes to the database.

## Hierarchy of Users

<img src="https://user-images.githubusercontent.com/59963061/125195035-7cecaf00-e271-11eb-82e1-0bdda7241f6d.png" width="90%"></img>

## Complaint Life Cycle

<img src="https://user-images.githubusercontent.com/59963061/125194990-4020b800-e271-11eb-9d5b-0c13281bc437.png" width="90%"></img>

## UI/UX

<img src="https://user-images.githubusercontent.com/59963061/125195039-8118cc80-e271-11eb-9c48-1b8485eb43df.png" width="45%"></img> <img src="https://user-images.githubusercontent.com/59963061/125195042-8413bd00-e271-11eb-82e7-8a723eda94be.png" width="45%"></img>
