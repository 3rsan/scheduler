package com.n11.scheduler.repositories;

import com.n11.scheduler.entities.Seminar;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeminarRepository extends CrudRepository<Seminar , Long> {
    
    List<Seminar> findBySeminarName(String seminarName);
    
}
