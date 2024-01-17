package uit.ensak.dish_wish_frontend.dto;

public class CommentResponseDTO {
    private ClientDTO client;
    private String content;

    public CommentResponseDTO(ClientDTO s, String commentContent) {
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}