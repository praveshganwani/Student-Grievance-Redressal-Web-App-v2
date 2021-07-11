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
public class AuthenticatedCommittee {
    private int status;
    private Committee committeeDetails;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Committee getCommitteeDetails() {
        return committeeDetails;
    }

    public void setCommitteeDetails(Committee committeeDetails) {
        this.committeeDetails = committeeDetails;
    }
    
    
}
