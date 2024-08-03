package tadodemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
class RestClientConfig{

    private OAuth2RequestInterceptor oauth2RequestInterceptor;
    RestClientConfig(OAuth2RequestInterceptor oauth2RequestInterceptor ) {
        this.oauth2RequestInterceptor = oauth2RequestInterceptor;
    }

    @Bean("tadoRestClient")
    public RestClient tadoRestClient() {
        return RestClient
                .builder()
                .baseUrl("https://my.tado.com/api/v2/")
//                .messageConverters { it.add(MappingJackson2HttpMessageConverter()) }
                .requestInterceptor(oauth2RequestInterceptor)
                .build();
    }

}