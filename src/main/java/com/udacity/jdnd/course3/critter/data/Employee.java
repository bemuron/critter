package com.udacity.jdnd.course3.critter.data;

import com.udacity.jdnd.course3.critter.user.EmployeeSkill;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Employee extends User {
    @ElementCollection()
    public Set<EmployeeSkill> skills;

    @ElementCollection()
    public Set<DayOfWeek> daysAvailable;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "employee_schedule",
            joinColumns = {
                    @JoinColumn(name = "employee_id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "schedule_id")
            }
    )
    private Set<Schedule> schedules = new HashSet<>();

    public Employee() {
    }

    public Employee(String name, Set<EmployeeSkill> skills, Set<DayOfWeek> daysAvailable, Set<Schedule> schedules) {
        this.name = name;
        this.skills = skills;
        this.daysAvailable = daysAvailable;
        this.schedules = schedules;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<EmployeeSkill> getSkills() {
        return skills;
    }

    public void setSkills(Set<EmployeeSkill> skills) {
        this.skills = skills;
    }

    public Set<DayOfWeek> getDaysAvailable() {
        return daysAvailable;
    }

    public void setDaysAvailable(Set<DayOfWeek> daysAvailable) {
        this.daysAvailable = daysAvailable;
    }

    public Set<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(Set<Schedule> schedules) {
        this.schedules = schedules;
    }

}
