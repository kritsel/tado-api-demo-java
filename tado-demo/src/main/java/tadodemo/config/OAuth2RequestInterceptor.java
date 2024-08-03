package tadodemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2RequestInterceptor implements ClientHttpRequestInterceptor {

    private AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager;

    OAuth2RequestInterceptor(
            @Value("${useWindowsKeystore:false}")
            Boolean useWindowsKeystore,

            // Inject the OAuth authorized client service and authorized client manager
            // from the OAuth2Config
            AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager) {
        this.authorizedClientServiceAndManager = authorizedClientServiceAndManager;

        if (useWindowsKeystore) {
            System.setProperty("javax.net.ssl.keyStore", "NONE");
            System.setProperty("javax.net.ssl.keyStoreType", "Windows-my");
            System.setProperty("javax.net.ssl.trustStore", "NONE");
            System.setProperty("javax.net.ssl.trustStoreType", "Windows-ROOT");
        }
    }

    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException, IOException {
        // Build an OAuth2 request for the tado oauth2 provider
        OAuth2AuthorizeRequest authorizeRequest =
                OAuth2AuthorizeRequest
                    .withClientRegistrationId("tado")
                    .principal("anonymous")
                    .build();

        // Perform the actual authorization request using the authorized client service and authorized client
        // manager. This is where the token is retrieved from the oauth2 server.
        OAuth2AuthorizedClient authorizedClient = authorizedClientServiceAndManager.authorize(authorizeRequest);

        // Get the token from the authorized client object
        assert authorizedClient != null;
        String token = authorizedClient.getAccessToken().getTokenValue();

        request.getHeaders().setBearerAuth(token);

//        System.out.println("OAuth2RequestInterceptor.intercept added accessToken ${token.substring(0, 15)}...")
        return execution.execute(request, body);

    }
}
