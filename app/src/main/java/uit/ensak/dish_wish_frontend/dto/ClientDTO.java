package uit.ensak.dish_wish_frontend.dto;

import java.util.List;

import uit.ensak.dish_wish_frontend.Models.Address;
import uit.ensak.dish_wish_frontend.Models.Diet;

public class ClientDTO {
    private String firstName;

    public ClientDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

    private String lastName;

    private Address address;

    private String phoneNumber;

    private String photo;

    private DietDTO dietDTO;

    private String allergies;

    public ClientDTO(String firstName, String lastName, Address address, String phoneNumber, String photo, DietDTO dietDTO, String allergies,Long id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
        this.dietDTO = dietDTO;
        this.allergies = allergies;
        this.id=id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public DietDTO getDietDTO() {
        return dietDTO;
    }

    public void setDietDTO(DietDTO dietDTO) {
        this.dietDTO = dietDTO;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }
}