package com.n11.scheduler;


import com.n11.scheduler.entities.Seminar;
import java.sql.Time;

public class Event {

    private Seminar seminar;
    private Time time;

    public Event(Seminar seminar,Time time){
        this.time=time;
        this.seminar=seminar;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Seminar getSeminar() {
        return seminar;
    }

    public void setSeminar(Seminar seminar) {
        this.seminar = seminar;
    }

}
