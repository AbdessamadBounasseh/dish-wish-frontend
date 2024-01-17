package uit.ensak.dish_wish_frontend.dto;

public class CommentRequestDTO {
    private String content;
    private Long chefId;
    private Long clientId;

    public CommentRequestDTO(String content, Long chefId, Long clientId) {
        this.content = content;
        this.chefId = chefId;
        this.clientId = clientId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getChefId() {
        return chefId;
    }

    public void setChefId(Long chefId) {
        this.chefId = chefId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}