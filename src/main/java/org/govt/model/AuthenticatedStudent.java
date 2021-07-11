/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.govt.model;

/**
 *
 * @author Pravesh Ganwani
 */

public class AuthenticatedStudent {
    private int status;
    private Student studentDetails;

    public Student getStudentDetails() {
        return studentDetails;
    }

    public void setStudentDetails(Student studentDetails) {
        this.studentDetails = studentDetails;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
}
