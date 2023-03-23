package cat.tecnocampus.favoritejourney.application.userServiceAdapter;

import cat.tecnocampus.favoritejourney.application.portsOut.UserServicePort;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserServiceAdapter implements UserServicePort {
    private CircuitBreakerFactory circuitBreakerFactory;
    private RestTemplate restTemplate;

    public UserServiceAdapter(CircuitBreakerFactory circuitBreakerFactory, RestTemplate restTemplate){
        this.circuitBreakerFactory = circuitBreakerFactory;
        this.restTemplate = restTemplate;
    }

    public String userExists(String username, int delay, int faultRatio) {
        CircuitBreaker circuitBreaker=circuitBreakerFactory.create("favoriteJourney");
        String url = "http://localhost:8080/users/exists/" + username + "?delay=" + delay + "&faultRatio=" + faultRatio;

        return circuitBreaker.run(
                () -> restTemplate.getForObject(url, String.class),
                throwable -> {
                    System.out.println(throwable.getMessage());
                    return "unchecked";});

    }
}
