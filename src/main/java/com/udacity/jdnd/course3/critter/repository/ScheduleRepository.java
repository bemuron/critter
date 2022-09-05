package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.data.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // find all schedules by petId
    List<Schedule> findAllByPets_Id(Long petId);

    // find all schedules by employee id
    List<Schedule> findAllByEmployees_Id(Long employeeId);

}
