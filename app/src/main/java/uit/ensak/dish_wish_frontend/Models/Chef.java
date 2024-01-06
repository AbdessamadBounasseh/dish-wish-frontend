package uit.ensak.dish_wish_frontend.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Chef extends Client {

    private String bio;

    private String idCard;

    private String certificate;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}