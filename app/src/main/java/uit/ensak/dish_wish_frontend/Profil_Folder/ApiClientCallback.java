package uit.ensak.dish_wish_frontend.Profil_Folder;

import uit.ensak.dish_wish_frontend.Models.Client;

public interface ApiClientCallback {
    void onClientReceived(Client client);
    void onFailure(String errorMessage);

}
