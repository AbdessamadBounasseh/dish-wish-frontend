package uit.ensak.dish_wish_frontend.Models;

import java.io.Serializable;
import java.time.Instant;

public class Rating {
    private long clientId;
    private long chefId;
    private double rating;


    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getChefId() {
        return chefId;
    }

    public void setChefId(long chefId) {
        this.chefId = chefId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}