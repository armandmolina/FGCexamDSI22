package cat.tecnocampus.favoritejourney.application.portsOut;

import cat.tecnocampus.favoritejourney.domain.Journey;

import java.util.List;

public interface JourneyPort {
    int saveJourney(Journey journey);

    String getJourneyId(Journey journey);

    Journey findJourney(String journeyId);

    void deleteJourneys(List<String> journeyId);
}
