package cat.tecnocampus.favoritejourney.application.portsIn;

import cat.tecnocampus.favoritejourney.domain.FavoriteJourney;
import cat.tecnocampus.favoritejourney.domain.Station;

import java.util.List;

public interface FavoriteJourneyPortIn {
    List<Station> getStations();

    Station getStation(String nom);

    void addUserFavoriteJourney(String username, FavoriteJourney favoriteJourney, int delay, int faultRatio);

    List<FavoriteJourney> getFavoriteJourneys(String username);

    void deleteFavoriteJourneys(String username);
}
