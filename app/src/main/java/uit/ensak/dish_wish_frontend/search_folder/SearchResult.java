package uit.ensak.dish_wish_frontend;

import uit.ensak.dish_wish_frontend.Models.Address;
import uit.ensak.dish_wish_frontend.dto.DietDTO;

// SearchResult.java
public class SearchResult {

    private Long id;
    private String firstName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String lastName;

    private Address address;

    private String phoneNumber;

    private String photo;

    private DietDTO dietDTO;

    private String allergies;

    private String bio;

    public SearchResult(String firstName, String lastName, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public SearchResult(){}

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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
