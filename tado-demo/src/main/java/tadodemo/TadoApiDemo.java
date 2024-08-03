package tadodemo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tadoclient.ApiClient;
import tadoclient.DefaultTado;
import tadoclient.User;

@Component
public class TadoApiDemo {

    private DefaultTado tadoApi;

    TadoApiDemo (
            @Qualifier("tadoRestClient")
            final RestClient tadoRestClient) {
        tadoApi = new DefaultTado(new ApiClient(tadoRestClient));
    }

    public void doIt() {
        User me = tadoApi.getMe();
        System.out.println("********************************************");
        System.out.println("* me:");
        System.out.println(me);
    }
}
