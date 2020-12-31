package com.n11.scheduler.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Seminar {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotBlank(message = "SeminarName is mandatory")
    private String seminarName;
    
    private int duration;

    public Seminar() {}

    public Seminar(String seminarName, int duration) {
        this.seminarName = seminarName;
        this.duration = duration;
    }

    public void setId(long id) { this.id = id; }
    
    public long getId() {
        return id;
    }
    
    public void setSeminarName(String seminarName) { this.seminarName = seminarName; }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getSeminarName() {
        return seminarName;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return  " " + seminarName + " " + "(" + duration + "min" + ")" ;
    }

}
