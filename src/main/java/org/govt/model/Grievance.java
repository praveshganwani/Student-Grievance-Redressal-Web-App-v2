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
public class Grievance {
    private String complaintId;
    private String complaintTitle;
    private String complaintDetail;
    private String complaintStudentId;
    private String complaintCommitteeId;
    private int categoryId;
    private String complaintDateTime;
    private int complaintIsSolved;
    private int complaintIsAnonymous;
    private int complaintIsRedFlag;
    private int daysElapsed;
    private String lastActivity;
    private String feedback;
    private String feedbackComment;
    private int complaintIsDelayed;
    private String imageUrl;
    private int complaintIsSpam;  

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getComplaintTitle() {
        return complaintTitle;
    }

    public void setComplaintTitle(String complaintTitle) {
        this.complaintTitle = complaintTitle;
    }

    public String getComplaintDetail() {
        return complaintDetail;
    }

    public void setComplaintDetail(String complaintDetail) {
        this.complaintDetail = complaintDetail;
    }

    public String getComplaintStudentId() {
        return complaintStudentId;
    }

    public void setComplaintStudentId(String complaintStudentId) {
        this.complaintStudentId = complaintStudentId;
    }

    public String getComplaintCommitteeId() {
        return complaintCommitteeId;
    }

    public void setComplaintCommitteeId(String complaintCommitteeId) {
        this.complaintCommitteeId = complaintCommitteeId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getComplaintDateTime() {
        return complaintDateTime;
    }

    public void setComplaintDateTime(String complaintDateTime) {
        this.complaintDateTime = complaintDateTime;
    }

    public int getComplaintIsSolved() {
        return complaintIsSolved;
    }

    public void setComplaintIsSolved(int complaintIsSolved) {
        this.complaintIsSolved = complaintIsSolved;
    }

    public int getComplaintIsAnonymous() {
        return complaintIsAnonymous;
    }

    public void setComplaintIsAnonymous(int complaintIsAnonymous) {
        this.complaintIsAnonymous = complaintIsAnonymous;
    }

    public int getComplaintIsRedFlag() {
        return complaintIsRedFlag;
    }

    public void setComplaintIsRedFlag(int complaintIsRedFlag) {
        this.complaintIsRedFlag = complaintIsRedFlag;
    }

    public int getDaysElapsed() {
        return daysElapsed;
    }

    public void setDaysElapsed(int daysElapsed) {
        this.daysElapsed = daysElapsed;
    }

    public String getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(String lastActivity) {
        this.lastActivity = lastActivity;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFeedbackComment() {
        return feedbackComment;
    }

    public void setFeedbackComment(String feedbackComment) {
        this.feedbackComment = feedbackComment;
    }

    public int getComplaintIsDelayed() {
        return complaintIsDelayed;
    }

    public void setComplaintIsDelayed(int complaintIsDelayed) {
        this.complaintIsDelayed = complaintIsDelayed;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getComplaintIsSpam() {
        return complaintIsSpam;
    }

    public void setComplaintIsSpam(int complaintIsSpam) {
        this.complaintIsSpam = complaintIsSpam;
    }

}
