package uit.ensak.dish_wish_frontend.dto;

import java.util.List;

import uit.ensak.dish_wish_frontend.Models.Address;
import uit.ensak.dish_wish_frontend.Models.Diet;

public class ChefDTO extends ClientDTO{

    private String bio;

    // Constructeur par défaut (sans paramètres)
    public ChefDTO() {
        super("", "", null , "", "", null, null);  // Remplacez les valeurs par défaut
    }

    // Constructeur avec les paramètres de ClientDTO et bio
    public ChefDTO(String firstName, String lastName, Address address, String phoneNumber, String photo, DietDTO dietDTO, String allergies, String bio) {
        super(firstName, lastName, address, phoneNumber, photo, dietDTO, allergies);
        this.bio = bio;
    }

    // Ajoutez ici les méthodes et les propriétés spécifiques à ChefDTO

    // Getter et setter pour la propriété bio
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

}
