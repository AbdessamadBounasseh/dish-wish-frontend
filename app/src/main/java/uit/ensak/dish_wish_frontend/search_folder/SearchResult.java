package uit.ensak.dish_wish_frontend;

// SearchResult.java
public class SearchResult {

    private Long Id;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String photo;
    //    private List<Rating> ratings;
//    private List<Diet> diets;
//    private List<Allergy> allergies;
    private String bio;
    private String certificate;

    public SearchResult(String firstName, String lastName, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
//
//    public List<Rating> getRatings() {
//        return ratings;
//    }
//
//    public void setRatings(List<Rating> ratings) {
//        this.ratings = ratings;
//    }
//
//    public List<Diet> getDiets() {
//        return diets;
//    }
//
//    public void setDiets(List<Diet> diets) {
//        this.diets = diets;
//    }
//
//    public List<Allergy> getAllergies() {
//        return allergies;
//    }
//
//    public void setAllergies(List<Allergy> allergies) {
//        this.allergies = allergies;
//    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }


}
