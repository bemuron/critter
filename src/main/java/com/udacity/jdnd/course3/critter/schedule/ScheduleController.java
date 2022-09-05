package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.data.Schedule;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.service.UserNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    ScheduleService scheduleService;

    @Autowired
    PetService petService;

    @Autowired
    EmployeeService employeeService;


    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule s = scheduleService.save(convertScheduleFromScheduleDTO(scheduleDTO));
        return convertScheduleDTOFromSchedule(s);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        schedules.forEach(schedule -> scheduleDTOS.add(convertScheduleDTOFromSchedule(schedule)));
        return scheduleDTOS;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules = scheduleService.getScheduleForPet(petId);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        schedules.forEach(schedule -> scheduleDTOS.add(convertScheduleDTOFromSchedule(schedule)));
        return scheduleDTOS;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedules = scheduleService.getScheduleForEmployee(employeeId);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for (Schedule schedule : schedules)
        {
            scheduleDTOS.add(convertScheduleDTOFromSchedule(schedule));
        }
        return scheduleDTOS;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> schedules = scheduleService.getScheduleForCustomer(customerId);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        schedules.forEach(schedule -> scheduleDTOS.add(convertScheduleDTOFromSchedule(schedule)));
        return scheduleDTOS;
    }

    private Schedule convertScheduleFromScheduleDTO(ScheduleDTO scheduleDTO){
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        List<Employee> employees = new ArrayList<>();
        for(Long employeeId : scheduleDTO.getEmployeeIds()){
            Optional<Employee> e = employeeService.findEmployeeById(employeeId);
            if (e.isPresent()){
                employees.add(e.get());
            } else {
                throw new UserNotFoundException("Employee not found, please try again.");
            }
        }
        schedule.setEmployees(employees);
        List<Pet> pets = new ArrayList<>();
        for(Long petId : scheduleDTO.getPetIds()){
            Optional<Pet> p = petService.getPetById(petId);
            if (p.isPresent()){
                pets.add(p.get());
            } else {
                throw new UserNotFoundException("Pet not found, please try again.");
            }
        }
        schedule.setPets(pets);
        return schedule;
    }

    private static ScheduleDTO convertScheduleDTOFromSchedule(Schedule schedule){
        ScheduleDTO dto = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, dto);
        List<Pet> pets = schedule.getPets();
        List<Long> petIds = new ArrayList<>();
        for (Pet p : pets) {
            petIds.add(p.getId());
        }
        dto.setPetIds(petIds);

        List<Employee> employees = schedule.getEmployees();
        List<Long> employeeIds = new ArrayList<>();
        for (Employee e : employees) {
            employeeIds.add(e.getId());
        }
        dto.setEmployeeIds(employeeIds);
        return dto;
    }
}
