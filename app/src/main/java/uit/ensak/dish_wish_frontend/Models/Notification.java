package uit.ensak.dish_wish_frontend.Models;

import java.time.Instant;

public class Notification {

    private Long id;

    private String type;

    private String content;

    private String seenAt;

    private Instant createdOn;

    private Client client;

}