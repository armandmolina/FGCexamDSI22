package cat.tecnocampus.favoritejourney.webAdapter;

import cat.tecnocampus.favoritejourney.application.portsIn.FavoriteJourneyPortIn;
import cat.tecnocampus.favoritejourney.domain.FavoriteJourney;
import cat.tecnocampus.favoritejourney.domain.Station;
import cat.tecnocampus.favoritejourney.webAdapter.webModel.DTOConverter;
import cat.tecnocampus.favoritejourney.webAdapter.webModel.FavoriteJourneyDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class FavoriteJourneyRestController {
    private FavoriteJourneyPortIn favoriteJourneyController;

    public FavoriteJourneyRestController(FavoriteJourneyPortIn favoriteJourneyController) {
        this.favoriteJourneyController = favoriteJourneyController;
    }

    @GetMapping("/stations")
    public List<Station> getStations() {
        return favoriteJourneyController.getStations();
     }

    @GetMapping("/stations/{nom}")
    public Station getStation(@PathVariable String nom) {
        return favoriteJourneyController.getStation(nom);
    }

    @PostMapping("/users/{username}/favoriteJourneys")
    public void postAddFavoriteJourney(@PathVariable String username, @RequestBody FavoriteJourneyDTO favoriteJourneyDTO,
                                       @RequestParam(value = "delay", required = false, defaultValue = "0") int delay,
                                       @RequestParam(value = "faultRatio", required = false, defaultValue = "0") int faultRatio) {
        FavoriteJourney favoriteJourney = DTOConverter.convertFavoriteJourneyDTO(favoriteJourneyDTO, favoriteJourneyController);
        favoriteJourneyController.addUserFavoriteJourney(username, favoriteJourney, delay, faultRatio);
    }

    @GetMapping("/users/{username}/favoriteJourneys")
    public List<FavoriteJourney> getFavoriteJourneys(@PathVariable String username) {
        return favoriteJourneyController.getFavoriteJourneys(username);
    }
}
