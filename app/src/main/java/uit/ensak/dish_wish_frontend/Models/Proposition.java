package uit.ensak.dish_wish_frontend.Models;

public class Proposition {
    private Long id;
    private Command command;
    private Client client;
    private float lastClientProposition;
    private float lastChefProposition;
    private Chef chef;

    public Proposition(Long id, Command command, Client client, float lastClientProposition, float lastChefProposition, Chef chef) {
        this.id = id;
        this.command = command;
        this.client = client;
        this.lastClientProposition = lastClientProposition;
        this.lastChefProposition = lastChefProposition;
        this.chef = chef;
    }

    public Proposition() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Command getCommand() {
        return command;
    }

    public float getLastClientProposition() {
        return lastClientProposition;
    }

    public float getLastChefProposition() {
        return lastChefProposition;
    }


    // Setters
    public void setCommand(Command command) {
        this.command = command;
    }

    public void setLastClientProposition(float lastClientProposition) {
        this.lastClientProposition = lastClientProposition;
    }

    public void setLastChefProposition(float lastChefProposition) {
        this.lastChefProposition = lastChefProposition;
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


