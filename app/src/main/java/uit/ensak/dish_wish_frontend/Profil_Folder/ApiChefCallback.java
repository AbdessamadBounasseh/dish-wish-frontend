package uit.ensak.dish_wish_frontend.Profil_Folder;

import uit.ensak.dish_wish_frontend.Models.Chef;

public interface ApiChefCallback {

    void onChefReceived(Chef chef);
    void onFailure(String errorMessage);

}
