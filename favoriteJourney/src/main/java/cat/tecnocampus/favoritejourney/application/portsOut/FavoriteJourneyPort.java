package cat.tecnocampus.favoritejourney.application.portsOut;

import cat.tecnocampus.favoritejourney.domain.FavoriteJourney;

import java.util.List;

public interface FavoriteJourneyPort {
    void saveFavoriteJourney(FavoriteJourney favoriteJourney, String username, boolean checked);

    List<FavoriteJourney> findFavoriteJourneys(String username);

    void deleteFavoriteJourney(String username);
}
