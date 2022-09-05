package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    public Employee save(Employee employee){
        return employeeRepository.save(employee);
    }

    public Optional<Employee> findEmployeeById(Long employeeId){
        return employeeRepository.findById(employeeId);
    }

    public List<Employee> findEmployeesForService(EmployeeRequestDTO requestDTO) {
        DayOfWeek dayOfWeek = requestDTO.getDate().getDayOfWeek();
        LocalDate requestDate = requestDTO.getDate();
        LocalDate dateToday = LocalDate.now();

        if(requestDate.compareTo(dateToday) < 0){
            return null;
        }

        //if request date is past already do not return employees
        if (requestDate.getYear() < dateToday.getYear() &&
                requestDate.getMonthValue() < dateToday.getMonthValue() &&
                requestDate.getDayOfMonth() < dateToday.getDayOfMonth()) {
            return null;
        } else if (requestDate.getYear() < dateToday.getYear() &&
                requestDate.getMonthValue() > dateToday.getMonthValue() &&
                requestDate.getDayOfMonth() > dateToday.getDayOfMonth()) {
            return null;
        }

        Set<EmployeeSkill> skills = requestDTO.getSkills();

        List<Employee> availableOnDayOfWeek = employeeRepository.findAllByDaysAvailableContaining(dayOfWeek);
        List<Employee> results = new ArrayList<>();
        if (availableOnDayOfWeek.size() == 0){
            return null;
        }
        for (Employee e : availableOnDayOfWeek) {
            if (e.getSkills().containsAll(skills)) {
                results.add(e);
            }
        }

        return results;
    }

    public void setEmployeeAvailability(Set<DayOfWeek> daysAvailable, long employeeId){
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (employee.isPresent()){
            Employee e = employee.get();
            e.setDaysAvailable(daysAvailable);
            employeeRepository.save(e);
        } else {
            throw new UserNotFoundException("Employee not found. Please try again.");
        }
    }
}
