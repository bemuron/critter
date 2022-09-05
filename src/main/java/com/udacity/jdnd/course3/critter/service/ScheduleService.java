package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.data.Schedule;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    CustomerRepository customerRepository;

    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules(){
        return scheduleRepository.findAll();
    }

    // find all schedules by petId
    public List<Schedule> getScheduleForPet(Long petId){
        return scheduleRepository.findAllByPets_Id(petId);
    }

    // find all schedules by employee id
    public List<Schedule> getScheduleForEmployee(Long employeeId) {
        return scheduleRepository.findAllByEmployees_Id(employeeId);
    }

    // find all schedules by customer id
    public List<Schedule> getScheduleForCustomer(Long customerId) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isPresent()){
            Customer customer = customerOptional.get();
            List<Pet> pets = customer.getPets();
            List<Schedule> schedules = new ArrayList<>();
            for (Pet pet : pets) {
                schedules.addAll(scheduleRepository.findAllByPets_Id(pet.getId()));
            }
            return schedules;
        } else {
            throw new UserNotFoundException();
        }
    }
}
