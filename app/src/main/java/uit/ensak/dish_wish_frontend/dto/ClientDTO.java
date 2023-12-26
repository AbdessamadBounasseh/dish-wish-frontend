package uit.ensak.dish_wish_frontend.dto;

import java.util.List;

import uit.ensak.dish_wish_frontend.Models.Allergy;
import uit.ensak.dish_wish_frontend.Models.Diet;

public class ClientDTO {
    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;

    private String photo;

    private List<Diet> diets;

    private List<Allergy> allergies;

    public ClientDTO(String firstName, String lastName, String address, String phoneNumber, String photo, List<Diet> diets, List<Allergy> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
        this.diets = diets;
        this.allergies = allergies;
    }
}
