package uit.ensak.dish_wish_frontend.Models;

import java.io.Serializable;
import java.time.Instant;

public class Rating {

    private long id;

    private Client client;


    private Chef chef;


    private Star star;

    private Instant createdOn;

    public Rating(long id, Client client, Chef chef, Star star, Instant createdOn) {
        this.id = id;
        this.client = client;
        this.chef = chef;
        this.star = star;
        this.createdOn = createdOn;
    }

    public long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Chef getChef() {
        return chef;
    }

    public Star getStar() {
        return star;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }
}
