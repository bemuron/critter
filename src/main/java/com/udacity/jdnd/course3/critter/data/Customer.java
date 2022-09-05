package com.udacity.jdnd.course3.critter.data;

import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer extends User {
    @Nationalized
    private String phoneNumber;

    @Column(name = "notes", length = 1000)
    private String notes;

    //one customer can have many pets. ownerId is in pet entity
    @OneToMany(mappedBy = "ownerId")
    private List<Pet> pets = new ArrayList<>();

    public Customer() {
    }

    public Customer(String name, String phoneNumber, List<Pet> pets) {
        super.name = name;
        this.phoneNumber = phoneNumber;
        this.pets = pets;
    }

    public Customer(String name, String phoneNumber, List<Pet> pets, String notes) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.pets = pets;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }
}
