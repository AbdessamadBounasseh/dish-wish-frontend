package uit.ensak.dish_wish_frontend.Models;

public class Comment {
    private long id;

    private Client client;

    private Chef chef;
    private String commentContent;

    public Comment(long id, Client client, Chef chef, String commentContent) {
        this.id = id;
        this.client = client;
        this.chef = chef;
        this.commentContent = commentContent;
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

    public String getCommentContent() {
        return commentContent;
    }
}
