package cat.tecnocampus.favoritejourney.webAdapter.webModel;

import cat.tecnocampus.favoritejourney.application.portsIn.FavoriteJourneyPortIn;
import cat.tecnocampus.favoritejourney.domain.DayTimeStart;
import cat.tecnocampus.favoritejourney.domain.FavoriteJourney;
import cat.tecnocampus.favoritejourney.domain.Journey;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DTOConverter {

    public static FavoriteJourney convertFavoriteJourneyDTO(FavoriteJourneyDTO favoriteJourneyDTO,
                                                            FavoriteJourneyPortIn favoriteJourneyController) {
        FavoriteJourney favoriteJourney = new FavoriteJourney();
        favoriteJourney.setId(UUID.randomUUID().toString());
        Journey journey = new Journey(favoriteJourneyController.getStation(favoriteJourneyDTO.getOrigin()),
                favoriteJourneyController.getStation(favoriteJourneyDTO.getDestination()),
                "empty id");
        favoriteJourney.setJourney(journey);

        List<DayTimeStart> dayTimeStarts = favoriteJourneyDTO.getDayTimes().stream().map(dt -> DTOConverter.convertDayTimeStartDTO(dt)).collect(Collectors.toList());
        favoriteJourney.setDateTimeStarts(dayTimeStarts);

        return favoriteJourney;
    }

    private static DayTimeStart convertDayTimeStartDTO(DayTimeStartDTO dayTimeStartDTO) {
        return new DayTimeStart(dayTimeStartDTO.getDayOfWeek(), dayTimeStartDTO.getTime(), UUID.randomUUID().toString());
    }

}
