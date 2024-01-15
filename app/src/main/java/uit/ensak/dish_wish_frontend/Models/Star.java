package uit.ensak.dish_wish_frontend.Models;

import java.util.List;

public class Star {
    private Long id;

    private float number;

    private List<Rating> ratings;

    public Star(Long id, float number, List<Rating> ratings) {
        this.id = id;
        this.number = number;
        this.ratings = ratings;
    }

    public Long getId() {
        return id;
    }

    public float getNumber() {
        return number;
    }

    public List<Rating> getRatings() {
        return ratings;
    }
}
