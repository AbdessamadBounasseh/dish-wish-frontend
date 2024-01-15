package uit.ensak.dish_wish_frontend.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ContactService {
    @Headers({"Accept: application/json"})
    @POST("support/complaint")
    Call<?> sendpayload(@Body ComplaintPayload payload);

}
