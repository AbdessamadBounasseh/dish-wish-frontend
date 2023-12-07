package uit.ensak.dish_wish_frontend;

// SearchResult.java
public class SearchResult {

    private String name;
    private String city;

    public SearchResult(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }
}
