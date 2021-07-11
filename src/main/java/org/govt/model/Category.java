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
public class Category {
    private int categoryId;
    private String categoryName;
    private String highCommittee;
    private String lowCommittee;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getHighCommittee() {
        return highCommittee;
    }

    public void setHighCommittee(String highCommittee) {
        this.highCommittee = highCommittee;
    }

    public String getLowCommittee() {
        return lowCommittee;
    }

    public void setLowCommittee(String lowCommittee) {
        this.lowCommittee = lowCommittee;
    }
    
    
}
