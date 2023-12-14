package uit.ensak.dish_wish_frontend.Models;

import java.util.List;

public class Chef extends Client {

    private String bio;

    private String idCard;

    private String certificate;

    private List<Rating> ratings;

    @Override
    public String getRole() {
        return "CHEF";
    }
}