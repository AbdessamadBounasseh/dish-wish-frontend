package uit.ensak.dish_wish_frontend.Models.Auth;

public class RegisterRequest {
    private String email;
    private String password;

    public RegisterRequest() {

    }

    public String getEmail() {
        return email;
    }

    public RegisterRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
