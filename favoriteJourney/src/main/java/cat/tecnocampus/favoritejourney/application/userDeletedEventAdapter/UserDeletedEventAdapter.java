package cat.tecnocampus.favoritejourney.application.userDeletedEventAdapter;

import cat.tecnocampus.favoritejourney.application.appUseCases.FavoriteJourneyUseCases;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class UserDeletedEventAdapter {
    private final FavoriteJourneyUseCases favoriteJourneyUseCases;

    public UserDeletedEventAdapter(FavoriteJourneyUseCases favoriteJourneyUseCases){

        this.favoriteJourneyUseCases = favoriteJourneyUseCases;
    }

    @Bean
    public Consumer<String> deleteFavoriteJourneyOfUser(){
        return username -> favoriteJourneyUseCases.deleteFavoriteJourneys(username);
    }
}
