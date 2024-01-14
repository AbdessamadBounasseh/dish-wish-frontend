package uit.ensak.dish_wish_frontend.Models;

import java.io.Serializable;
import java.time.Instant;

import java.time.Instant;

import kotlin.text.UStringsKt;

public class Command implements Serializable {
    private Long id;
    private String title;
    private String description;
    private String serving;
    private String address;
    private String deadline;
    private String city;
    private String price;
    private String status;
    private Client client;
    private Chef chef;



    private boolean allergie;

    // Constructor
    public Command(Long id, String title, String description, String serving, String address, String deadline,
                   String city,String price, String status,
                   Client client, Chef chef,boolean allergie) {
        this.id = id;
        this.title= title;
        this.description = description;
        this.serving = serving;
        this.address = address;
        this.deadline = deadline;
        this.city = city;
        this.price = price;
        this.status = status;
        this.client = client;
        this.chef = chef;
        this.allergie = allergie;
    }

    public Command() {

    }

    // Getters and setters for all fields
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getAllergie() {
        return allergie;
    }
    public void setAllergie(boolean allergie) {
        this.allergie = allergie;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServing() {
        return serving;
    }

    public void setServing(String serving) {
        this.serving = serving;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Chef getChef() {
        return chef;
    }

    public void setChef(Chef chef) {
        this.chef = chef;
    }
}
