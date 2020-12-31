package com.n11.scheduler;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.n11.scheduler.entities.Seminar;

public class SeminarUnitTest {
    
    @Test
    public void whenCalledGetName_thenCorrect() {
        Seminar seminar = new Seminar("Flavors of Concurrency in Java", 30);
        
        assertThat(seminar.getSeminarName()).isEqualTo("Flavors of Concurrency in Java");
    }
    
    @Test
    public void whenCalledGetDuration_thenCorrect() {
        Seminar seminar = new Seminar("Flavors of Concurrency in Java", 30);
        
        assertThat(seminar.getDuration()).isEqualTo(30);
    }
    
    @Test
    public void whenCalledSetName_thenCorrect() {
        Seminar seminar = new Seminar("Flavors of Concurrency in Java", 30);
        
        seminar.setSeminarName("Overdoing it in Python");
        
        assertThat(seminar.getSeminarName()).isEqualTo("Overdoing it in Python");
    }
    
    @Test
    public void whenCalledSetDuration_thenCorrect() {
        Seminar seminar = new Seminar("Flavors of Concurrency in Java", 30);
        
        seminar.setDuration(45);
        
        assertThat(seminar.getDuration()).isEqualTo(45);
    }
    
    @Test
    public void whenCalledtoString_thenCorrect() {
        Seminar seminar = new Seminar("Flavors of Concurrency in Java", 30);
        assertThat(seminar.toString()).isEqualTo("Seminar{id=0, seminarName=Flavors of Concurrency in Java, duration=30}");
    }
}
