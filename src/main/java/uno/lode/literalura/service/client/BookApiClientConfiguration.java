package uno.lode.literalura.service.client;

import java.net.http.HttpClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

//https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-restclient
//https://certidevs.com/tutorial-spring-boot-rest-client-y-rest-template

@Configuration
public class BookApiClientConfiguration {
    HttpClient httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();

    JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);

    @Bean
    RestClient restClient() {
        return RestClient.builder()
            .baseUrl("https://gutendex.com")
            .requestFactory(requestFactory)
            .build();
    }
}
