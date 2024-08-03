package tadodemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;

import static java.util.Map.entry;

// based on https://developer.okta.com/blog/2021/05/05/client-credentials-spring-security
@Configuration
public class OAuth2Config {

    // Create the tado client registration
    @Bean
    ClientRegistration tadoClientRegistration(
            @Value("${spring.security.oauth2.client.provider.tado.token-uri}") String token_uri,
            @Value("${spring.security.oauth2.client.registration.tado.client-id}") String client_id,
            @Value("${spring.security.oauth2.client.registration.tado.client-secret}") String client_secret,
            @Value("${spring.security.oauth2.client.registration.tado.authorization-grant-type}") String authorizationGrantType
    ) {
        return ClientRegistration
                .withRegistrationId("tado")
                .tokenUri(token_uri)
                .clientId(client_id)
                .clientSecret(client_secret)
                .authorizationGrantType(new AuthorizationGrantType(authorizationGrantType))
                .build();
    }

    // Create the client registration repository
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(ClientRegistration tadoClientRegistration) {
        return new InMemoryClientRegistrationRepository(tadoClientRegistration);
    }

    // Create the authorized client service
    @Bean
    public OAuth2AuthorizedClientService auth2AuthorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
    }

    // Create the authorized client manager and service manager using the
    // beans created and configured above
    @Bean
    public AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager (
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService,
            @Value("${tado.username:undefined}")
            String tadoUsername,
            @Value("${tado.password:undefined}")
            String tadoPassword) {
        System.out.println ("create bean AuthorizedClientServiceOAuth2AuthorizedClientManager");

        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .password()
                        .refreshToken()
                        .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);


        authorizedClientManager.setContextAttributesMapper(r -> Map.ofEntries(
                entry(OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME, tadoUsername),
                entry(OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME, tadoPassword)));

        return authorizedClientManager;
    }
}
