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
public class AuthenticatedAdmin {
    private int status;
    private Admin adminDetails;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Admin getAdminDetails() {
        return adminDetails;
    }

    public void setAdminDetails(Admin adminDetails) {
        this.adminDetails = adminDetails;
    }
    
    
}
