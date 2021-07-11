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
public class Committee {
    private String committeeId;
    private String committeeName;
    private String committeeType;
    private String committeeEmail;
    private String committeeContact;
    private String committeePassword;
    private String committeeRegistrationDate;
    private int isVerified;
    private int isActive;
    private String parentId;

    public String getCommitteeId() {
        return committeeId;
    }

    public void setCommitteeId(String committeeId) {
        this.committeeId = committeeId;
    }

    public String getCommitteeName() {
        return committeeName;
    }

    public void setCommitteeName(String committeeName) {
        this.committeeName = committeeName;
    }

    public String getCommitteeType() {
        return committeeType;
    }

    public void setCommitteeType(String committeeType) {
        this.committeeType = committeeType;
    }

    public String getCommitteeEmail() {
        return committeeEmail;
    }

    public void setCommitteeEmail(String committeeEmail) {
        this.committeeEmail = committeeEmail;
    }

    public String getCommitteePassword() {
        return committeePassword;
    }

    public void setCommitteePassword(String committeePassword) {
        this.committeePassword = committeePassword;
    }

    public String getCommitteeRegistrationDate() {
        return committeeRegistrationDate;
    }

    public void setCommitteeRegistrationDate(String committeeRegistrationDate) {
        this.committeeRegistrationDate = committeeRegistrationDate;
    }

    public int getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(int isVerified) {
        this.isVerified = isVerified;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCommitteeContact() {
        return committeeContact;
    }

    public void setCommitteeContact(String committeeContact) {
        this.committeeContact = committeeContact;
    }
}
