package uit.ensak.dish_wish_frontend.Models;

import java.time.Instant;
import java.util.List;

public class Diet {

    private Long id;

    private String title;

    private String description;

    private Instant createdOn;

    private List<Client> clients;
}