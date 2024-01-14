package uit.ensak.dish_wish_frontend.Models;

public class Comment {
    private String username;
    private String commentContent;

    public Comment(String username, String commentContent) {
        this.username = username;
        this.commentContent = commentContent;
    }

    public String getUsername() {
        return username;
    }

    public String getCommentContent() {
        return commentContent;
    }
}
