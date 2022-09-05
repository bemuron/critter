package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetNotFoundException;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    PetService petService;
    @Autowired
    CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = petService.save(createPetFromPetDTO(petDTO));
        return createPetDTOFromPet(pet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Optional<Pet> pet = petService.getPetById(petId);
        if (pet.isPresent()){
            return createPetDTOFromPet(pet.get());
        } else {
            throw new PetNotFoundException();
        }
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> pets = petService.getAllPets();
        List<PetDTO> petDTOS = new ArrayList<>();
        pets.forEach(pet -> petDTOS.add(createPetDTOFromPet(pet)));
        return petDTOS;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets = petService.getPetsByOwnerId(ownerId);
        List<PetDTO> petDTOS = new ArrayList<>();
        if (pets != null){
            pets.forEach(pet -> petDTOS.add(createPetDTOFromPet(pet)));
        }
        return petDTOS;
    }

    public static PetDTO createPetDTOFromPet(Pet pet){
        PetDTO dto = new PetDTO();
        //BeanUtils.copyProperties(pet, dto);
        if (pet.getOwnerId() != null){
            dto.setOwnerId(pet.getOwnerId().getId());
        }
        dto.setBirthDate(pet.getBirthDate());
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setNotes(pet.getNotes());
        dto.setType(pet.getType());
        return dto;
    }

    private Pet createPetFromPetDTO(PetDTO petDTO){
        Pet pet = new Pet();
        //BeanUtils.copyProperties(petDTO, pet);
        Optional<Customer> owner = customerService.getCustomerById(petDTO.getOwnerId());
        if (owner.isPresent()) {
            pet.setOwnerId(owner.get());
        }
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setName(petDTO.getName());
        pet.setNotes(petDTO.getNotes());
        pet.setType(petDTO.getType());
        return pet;
    }
}
