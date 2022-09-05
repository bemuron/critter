package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    CustomerService customerService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    PetService petService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = customerService.save(createCustomerFromCustomerDTO(customerDTO));
        customerDTO.setId(customer.getId());
        return customerDTO;
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = customerService.getAllCustomers();
        List<CustomerDTO> customerDTOs = new ArrayList<>();
        customers.forEach(customer -> customerDTOs.add(createCustomerDTOFromCustomer(customer)));
        return customerDTOs;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Optional<Pet> petOptional = petService.getPetById(petId);
        if (petOptional.isPresent()){
            Pet pet = petOptional.get();
            Customer customer = pet.getOwnerId();
            return createCustomerDTOFromCustomer(customer);
        } else {
            throw new PetNotFoundException();
        }
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = employeeService.save(createEmployeeFromEmployeeDTO(employeeDTO));
        employeeDTO.setId(employee.getId());
        return employeeDTO;
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Optional<Employee> employee = employeeService.findEmployeeById(employeeId);
        if (employee.isPresent()){
            return createEmployeeDTOFromEmployee(employee.get());
        } else {
            throw new UserNotFoundException("Employee not found. Please try again.");
        }
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        employeeService.setEmployeeAvailability(daysAvailable, employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = employeeService.findEmployeesForService(employeeDTO);
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        if (employees != null){
            employees.forEach(employee -> employeeDTOs.add(createEmployeeDTOFromEmployee(employee)));
            return employeeDTOs;
        }else{
            throw new EmployeeNotAvailableResponse("No employee available for the specified date.");
        }

    }

    private static CustomerDTO createCustomerDTOFromCustomer(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setNotes(customer.getNotes());

        List<Long> petIds;

        if(customer.getPets() !=null){
            petIds = customer.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        } else {
            petIds = new ArrayList<>();
        }

        customerDTO.setPetIds(petIds);

        return customerDTO;
    }

    private Customer createCustomerFromCustomerDTO(CustomerDTO customerDTO){
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setNotes(customerDTO.getNotes());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());

        return customerService.save(customer);
    }

    private static EmployeeDTO createEmployeeDTOFromEmployee(Employee employee){
        EmployeeDTO dto = new EmployeeDTO();
        BeanUtils.copyProperties(employee, dto);
        return dto;
    }

    private static Employee createEmployeeFromEmployeeDTO(EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());

        Set<DayOfWeek> daysAvailable = employeeDTO.getDaysAvailable();
        if (daysAvailable != null) {
            employee.setDaysAvailable(daysAvailable);
        } else {
            employee.setDaysAvailable(new HashSet<>());
        }
        Set<EmployeeSkill> skills = employeeDTO.getSkills();
        if (skills != null) {
            employee.setSkills(skills);
        } else {
            employee.setSkills(new HashSet<>());
        }
        return employee;
    }

}
