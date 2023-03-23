package cat.tecnocampus.usersservice.favoriteJourneyAdapter;

import cat.tecnocampus.usersservice.application.portsOut.FavoriteJourneyPort;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class FavoriteJourneyAdapter implements FavoriteJourneyPort {
    private StreamBridge streamBridge;

    public FavoriteJourneyAdapter(StreamBridge streamBridge){

        this.streamBridge = streamBridge;
    }
    @Override
    public void deleteUserFavoriteJourneys(String username) {
        streamBridge.send("favoriteJourneyChannel",username);
    }
}
