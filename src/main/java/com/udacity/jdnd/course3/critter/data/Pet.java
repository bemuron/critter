package com.udacity.jdnd.course3.critter.data;

import com.udacity.jdnd.course3.critter.pet.PetType;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nationalized
    private String name;

    @ManyToOne //many pets can belong to one owner
    @JoinColumn(name = "owner_id")
    private Customer ownerId;

    private LocalDate birthDate;

    @Column(name = "notes", length = 1000)
    private String notes;

    private PetType type;

    @ManyToMany
    private List<Schedule> schedules;

    public Pet() {
    }

    public Pet(PetType type, String name, Customer owner, LocalDate birthDate) {
        this.type = type;
        this.name = name;
        this.ownerId = owner;
        this.birthDate = birthDate;
    }


    public Pet(PetType type, String name, Customer owner, LocalDate birthDate, String notes, List<Schedule> schedules) {
        this.type = type;
        this.name = name;
        this.ownerId = owner;
        this.birthDate = birthDate;
        this.notes = notes;
        this.schedules = schedules;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Customer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Customer ownerId) {
        this.ownerId = ownerId;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}
