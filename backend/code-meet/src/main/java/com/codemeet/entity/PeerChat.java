package com.codemeet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class PeerChat extends Chat {

    @ManyToOne
    @JoinColumn(nullable = false)
    private User one;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private User theOther;
    
    public User getOne() {
        return one;
    }
    
    public void setOne(User one) {
        this.one = one;
    }
    
    public User getTheOther() {
        return theOther;
    }
    
    public void setTheOther(User theOther) {
        this.theOther = theOther;
    }
}
