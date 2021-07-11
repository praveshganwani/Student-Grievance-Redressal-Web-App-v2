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
public class FAQs {
    private int faqId;
    private String faqKeywords;
    private String faqTitle;
    private String faqDetails;
    private String faqComment;
    private String committeeId;
    
    public int getFaqId() {
        return faqId;
    }

    public void setFaqId(int faqId) {
        this.faqId = faqId;
    }

    public String getFaqKeywords() {
        return faqKeywords;
    }

    public void setFaqKeywords(String faqKeywords) {
        this.faqKeywords = faqKeywords;
    }

    public String getFaqTitle() {
        return faqTitle;
    }

    public void setFaqTitle(String faqTitle) {
        this.faqTitle = faqTitle;
    }

    public String getFaqDetails() {
        return faqDetails;
    }

    public void setFaqDetails(String faqDetails) {
        this.faqDetails = faqDetails;
    }

    public String getFaqComment() {
        return faqComment;
    }

    public void setFaqComment(String faqComment) {
        this.faqComment = faqComment;
    }

    public String getCommitteeId() {
        return committeeId;
    }

    public void setCommitteeId(String committeeId) {
        this.committeeId = committeeId;
    }
    
    
}
